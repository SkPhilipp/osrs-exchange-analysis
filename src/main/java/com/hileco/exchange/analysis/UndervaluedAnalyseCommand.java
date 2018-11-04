package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDateTime;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Analysis method retrieving the top items where the official price is below the actual buy average.",
        name = "analyse-undervalued")
public class UndervaluedAnalyseCommand implements Runnable {

    private static final int MINIMUM_BUY_QUANTITY = 3;

    @Option(names = {"-d", "--delete"}, description = "Whether to delete all previous analysis data.")
    private boolean delete = false;

    @Override
    public void run() {
        var timestamp = LocalDateTime.now();
        var database = new Database();
        database.findIds(database.getSourceOsBuddy()).forEachRemaining(id -> {
            var latestOsBuddy = database.findLast(database.getSourceOsBuddy(), id);
            var latestOfficial = database.findLast(database.getSourceOfficial(), id);
            if (latestOsBuddy.isPresent() && latestOfficial.isPresent()) {
                var official = new OfficialView(latestOfficial.get());
                var osBuddy = new OsBuddyView(latestOsBuddy.get());
                if (osBuddy.buyQuantity().get() > MINIMUM_BUY_QUANTITY) {
                    var deltaAbsolute = osBuddy.buyAverage().get() - official.price().get();
                    if (deltaAbsolute > 0) {
                        var document = new Document();
                        var undervalued = new UndervaluedView(document);
                        undervalued.id().set(id);
                        undervalued.timestamp().set(timestamp);
                        undervalued.deltaAbsolute().set(deltaAbsolute);
                        undervalued.deltaPercent().set(deltaAbsolute * 100 / official.price().get());
                        database.getMethodUndervalued().insertOne(document);
                    }
                }
            }
        });
        if (delete) {
            database.getMethodOvervalued().deleteMany(Filters.lt("timestamp", timestamp));
        }
    }
}
