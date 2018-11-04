package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import org.bson.Document;

import java.time.LocalDateTime;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;


@Command(description = "Analysis method retrieving the top items where the official price is above the actual sell average.",
        name = "analyse-overvalued")
public class OvervaluedAnalyseCommand implements Runnable {

    private static final int MINIMUM_SELL_AVERAGE = 5;

    @Option(names = {"-r", "--reload"}, description = "Whether to reload analysis data.")
    private boolean reload = false;

    @Override
    public void run() {
        var timestamp = LocalDateTime.now();
        var database = new Database();
        var distinctIds = database.getSourceOsBuddy().distinct("id", String.class).batchSize(100);
        distinctIds.spliterator().forEachRemaining(itemId -> {
            var latestOsBuddy = database.findLast(database.getSourceOsBuddy(), itemId);
            var latestOfficial = database.findLast(database.getSourceOfficial(), itemId);
            if (latestOsBuddy.isPresent() && latestOfficial.isPresent()) {
                var official = new OfficialView(latestOfficial.get());
                var osBuddy = new OsBuddyView(latestOsBuddy.get());
                if (osBuddy.sellAverage().get() > MINIMUM_SELL_AVERAGE) {
                    official.price();
                    osBuddy.sellAverage();
                    osBuddy.sellQuantity();
                    var deltaAbsolute = official.price().get() - osBuddy.sellAverage().get();
                    if (deltaAbsolute > 0) {
                        var document = new Document();
                        var overvaluedView = new OvervaluedView(document);
                        overvaluedView.id().set(itemId);
                        overvaluedView.timestamp().set(timestamp);
                        overvaluedView.deltaAbsolute().set(deltaAbsolute);
                        overvaluedView.deltaPercent().set(deltaAbsolute * 100 / official.price().get());
                        database.getMethodOvervalued().insertOne(document);
                    }
                }
            }
        });
    }
}
