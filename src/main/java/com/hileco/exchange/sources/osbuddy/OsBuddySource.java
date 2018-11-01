package com.hileco.exchange.sources.osbuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hileco.exchange.sources.Currency;
import com.mashape.unirest.http.Unirest;

import java.util.HashMap;
import java.util.Map;

public class OsBuddySource {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * @return map of item id's to views
     */
    public Map<String, OsBuddyView> views() {
        var mapping = new HashMap<String, OsBuddyView>();
        try {
            var responseStream = Unirest.get("https://storage.googleapis.com/osbuddy-exchange/summary.json")
                    .header("accept", "application/json")
                    .asBinary()
                    .getBody();
            var result = OBJECT_MAPPER.readTree(responseStream);
            result.fieldNames().forEachRemaining(itemId -> {
                var osBuddyView = new OsBuddyView();
                var item = result.get(itemId);
                osBuddyView.name().set(item.get("name").asText());
                osBuddyView.members().set(item.get("members").asBoolean());
                osBuddyView.sp().set(Currency.from(item.get("sp").asText()));
                osBuddyView.buyAverage().set(Currency.from(item.get("buy_average").asText()));
                osBuddyView.buyQuantity().set(Currency.from(item.get("buy_quantity").asText()));
                osBuddyView.sellAverage().set(Currency.from(item.get("sell_average").asText()));
                osBuddyView.sellQuantity().set(Currency.from(item.get("sell_quantity").asText()));
                osBuddyView.overallAverage().set(Currency.from(item.get("overall_average").asText()));
                osBuddyView.overallQuantity().set(Currency.from(item.get("overall_quantity").asText()));
                mapping.put(itemId, osBuddyView);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping;
    }
}
