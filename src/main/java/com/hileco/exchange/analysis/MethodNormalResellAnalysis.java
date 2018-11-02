package com.hileco.exchange.analysis;

import com.hileco.exchange.Database;
import com.hileco.exchange.sources.official.OfficialView;
import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public class MethodNormalResellAnalysis {

    private Database database;

    public MethodNormalResellAnalysis() {
        this.database = new Database();
    }

    public MethodNormalResellAnalysis(Database database) {
        this.database = database;
    }

    public static void main(String[] args) {
        var method = new MethodNormalResellAnalysis();
        method.run();
    }

    public void run() {
        database.getItems()
                .find(Filters.and(Filters.gte("methodNormalResell.profitPercent", 3),
                                  Filters.gte("osBuddy.buyQuantity", 5)))
                .sort(Sorts.descending("methodNormalResell.profitPercent"))
                .spliterator()
                .forEachRemaining(document -> {
                    var officialView = new OfficialView(document);
                    var osBuddyView = new OsBuddyView(document);
                    System.out.println(String.format("%24s %9s: %9s --> %9s (%s buyers)",
                                                     osBuddyView.name().get(),
                                                     officialView.price().get(),
                                                     osBuddyView.sellAverage().get(),
                                                     osBuddyView.buyAverage().get(),
                                                     osBuddyView.buyQuantity().get()));
                });
    }
}
