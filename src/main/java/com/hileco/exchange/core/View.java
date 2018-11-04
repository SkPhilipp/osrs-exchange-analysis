package com.hileco.exchange.core;

import org.bson.Document;

public abstract class View {
    private Document document;
    private String name;

    public View(Document document, String name) {
        this.document = document;
        this.name = name;
    }

    public abstract boolean isAvailable();

    protected Document get() {
        return (Document) document.get(name);
    }

    protected void initialize() {
        if (!document.containsKey(name)) {
            document.put(name, new Document());
        }
    }

    public void writeInto(Document other) {
        other.put(name, this.get());
    }
}
