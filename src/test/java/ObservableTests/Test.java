package ObservableTests;
import it.polimi.ingsw.utils.observerPattern.*;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        ObservedSubject abstractSubject = new ObservedSubject(new ArrayList<Observer>(), "Soccer  Match [2014AUG24]");
        Observer observer = new Osservatore(abstractSubject, "Adam Warner [New York]");
        observer.subscribe();

        System.out.println();

        Observer observer2 = new Osservatore(abstractSubject, "Tim Ronney [London]");
        observer2.subscribe();
        abstractSubject.setDesc("Welcome to live Soccer match");
        abstractSubject.setDesc("Current score 0-0");

        System.out.println();

        observer2.unSubscribe();

        System.out.println();

        abstractSubject.setDesc("It’s a goal!!");
        abstractSubject.setDesc("Current score 1-0");

        System.out.println();

        Observer observer3 = new Osservatore(abstractSubject, "Marrie [Paris]");
        observer3.subscribe();

        System.out.println();

        abstractSubject.setDesc("It’s another goal!!");
        abstractSubject.setDesc("Half-time score 2-0");
    }
}
