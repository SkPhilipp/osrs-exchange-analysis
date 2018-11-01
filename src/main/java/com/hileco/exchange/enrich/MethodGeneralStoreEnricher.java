package com.hileco.exchange.enrich;

import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import com.hileco.exchange.sources.wikia.WikiaView;
import org.bson.Document;

public class MethodGeneralStoreEnricher {

    public void enrich(Document document) {
        var osBuddyView = new OsBuddyView(document);
        var wikiaView = new WikiaView(document);
        if (osBuddyView.isAvailable() && wikiaView.isAvailable()) {
            wikiaView.highAlchemy().get();
            wikiaView.lowAlchemy().get();
            int price = (int) ((float) wikiaView.lowAlchemy().get() * 0.26f);
            int profit = price - osBuddyView.sellAverage().get();
            int profitPerClick = profit * 50;
            int profitPercent = profit * 100 / osBuddyView.sellAverage().get();
            boolean profitable = profit > 0;
            osBuddyView.sellAverage().get();
            var method = new MethodGeneralStoreView(document);
            method.profit().set(profit);
            method.profitPerClick().set(profitPerClick);
            method.profitPercent().set(profitPercent);
            method.profitable().set(profitable);
        }
    }
}
