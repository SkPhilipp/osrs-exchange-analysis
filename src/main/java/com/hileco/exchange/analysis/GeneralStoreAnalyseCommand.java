package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.wikia.WikiaView;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDateTime;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Analysis method retrieving the top items to vendor.",
        name = "analyse-general-store")
public class GeneralStoreAnalyseCommand implements Runnable {

    private static final double GENERAL_STORE_MULTIPLIER = 50d;
    private static final double GENERAL_STORE_SELL_STACK_SIZE = 50d;

    @Option(names = {"-d", "--delete"}, description = "Whether to delete all previous analysis data.")
    private boolean delete = false;

    @Override
    public void run() {
        var timestamp = LocalDateTime.now();
        var database = new Database();
        database.findIds(database.getSourceOsBuddy()).forEachRemaining(id -> {
            var latestWikia = database.findLast(database.getSourceWikia(), id);
            var latestOfficial = database.findLast(database.getSourceOfficial(), id);
            if (latestWikia.isPresent() && latestOfficial.isPresent()) {
                var wikia = new WikiaView(latestWikia.get());
                var official = new OfficialView(latestOfficial.get());
                var generalStorePrice = wikia.lowAlchemy().get() * GENERAL_STORE_MULTIPLIER;
                var deltaAbsolute = generalStorePrice - official.price().get();
                if (deltaAbsolute > 0) {
                    var document = new Document();
                    var generalStore = new GeneralStoreView(document);
                    generalStore.id().set(id);
                    generalStore.timestamp().set(timestamp);
                    generalStore.deltaAbsolute().set(deltaAbsolute);
                    generalStore.deltaAbsoluteStack().set(deltaAbsolute * GENERAL_STORE_SELL_STACK_SIZE);
                    generalStore.deltaPercent().set(deltaAbsolute * 100 / official.price().get());
                    database.getMethodOvervalued().insertOne(document);
                }
            }
        });
        if (delete) {
            database.getMethodOvervalued().deleteMany(Filters.lt("timestamp", timestamp));
        }
    }
}
