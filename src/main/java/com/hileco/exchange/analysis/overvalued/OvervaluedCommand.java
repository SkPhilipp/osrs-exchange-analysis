package com.hileco.exchange.analysis.overvalued;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import picocli.CommandLine;

@CommandLine.Command(
        description = "Analysis method retrieving the top overvalued items.",
        name = "overvalued",
        mixinStandardHelpOptions = true)
public class OvervaluedCommand implements Runnable {

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
                .find(Filters.and(Filters.gte("osBuddy.sellPrice", 1.2),
                                  Filters.gte("osBuddy.sellQuantity", 50)))
                .sort(Sorts.descending("methodOvervalued.profitPercent"))
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
}
