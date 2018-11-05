package com.hileco.exchange.wikia;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import org.bson.Document;

public class WikiaView extends View {
    public final ValueReference<String> page;
    public final ValueReference<Double> highAlchemy;
    public final ValueReference<Double> lowAlchemy;

    public WikiaView(Document document) {
        super(document);
        page = new ValueReference<>(document, "page");
        highAlchemy = new ValueReference<>(document, "highAlchemy");
        lowAlchemy = new ValueReference<>(document, "lowAlchemy");
    }
}
