package com.hileco.exchange.wikia;

import com.hileco.exchange.core.ValueReference;
import org.bson.Document;

import java.time.LocalDateTime;

public class WikiaView {

    private final Document document;

    public WikiaView(Document document) {
        this.document = document;
    }

    public ValueReference<String> id() {
        return new ValueReference<>(document, "id");
    }

    public ValueReference<LocalDateTime> timestamp() {
        return new ValueReference<>(document, "timestamp");
    }

    public ValueReference<String> page() {
        return new ValueReference<>(document, "page");
    }

    public ValueReference<Double> highAlchemy() {
        return new ValueReference<>(document, "highAlchemy");
    }

    public ValueReference<Double> lowAlchemy() {
        return new ValueReference<>(document, "lowAlchemy");
    }
}
