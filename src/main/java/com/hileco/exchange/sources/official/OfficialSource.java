package com.hileco.exchange.sources.official;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hileco.exchange.sources.Currency;
import com.mashape.unirest.http.Unirest;
import org.json.JSONException;

import java.io.IOException;

public class OfficialSource {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public OfficialView view(String itemId) {
        var officialView = new OfficialView();
        try {
            var responseStream = Unirest.get("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json")
                    .header("accept", "application/json")
                    .queryString("item", itemId)
                    .getBody()
                    .getEntity()
                    .getContent();
            try {
                var item = OBJECT_MAPPER.readTree(responseStream).get("item");
                officialView.price().set(Currency.from(item.get("current").get("price").textValue()));
                officialView.priceChange().set(Currency.from(item.get("today").get("price").textValue()));
                officialView.price180().set(item.get("day180").get("change").textValue());
                officialView.price90().set(item.get("day90").get("change").textValue());
                officialView.price30().set(item.get("day30").get("change").textValue());
                officialView.description().set(item.get("description").textValue());
                officialView.icon().set(item.get("icon").textValue());
                officialView.iconLarge().set(item.get("icon_large").textValue());
                officialView.members().set(item.get("members").booleanValue());
                officialView.name().set(item.get("name").textValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return officialView;
    }
}
