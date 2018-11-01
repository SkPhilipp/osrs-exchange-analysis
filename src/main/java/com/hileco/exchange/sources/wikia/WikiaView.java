package com.hileco.exchange.sources.wikia;

import com.hileco.exchange.sources.ValueReference;
import com.hileco.exchange.sources.View;
import org.bson.Document;

import java.math.BigDecimal;

public class WikiaView extends View {

    private static final String VIEW_NAME = "wikia";

    public WikiaView(Document document) {
        super(document, VIEW_NAME);
    }

    public WikiaView() {
        super(new Document(), VIEW_NAME);
        super.initialize();
    }

    public ValueReference<String> page() {
        return new ValueReference<>(this.get(), "page");
    }

    public ValueReference<BigDecimal> highAlchemy() {
        return new ValueReference<>(this.get(), "highAlchemy");
    }

    public ValueReference<BigDecimal> lowAlchemy() {
        return new ValueReference<>(this.get(), "lowAlchemy");
    }

    @Override
    public boolean isAvailable() {
        return this.get() != null
                && page().exists()
                && highAlchemy().exists()
                && lowAlchemy().exists();
    }
}
