package ObservableTests;

import it.polimi.ingsw.utils.observerPattern.*;

import java.io.IOException;
import java.util.List;

public class SoggettoOsservato implements Subject {
    private final List<Observer> observers;
    private String desc;

    public SoggettoOsservato(List<Observer> observers, String subjectDetails) {
        this.observers = observers;
    }

    public void subscribeObserver(Observer observer) {
        observers.add(observer);
    }

    public void setDesc(String desc) {
        this.desc = desc;
        notifyObservers();
    }

    public void unsubscribeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        System.out.println();
        observers.forEach(observer -> {
            try {
                observer.update(desc);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

}
