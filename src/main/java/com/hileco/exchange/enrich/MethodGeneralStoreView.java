package com.hileco.exchange.enrich;

import com.hileco.exchange.sources.ValueReference;
import com.hileco.exchange.sources.View;
import org.bson.Document;

public class MethodGeneralStoreView extends View {
    private static final String VIEW_NAME = "methodGeneralStore";

    public MethodGeneralStoreView(Document document) {
        super(document, VIEW_NAME);
        super.initialize();
    }

    public ValueReference<Integer> profit() {
        return new ValueReference<>(this.get(), "profit");
    }

    public ValueReference<Integer> profitPerClick() {
        return new ValueReference<>(this.get(), "profitPerClick");
    }

    public ValueReference<Integer> profitPercent() {
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
