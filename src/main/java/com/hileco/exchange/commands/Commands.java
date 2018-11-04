package com.hileco.exchange.commands;

import picocli.CommandLine;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Command(description = "Tool for working with a virtual economy.", name = "ge", subcommands = {
        LoadOfficial.class,
        LoadOsBuddy.class,
        Undervalued.class
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
