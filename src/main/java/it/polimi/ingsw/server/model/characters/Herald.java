package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.*;

public class Herald extends CharacterCard{

    public Herald(){
        super("Scegli un isola e calcola la maggioranza come se madre natura avesse terminato il suo movimento li (questo turno si svolgerà comunque la mother phase)",3,"HERALD");
    }

    public boolean useEffect(Player player, int index_island, Model model) { //ritorna true se gioco è da terminare
        player.reduceCoin(getCost());
        improveCost();
        Island target= model.getTable().getIslands().get(index_island);
            if (model.getNumberOfPlayers() == 4) {
                Team influence_team = target.team_influence(model.getTeams(), model.getTable().getProfessors(),model.getTable().isCentaurEffect(),model.getTable().getMushroomColor(),model.getTable().getKnightEffect());
                if (influence_team != null) {
                    if (target.getNumberOfTowers() == 0) {
                        target.controllIsland(influence_team);
                        target.placeTower();
                    } else if (!(target.getTowerColor().equals(influence_team.getPlayer1().getSchoolBoard().getTowerColor()))) {
                        model.getTable().ConquestIsland(model.getTable().getMotherNaturePosition(), model.getTeams(), influence_team);
                    }
                    if (influence_team.getPlayer1().getSchoolBoard().getNumOfTowers() == 0) {
                        return true;
                    }
                }
            } else {
                Player influence_player = target.player_influence(model.getPlayers(), model.getTable().getProfessors(),model.getTable().isCentaurEffect(),model.getTable().getMushroomColor(),model.getTable().getKnightEffect());
                if (influence_player != null) {
                    if (target.getNumberOfTowers() == 0) {
                        target.controllIsland(influence_player);
                        target.placeTower();
                    } else if (!(target.getTowerColor().equals(influence_player.getSchoolBoard().getTowerColor()))) {
                        model.getTable().ConquestIsland(model.getTable().getMotherNaturePosition(), model.getPlayers(), influence_player);
                    }
                    if (influence_player.getSchoolBoard().getNumOfTowers() == 0) {
                        return true;
                    }
                }
            }
            if (model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()) != null) {
                if (model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                    model.getTable().MergeIsland(model.getTable().getMotherNaturePosition(), ((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()));
                }
                if (model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                    model.getTable().MergeIsland(model.getTable().getMotherNaturePosition(), ((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size()));
                }
            }
        return model.getTable().getIslands().size() == 3;
    }
    public String toString() {
        return "HERALD - " + super.toString();
    }
}
