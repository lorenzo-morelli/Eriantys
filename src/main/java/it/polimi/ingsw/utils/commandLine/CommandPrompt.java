package it.polimi.ingsw.utils.commandLine;

import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.observerPattern.Subject;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta il terminale (o cmd in windows)
 * Ho usato il pattern Singleton per far in modo che ci sia sempre e solo un cmd
 * Il terminale è il soggetto osservato (pattern observer) dall'evento TerminalEvent
 *
 * @author Fernando
 */
public class CommandPrompt implements Subject{
    private static String fromTerminal;
    private static ConsoleReader console;
    private static List<Observer> observers = null;
    private static CommandPrompt instance = null;
    private static boolean debug = false;

    private CommandPrompt() throws IOException {
        console = new ConsoleReader();
        observers = new ArrayList<>();
    }

    public static CommandPrompt getInstance() throws IOException {
        if (instance == null){
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
        fromTerminal=console.readLine();
        instance.notifyObservers();
    }

    public static String gotFromTerminal() {
        return fromTerminal;
    }

    public static void println(String toPrint) throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        if(!debug) {
            // clear screen works only on windows
            CommandPrompt.clearScreen();
            // ascii code for clear screen shoud work on unix
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        instance.getConsole().println(toPrint);
        instance.getConsole().flush();
    }

    public static void setPrompt(String toSet) throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        instance.getConsole().setPrompt(toSet);
        instance.getConsole().flush();
    }

    public static void clearScreen() throws IOException {
        if (instance == null) {
            instance = new CommandPrompt();
        }
        instance.getConsole().clearScreen();
    }

    public static void setDebug() {
        CommandPrompt.debug = true;
    }

    public static void ask(String suggestion, String console){
        try {
            if(!debug) {
                CommandPrompt.clearScreen();
            }
            CommandPrompt.println(suggestion);
            CommandPrompt.setPrompt(console);
            CommandPrompt.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribeObserver(Observer observer) {
        observers.add(observer);
        System.out.println("[L'osservatore TerminalEvent sta osservando la Console]");
    }

    @Override
    public void unsubscribeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        CommandPrompt.observers.stream().forEach(observer -> {
            try {
                observer.update(fromTerminal);
                System.out.println("[L'osservatore è stato notificato]");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
