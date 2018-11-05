package com.hileco.exchange.analysis;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import org.bson.Document;

public class UndervaluedView extends View {

    public final ValueReference<Double> deltaAbsolute;
    public final ValueReference<Double> deltaPercent;

    public UndervaluedView(Document document) {
        super(document);
        deltaAbsolute = new ValueReference<>(document, "deltaAbsolute");
        deltaPercent = new ValueReference<>(document, "deltaPercent");
    }
}
