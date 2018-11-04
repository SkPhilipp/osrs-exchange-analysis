package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;
import com.hileco.exchange.official.OfficialView;
import com.hileco.exchange.osbuddy.OsBuddyView;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Analysis method retrieving the top items to vendor.",
        name = "analyse-general-store")
public class GeneralStoreAnalyseCommand implements Runnable {

//    private static final BigDecimal GENERAL_STORE_MULTIPLIER = new BigDecimal("0.26");
//    private static final Double GENERAL_STORE_SELL_STACK_SIZE = new Double("50");

    @Option(names = {"-r", "--reload"}, description = "Whether to reload analysis data.")
    private boolean reload = false;

//        var osBuddyView = new OsBuddyView(document);
//        var wikiaView = new WikiaView(document);
//        if (osBuddyView.isAvailable() && wikiaView.isAvailable()) {
//            wikiaView.highAlchemy().get();
//            wikiaView.lowAlchemy().get();
//            var price = wikiaView.lowAlchemy().get().multiply(GENERAL_STORE_MULTIPLIER);
//            var profit = price.subtract(osBuddyView.sellAverage().get());
//            var profitPerClick = profit.multiply(GENERAL_STORE_SELL_STACK_SIZE);
//            var profitPercent = profit.divide(osBuddyView.sellAverage().get(), RoundingMode.HALF_DOWN);
//            var profitable = profit.compareTo(Double.ZERO) > 0;
//            osBuddyView.sellAverage().get();
//            var method = new GeneralStoreView(document);
//            method.profit().set(profit);
//            method.profitPerClick().set(profitPerClick);
//            method.profitPercent().set(profitPercent);
//            method.profitable().set(profitable);
//        }

    @Override
    public void run() {
        var database = new Database();
        var distinctIds = database.getSourceOsBuddy().distinct("id", String.class).batchSize(100);
        distinctIds.spliterator().forEachRemaining(itemId -> {
            System.out.println(itemId);
            var latestOsBuddy = database.getSourceOsBuddy()
                    .find(Filters.eq("id", itemId))
                    .sort(Sorts.descending("timestamp"))
                    .limit(1)
                    .iterator();
            var latestOfficial = database.getSourceOfficial()
                    .find(Filters.eq("id", itemId))
                    .sort(Sorts.descending("timestamp"))
                    .limit(1)
                    .iterator();
            if (latestOsBuddy.hasNext() && latestOfficial.hasNext()) {
                var officialView = new OfficialView(latestOfficial.next());
                var osBuddyView = new OsBuddyView(latestOsBuddy.next());
                System.out.println(String.format("-------------- %s", itemId));
                System.out.println(officialView.price().get());
                System.out.println(osBuddyView.buyAverage().get());
                System.out.println(osBuddyView.buyQuantity().get());
                System.out.println(osBuddyView.sellAverage().get());
                System.out.println(osBuddyView.sellQuantity().get());
            }
            latestOsBuddy.close();
            latestOfficial.close();
        });

        // Do a reload ?

        // If so delete all previous entries, and add all new entries

        // Grab the top ones by mongo query

        System.out.println(String.format("%24s %9s %9s %9s %9s",
                                         "NAME",
                                         "PRICE",
                                         "SELL_AVG",
                                         "SELL_QTY",
                                         "STORE_PRICE"));
    }
}
