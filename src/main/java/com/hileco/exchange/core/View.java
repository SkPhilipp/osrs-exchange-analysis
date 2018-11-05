package com.hileco.exchange.core;

import com.mongodb.DBRef;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public abstract class View {
    private final Document document;
    public final ValueReference<String> id;
    public final ValueReference<LocalDateTime> timestamp;

    public View(Document document) {
        this.document = document;
        this.id = new ValueReference<>(document, "id");
        this.timestamp = new ValueReference<>(document, "timestamp");
    }

    abstract public DBRef reference();

    public ObjectId objectId() {
        return document.getObjectId("_id");
    }
}
