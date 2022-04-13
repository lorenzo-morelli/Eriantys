package it.polimi.ingsw.utils.ObserverPattern;

import it.polimi.ingsw.utils.ObserverPattern.Observer;

public interface Subject {
    /**
     * permette ad un osservatore di iscriversi all'elenco degli osservatori
     * che saranno poi notificati in caso di cambiamento di stato
     */
    public void subscribeObserver(Observer observer);
    /**
     * permette ad un osservatore di disiscriversi all'elenco degli osservatori
     * che saranno poi notificati in caso di cambiamento di stato
     */
    public void unsubscribeObserver(Observer observer);

    /**
     * notifica tutti gli osservatori
     */
    public void notifyObservers();

    public String subjectDetails();

}
