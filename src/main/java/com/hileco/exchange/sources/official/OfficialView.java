package com.hileco.exchange.sources.official;

import com.hileco.exchange.sources.ValueReference;
import com.hileco.exchange.sources.View;
import org.bson.Document;

import java.math.BigDecimal;

public class OfficialView extends View {

    private static final String VIEW_NAME = "official";

    public OfficialView(Document document) {
        super(document, VIEW_NAME);
    }

    public OfficialView() {
        super(new Document(), "official");
        super.initialize();
    }

    public ValueReference<BigDecimal> price() {
        return new ValueReference<>(this.get(), "price");
    }

    public ValueReference<BigDecimal> priceChange() {
        return new ValueReference<>(this.get(), "priceChange");
    }

    public ValueReference<String> price180() {
        return new ValueReference<>(this.get(), "price180");
    }

    public ValueReference<String> price90() {
        return new ValueReference<>(this.get(), "price90");
    }

    public ValueReference<String> price30() {
        return new ValueReference<>(this.get(), "price30");
    }

    public ValueReference<String> description() {
        return new ValueReference<>(this.get(), "description");
    }

    public ValueReference<String> icon() {
        return new ValueReference<>(this.get(), "icon");
    }

    public ValueReference<String> iconLarge() {
        return new ValueReference<>(this.get(), "iconLarge");
    }

    public ValueReference<Boolean> members() {
        return new ValueReference<>(this.get(), "members");
    }

    public ValueReference<String> name() {
        return new ValueReference<>(this.get(), "name");
    }

    @Override
    public boolean isAvailable() {
        return this.get() != null
                && price().exists()
                && priceChange().exists()
                && price180().exists()
                && price90().exists()
                && price30().exists()
                && description().exists()
                && icon().exists()
                && iconLarge().exists()
                && members().exists()
                && name().exists();
    }
}
