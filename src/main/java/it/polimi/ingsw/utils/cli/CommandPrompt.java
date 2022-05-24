package it.polimi.ingsw.utils.cli;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.observerPattern.Subject;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Classe che rappresenta il terminale (o cmd in windows)
 * Ho usato il pattern Singleton per far in modo che ci sia sempre e solo un cmd
 * Il terminale è il soggetto osservato (pattern observer) dagli Eventi
 *
 * @author Fernando
 */
public class CommandPrompt implements Subject{
    private static String fromTerminal;
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

        // Interrompere la lettura dell'input se si disconnette un client
        Thread t= new Thread(() -> {
            try {
                if(!debug) {
                    CommandPrompt.clearScreen();
                }
                CommandPrompt.println(suggestion);
                CommandPrompt.setPrompt(console);
                CommandPrompt.read();
                if (Network.disconnectedClient()){
                    System.out.println("Bene, questo input verrà ignorato");
                    return;
                }
                setInputLetto(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        while(!inputLetto() && !Network.disconnectedClient()){
            TimeUnit.MILLISECONDS.sleep(250);
        }
        if (Network.disconnectedClient()){
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
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
