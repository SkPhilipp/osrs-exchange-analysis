package com.hileco.exchange.core;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

public class Database {

    private static final String CONNECTION_STRING = "mongodb+srv://pepe:VzGP54FpW7ctWaTh@thegrandexchange-zules.azure.mongodb.net/test?retryWrites=true";
    private static final String DATABASE_NAME = "grandexchange";
    private static final String COLLECTION_ITEMS = "items";

    private final MongoClient mongoClient;
    private final MongoDatabase grandExchange;
    private final MongoCollection<Document> items;

    public Database() {
        this.mongoClient = MongoClients.create(CONNECTION_STRING);
        this.grandExchange = mongoClient.getDatabase(DATABASE_NAME);
        this.items = grandExchange.getCollection(COLLECTION_ITEMS);
        this.items.createIndex(Indexes.ascending("id"));
        this.items.createIndex(Indexes.ascending("timestamp"));
    }

    public MongoCollection<Document> getItems() {
        return items;
    }
}
