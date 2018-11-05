package com.hileco.exchange.osbuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hileco.exchange.core.Currency;
import com.hileco.exchange.core.Database;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.hileco.exchange.core.Database.SOURCE_OS_BUDDY;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Loads in the OsBuddy source.",
        name = "load-osbuddy")
public class OsBuddyLoadCommand implements Runnable {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final int OSBUDDY_PUBLISHING_INTERVAL = 300000;

    @Option(names = {"-l", "--loop"}, description = "Continue loading indefinitely.")
    private boolean loop = false;

    @Override
    public void run() {
        do {
            try {
                var database = new Database();
                var responseStream = Unirest.get("https://storage.googleapis.com/osbuddy-exchange/summary.json")
                        .header("accept", "application/json")
                        .asBinary()
                        .getBody();
                var result = OBJECT_MAPPER.readTree(responseStream);
                var timestamp = LocalDateTime.now();
                var documents = new ArrayList<Document>();
                result.fieldNames().forEachRemaining(itemId -> {
                    var document = new Document();
                    var osBuddyView = new OsBuddyView(document);
                    var item = result.get(itemId);
                    osBuddyView.id.set(itemId);
                    osBuddyView.timestamp.set(timestamp);
                    osBuddyView.name.set(item.get("name").asText());
                    osBuddyView.members.set(item.get("members").asBoolean());
                    osBuddyView.sp.set(Currency.parse(item.get("sp").asText()));
                    osBuddyView.buyAverage.set(Currency.parse(item.get("buy_average").asText()));
                    osBuddyView.buyQuantity.set(Currency.parse(item.get("buy_quantity").asText()));
                    osBuddyView.sellAverage.set(Currency.parse(item.get("sell_average").asText()));
                    osBuddyView.sellQuantity.set(Currency.parse(item.get("sell_quantity").asText()));
                    osBuddyView.overallAverage.set(Currency.parse(item.get("overall_average").asText()));
                    osBuddyView.overallQuantity.set(Currency.parse(item.get("overall_quantity").asText()));
                    documents.add(document);
                });
                database.collection(SOURCE_OS_BUDDY).insertMany(documents);
                System.out.println(String.format("Stored %s items from OsBuddy", documents.size()));
                if (loop) {
                    Thread.sleep(OSBUDDY_PUBLISHING_INTERVAL);
                }
            } catch (UnirestException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (loop);
    }
}
