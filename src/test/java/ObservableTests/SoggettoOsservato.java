package ObservableTests;

import it.polimi.ingsw.utils.ObserverPattern.*;
import java.util.List;

public class SoggettoOsservato implements Subject {
    private final List<Observer> observers;
    private String desc;
    private final String subjectDetails;

    public SoggettoOsservato(List<Observer> observers, String subjectDetails) {
        this.observers = observers;
        this.subjectDetails = subjectDetails;
    }

    public void subscribeObserver(Observer observer) {
        observers.add(observer);
    }

    public void setDesc(String desc) {
        this.desc = desc;
        notifyObservers();
    }

    public void unsubscribeObserver(Observer observer) {
        int index = observers.indexOf(observer);
        observers.remove(index);
    }

    public void notifyObservers() {
        System.out.println();
        observers.stream().forEach(observer -> observer.update(desc));

    }

    public String subjectDetails() {
        return subjectDetails;
    }
}
