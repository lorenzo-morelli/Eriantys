package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.*;

/**
 * This class contain the info and the methods to use the card HERALD described in the rules
 */
public class Herald extends CharacterCard{

    public Herald(){
        super(3,"HERALD");
    }

    public boolean useEffect(Player player, int index_island, Model model) {
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
                        model.getTable().conquestIsland(index_island, model.getTeams(), influence_team);
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
                        model.getTable().conquestIsland(index_island, model.getPlayers(), influence_player);
                    }
                    if (influence_player.getSchoolBoard().getNumOfTowers() == 0) {
                        return true;
                    }
                }
            }
        if (model.getTable().getIslands().get(index_island) != null) {
            if (model.getTable().getIslands().get((index_island + 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((index_island + 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(index_island).getTowerColor())) {
                model.getTable().mergeIsland(index_island, ((index_island + 1) % model.getTable().getIslands().size()));
            }
            Island merging;
            if(index_island==0){
                merging=model.getTable().getIslands().get(model.getTable().getIslands().size()-1);
            }
            else{
                merging=model.getTable().getIslands().get((index_island - 1)% model.getTable().getIslands().size());
            }
            if (merging.getTowerColor() != null && merging.getTowerColor().equals(model.getTable().getIslands().get(index_island).getTowerColor())) {
                int merging_index;
                if(index_island==0){
                    merging_index=model.getTable().getIslands().size()-1;
                }
                else{
                    merging_index=(index_island - 1)% model.getTable().getIslands().size();
                }
                model.getTable().mergeIsland(index_island, merging_index);
            }
        }
        return model.getTable().getIslands().size() == 3;
    }
}
