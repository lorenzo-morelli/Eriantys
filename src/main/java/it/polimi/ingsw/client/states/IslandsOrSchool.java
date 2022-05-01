package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

public class IslandsOrSchool extends Decision{
    public IslandsOrSchool(View view, Model model) throws IOException {
        super(view, model, "ISLANDS", "SCHOOL");
    }
}
