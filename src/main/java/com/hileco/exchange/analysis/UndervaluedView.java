package com.hileco.exchange.analysis;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import com.mongodb.DBRef;
import org.bson.Document;

import static com.hileco.exchange.core.Database.METHOD_UNDERVALUED;

public class UndervaluedView extends View {

    public final ValueReference<Double> deltaAbsolute;
    public final ValueReference<Double> deltaPercent;
    public final ValueReference<DBRef> official;
    public final ValueReference<DBRef> osBuddy;

    public UndervaluedView(Document document) {
        super(document);
        deltaAbsolute = new ValueReference<>(document, "deltaAbsolute");
        deltaPercent = new ValueReference<>(document, "deltaPercent");
        official = new ValueReference<>(document, "official");
        osBuddy = new ValueReference<>(document, "osBuddy");
    }

    @Override
    public DBRef reference() {
        return new DBRef(METHOD_UNDERVALUED, this.objectId());
    }
}
