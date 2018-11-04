package com.hileco.exchange.analysis;

import com.hileco.exchange.core.ValueReference;
import org.bson.Document;

import java.time.LocalDateTime;

public class UndervaluedView {

    private final Document document;

    public UndervaluedView(Document document) {
        this.document = document;
    }

    public ValueReference<String> id() {
        return new ValueReference<>(document, "id");
    }

    public ValueReference<LocalDateTime> timestamp() {
        return new ValueReference<>(document, "timestamp");
    }

    public ValueReference<Double> deltaAbsolute() {
        return new ValueReference<>(document, "deltaAbsolute");
    }

    public ValueReference<Double> deltaPercent() {
        return new ValueReference<>(document, "deltaPercent");
    }
}
