package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.time.LocalDateTime;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;


@Command(description = "Analysis method retrieving the top items where the official price is above the actual sell average.",
        name = "analyse-overvalued")
public class OvervaluedAnalyseCommand implements Runnable {

    public static final int MINUMUM_SELL_AVERAGE = 5;
    @Option(names = {"-r", "--reload"}, description = "Whether to reload analysis data.")
    private boolean reload = false;

    @Override
    public void run() {
        var timestamp = LocalDateTime.now();
        var database = new Database();
        var distinctIds = database.getSourceOsBuddy().distinct("id", String.class).batchSize(100);
        distinctIds.spliterator().forEachRemaining(itemId -> {
            var latestOsBuddy = database.getSourceOsBuddy()
                    .find(Filters.eq("id", itemId))
                    .sort(Sorts.descending("timestamp"))
                    .limit(1)
                    .iterator();
            var latestOfficial = database.getSourceOfficial()
                    .find(Filters.eq("id", itemId))
                    .sort(Sorts.descending("timestamp"))
                    .limit(1)
                    .iterator();
            if (latestOsBuddy.hasNext() && latestOfficial.hasNext()) {
                var official = new OfficialView(latestOfficial.next());
                var osBuddy = new OsBuddyView(latestOsBuddy.next());
                if (osBuddy.sellAverage().get() > MINUMUM_SELL_AVERAGE) {
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
            latestOsBuddy.close();
            latestOfficial.close();
        });
    }
}
