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

    public ValueReference<LocalDateTime> timestamp() {
        return new ValueReference<>(document, "timestamp");
    }
}
