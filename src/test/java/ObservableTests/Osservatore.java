package ObservableTests;

import it.polimi.ingsw.utils.observerPattern.*;

public class Osservatore implements Observer {

    private final Subject subject;
    private String desc;   // risorsa che voglio che sia sincronizzata col soggetto
    private String userInfo;

    public Osservatore(Subject subject, String userInfo) {
        if (subject==null){
            throw new IllegalArgumentException("No Publisher found");
        }
        this.subject = subject;
        this.userInfo = userInfo;
    }

    private void display(){
        System.out.println("["+userInfo+"]: "+desc);
    }

    /**
     * metodo che verrà chiamato dal Soggetto per sincronizzarlo con il suo
     * @param desc
     */
    public void update(Object desc) {
        this.desc = (String)desc;
        display();
    }

    public void subscribe() {
        System.out.println("Subscribing "+userInfo+" to "+" ...");
        this.subject.subscribeObserver(this);
        System.out.println("Subscribed successfully");
    }

    public void unSubscribe() {
        System.out.println("Unsubscribing "+userInfo+" to "+" ...");
        this.subject.unsubscribeObserver(this);
        System.out.println("Unsubscribed successfully");
    }
}
