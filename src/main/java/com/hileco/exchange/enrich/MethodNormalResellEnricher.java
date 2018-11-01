package com.hileco.exchange.enrich;

import com.hileco.exchange.sources.official.OfficialView;
import com.hileco.exchange.sources.osbuddy.OsBuddyView;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MethodNormalResellEnricher {

    public void enrich(Document document) {
        var osBuddyView = new OsBuddyView(document);
        var officialView = new OfficialView(document);
        if (osBuddyView.isAvailable() && officialView.isAvailable()) {
            var osBuddyBuyAverage = osBuddyView.buyAverage().get();
            var officialPrice = officialView.price().get();
            var profit = osBuddyBuyAverage.subtract(officialPrice);
            var profitPercent = profit.divide(officialPrice, RoundingMode.HALF_DOWN);
            var profitable = profit.compareTo(BigDecimal.ZERO) > 0;
            var method = new MethodNormalResellView(document);
            method.profit().set(profit);
            method.profitPercent().set(profitPercent);
            method.profitable().set(profitable);
        }
    }
}
