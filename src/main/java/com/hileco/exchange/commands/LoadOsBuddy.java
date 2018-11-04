package com.hileco.exchange.commands;

import picocli.CommandLine;

@CommandLine.Command(
        description = "Loads in the OsBuddy source.",
        name = "loadosbuddy",
        mixinStandardHelpOptions = true)
class LoadOsBuddy implements Runnable {

    @CommandLine.Option(names = {"-l", "--loop"}, description = "Continue loading indefinitely.")
    private boolean loop = false;

    @Override
    public void run() {
        System.out.println(loop);
    }
}
