package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDateTime;

import static com.hileco.exchange.core.Database.METHOD_OVERVALUED;
import static com.hileco.exchange.core.Database.SOURCE_OFFICIAL;
import static com.hileco.exchange.core.Database.SOURCE_OS_BUDDY;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Analysis method to compute the top items where the official price is above the actual sell average.",
        name = "load-overvalued")
public class OvervaluedAnalyseCommand implements Runnable {

    private static final int MINIMUM_SELL_QUANTITY = 3;

    @Option(names = {"-d", "--delete"}, description = "Whether to delete all previous analysis data.")
    private boolean delete = false;

    @Override
    public void run() {
        var timestamp = LocalDateTime.now();
        var database = new Database();
        database.findIds(SOURCE_OS_BUDDY).forEachRemaining(id -> {
            var latestOsBuddy = database.findLast(SOURCE_OS_BUDDY, id);
            var latestOfficial = database.findLast(SOURCE_OFFICIAL, id);
            if (latestOsBuddy.isPresent() && latestOfficial.isPresent()) {
                var official = new OfficialView(latestOfficial.get());
                var osBuddy = new OsBuddyView(latestOsBuddy.get());
                if (osBuddy.sellQuantity.get() > MINIMUM_SELL_QUANTITY) {
                    var deltaAbsolute = official.price.get() - osBuddy.sellAverage.get();
                    if (deltaAbsolute > 0) {
                        var document = new Document();
                        var overvalued = new OvervaluedView(document);
                        overvalued.id.set(id);
                        overvalued.timestamp.set(timestamp);
                        overvalued.deltaAbsolute.set(deltaAbsolute);
                        overvalued.deltaPercent.set(deltaAbsolute * 100 / official.price.get());
                        overvalued.official.set(official.reference());
                        overvalued.osBuddy.set(osBuddy.reference());
                        database.collection(METHOD_OVERVALUED).insertOne(document);
                    }
                }
            }
        });
        if (delete) {
            database.collection(METHOD_OVERVALUED).deleteMany(Filters.lt("timestamp", timestamp));
        }
    }
}
