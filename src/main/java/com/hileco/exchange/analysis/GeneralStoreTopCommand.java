package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.mongodb.client.model.Sorts;

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
        database.getMethodGeneralStore().find()
                .sort(Sorts.descending("deltaPercent", "deltaAbsoluteStack"))
                .limit(limit)
                .spliterator()
                .forEachRemaining(document -> {
                    var generalStore = new GeneralStoreView(document);
                    System.out.println(String.format("%9s %24s %9s %9s %9s %9s %9s %24s",
                                                     generalStore.id(),
                                                     generalStore.name(),
                                                     generalStore.officialPrice(),
                                                     generalStore.generalStorePrice(),
                                                     generalStore.deltaAbsolute().get(),
                                                     generalStore.deltaAbsoluteStack().get(),
                                                     generalStore.deltaPercent().get(),
                                                     generalStore.timestamp().get()));
                });
    }
}
