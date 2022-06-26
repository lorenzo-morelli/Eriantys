package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.observerPattern.Subject;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the terminal (or cmd in windows)
 * I used the Singleton pattern to make sure that there is only ever one cmd
 * The terminal is the subject observed (pattern observer) by the Events
 *
 * @author Fernando
 */
public class CommandPrompt implements Subject {
    private static String fromTerminal = null;
    private static ConsoleReader console;
    private static List<Observer> observers = null;
    private static CommandPrompt instance = null;
    private static final boolean debug = false;
    private static boolean inputLetto = false;

    private CommandPrompt() throws IOException {
        console = new ConsoleReader();
        observers = new ArrayList<>();
    }

    public static CommandPrompt getInstance() throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        return instance;
    }

    public static ConsoleReader getConsole() throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        return console;
    }

    public static void read() throws IOException {
        fromTerminal = console.readLine();
        instance.notifyObservers();
    }

    public static String gotFromTerminal() {
        return fromTerminal;
    }

    public static void forceInput(String input) {
        CommandPrompt.fromTerminal = input;
    }

    public static void println(String toPrint) throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        if (!debug) {
            // clear screen works only on Windows
            CommandPrompt.clearScreen();
            // ascii code for clear screen should work on unix
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        getConsole().println(toPrint);
        getConsole().flush();
    }

    public static void setPrompt(String toSet) throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        getConsole().setPrompt(toSet);
        getConsole().flush();
    }

    public static void clearScreen() throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        getConsole().clearScreen();
    }

    public static void ask(String suggestion, String console) throws InterruptedException {

        // Stop reading input if a client disconnects
        Thread t = new Thread(() -> {
            try {
                if (!debug) {
                    CommandPrompt.clearScreen();
                }
                CommandPrompt.println(suggestion);
                CommandPrompt.setPrompt(console);
                CommandPrompt.read();
                if (Network.disconnectedClient()) {
                    System.out.println("Bene, questo input verrà ignorato");
                    return;
                }
                setInputLetto(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();

        while (!inputLetto() && !Network.disconnectedClient()) {

            TimeUnit.MILLISECONDS.sleep(250);
        }
        if (Network.disconnectedClient()) {
            t.interrupt();
        }
        setInputLetto(false);


    }

    public static void setInputLetto(boolean inputLetto) {
        CommandPrompt.inputLetto = inputLetto;
    }

    public static synchronized boolean inputLetto() {
        return inputLetto;
    }

    @Override
    public void subscribeObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        CommandPrompt.observers.forEach(observer -> {
            try {
                observer.update(fromTerminal);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
