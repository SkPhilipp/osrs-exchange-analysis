package com.hileco.exchange.sources.wikia;

public class WikiaSource {

    public WikiaView wikiaView(String itemId) {
        /*
            PageName=$1 | tr ' ' '_'
            cache http://oldschoolrunescape.wikia.com/wiki/${PageName} $(source_wikia ${PageName})
            cat | pup ':parent-of(:parent-of([title="High Level Alchemy"])) .pi-data-value text{}' | first
            cat | pup ':parent-of(:parent-of([title="Low Level Alchemy"])) .pi-data-value text{}' | first
            ItemId=$1
            Destination=$(local_main ${ItemId})
            WikiaName=$(cat ${Destination} | jq -r '.osbuddy.name' | source_wikia_convert_name)
            WikiaHighAlchemy=$(source_wikia_load ${WikiaName} | source_wikia_high_alchemy)
            WikiaLowAlchemy=$(source_wikia_load ${WikiaName} | source_wikia_low_alchemy)
            function first() {
                cat | tr '\n' ' ' | awk '{print $1}'
            }
         */
        return new WikiaView();
    }
}