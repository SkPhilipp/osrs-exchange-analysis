package com.hileco.exchange.official;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hileco.exchange.core.Currency;
import com.mashape.unirest.http.Unirest;

public class OfficialSource {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public OfficialView view(String itemId) {
        var officialView = new OfficialView();
        try {
            // TODO: Rate-limiting appears to return no-content 200 OK responses
            var responseStream = Unirest.get("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json")
                    .header("accept", "application/json")
                    .queryString("item", itemId)
                    .asBinary()
                    .getBody();
            var item = OBJECT_MAPPER.readTree(responseStream).get("item");
            officialView.price().set(Currency.from(item.get("current").get("price").asText()));
            officialView.priceChange().set(Currency.from(item.get("today").get("price").asText()));
            officialView.price180().set(item.get("day180").get("change").asText());
            officialView.price90().set(item.get("day90").get("change").asText());
            officialView.price30().set(item.get("day30").get("change").asText());
            officialView.description().set(item.get("description").asText());
            officialView.icon().set(item.get("icon").asText());
            officialView.iconLarge().set(item.get("icon_large").asText());
            officialView.members().set(item.get("members").asBoolean());
            officialView.name().set(item.get("name").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return officialView;
    }
}
