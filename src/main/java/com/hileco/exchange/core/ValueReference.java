package com.hileco.exchange.core;

import org.bson.Document;

import java.util.Objects;

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

    @Override
    public String toString() {
        return Objects.toString(this.get());
    }
}
