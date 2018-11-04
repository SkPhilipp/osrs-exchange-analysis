package com.hileco.exchange.analysis.undervalued;

import com.hileco.exchange.analysis.Enricher;
import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.math.RoundingMode;

@CommandLine.Command(
        description = "Analysis method retrieving the top undervalued items.",
        name = "undervalued",
        mixinStandardHelpOptions = true)
public class UndervaluedCommand implements Runnable, Enricher {

    @Override
    public void run() {
        var database = new Database();
        System.out.println(String.format("%24s %9s %9s %9s %9s %9s",
                                         "NAME",
                                         "PRICE",
                                         "SELL_AVG",
                                         "SELL_QTY",
                                         "BUY_AVG",
                                         "BUY_QTY"));
        database.getItems()
                .find(Filters.and(Filters.gte("methodNormalResell.profitPercent", 1),
                                  Filters.gte("osBuddy.buyQuantity", 50)))
                .sort(Sorts.descending("methodNormalResell.profitPercent"))
                .spliterator()
                .forEachRemaining(document -> {
                    var officialView = new OfficialView(document);
                    var osBuddyView = new OsBuddyView(document);
                    System.out.println(String.format("%24s %9s %9s %9s %9s %9s",
                                                     osBuddyView.name().get(),
                                                     officialView.price().get(),
                                                     osBuddyView.sellAverage().get(),
                                                     osBuddyView.sellQuantity().get(),
                                                     osBuddyView.buyAverage().get(),
                                                     osBuddyView.buyQuantity().get()));
                });
    }

    @Override
    public void enrich(Document document) {
        var osBuddyView = new OsBuddyView(document);
        var officialView = new OfficialView(document);
        if (osBuddyView.isAvailable() && officialView.isAvailable()) {
            var osBuddyBuyAverage = osBuddyView.buyAverage().get();
            var officialPrice = officialView.price().get();
            var profit = osBuddyBuyAverage.subtract(officialPrice);
            var profitPercent = profit.divide(officialPrice, RoundingMode.HALF_DOWN);
            var profitable = profit.compareTo(BigDecimal.ZERO) > 0;
            var method = new UndervaluedView(document);
            method.profit().set(profit);
            method.profitPercent().set(profitPercent);
            method.profitable().set(profitable);
        }
    }
}
