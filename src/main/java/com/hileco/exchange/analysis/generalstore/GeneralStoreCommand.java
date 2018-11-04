package com.hileco.exchange.analysis.generalstore;

import com.hileco.exchange.analysis.Enricher;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.hileco.exchange.wikia.WikiaView;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeneralStoreCommand implements Enricher {

    private static final BigDecimal GENERAL_STORE_MULTIPLIER = new BigDecimal("0.26");
    private static final BigDecimal GENERAL_STORE_SELL_STACK_SIZE = new BigDecimal("50");

    @Override
    public void enrich(Document document) {
        var osBuddyView = new OsBuddyView(document);
        var wikiaView = new WikiaView(document);
        if (osBuddyView.isAvailable() && wikiaView.isAvailable()) {
            wikiaView.highAlchemy().get();
            wikiaView.lowAlchemy().get();
            var price = wikiaView.lowAlchemy().get().multiply(GENERAL_STORE_MULTIPLIER);
            var profit = price.subtract(osBuddyView.sellAverage().get());
            var profitPerClick = profit.multiply(GENERAL_STORE_SELL_STACK_SIZE);
            var profitPercent = profit.divide(osBuddyView.sellAverage().get(), RoundingMode.HALF_DOWN);
            var profitable = profit.compareTo(BigDecimal.ZERO) > 0;
            osBuddyView.sellAverage().get();
            var method = new GeneralStoreView(document);
            method.profit().set(profit);
            method.profitPerClick().set(profitPerClick);
            method.profitPercent().set(profitPercent);
            method.profitable().set(profitable);
        }
    }
}
