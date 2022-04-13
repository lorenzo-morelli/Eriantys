package it.polimi.ingsw.utils.ObserverPattern;


public interface Observer {
    /**
     * se vieni notificato cosa vuoi fare?
     */
    public  void update(Object message);

    /**
     * iscriviti
     */
    public void subscribe();

    /**
     * disiscriviti
     */
    public void unSubscribe();

}
