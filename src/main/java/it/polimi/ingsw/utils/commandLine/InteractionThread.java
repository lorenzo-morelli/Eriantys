package it.polimi.ingsw.utils.commandLine;

import java.io.IOException;

/**
 * Thread di richiesta di interazione con la CLI
 * Viene printato a video un suggerimento del comando che l'utente dovrebbe inserire
 */

public class InteractionThread implements Runnable{
    private final Thread t;
    private volatile boolean shouldStop = false;
    private final String suggestion;
    private CommandPrompt commandPrompt = CommandPrompt.getInstance();

    InteractionThread(String suggestion) throws IOException {
        t = new Thread (this, "Thread di richiesta di interazione con la CLI");
        this.suggestion = suggestion;
    }

    public void start() {
        t.start();
    }

    public void stop() {
        shouldStop = true;
    }

    @Override
    public void run()  {
        while(!shouldStop)
        {
            try {
                commandPrompt.getConsole().println(suggestion);
                commandPrompt.getConsole().setPrompt("Eriantys> ");
                CommandPrompt.read();
                this.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
