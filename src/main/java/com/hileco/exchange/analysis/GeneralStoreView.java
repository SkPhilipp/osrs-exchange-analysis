package com.hileco.exchange.analysis;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import com.mongodb.DBRef;
import org.bson.Document;

import static com.hileco.exchange.core.Database.METHOD_GENERAL_STORE;

public class GeneralStoreView extends View {

    public final ValueReference<Double> deltaAbsolute;
    public final ValueReference<Double> deltaAbsoluteStack;
    public final ValueReference<Double> deltaPercent;
    public final ValueReference<DBRef> official;
    public final ValueReference<DBRef> wikia;

    public GeneralStoreView(Document document) {
        super(document);
        deltaAbsolute = new ValueReference<>(document, "deltaAbsolute");
        deltaAbsoluteStack = new ValueReference<>(document, "deltaAbsoluteStack");
        deltaPercent = new ValueReference<>(document, "deltaPercent");
        official = new ValueReference<>(document, "official");
        wikia = new ValueReference<>(document, "wikia");
    }

    @Override
    public DBRef reference() {
        return new DBRef(METHOD_GENERAL_STORE, this.objectId());
    }
}
