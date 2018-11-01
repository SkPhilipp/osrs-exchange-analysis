package com.hileco.exchange;

import com.hileco.exchange.enrich.MethodGeneralStoreEnricher;
import com.hileco.exchange.enrich.MethodNormalResellEnricher;
import com.hileco.exchange.sources.official.OfficialSource;
import com.hileco.exchange.sources.osbuddy.OsBuddySource;
import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import com.hileco.exchange.sources.wikia.WikiaSource;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Application {

    private final OfficialSource officialSource = new OfficialSource();
    private final OsBuddySource osBuddySource = new OsBuddySource();
    private final WikiaSource wikiaSource = new WikiaSource();
    private final MethodGeneralStoreEnricher methodGeneralStoreEnricher = new MethodGeneralStoreEnricher();
    private final MethodNormalResellEnricher methodNormalResellEnricher = new MethodNormalResellEnricher();

    public static void main(String[] args) {
        var application = new Application();
        application.main();
    }

    private void main() {
        var mongoClient = MongoClients.create("mongodb+srv://pepe:VzGP54FpW7ctWaTh@thegrandexchange-zules.azure.mongodb.net/test?retryWrites=true");
        var grandExchange = mongoClient.getDatabase("grandexchange");
        var items = grandExchange.getCollection("items");
        var osBuddyViews = osBuddySource.views();
        var resumeFrom = "1081";
        var resuming = new AtomicReference<>(true);
        osBuddyViews.forEach((s, osBuddyView) -> {
            if (resumeFrom.equals(s)) {
                resuming.set(false);
            }
            if (resuming.get()) {
                System.out.println(String.format("Skipping %s", s));
            } else {
                System.out.println(String.format("Processing %s", s));
                items.insertOne(process(s, osBuddyViews));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Document process(String id, Map<String, OsBuddyView> osBuddyViews) {
        var osBuddyView = osBuddyViews.get(id);
        var officialView = officialSource.view(id);
        var wikiaView = wikiaSource.wikiaView(id);
        Document document = new Document();
        document.put("id", id);
        document.put("timestamp", LocalDateTime.now());
        if (osBuddyView.isAvailable()) {
            osBuddyView.writeInto(document);
        }
        if (officialView.isAvailable()) {
            officialView.writeInto(document);
        }
        if (wikiaView.isAvailable()) {
            wikiaView.writeInto(document);
        }
        methodGeneralStoreEnricher.enrich(document);
        methodNormalResellEnricher.enrich(document);
        return document;
    }
}
