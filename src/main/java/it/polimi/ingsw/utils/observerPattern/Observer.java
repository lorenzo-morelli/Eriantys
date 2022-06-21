package it.polimi.ingsw.utils.observerPattern;


public interface Observer {
    /**
     * update
     */
    void update(Object message) throws Exception;

    /**
     * subscribe
     */
    void subscribe();

    /**
     * unsubscribe
     */
    void unSubscribe();

}
