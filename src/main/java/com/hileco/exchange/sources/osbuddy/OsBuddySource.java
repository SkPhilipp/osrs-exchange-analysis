package com.hileco.exchange.sources.osbuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hileco.exchange.sources.Currency;
import com.mashape.unirest.http.Unirest;
import org.json.JSONException;

import java.io.IOException;
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
                    .getBody()
                    .getEntity()
                    .getContent();
            var result = OBJECT_MAPPER.readTree(responseStream);
            result.fieldNames().forEachRemaining(itemId -> {
                var osBuddyView = new OsBuddyView();
                var item = result.get(itemId);
                osBuddyView.name().set(item.get("name").textValue());
                osBuddyView.members().set(item.get("members").booleanValue());
                osBuddyView.sp().set(Currency.from(item.get("sp").textValue());
                osBuddyView.buyAverage().set(Currency.from(item.get("buy_average").textValue()));
                osBuddyView.buyQuantity().set(Currency.from(item.get("buy_quantity").textValue()));
                osBuddyView.sellAverage().set(Currency.from(item.get("sell_average").textValue()));
                osBuddyView.sellQuantity().set(Currency.from(item.get("sell_quantity").textValue()));
                osBuddyView.overallAverage().set(Currency.from(item.get("overall_average").textValue()));
                osBuddyView.overallQuantity().set(Currency.from(item.get("overall_quantity").textValue()));
                mapping.put(itemId, osBuddyView);
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }
}
