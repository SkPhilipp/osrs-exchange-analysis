package com.hileco.exchange.commands;

import com.hileco.exchange.Database;
import com.hileco.exchange.sources.official.OfficialView;
import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import picocli.CommandLine;

@CommandLine.Command(
        description = "Analysis method retrieving the top undervalued items.",
        name = "undervalued",
        mixinStandardHelpOptions = true)
public class Undervalued implements Runnable {

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
                .find(Filters.and(Filters.gte("methodUndervaluedResell.profitPercent", 1),
                                  Filters.gte("osBuddy.buyQuantity", 50)))
                .sort(Sorts.descending("methodUndervaluedResell.profitPercent"))
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
