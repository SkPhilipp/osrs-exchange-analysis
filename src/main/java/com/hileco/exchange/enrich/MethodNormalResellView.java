package com.hileco.exchange.enrich;

import com.hileco.exchange.sources.ValueReference;
import com.hileco.exchange.sources.View;
import org.bson.Document;

import java.math.BigDecimal;

public class MethodNormalResellView extends View {
    private static final String VIEW_NAME = "methodNormalResell";

    public MethodNormalResellView(Document document) {
        super(document, VIEW_NAME);
        super.initialize();
    }

    public ValueReference<BigDecimal> profit() {
        return new ValueReference<>(this.get(), "profit");
    }

    public ValueReference<BigDecimal> profitPercent() {
        return new ValueReference<>(this.get(), "profitPercent");
    }

    public ValueReference<Boolean> profitable() {
        return new ValueReference<>(this.get(), "profitable");
    }

    @Override
    public boolean isAvailable() {
        return this.get() != null
                && profit().exists()
                && profitPercent().exists()
                && profitable().exists();
    }
}