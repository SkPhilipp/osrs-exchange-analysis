package com.hileco.exchange.official;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hileco.exchange.core.Currency;
import com.hileco.exchange.core.Database;
import com.mashape.unirest.http.Unirest;
import org.bson.Document;

import java.time.LocalDateTime;

import static com.hileco.exchange.core.Database.SOURCE_OFFICIAL;
import static com.hileco.exchange.core.Database.SOURCE_OS_BUDDY;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Loads in the official source.",
        name = "load-official")
public class OfficialLoadCommand implements Runnable {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int OFFICIAL_RATE_LIMIT = 5500;

    @Option(names = {"-l", "--loop"}, description = "Continue loading indefinitely.")
    private boolean loop = false;

    @Override
    public void run() {
        var database = new Database();
        do {
            var timestamp = LocalDateTime.now();
            database.findIds(SOURCE_OS_BUDDY).forEachRemaining(id -> {
                var timeStart = System.currentTimeMillis();
                try {
                    var responseStream = Unirest.get("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json")
                            .header("accept", "application/json")
                            .queryString("item", id)
                            .asBinary()
                            .getBody();
                    var item = OBJECT_MAPPER.readTree(responseStream).get("item");
                    var document = new Document();
                    var officialView = new OfficialView(document);
                    officialView.id.set(id);
                    officialView.timestamp.set(timestamp);
                    officialView.price.set(Currency.parse(item.get("current").get("price").asText()));
                    officialView.priceChange.set(Currency.parse(item.get("today").get("price").asText()));
                    officialView.price180.set(item.get("day180").get("change").asText());
                    officialView.price90.set(item.get("day90").get("change").asText());
                    officialView.price30.set(item.get("day30").get("change").asText());
                    officialView.description.set(item.get("description").asText());
                    officialView.icon.set(item.get("icon").asText());
                    officialView.iconLarge.set(item.get("icon_large").asText());
                    officialView.members.set(item.get("members").asBoolean());
                    officialView.name.set(item.get("name").asText());
                    database.collection(SOURCE_OFFICIAL).insertOne(document);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    var timeSpent = System.currentTimeMillis() - timeStart;
                    var timeToWait = OFFICIAL_RATE_LIMIT - timeSpent;
                    if (timeToWait > 0) {
                        try {
                            Thread.sleep(timeToWait);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        while (loop);
    }
}
