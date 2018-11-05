package com.hileco.exchange.official;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import org.bson.Document;

public class OfficialView extends View {

    public final ValueReference<Double> price;
    public final ValueReference<Double> priceChange;
    public final ValueReference<String> price180;
    public final ValueReference<String> price90;
    public final ValueReference<String> price30;
    public final ValueReference<String> description;
    public final ValueReference<String> icon;
    public final ValueReference<String> iconLarge;
    public final ValueReference<Boolean> members;
    public final ValueReference<String> name;

    public OfficialView(Document document) {
        super(document);
        price = new ValueReference<>(document, "price");
        priceChange = new ValueReference<>(document, "priceChange");
        price180 = new ValueReference<>(document, "price180");
        price90 = new ValueReference<>(document, "price90");
        price30 = new ValueReference<>(document, "price30");
        description = new ValueReference<>(document, "description");
        icon = new ValueReference<>(document, "icon");
        iconLarge = new ValueReference<>(document, "iconLarge");
        members = new ValueReference<>(document, "members");
        name = new ValueReference<>(document, "name");
    }
}
