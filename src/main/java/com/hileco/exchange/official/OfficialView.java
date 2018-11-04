package com.hileco.exchange.official;

import com.hileco.exchange.core.ValueReference;
import org.bson.Document;

import java.time.LocalDateTime;

public class OfficialView {

    private final Document document;

    public OfficialView(Document document) {
        this.document = document;
    }

    public ValueReference<String> id() {
        return new ValueReference<>(document, "id");
    }

    public ValueReference<LocalDateTime> timestamp() {
        return new ValueReference<>(document, "timestamp");
    }

    public ValueReference<Double> price() {
        return new ValueReference<>(document, "price");
    }

    public ValueReference<Double> priceChange() {
        return new ValueReference<>(document, "priceChange");
    }

    public ValueReference<String> price180() {
        return new ValueReference<>(document, "price180");
    }

    public ValueReference<String> price90() {
        return new ValueReference<>(document, "price90");
    }

    public ValueReference<String> price30() {
        return new ValueReference<>(document, "price30");
    }

    public ValueReference<String> description() {
        return new ValueReference<>(document, "description");
    }

    public ValueReference<String> icon() {
        return new ValueReference<>(document, "icon");
    }

    public ValueReference<String> iconLarge() {
        return new ValueReference<>(document, "iconLarge");
    }

    public ValueReference<Boolean> members() {
        return new ValueReference<>(document, "members");
    }

    public ValueReference<String> name() {
        return new ValueReference<>(document, "name");
    }
}
