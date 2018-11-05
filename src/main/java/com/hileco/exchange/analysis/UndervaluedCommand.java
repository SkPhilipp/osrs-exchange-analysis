package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Sorts;

import static com.hileco.exchange.core.Database.METHOD_UNDERVALUED;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Retrieve the top items currently undervalued by the Grand Exchange.",
        name = "undervalued")
public class UndervaluedCommand implements Runnable {

    @Option(names = {"-n", "--number"}, description = "How many entries to retrieve.")
    private int limit = 30;

    @Override
    public void run() {
        var database = new Database();
        System.out.println(String.format("%9s %24s %9s %12s %9s %9s %24s",
                                         "Id",
                                         "Name",
                                         "Exchange",
                                         "Actual(Buy)",
                                         "Delta",
                                         "Delta%",
                                         "Timestamp"));
        database.collection(METHOD_UNDERVALUED).find()
                .sort(Sorts.descending("deltaPercent", "deltaAbsoluteStack"))
                .limit(limit)
                .spliterator()
                .forEachRemaining(document -> {
                    var undervalued = new UndervaluedView(document);
                    var osBuddy = new OsBuddyView(database.find(undervalued.osBuddy.get()).orElseThrow());
                    var official = new OfficialView(database.find(undervalued.official.get()).orElseThrow());
                    System.out.println(String.format("%9s %24s %9s %12s %9s %9s %24s",
                                                     undervalued.id,
                                                     official.name,
                                                     official.price,
                                                     osBuddy.buyAverage,
                                                     undervalued.deltaAbsolute.get(),
                                                     undervalued.deltaPercent.get(),
                                                     undervalued.timestamp.get()));
                });
    }
}
