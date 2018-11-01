package com.hileco.exchange.sources.osbuddy;

import com.hileco.exchange.sources.ValueReference;
import com.hileco.exchange.sources.View;
import org.bson.Document;

import java.math.BigDecimal;

public class OsBuddyView extends View {

    private static final String VIEW_NAME = "osBuddy";

    public OsBuddyView(Document document) {
        super(document, VIEW_NAME);
    }

    public OsBuddyView() {
        super(new Document(), VIEW_NAME);
        super.initialize();
    }

    public ValueReference<String> name() {
        return new ValueReference<>(this.get(), "name");
    }

    public ValueReference<Boolean> members() {
        return new ValueReference<>(this.get(), "members");
    }

    public ValueReference<BigDecimal> sp() {
        return new ValueReference<>(this.get(), "sp");
    }

    public ValueReference<BigDecimal> buyAverage() {
        return new ValueReference<>(this.get(), "buyAverage");
    }

    public ValueReference<BigDecimal> buyQuantity() {
        return new ValueReference<>(this.get(), "buyQuantity");
    }

    public ValueReference<BigDecimal> sellAverage() {
        return new ValueReference<>(this.get(), "sellAverage");
    }

    public ValueReference<BigDecimal> sellQuantity() {
        return new ValueReference<>(this.get(), "sellQuantity");
    }

    public ValueReference<BigDecimal> overallAverage() {
        return new ValueReference<>(this.get(), "overallAverage");
    }

    public ValueReference<BigDecimal> overallQuantity() {
        return new ValueReference<>(this.get(), "overallQuantity");
    }

    @Override
    public boolean isAvailable() {
        return this.get() != null
                && name().exists()
                && members().exists()
                && sp().exists()
                && buyAverage().exists()
                && buyQuantity().exists()
                && sellAverage().exists()
                && sellQuantity().exists()
                && overallAverage().exists()
                && overallQuantity().exists();
    }
}
