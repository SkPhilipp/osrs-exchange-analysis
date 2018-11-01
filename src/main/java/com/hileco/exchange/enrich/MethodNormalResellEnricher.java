package com.hileco.exchange.enrich;

import com.hileco.exchange.sources.official.OfficialView;
import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import org.bson.Document;

public class MethodNormalResellEnricher {

    public void enrich(Document document) {
        var osBuddyView = new OsBuddyView(document);
        var officialView = new OfficialView(document);
        if (osBuddyView.isAvailable() && officialView.isAvailable()) {
            var osBuddyBuyAverage = osBuddyView.buyAverage().get();
            var officialPrice = officialView.price().get();
            var profit = osBuddyBuyAverage - officialPrice;
            var profitPercent = profit * 100 / officialPrice;
            var profitable = profit > 0;
            var method = new MethodNormalResellView(document);
            method.profit().set(profit);
            method.profitPercent().set(profitPercent);
            method.profitable().set(profitable);
        }
    }
}
