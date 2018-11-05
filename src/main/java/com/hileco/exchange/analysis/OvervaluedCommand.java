package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Sorts;

import static com.hileco.exchange.core.Database.METHOD_OVERVALUED;
import static com.hileco.exchange.core.Database.SOURCE_OFFICIAL;
import static com.hileco.exchange.core.Database.SOURCE_OS_BUDDY;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Retrieve the top items currently overvalued by the Grand Exchange.",
        name = "overvalued")
public class OvervaluedCommand implements Runnable {

    @Option(names = {"-n", "--number"}, description = "How many entries to retrieve.")
    private int limit = 30;

    @Override
    public void run() {
        var database = new Database();
        System.out.println(String.format("%9s %24s %9s %12s %9s %9s %24s",
                                         "Id",
                                         "Name",
                                         "Exchange",
                                         "Actual(Sell)",
                                         "Delta",
                                         "Delta%",
                                         "Timestamp"));
        database.collection(METHOD_OVERVALUED).find()
                .sort(Sorts.descending("deltaPercent", "deltaAbsoluteStack"))
                .limit(limit)
                .spliterator()
                .forEachRemaining(document -> {
                    var overvalued = new OvervaluedView(document);
                    var osBuddy = new OsBuddyView(database.findLast(SOURCE_OS_BUDDY, overvalued.id.get()).orElseThrow());
                    var official = new OfficialView(database.findLast(SOURCE_OFFICIAL, overvalued.id.get()).orElseThrow());
                    System.out.println(String.format("%9s %24s %9s %12s %9s %9s %24s",
                                                     overvalued.id,
                                                     official.name,
                                                     official.price,
                                                     osBuddy.sellAverage,
                                                     overvalued.deltaAbsolute.get(),
                                                     overvalued.deltaPercent.get(),
                                                     overvalued.timestamp.get()));
                });
    }
}
