package com.hileco.exchange.analysis;

import com.hileco.exchange.core.Database;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Analysis method retrieving the top undervalued items.",
        name = "analyse-undervalued")
public class UndervaluedAnalyseCommand implements Runnable {

    @Option(names = {"-r", "--r"}, description = "Whether to reload analysis data.")
    private boolean reload = false;

//        database.getItems()
//                .find(Filters.and(Filters.gte("methodNormalResell.profitPercent", 1),
//                                  Filters.gte("osBuddy.buyQuantity", 50)))
//                .sort(Sorts.descending("methodNormalResell.profitPercent"))
//                .spliterator()
//                .forEachRemaining(document -> {
//                    var officialView = new OfficialView(document);
//                    var osBuddyView = new OsBuddyView(document);
//                    System.out.println(String.format("%24s %9s %9s %9s %9s %9s",
//                                                     osBuddyView.name().get(),
//                                                     officialView.price().get(),
//                                                     osBuddyView.sellAverage().get(),
//                                                     osBuddyView.sellQuantity().get(),
//                                                     osBuddyView.buyAverage().get(),
//                                                     osBuddyView.buyQuantity().get()));
//                });
//    }
//
//    public void enrich(Document document) {
//        var osBuddyView = new OsBuddyView(document);
//        var officialView = new OfficialView(document);
//        var osBuddyBuyAverage = osBuddyView.buyAverage().get();
//        var officialPrice = officialView.price().get();
//        var profit = osBuddyBuyAverage.subtract(officialPrice);
//        var profitPercent = profit.divide(officialPrice, RoundingMode.HALF_DOWN);
//        var profitable = profit.compareTo(Double.ZERO) > 0;
//        var method = new UndervaluedView(document);
//        method.profit().set(profit);
//        method.profitPercent().set(profitPercent);
//        method.profitable().set(profitable);
//    }

    //    private final OfficialSource officialSource = new OfficialSource();
//    private final OsBuddyLoadCommand osBuddyCommand = new OsBuddyLoadCommand();
//    private final WikiaCommand wikiaSource = new WikiaCommand();
//    private final Database database = new Database();
//
//    public static void main(String[] args) {
//        var application = new Application();
//        application.run();
//    }
//
//    private void run() {
//        osBuddyViews.forEach((s, osBuddyView) -> {
//            // TODO: Instead query mongo to whether the item should be (GE-)updated
//            if (resumeFrom.equals(s)) {
//                System.out.println(String.format("Skipped %d of %d", skipping.get(), osBuddyViews.size()));
//                resuming.set(false);
//            }
//            if (resuming.get()) {
//                skipping.getAndIncrement();
//            } else {
//                System.out.println(String.format("Processing %s", s));
//                database.getItems().insertOne(process(s, osBuddyViews));
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private Document process(String id, Map<String, OsBuddyView> osBuddyViews) {
//        var osBuddyView = osBuddyViews.get(id);
//        var officialView = officialSource.view(id);
//        var wikiaView = wikiaSource.wikiaView(id);
//        Document document = new Document();
//        document.put("id", id);
//        document.put("timestamp", LocalDateTime.now());
//        if (osBuddyView.isAvailable()) {
//            osBuddyView.writeInto(document);
//        }
//        if (officialView.isAvailable()) {
//            officialView.writeInto(document);
//        }
//        if (wikiaView.isAvailable()) {
//            wikiaView.writeInto(document);
//        }
//        return document;
//    }

    @Override
    public void run() {
        var database = new Database();
        // Do a reload ?

        // If so delete all previous entries, and add all new entries

        // Grab the top ones by mongo query
        System.out.println(String.format("%24s %9s %9s %9s %9s %9s",
                                         "NAME",
                                         "PRICE",
                                         "SELL_AVG",
                                         "SELL_QTY",
                                         "BUY_AVG",
                                         "BUY_QTY"));
    }
}
