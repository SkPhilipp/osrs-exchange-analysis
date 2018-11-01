package com.hileco.exchange.sources;

import org.bson.Document;

public class ValueReference<T> {

    private Document parent;
    private String name;

    public ValueReference(Document parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public void set(T value) {
        parent.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) parent.get(name);
    }

    public boolean exists() {
        return parent.containsKey(name);
    }
}
