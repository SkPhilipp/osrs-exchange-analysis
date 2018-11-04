package com.hileco.exchange;

import com.hileco.exchange.analysis.overvalued.OvervaluedCommand;
import com.hileco.exchange.analysis.undervalued.UndervaluedCommand;
import com.hileco.exchange.official.LoadOfficial;
import com.hileco.exchange.osbuddy.LoadOsBuddy;
import picocli.CommandLine;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Command(description = "Tool for working with a virtual economy.", name = "exchange", subcommands = {
        LoadOfficial.class,
        LoadOsBuddy.class,
        UndervaluedCommand.class,
        OvervaluedCommand.class
})
public class Commands {

    public static void main(String[] args) throws Exception {
        var commandLine = new CommandLine(new Commands());
        var parse = commandLine.parse(args);
        var usage = true;
        for (var entry : parse) {
            var command = entry.getCommand();
            if (command instanceof Callable) {
                ((Callable) command).call();
                usage = false;
            }
        }
        if (usage) {
            commandLine.usage(System.err);
        }
    }
}
