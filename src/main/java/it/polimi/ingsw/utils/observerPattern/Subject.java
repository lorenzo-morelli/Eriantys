package it.polimi.ingsw.utils.observerPattern;

public interface Subject {
    /**
     * Allows an observer to subscribe to the list of observers
     * which will then be notified in the event of a change of status.
     */
    void subscribeObserver(Observer observer);

    /**
     * Allows an observer to unsubscribe from the observer list
     * which will then be notified in the event of a change of status.
     */
    void unsubscribeObserver(Observer observer);

    /**
     * Notifies all the observers.
     */
    void notifyObservers();

}
