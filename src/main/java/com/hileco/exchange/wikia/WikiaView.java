package com.hileco.exchange.wikia;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import com.mongodb.DBRef;
import org.bson.Document;

import static com.hileco.exchange.core.Database.SOURCE_WIKIA;

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

    @Override
    public DBRef reference() {
        return new DBRef(SOURCE_WIKIA, this.objectId());
    }
}
