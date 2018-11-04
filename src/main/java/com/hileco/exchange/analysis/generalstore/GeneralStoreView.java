package com.hileco.exchange.analysis.generalstore;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import org.bson.Document;

import java.math.BigDecimal;

public class GeneralStoreView extends View {
    private static final String VIEW_NAME = "methodGeneralStore";

    public GeneralStoreView(Document document) {
        super(document, VIEW_NAME);
        super.initialize();
    }

    public ValueReference<BigDecimal> profit() {
        return new ValueReference<>(this.get(), "profit");
    }

    public ValueReference<BigDecimal> profitPerClick() {
        return new ValueReference<>(this.get(), "profitPerClick");
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
                && profitPerClick().exists()
                && profitPercent().exists()
                && profitable().exists();
    }
}
