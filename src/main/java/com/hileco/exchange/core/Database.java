package com.hileco.exchange.core;

import com.mongodb.DBRef;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;

public class Database {

    private static final String CONNECTION_STRING = "mongodb+srv://pepe:VzGP54FpW7ctWaTh@thegrandexchange-zules.azure.mongodb.net/test?retryWrites=true";
    private static final String DATABASE_NAME = "grandexchange";
    public static final String SOURCE_OS_BUDDY = "sourceOsBuddy";
    public static final String SOURCE_OFFICIAL = "sourceOfficial";
    public static final String SOURCE_WIKIA = "sourceWikia";
    public static final String METHOD_UNDERVALUED = "methodUndervalued";
    public static final String METHOD_OVERVALUED = "methodOvervalued";
    public static final String METHOD_GENERAL_STORE = "methodGeneralStore";

    private final Map<String, MongoCollection<Document>> collections;

    public Database() {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase grandExchange = mongoClient.getDatabase(DATABASE_NAME);
        this.collections = new HashMap<>();
        this.collections.put(SOURCE_OS_BUDDY, itemCollection(grandExchange, SOURCE_OS_BUDDY));
        this.collections.put(SOURCE_OFFICIAL, itemCollection(grandExchange, SOURCE_OFFICIAL));
        this.collections.put(SOURCE_WIKIA, itemCollection(grandExchange, SOURCE_WIKIA));
        this.collections.put(METHOD_UNDERVALUED, itemCollection(grandExchange, METHOD_UNDERVALUED));
        this.collections.put(METHOD_OVERVALUED, itemCollection(grandExchange, METHOD_OVERVALUED));
        this.collections.put(METHOD_GENERAL_STORE, itemCollection(grandExchange, METHOD_GENERAL_STORE));
    }

    private static MongoCollection<Document> itemCollection(MongoDatabase database, String name) {
        var collection = database.getCollection(name);
        collection.createIndex(Indexes.ascending("id"));
        collection.createIndex(Indexes.ascending("timestamp"));
        return collection;
    }

    public MongoCollection<Document> collection(String collection) {
        return this.collections.get(collection);
    }

    public Optional<Document> find(DBRef dbRef) {
        try (var cursor = this.collections.get(dbRef.getCollectionName())
                .find(Filters.eq("_id", dbRef.getId()))
                .limit(1)
                .iterator()) {
            if (cursor.hasNext()) {
                return Optional.of(cursor.next());
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<Document> findLast(String collection, String id) {
        try (var cursor = this.collections.get(collection).find(Filters.eq("id", id))
                .sort(Sorts.descending("timestamp"))
                .limit(1)
                .iterator()) {
            if (cursor.hasNext()) {
                return Optional.of(cursor.next());
            } else {
                return Optional.empty();
            }
        }
    }

    public Spliterator<String> findIds(String collection) {
        return this.collections.get(collection).distinct("id", String.class).batchSize(100).spliterator();
    }
}
