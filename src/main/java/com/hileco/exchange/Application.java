package com.hileco.exchange;

import com.hileco.exchange.enrich.MethodGeneralStoreEnricher;
import com.hileco.exchange.enrich.MethodNormalResellEnricher;
import com.hileco.exchange.sources.official.OfficialSource;
import com.hileco.exchange.sources.osbuddy.OsBuddySource;
import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import com.hileco.exchange.sources.wikia.WikiaSource;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Application {

    private final OfficialSource officialSource = new OfficialSource();
    private final OsBuddySource osBuddySource = new OsBuddySource();
    private final WikiaSource wikiaSource = new WikiaSource();
    private final MethodGeneralStoreEnricher methodGeneralStoreEnricher = new MethodGeneralStoreEnricher();
    private final MethodNormalResellEnricher methodNormalResellEnricher = new MethodNormalResellEnricher();
    private final Database database = new Database();

    public static void main(String[] args) {
        var application = new Application();
        application.run();
    }

    private void run() {
        var osBuddyViews = osBuddySource.views();
        var resumeFrom = "7158";
        var resuming = new AtomicReference<>(true);
        var skipping = new AtomicInteger(0);
        osBuddyViews.forEach((s, osBuddyView) -> {
            // TODO: Instead query mongo to whether the item should be (GE-)updated
            if (resumeFrom.equals(s)) {
                System.out.println(String.format("Skipped %d of %d", skipping.get(), osBuddyViews.size()));
                resuming.set(false);
            }
            if (resuming.get()) {
                skipping.getAndIncrement();
            } else {
                System.out.println(String.format("Processing %s", s));
                database.getItems().insertOne(process(s, osBuddyViews));
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
