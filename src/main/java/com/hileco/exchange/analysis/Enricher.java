package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import org.bson.Document;

public interface Enricher {

    default void enrichAll(Database database) {
        database.getItems().find().spliterator().forEachRemaining(this::enrich);
    }

    void enrich(Document document);
}
