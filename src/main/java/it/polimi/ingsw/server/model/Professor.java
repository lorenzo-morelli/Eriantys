package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.Optional;

public class Professor {
    private PeopleColor color;
    private Optional<Player> heldBy;
}
