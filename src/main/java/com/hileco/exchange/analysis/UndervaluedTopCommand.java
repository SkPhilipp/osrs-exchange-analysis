package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Sorts;

import static com.hileco.exchange.core.Database.METHOD_UNDERVALUED;
import static com.hileco.exchange.core.Database.SOURCE_OFFICIAL;
import static com.hileco.exchange.core.Database.SOURCE_OS_BUDDY;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Retrieve the top items currently undervalued by the Grand Exchange.",
        name = "top-undervalued")
public class UndervaluedTopCommand implements Runnable {

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
                    var osBuddy = new OsBuddyView(database.findLast(SOURCE_OS_BUDDY, undervalued.id.get()).orElseThrow());
                    var official = new OfficialView(database.findLast(SOURCE_OFFICIAL, undervalued.id.get()).orElseThrow());
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
