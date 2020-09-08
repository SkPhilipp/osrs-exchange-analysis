# Exchange

Analyses products to gets the most profit out of the Grand Exchange! (in the game, ofcourse..)

    ./gradlew clean build
    java -jar build/libs/exchange.jar load-official
    java -jar build/libs/exchange.jar load-osbuddy
    java -jar build/libs/exchange.jar load-wikia
    java -jar build/libs/exchange.jar load-undervalued
    java -jar build/libs/exchange.jar load-overvalued
    java -jar build/libs/exchange.jar load-vendor

        Usage: exchange [COMMAND]
        Tool for working with a virtual economy.
        Commands:
          load-official     Loads in the official source.
          load-osbuddy      Loads in the OsBuddy source.
          load-wikia        Loads in the Wikia source.
          load-undervalued  Analysis method to compute the top items where the official
                              price is below the actual buy average.
          load-overvalued   Analysis method to compute the top items where the official
                              price is above the actual sell average.
          load-vendor       Analysis method to compute the top items to vendor.
          undervalued       Retrieve the top items currently undervalued by the Grand
                              Exchange.
          overvalued        Retrieve the top items currently overvalued by the Grand
                              Exchange.
          vendor            Retrieve the top items to vendor.

Note: This project has an old connection string somewhere in Database.java / Database.kt, you'll have to update it if you want to get it to work.
