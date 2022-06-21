package it.polimi.ingsw.utils.observerPattern;

public interface Subject {
    /**
     * allows an observer to subscribe to the list of observers
     * which will then be notified in the event of a change of status
     */
    void subscribeObserver(Observer observer);

    /**
     * allows an observer to unsubscribe from the observer list
     * which will then be notified in the event of a change of status
     */
    void unsubscribeObserver(Observer observer);

    /**
     * notify all the observers
     */
    void notifyObservers();

}
