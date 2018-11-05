package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.wikia.WikiaView;
import com.mongodb.client.model.Sorts;

import static com.hileco.exchange.analysis.GeneralStoreAnalyseCommand.GENERAL_STORE_MULTIPLIER;
import static com.hileco.exchange.core.Database.METHOD_GENERAL_STORE;
import static com.hileco.exchange.core.Database.SOURCE_OFFICIAL;
import static com.hileco.exchange.core.Database.SOURCE_WIKIA;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Retrieve the top items to vendor.",
        name = "top-general-store")
public class GeneralStoreTopCommand implements Runnable {

    @Option(names = {"-n", "--number"}, description = "How many entries to retrieve.")
    private int limit = 30;

    @Override
    public void run() {
        var database = new Database();
        System.out.println(String.format("%9s %24s %9s %9s %9s %9s %9s %24s",
                                         "Id",
                                         "Name",
                                         "Exchange",
                                         "Store",
                                         "Delta",
                                         "x50",
                                         "Delta%",
                                         "Timestamp"));
        database.collection(METHOD_GENERAL_STORE).find()
                .sort(Sorts.descending("deltaPercent", "deltaAbsoluteStack"))
                .limit(limit)
                .spliterator()
                .forEachRemaining(document -> {
                    var generalStore = new GeneralStoreView(document);
                    var wikia = new WikiaView(database.findLast(SOURCE_WIKIA, generalStore.id.get()).orElseThrow());
                    var official = new OfficialView(database.findLast(SOURCE_OFFICIAL, generalStore.id.get()).orElseThrow());
                    System.out.println(String.format("%9s %24s %9s %9s %9s %9s %9s %24s",
                                                     generalStore.id,
                                                     official.name,
                                                     official.price,
                                                     wikia.lowAlchemy.get() * GENERAL_STORE_MULTIPLIER,
                                                     generalStore.deltaAbsolute.get(),
                                                     generalStore.deltaAbsoluteStack.get(),
                                                     generalStore.deltaPercent.get(),
                                                     generalStore.timestamp.get()));
                });
    }
}
