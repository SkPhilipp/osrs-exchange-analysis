package com.hileco.exchange;

import com.hileco.exchange.analysis.GeneralStoreAnalyseCommand;
import com.hileco.exchange.analysis.GeneralStoreCommand;
import com.hileco.exchange.analysis.OvervaluedAnalyseCommand;
import com.hileco.exchange.analysis.OvervaluedCommand;
import com.hileco.exchange.analysis.UndervaluedAnalyseCommand;
import com.hileco.exchange.analysis.UndervaluedCommand;
import com.hileco.exchange.official.OfficialLoadCommand;
import com.hileco.exchange.osbuddy.OsBuddyLoadCommand;
import com.hileco.exchange.wikia.WikiaLoadCommand;
import picocli.CommandLine;

import static picocli.CommandLine.Command;

@Command(description = "Tool for working with a virtual economy.", name = "exchange", subcommands = {
        OfficialLoadCommand.class,
        OsBuddyLoadCommand.class,
        WikiaLoadCommand.class,
        UndervaluedAnalyseCommand.class,
        OvervaluedAnalyseCommand.class,
        GeneralStoreAnalyseCommand.class,
        UndervaluedCommand.class,
        OvervaluedCommand.class,
        GeneralStoreCommand.class
})
public class Commands {

    public static void main(String[] args) {
        var commandLine = new CommandLine(new Commands());
        var parse = commandLine.parse(args);
        var usage = true;
        var exitCode = 0;
        for (var entry : parse) {
            var command = entry.getCommand();
            if (command instanceof Runnable) {
                ((Runnable) command).run();
                usage = false;
            }
        }
        if (usage) {
            commandLine.usage(System.err);
            exitCode = 1;
        }
        System.exit(exitCode);
    }
}
