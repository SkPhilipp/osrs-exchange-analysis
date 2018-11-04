package com.hileco.exchange.osbuddy;

import com.hileco.exchange.core.ValueReference;
import org.bson.Document;

import java.time.LocalDateTime;

public class OsBuddyView {

    private final Document document;

    public OsBuddyView(Document document) {
        this.document = document;
    }

    public ValueReference<String> id() {
        return new ValueReference<>(document, "id");
    }

    public ValueReference<LocalDateTime> timestamp() {
        return new ValueReference<>(document, "timestamp");
    }

    public ValueReference<String> name() {
        return new ValueReference<>(document, "name");
    }

    public ValueReference<Boolean> members() {
        return new ValueReference<>(document, "members");
    }

    public ValueReference<Double> sp() {
        return new ValueReference<>(document, "sp");
    }

    public ValueReference<Double> buyAverage() {
        return new ValueReference<>(document, "buyAverage");
    }

    public ValueReference<Double> buyQuantity() {
        return new ValueReference<>(document, "buyQuantity");
    }

    public ValueReference<Double> sellAverage() {
        return new ValueReference<>(document, "sellAverage");
    }

    public ValueReference<Double> sellQuantity() {
        return new ValueReference<>(document, "sellQuantity");
    }

    public ValueReference<Double> overallAverage() {
        return new ValueReference<>(document, "overallAverage");
    }

    public ValueReference<Double> overallQuantity() {
        return new ValueReference<>(document, "overallQuantity");
    }
}
