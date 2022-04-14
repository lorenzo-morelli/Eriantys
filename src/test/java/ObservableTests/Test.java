package ObservableTests;
import it.polimi.ingsw.utils.observerPattern.*;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        Subject abstractSubject = new SoggettoOsservato(new ArrayList<Observer>(), "Soccer  Match [2014AUG24]");
        Observer observer = new Osservatore(abstractSubject, "Adam Warner [New York]");
        observer.subscribe();

        System.out.println();

        Observer observer2 = new Osservatore(abstractSubject, "Tim Ronney [London]");
        observer2.subscribe();
        SoggettoOsservato concreteSubject = ((SoggettoOsservato)abstractSubject);
        concreteSubject.setDesc("Welcome to live Soccer match");
        concreteSubject.setDesc("Current score 0-0");

        System.out.println();

        observer2.unSubscribe();

        System.out.println();

        concreteSubject.setDesc("It’s a goal!!");
        concreteSubject.setDesc("Current score 1-0");

        System.out.println();

        Observer observer3 = new Osservatore(abstractSubject, "Marrie [Paris]");
        observer3.subscribe();

        System.out.println();

        concreteSubject.setDesc("It’s another goal!!");
        concreteSubject.setDesc("Half-time score 2-0");
    }
}
