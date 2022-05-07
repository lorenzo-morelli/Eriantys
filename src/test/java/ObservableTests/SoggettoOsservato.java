package ObservableTests;

import it.polimi.ingsw.utils.observerPattern.*;

import java.io.IOException;
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
        observers.stream().forEach(observer -> {
            try {
                observer.update(desc);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    public String subjectDetails() {
        return subjectDetails;
    }
}
