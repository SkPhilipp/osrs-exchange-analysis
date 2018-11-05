package com.hileco.exchange.analysis;

import com.hileco.exchange.core.ValueReference;
import org.bson.Document;

import java.time.LocalDateTime;

public class GeneralStoreView {

    private final Document document;

    public GeneralStoreView(Document document) {
        this.document = document;
    }

    public ValueReference<String> id() {
        return new ValueReference<>(document, "id");
    }

    public ValueReference<String> name() {
        return new ValueReference<>(document, "name");
    }

    public ValueReference<LocalDateTime> timestamp() {
        return new ValueReference<>(document, "timestamp");
    }

    public ValueReference<Double> officialPrice() {
        return new ValueReference<>(document, "officialPrice");
    }

    public ValueReference<Double> generalStorePrice() {
        return new ValueReference<>(document, "generalStorePrice");
    }

    public ValueReference<Double> deltaAbsolute() {
        return new ValueReference<>(document, "deltaAbsolute");
    }

    public ValueReference<Double> deltaAbsoluteStack() {
        return new ValueReference<>(document, "deltaAbsoluteStack");
    }

    public ValueReference<Double> deltaPercent() {
        return new ValueReference<>(document, "deltaPercent");
    }
}
