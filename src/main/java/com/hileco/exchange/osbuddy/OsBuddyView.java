package com.hileco.exchange.osbuddy;

import com.hileco.exchange.core.ValueReference;
import com.hileco.exchange.core.View;
import com.mongodb.DBRef;
import org.bson.Document;

import static com.hileco.exchange.core.Database.SOURCE_OS_BUDDY;

public class OsBuddyView extends View {

    public final ValueReference<String> name;
    public final ValueReference<Boolean> members;
    public final ValueReference<Double> sp;
    public final ValueReference<Double> buyAverage;
    public final ValueReference<Double> buyQuantity;
    public final ValueReference<Double> sellAverage;
    public final ValueReference<Double> sellQuantity;
    public final ValueReference<Double> overallAverage;
    public final ValueReference<Double> overallQuantity;

    public OsBuddyView(Document document) {
        super(document);
        name = new ValueReference<>(document, "name");
        members = new ValueReference<>(document, "members");
        sp = new ValueReference<>(document, "sp");
        buyAverage = new ValueReference<>(document, "buyAverage");
        buyQuantity = new ValueReference<>(document, "buyQuantity");
        sellAverage = new ValueReference<>(document, "sellAverage");
        sellQuantity = new ValueReference<>(document, "sellQuantity");
        overallAverage = new ValueReference<>(document, "overallAverage");
        overallQuantity = new ValueReference<>(document, "overallQuantity");
    }

    @Override
    public DBRef reference() {
        return new DBRef(SOURCE_OS_BUDDY, this.objectId());
    }
}
