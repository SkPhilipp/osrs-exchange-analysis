package com.hileco.exchange.core;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.Optional;

public class Database {

    private static final String CONNECTION_STRING = "mongodb+srv://pepe:VzGP54FpW7ctWaTh@thegrandexchange-zules.azure.mongodb.net/test?retryWrites=true";
    private static final String DATABASE_NAME = "grandexchange";
    private static final String COLLECTION_SOURCE_OS_BUDDY = "sourceOsBuddy";
    private static final String COLLECTION_SOURCE_OFFICIAL = "sourceOfficial";
    private static final String COLLECTION_METHOD_UNDERVALUED = "methodUndervalued";
    private static final String COLLECTION_METHOD_OVERVALUED = "methodOvervalued";
    private static final String COLLECTION_METHOD_GENERAL_STORE = "methodGeneralStore";

    private final MongoCollection<Document> sourceOsBuddy;
    private final MongoCollection<Document> sourceOfficial;
    private final MongoCollection<Document> methodUndervalued;
    private final MongoCollection<Document> methodOvervalued;
    private final MongoCollection<Document> methodGeneralStore;

    public Database() {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase grandExchange = mongoClient.getDatabase(DATABASE_NAME);
        this.sourceOsBuddy = itemCollection(grandExchange, COLLECTION_SOURCE_OS_BUDDY);
        this.sourceOfficial = itemCollection(grandExchange, COLLECTION_SOURCE_OFFICIAL);
        this.methodUndervalued = itemCollection(grandExchange, COLLECTION_METHOD_UNDERVALUED);
        this.methodOvervalued = itemCollection(grandExchange, COLLECTION_METHOD_OVERVALUED);
        this.methodGeneralStore = itemCollection(grandExchange, COLLECTION_METHOD_GENERAL_STORE);
    }

    private static MongoCollection<Document> itemCollection(MongoDatabase database, String name) {
        var collection = database.getCollection(name);
        collection.createIndex(Indexes.ascending("id"));
        collection.createIndex(Indexes.ascending("timestamp"));
        return collection;
    }

    public MongoCollection<Document> getSourceOsBuddy() {
        return sourceOsBuddy;
    }

    public MongoCollection<Document> getSourceOfficial() {
        return sourceOfficial;
    }

    public MongoCollection<Document> getMethodUndervalued() {
        return methodUndervalued;
    }

    public MongoCollection<Document> getMethodOvervalued() {
        return methodOvervalued;
    }

    public MongoCollection<Document> getMethodGeneralStore() {
        return methodGeneralStore;
    }

    public Optional<Document> findLast(MongoCollection<Document> collection, String id) {
        try (var cursor = collection.find(Filters.eq("id", id))
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
}
