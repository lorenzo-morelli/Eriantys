package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    @Test
    void testCreateModel2playerPrincipiante() {
        Model model = new Model(2, "PRINCIPIANT", false);
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, false));
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, false));

        //assert Model
        assertEquals(2, model.getPlayers().size());
        assertEquals(GameMode.PRINCIPIANT, model.getGameMode());
        assertEquals(2, model.getNumberOfPlayers());
        assertNull(model.getTeams());
        assertEquals(model.getPlayers().get(0), model.getCurrentPlayer());


        //assert player 1
        assertEquals("pippo", model.getPlayers().get(0).getNickname());
        assertEquals("192.168.0.1", model.getPlayers().get(0).getIp());
        assertEquals(10, model.getPlayers().get(0).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(0).getChosenCard());
        assertEquals(0, model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
        assertEquals(7, model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
        assertEquals(8, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertTrue(TowerColor.BLACK == model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE == model.getPlayers().get(0).getSchoolBoard().getTowerColor());
        //assert player 2
        assertEquals("paperino", model.getPlayers().get(1).getNickname());
        assertEquals("192.168.0.2", model.getPlayers().get(1).getIp());
        assertEquals(10, model.getPlayers().get(1).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(1).getChosenCard());
        assertEquals(0, model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
        assertEquals(7, model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(8, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());

        //assert CentreTable
        //assert clouds
        assertEquals(2, model.getTable().getClouds().size());
        for (int i = 0; i < model.getTable().getClouds().size(); i++) {
            assertEquals(3, model.getTable().getClouds().get(i).getStudentsAccumulator().size());
        }
        //assert Island and mother nature position
        assertEquals(12, model.getTable().getIslands().size());
        for (int i = 0; i < model.getTable().getIslands().size(); i++) {
            if (i == 0) {

                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
                assertEquals(0, model.getTable().getIslands().get(i).getInhabitants().size());
            } else if (i == 6) {
                assertEquals(0, model.getTable().getIslands().get(i).getInhabitants().size());
            } else {
                assertEquals(1, model.getTable().getIslands().get(i).getInhabitants().size());
            }
            assertEquals(0, model.getTable().getIslands().get(i).getNumberOfTowers());
            assertNull(model.getTable().getIslands().get(i).getTowerColor());
            assertFalse(model.getTable().getIslands().get(i).isBlocked());
        }
        //assert bag
        assertEquals(100, model.getTable().getBag().size());
        //assert professor
        assertEquals(5, model.getTable().getProfessors().size());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
            for (int j = 0; j < i; j++) {
                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
            }
            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
        }
    }

    @Test
    void testCreateModel3playerPrincipiante() {
        Model model = new Model(3, "PRINCIPIANT", false);
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, false));
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, false));
        model.getPlayers().add(new Player("pluto", "192.168.0.3", model, false));

        //assert Model
        assertEquals(3, model.getPlayers().size());
        assertEquals(GameMode.PRINCIPIANT, model.getGameMode());
        assertEquals(3, model.getNumberOfPlayers());
        assertNull(model.getTeams());
        assertEquals(model.getPlayers().get(0), model.getCurrentPlayer());


        //assert player 1
        assertEquals("pippo", model.getPlayers().get(0).getNickname());
        assertEquals("192.168.0.1", model.getPlayers().get(0).getIp());
        assertEquals(10, model.getPlayers().get(0).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(0).getChosenCard());
        assertEquals(0, model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
        assertEquals(9, model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
        assertEquals(6, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertTrue(TowerColor.BLACK == model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE == model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.GREY == model.getPlayers().get(0).getSchoolBoard().getTowerColor());

        //assert player 3
        assertEquals("paperino", model.getPlayers().get(1).getNickname());
        assertEquals("192.168.0.2", model.getPlayers().get(1).getIp());
        assertEquals(10, model.getPlayers().get(1).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(1).getChosenCard());
        assertEquals(0, model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
        assertEquals(9, model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE) || TowerColor.GREY == model.getPlayers().get(1).getSchoolBoard().getTowerColor()) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(6, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());

        //assert player 2
        assertEquals("pluto", model.getPlayers().get(2).getNickname());
        assertEquals("192.168.0.3", model.getPlayers().get(2).getIp());
        assertEquals(10, model.getPlayers().get(2).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(2).getChosenCard());
        assertEquals(0, model.getPlayers().get(2).getSchoolBoard().getDinnerTable().size());
        assertEquals(9, model.getPlayers().get(2).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE) || TowerColor.GREY == model.getPlayers().get(2).getSchoolBoard().getTowerColor()) && !(model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(1).getSchoolBoard().getTowerColor())) && !(model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(6, model.getPlayers().get(2).getSchoolBoard().getNumOfTowers());


        //assert CentreTable
        //assert clouds
        assertEquals(3, model.getTable().getClouds().size());
        for (int i = 0; i < model.getTable().getClouds().size(); i++) {
            assertEquals(4, model.getTable().getClouds().get(i).getStudentsAccumulator().size());
            assertEquals(4, model.getTable().getClouds().get(i).getStudentsAccumulator().size());
        }
        //assert Island and mother nature position
        assertEquals(12, model.getTable().getIslands().size());
        for (int i = 0; i < model.getTable().getIslands().size(); i++) {
            if (i == 0) {

                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
                assertEquals(0, model.getTable().getIslands().get(i).getInhabitants().size());
            } else if (i == 6) {
                assertEquals(0, model.getTable().getIslands().get(i).getInhabitants().size());
            } else {
                assertEquals(1, model.getTable().getIslands().get(i).getInhabitants().size());
            }
            assertEquals(0, model.getTable().getIslands().get(i).getNumberOfTowers());
            assertNull(model.getTable().getIslands().get(i).getTowerColor());
            assertFalse(model.getTable().getIslands().get(i).isBlocked());
        }
        //assert bag
        assertEquals(81, model.getTable().getBag().size());
        //assert professor
        assertEquals(5, model.getTable().getProfessors().size());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
            for (int j = 0; j < i; j++) {
                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
            }
            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
        }
    }

    @Test
    void testCreateModel4playerPrincipiante() {
        Model model = new Model(4, "PRINCIPIANT", false);
        model.getPlayers().add(new Player("pippo", "192.168.0.1", 1, model, false));
        model.getPlayers().add(new Player("paperino", "192.168.0.2", 2, model, false));
        model.getPlayers().add(new Player("pluto", "192.168.0.3", 1, model, false));
        model.getPlayers().add(new Player("minnie", "192.168.0.4", 2, model, false));

        //assert Model
        assertEquals(4, model.getPlayers().size());
        assertEquals(GameMode.PRINCIPIANT, model.getGameMode());
        assertEquals(4, model.getNumberOfPlayers());
        assertEquals(2, model.getTeams().size());

        //assert team
        assertEquals(1, model.getTeams().get(0).getTeamNumber());
        assertEquals(2, model.getTeams().get(1).getTeamNumber());
        assertEquals(model.getPlayers().get(0), model.getTeams().get(0).getPlayer1());
        assertEquals(model.getPlayers().get(1), model.getTeams().get(1).getPlayer1());
        assertEquals(model.getPlayers().get(2), model.getTeams().get(0).getPlayer2());
        assertEquals(model.getPlayers().get(3), model.getTeams().get(1).getPlayer2());

        assertEquals(model.getPlayers().get(0), model.getCurrentPlayer());


        //assert player 1
        assertEquals("pippo", model.getPlayers().get(0).getNickname());
        assertEquals("192.168.0.1", model.getPlayers().get(0).getIp());
        assertEquals(10, model.getPlayers().get(0).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(0).getChosenCard());
        assertEquals(0, model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
        assertEquals(7, model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
        assertEquals(8, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertTrue(TowerColor.BLACK == model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE == model.getPlayers().get(0).getSchoolBoard().getTowerColor());

        //assert player 2
        assertEquals("paperino", model.getPlayers().get(1).getNickname());
        assertEquals("192.168.0.2", model.getPlayers().get(1).getIp());
        assertEquals(10, model.getPlayers().get(1).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(1).getChosenCard());
        assertEquals(0, model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
        assertEquals(7, model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(8, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());

        //assert player 3
        assertEquals("pluto", model.getPlayers().get(2).getNickname());
        assertEquals("192.168.0.3", model.getPlayers().get(2).getIp());
        assertEquals(10, model.getPlayers().get(2).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(2).getChosenCard());
        assertEquals(0, model.getPlayers().get(2).getSchoolBoard().getDinnerTable().size());
        assertEquals(7, model.getPlayers().get(2).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && (model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(8, model.getPlayers().get(2).getSchoolBoard().getNumOfTowers());

        //assert player 4
        assertEquals("minnie", model.getPlayers().get(3).getNickname());
        assertEquals("192.168.0.4", model.getPlayers().get(3).getIp());
        assertEquals(10, model.getPlayers().get(3).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(3).getChosenCard());
        assertEquals(0, model.getPlayers().get(3).getSchoolBoard().getDinnerTable().size());
        assertEquals(7, model.getPlayers().get(3).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && (model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(1).getSchoolBoard().getTowerColor())));
        assertEquals(8, model.getPlayers().get(3).getSchoolBoard().getNumOfTowers());


        //assert CentreTable
        //assert clouds
        assertEquals(4, model.getTable().getClouds().size());
        for (int i = 0; i < model.getTable().getClouds().size(); i++) {
            assertEquals(3, model.getTable().getClouds().get(i).getStudentsAccumulator().size());
        }
        //assert Island and mother nature position
        assertEquals(12, model.getTable().getIslands().size());
        for (int i = 0; i < model.getTable().getIslands().size(); i++) {
            if (i == 0) {

                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
                assertEquals(0, model.getTable().getIslands().get(i).getInhabitants().size());
            } else if (i == 6) {
                assertEquals(0, model.getTable().getIslands().get(i).getInhabitants().size());
            } else {
                assertEquals(1, model.getTable().getIslands().get(i).getInhabitants().size());
            }
            assertEquals(0, model.getTable().getIslands().get(i).getNumberOfTowers());
            assertNull(model.getTable().getIslands().get(i).getTowerColor());
            assertFalse(model.getTable().getIslands().get(i).isBlocked());
        }
        //assert bag
        assertEquals(80, model.getTable().getBag().size());
        //assert professor
        assertEquals(5, model.getTable().getProfessors().size());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
            for (int j = 0; j < i; j++) {
                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
            }
            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
        }
    }

    @Test
    void testDeck() {
        Deck deck = new Deck();
        assertEquals(10, deck.getCardsList().size());
        AssistantCard card = new AssistantCard(1, 1);
        assertTrue(deck.inDeck(card));
        assertFalse(deck.remove(card));
        assertFalse(deck.inDeck(card));
        assertEquals(9, deck.getCardsList().size());
        AssistantCard card2 = new AssistantCard(2, 1);
        assertTrue(deck.inDeck(card2));
        assertFalse(deck.remove(card2));
        assertFalse(deck.inDeck(card2));
        assertEquals(8, deck.getCardsList().size());
        AssistantCard card3 = new AssistantCard(3, 2);
        assertTrue(deck.inDeck(card3));
        assertFalse(deck.remove(card3));
        assertFalse(deck.inDeck(card3));
        assertEquals(7, deck.getCardsList().size());
        AssistantCard card6 = new AssistantCard(6, 3);
        assertTrue(deck.inDeck(card6));
        assertFalse(deck.remove(card6));
        assertFalse(deck.inDeck(card6));
        assertEquals(6, deck.getCardsList().size());
        AssistantCard card4 = new AssistantCard(4, 2);
        assertTrue(deck.inDeck(card4));
        assertFalse(deck.remove(card4));
        assertFalse(deck.inDeck(card4));
        assertEquals(5, deck.getCardsList().size());
        AssistantCard card5 = new AssistantCard(5, 3);
        assertTrue(deck.inDeck(card5));
        assertFalse(deck.remove(card5));
        assertFalse(deck.inDeck(card5));
        assertEquals(4, deck.getCardsList().size());
        AssistantCard card9 = new AssistantCard(9, 5);
        assertTrue(deck.inDeck(card9));
        assertFalse(deck.remove(card9));
        assertFalse(deck.inDeck(card9));
        assertThrows(IllegalArgumentException.class, () -> assertFalse(deck.remove(card3)));
        assertEquals(3, deck.getCardsList().size());
        AssistantCard card10 = new AssistantCard(10, 5);
        assertTrue(deck.inDeck(card10));
        assertFalse(deck.remove(card10));
        assertFalse(deck.inDeck(card10));
        assertEquals(2, deck.getCardsList().size());
        AssistantCard card7 = new AssistantCard(7, 4);
        assertTrue(deck.inDeck(card7));
        assertFalse(deck.remove(card7));
        assertFalse(deck.inDeck(card7));
        assertEquals(1, deck.getCardsList().size());
        AssistantCard card8 = new AssistantCard(8, 4);
        assertTrue(deck.inDeck(card8));
        assertTrue(deck.remove(card8));
        assertFalse(deck.inDeck(card8));
        assertEquals(0, deck.getCardsList().size());
    }

    @Test
    void StudentSetTest() {
        StudentSet set0 = new StudentSet();
        for (PeopleColor color : PeopleColor.values()) {
            assertEquals(0, set0.numStudentsByColor(color));
        }

        StudentSet set = new StudentSet(2, 2, 2, 2, 2);
        assertEquals(10, set.size());
        set.removeStudent(2, PeopleColor.RED);
        set.removeStudent(3, PeopleColor.GREEN);
        set.removeStudent(1, PeopleColor.YELLOW);
        set.addStudents(2, PeopleColor.PINK);
        set.addStudents(27, PeopleColor.BLUE);
        set.addStudents(1, PeopleColor.BLUE);
        assertEquals(0, set.numStudentsByColor(PeopleColor.RED));
        assertEquals(0, set.numStudentsByColor(PeopleColor.GREEN));
        assertEquals(1, set.numStudentsByColor(PeopleColor.YELLOW));
        assertEquals(30, set.numStudentsByColor(PeopleColor.BLUE));
        assertEquals(4, set.numStudentsByColor(PeopleColor.PINK));
        assertEquals(35, set.size());
        StudentSet set2 = new StudentSet(1, 1, 1, 1, 1);
        set2.setAllStudentToZero();
        set2.addStudents(2, PeopleColor.RED);
        StudentSet set3 = new StudentSet(2, 0, 0, 0, 0);
        assertEquals(set2, set3);
    }

    @Test
    void numStudentsColor() {
        StudentSet bag = new StudentSet(2, 2, 2, 2, 2);
        StudentSet set = new StudentSet();

        set.setStudentsRandomly(1, bag);

        assertEquals(1, set.size());
        assertEquals(9, bag.size());

        set.setStudentsRandomly(9, bag);
        assertEquals(0, bag.size());
        assertEquals(10, set.size());
        assertThrows(IllegalArgumentException.class, () -> set.setStudentsRandomly(1, bag));
    }

    @Test
    void SimulateGame2Player() {
        Model model = new Model(2, "PRINCIPIANT", false);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, false));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, false));

        assertEquals(model.playerWinner(), "PAREGGIO");

        model.getPlayers().get(1).setChosenCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(0));
        model.getPlayers().get(0).setChosenCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(1));

        assertEquals("pippo", model.getPlayers().get(1).getNickname());
        model.schedulePlayers();
        assertEquals("pippo", model.getPlayers().get(0).getNickname());

        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addStudents(2, PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addStudents(2, PeopleColor.YELLOW);
        model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "pippo");
            }
        }
        model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(0), PeopleColor.YELLOW, 2);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "pippo");
            }
        }
        assertEquals(2, model.getTable().getIslands().get(2).getInhabitants().size());

        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addStudents(2, PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addStudents(3, PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(1), PeopleColor.YELLOW, 2);
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "pippo");
            }
        }
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.YELLOW);
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.YELLOW);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "paperino");
            }
        }

        model.getTable().mother(2);

        assertEquals(model.getTable().getMotherNaturePosition(), 2);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getInhabitants().size(), 3);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).playerInfluenceCalculator(model.getPlayers(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect()).getNickname(), "paperino");

        model.getTable().getIslands().get(3).getInhabitants().addStudents(2, PeopleColor.YELLOW);
        model.getTable().getIslands().get(2).controlIsland(model.getPlayers().get(1));
        model.getTable().getIslands().get(3).controlIsland(model.getPlayers().get(1));
        model.getTable().getIslands().get(3).placeTower();
        model.getTable().getIslands().get(2).placeTower();

        assertEquals(6, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(8, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(1, model.getTable().getIslands().get(3).getNumberOfTowers());
        assertEquals(1, model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(3).getTowerColor());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());

        model.getTable().mergeIsland(2, 3);

        assertEquals(11, model.getTable().getIslands().size());
        assertEquals(2, model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(6, model.getTable().getIslands().get(2).getInhabitants().size());

        model.getTable().conquestIsland(2, model.getPlayers(), model.getPlayers().get(0));
        assertEquals(8, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(6, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(model.getPlayers().get(0).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());

        model.getPlayers().get(0).getSchoolBoard().loadEntrance(model.getTable().getClouds().get(0), model.getTable().getClouds());
        assertEquals(model.getTable().getClouds().get(0).getStudentsAccumulator().size(), 0);
        assertEquals(model.playerWinner(), model.getPlayers().get(0).getNickname());

        String view = "\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + 0 + "    " + "" + "\n\n" + model.getTable().toString(false) +
                "-----------------PLAYER---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.printPlayers("pippo") +
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "ciao" + "\n";
        assertEquals(model.toString("pippo", "ciao"), view);

        view = "-----------------------------------------TABLE----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "\n----------------ISLANDS---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.getTable().printIslands() + "------------------BAG-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    SIZE : " + model.getTable().getBag().size() + "    " + model.getTable().getBag().toString() +
                "\n----------------CLOUDS----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.getTable().printClouds() +
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" +
                "---------------PROFESSORS-------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                model.getTable().printProfessors() + "\n\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n";

        assertEquals(model.getTable().toString(false), view);

    }

    @Test
    void SimulateGame4Player() {
        Model model = new Model(4, "PRINCIPIANT", false);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", 1, model, false));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", 2, model, false));
        model.getPlayers().add(new Player("paperino2", "192.168.0.2", 1, model, false));
        model.getPlayers().add(new Player("pippo2", "192.168.0.1", 2, model, false));

        assertEquals(model.teamWinner(), "PAREGGIO");

        model.getPlayers().get(1).setChosenCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(0));
        model.getPlayers().get(0).setChosenCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(1));
        model.getPlayers().get(2).setChosenCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(5));
        model.getPlayers().get(3).setChosenCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(6));

        assertEquals("pippo", model.getPlayers().get(1).getNickname());
        model.schedulePlayers();
        assertEquals("pippo", model.getPlayers().get(0).getNickname());

        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addStudents(2, PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addStudents(2, PeopleColor.YELLOW);
        model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "pippo");
            }
        }
        model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(0), PeopleColor.YELLOW, 2);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "pippo");
            }
        }
        assertEquals(2, model.getTable().getIslands().get(2).getInhabitants().size());

        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addStudents(2, PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addStudents(3, PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(1), PeopleColor.YELLOW, 2);
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "pippo");
            }
        }
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.YELLOW);
        model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.YELLOW);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for (int i = 0; i < model.getTable().getProfessors().size(); i++) {
            if (model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)) {
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(), "paperino");
            }
        }

        model.getTable().mother(2);

        assertEquals(model.getTable().getMotherNaturePosition(), 2);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getInhabitants().size(), 3);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).teamInfluenceCalculator(model.getTeams(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect()).getPlayer1().getNickname(), "paperino");

        assertEquals(model.getTeams().get(1).getPlayer1().getNickname(), "pippo");
        assertEquals(model.getPlayers().get(1).getNickname(), "paperino");
        model.getTable().getIslands().get(3).getInhabitants().addStudents(2, PeopleColor.YELLOW);
        model.getTable().getIslands().get(2).controlIsland((model.getTeams().get(0)));

        model.getTable().getIslands().get(3).controlIsland((model.getTeams().get(0)));
        model.getTable().getIslands().get(3).placeTower();
        model.getTable().getIslands().get(2).placeTower();

        assertEquals(6, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(8, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(6, model.getTeams().get(0).getPlayer1().getSchoolBoard().getNumOfTowers());
        assertEquals(6, model.getTeams().get(0).getPlayer2().getSchoolBoard().getNumOfTowers());
        assertEquals(8, model.getTeams().get(1).getPlayer1().getSchoolBoard().getNumOfTowers());
        assertEquals(1, model.getTable().getIslands().get(3).getNumberOfTowers());
        assertEquals(1, model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(3).getTowerColor());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(model.getTeams().get(0).getPlayer1().getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(model.getTeams().get(0).getPlayer2().getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());


        model.getTable().mergeIsland(2, 3);

        assertEquals(11, model.getTable().getIslands().size());
        assertEquals(2, model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(model.getTeams().get(0).getPlayer2().getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(6, model.getTable().getIslands().get(2).getInhabitants().size());

        model.getTable().conquestIsland(2, model.getTeams(), model.getTeams().get(1));
        assertEquals(6, model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(6, model.getTeams().get(1).getPlayer2().getSchoolBoard().getNumOfTowers());
        assertEquals(8, model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(8, model.getTeams().get(0).getPlayer2().getSchoolBoard().getNumOfTowers());
        assertEquals(model.getPlayers().get(0).getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(model.getTeams().get(1).getPlayer1().getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(2).getTowerColor());

        String winner = model.getPlayers().get(0).getNickname() + "+" + model.getTeams().get(1).getPlayer2().getNickname();

        model.getPlayers().get(0).getSchoolBoard().loadEntrance(model.getTable().getClouds().get(0), model.getTable().getClouds());
        assertEquals(model.getTable().getClouds().get(0).getStudentsAccumulator().size(), 0);
        assertEquals(model.teamWinner(), winner);

        String view = "\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + 0 + "    " + "" + "\n\n" +
                model.getTable().toString(false) +
                "-----------------TEAM-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.printTeam("pippo") +
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "ciao" + "\n";
        assertEquals(model.toString("pippo", "ciao"), view);

    }

    @Test
    void SimulateExpertCreation() {
        Model model = new Model(2, "EXPERT", false);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, false));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, false));

        assertEquals(model.getTable().getCharacters().size(), 3);
        assertNotEquals(model.getTable().getCharacters().get(0).getName(), model.getTable().getCharacters().get(1).getName());
        assertNotEquals(model.getTable().getCharacters().get(0).getName(), model.getTable().getCharacters().get(2).getName());
        assertNotEquals(model.getTable().getCharacters().get(2).getName(), model.getTable().getCharacters().get(1).getName());
    }

    @Test
    void SimulateGRANNY() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Granny) {
                assertEquals(player.getCoins(), coins);
                assertEquals(((Granny) model.getTable().getCharacters().get(i)).getNumDivieti(), 4);
                ((Granny) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), 2, model.getTable());
                assertEquals(((Granny) model.getTable().getCharacters().get(i)).getNumDivieti(), 3);
                assertTrue(model.getTable().getIslands().get(2).isBlocked());
                assertEquals(player.getCoins(), coins - 2);
            }
        }
    }

    @Test
    void SimulateCENTAUR() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        Player otherPlayer = model.getPlayers().get(1);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Centaur) {
                assertEquals(player.getCoins(), coins);
                ((Centaur) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), model.getTable());
                assertEquals(player.getCoins(), coins - 3);
                player.getSchoolBoard().getDinnerTable().addStudents(1, PeopleColor.RED);
                model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
                model.getTable().getIslands().get(0).getInhabitants().addStudents(1, PeopleColor.RED);
                model.getTable().getIslands().get(0).placeTower();
                model.getTable().getIslands().get(0).placeTower();
                model.getTable().getIslands().get(0).setTowerColor(otherPlayer.getSchoolBoard().getTowerColor());
                assertEquals(player, model.getTable().getIslands().get(0).playerInfluenceCalculator(model.getPlayers(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect()));
            }
        }
    }

    @Test
    void SimulateFARMER() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        Player otherPlayer = model.getPlayers().get(1);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Farmer) {
                assertEquals(player.getCoins(), coins);
                ((Farmer) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), model.getTable(), model.getPlayers());
                otherPlayer.getSchoolBoard().getEntranceSpace().addStudents(3, PeopleColor.RED);
                model.getPlayers().get(1).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.RED)) {
                        assertEquals(model.getTable().getProfessors().get(j).getHeldBy().getNickname(), "pippo");
                    }
                }
                otherPlayer.getSchoolBoard().getEntranceSpace().addStudents(3, PeopleColor.RED);
                model.getPlayers().get(0).getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.RED)) {
                        assertEquals(model.getTable().getProfessors().get(j).getHeldBy().getNickname(), "paperino");
                    }
                }
                assertEquals(player.getCoins(), coins - 2);
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateHERALD() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        Player otherPlayer = model.getPlayers().get(1);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Herald) {
                assertEquals(player.getCoins(), coins);
                model.getTable().getIslands().get(0).getInhabitants().addStudents(1, PeopleColor.BLUE);
                player.getSchoolBoard().getEntranceSpace().addStudents(9, PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                model.getTable().checkProfessor(PeopleColor.BLUE, model.getPlayers());
                ((Herald) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), 0, model);
                assertEquals(player.getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(0).getTowerColor());
                assertEquals(player.getCoins(), coins - 3);
                otherPlayer.getSchoolBoard().getEntranceSpace().addStudents(12, PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.BLUE);
                model.getTable().checkProfessor(PeopleColor.BLUE, model.getPlayers());
                ((Herald) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), 0, model);
                assertNotEquals(otherPlayer.getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(0).getTowerColor());
                model.getTable().getIslands().get(0).getInhabitants().addStudents(2, PeopleColor.BLUE);
                ((Herald) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), 0, model);
                assertEquals(otherPlayer.getSchoolBoard().getTowerColor(), model.getTable().getIslands().get(0).getTowerColor());
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateKNIGHT() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Knight) {
                assertEquals(player.getCoins(), coins);
                ((Knight) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), model.getTable());
                assertEquals(model.getTable().getIslands().get(6).playerInfluenceCalculator(model.getPlayers(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), player), player);
                assertEquals(player.getCoins(), coins - 2);
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateMONK() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Monk) {
                assertEquals(player.getCoins(), coins);
                ((Monk) model.getTable().getCharacters().get(i)).getSet().addStudents(1, PeopleColor.PINK);
                ((Monk) model.getTable().getCharacters().get(i)).useEffect(player, PeopleColor.PINK, 7, model.getTable());
                assertTrue(model.getTable().getIslands().get(7).getInhabitants().numStudentsByColor(PeopleColor.PINK) == 2 || model.getTable().getIslands().get(7).getInhabitants().numStudentsByColor(PeopleColor.PINK) == 1);
                assertEquals(player.getCoins(), coins - 1);
                assertEquals(5, ((Monk) model.getTable().getCharacters().get(i)).getSet().size());
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateMUSHHUNTER() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        Player otherPlayer = model.getPlayers().get(1);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof MushroomHunter) {
                assertEquals(player.getCoins(), coins);
                otherPlayer.getSchoolBoard().getEntranceSpace().addStudents(9, PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                otherPlayer.getSchoolBoard().loadDinnerTable(PeopleColor.PINK);
                model.getTable().checkProfessor(PeopleColor.PINK, model.getPlayers());

                player.getSchoolBoard().getEntranceSpace().addStudents(9, PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                player.getSchoolBoard().loadDinnerTable(PeopleColor.RED);
                model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());

                model.getTable().getIslands().get(0).getInhabitants().addStudents(2, PeopleColor.PINK);
                model.getTable().getIslands().get(0).getInhabitants().addStudents(1, PeopleColor.RED);

                assertEquals(otherPlayer, model.getTable().getIslands().get(0).playerInfluenceCalculator(model.getPlayers(), model.getTable().getProfessors(), false, model.getTable().getMushroomColor(), model.getTable().getKnightEffect()));
                ((MushroomHunter) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), PeopleColor.PINK, model.getTable());
                assertEquals(player, model.getTable().getIslands().get(0).playerInfluenceCalculator(model.getPlayers(), model.getTable().getProfessors(), false, model.getTable().getMushroomColor(), model.getTable().getKnightEffect()));
                assertEquals(player.getCoins(), coins - 3);
            }
        }
    }

    @Test
    void SimulatePOSTMAN() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        player.setChosenCard(player.getAvailableCards().getCardsList().get(0));
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Postman) {
                assertEquals(player.getCoins(), coins);
                assertEquals(player.getChosenCard().getMoves(), 1);
                ((Postman) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0));
                assertEquals(player.getChosenCard().getMoves(), 3);
                assertEquals(player.getCoins(), coins - 1);
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulatePrincess() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        player.setChosenCard(player.getAvailableCards().getCardsList().get(0));
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Princess) {
                assertEquals(player.getCoins(), coins);
                int numColor = player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.RED);
                ((Princess) model.getTable().getCharacters().get(i)).useEffect(model.getPlayers().get(0), PeopleColor.RED, model.getTable(), model.getPlayers());
                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.RED)) {
                        assertEquals(model.getTable().getProfessors().get(j).getHeldBy().getNickname(), "paperino");
                    }
                }
                assertEquals(numColor + 1, player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.RED));
                assertEquals(player.getCoins(), coins - 2);
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateTHIEF() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        Player otherPlayer = model.getPlayers().get(1);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Thief) {
                assertEquals(player.getCoins(), coins);
                player.getSchoolBoard().getDinnerTable().addStudents(4, PeopleColor.YELLOW);
                otherPlayer.getSchoolBoard().getDinnerTable().addStudents(1, PeopleColor.YELLOW);
                ((Thief) model.getTable().getCharacters().get(i)).useEffect(player, model.getPlayers(), PeopleColor.YELLOW, model.getTable());

                assertEquals(1, player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.YELLOW));
                assertEquals(0, otherPlayer.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.YELLOW));

                assertEquals(player.getCoins(), coins - 3);
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateJESTER() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Jester) {
                assertEquals(player.getCoins(), coins);
                ((Jester) model.getTable().getCharacters().get(i)).getSet().addStudents(1, PeopleColor.PINK);
                int numColor1A = ((Jester) model.getTable().getCharacters().get(i)).getSet().numStudentsByColor(PeopleColor.PINK);
                int numColor1B = ((Jester) model.getTable().getCharacters().get(i)).getSet().numStudentsByColor(PeopleColor.RED);
                ArrayList<PeopleColor> colors1 = new ArrayList<>();
                colors1.add(PeopleColor.PINK);
                player.getSchoolBoard().getEntranceSpace().addStudents(1, PeopleColor.RED);
                int numColor2A = player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.RED);
                int numColor2B = player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.PINK);
                ArrayList<PeopleColor> colors2 = new ArrayList<>();
                colors2.add(PeopleColor.RED);
                ((Jester) model.getTable().getCharacters().get(i)).useEffect(player, colors1, colors2);

                assertEquals(numColor1A, ((Jester) model.getTable().getCharacters().get(i)).getSet().numStudentsByColor(PeopleColor.PINK) + 1);
                assertEquals(numColor1B, ((Jester) model.getTable().getCharacters().get(i)).getSet().numStudentsByColor(PeopleColor.RED) - 1);

                assertEquals(numColor2A, player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.RED) + 1);
                assertEquals(numColor2B, player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.PINK) - 1);

                assertEquals(player.getCoins(), coins - 1);
                coins = player.getCoins();
            }
        }
    }

    @Test
    void SimulateMINSTREL() {
        Model model = new Model(2, "EXPERT", true);
        model.getPlayers().add(new Player("paperino", "192.168.0.2", model, true));
        model.getPlayers().add(new Player("pippo", "192.168.0.1", model, true));

        Player player = model.getPlayers().get(0);
        int coins = player.getCoins();

        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
            if (model.getTable().getCharacters().get(i) instanceof Minstrel) {
                assertEquals(player.getCoins(), coins);
                player.getSchoolBoard().getDinnerTable().addStudents(1, PeopleColor.PINK);
                model.getTable().checkProfessor(PeopleColor.PINK, model.getPlayers());
                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.PINK)) {
                        assertEquals(model.getTable().getProfessors().get(j).getHeldBy().getNickname(), "paperino");
                    }
                }
                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.RED)) {
                        assertNull(model.getTable().getProfessors().get(j).getHeldBy());
                    }
                }

                int numColor1A = player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.PINK);
                int numColor1B = player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.RED);
                ArrayList<PeopleColor> colors1 = new ArrayList<>();
                colors1.add(PeopleColor.PINK);
                player.getSchoolBoard().getEntranceSpace().addStudents(1, PeopleColor.RED);
                int numColor2A = player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.RED);
                int numColor2B = player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.PINK);
                ArrayList<PeopleColor> colors2 = new ArrayList<>();
                colors2.add(PeopleColor.RED);
                ((Minstrel) model.getTable().getCharacters().get(i)).useEffect(player, colors1, colors2, model.getTable(), model.getPlayers());

                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.PINK)) {
                        assertEquals(model.getTable().getProfessors().get(j).getHeldBy().getNickname(), "paperino");
                    }
                }
                for (int j = 0; j < model.getTable().getProfessors().size(); j++) {
                    if (model.getTable().getProfessors().get(j).getColor().equals(PeopleColor.RED)) {
                        assertEquals(model.getTable().getProfessors().get(j).getHeldBy().getNickname(), "paperino");
                    }
                }

                assertEquals(numColor1A, player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.PINK) + 1);
                assertEquals(numColor1B, player.getSchoolBoard().getDinnerTable().numStudentsByColor(PeopleColor.RED) - 1);

                assertEquals(numColor2A, player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.RED) + 1);
                assertEquals(numColor2B, player.getSchoolBoard().getEntranceSpace().numStudentsByColor(PeopleColor.PINK) - 1);

                assertEquals(player.getCoins(), coins - 1);
                coins = player.getCoins();
            }
        }
    }
}
