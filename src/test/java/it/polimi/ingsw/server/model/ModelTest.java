package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.characters.Postman;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    @Test
    void testCreateModel2playerPrincipiante() {
        Model model=new Model(2,"PRINCIPIANT");
        model.getPlayers().add(new Player("pippo","192.168.0.1", model));
        model.getPlayers().add(new Player("paperino","192.168.0.2", model));

        //assert Model
        assertEquals(2,model.getPlayers().size());
        assertEquals(GameMode.PRINCIPIANT,model.getGameMode());
       assertEquals(2,model.getNumberOfPlayers());
        assertNull(model.getTeams());
        assertEquals(model.getPlayers().get(0),model.getcurrentPlayer());


        //assert player 1
       assertEquals("pippo",model.getPlayers().get(0).getNickname());
        assertEquals("192.168.0.1",model.getPlayers().get(0).getIp());
        assertEquals(10,model.getPlayers().get(0).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(0).getChoosedCard());
        assertEquals(0,model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
        assertEquals(7,model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertTrue(TowerColor.BLACK==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE==model.getPlayers().get(0).getSchoolBoard().getTowerColor());
        //assert player 2
        assertEquals("paperino",model.getPlayers().get(1).getNickname());
        assertEquals("192.168.0.2",model.getPlayers().get(1).getIp());
        assertEquals(10,model.getPlayers().get(1).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(1).getChoosedCard());
        assertEquals(0,model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
        assertEquals(7,model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(8,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());

        //assert CentreTable
            //assert clouds
        assertEquals(2,model.getTable().getClouds().size());
        for(int i=0;i<model.getTable().getClouds().size();i++)
        {
            assertEquals(3,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
        }
           //assert Island and mother nature position
        assertEquals(12,model.getTable().getIslands().size());
        for(int i=0;i<model.getTable().getIslands().size();i++){
            if(i==0){

                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            else if(i==6){
                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            else{
                assertEquals(1,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            assertEquals(0,model.getTable().getIslands().get(i).getNumberOfTowers());
            assertNull(model.getTable().getIslands().get(i).getTowerColor());
            assertFalse(model.getTable().getIslands().get(i).isBlocked());
        }
            //assert bag
       assertEquals(100,model.getTable().getBag().size());
           //assert professor
       assertEquals(5,model.getTable().getProfessors().size());
        for(int i=0; i<model.getTable().getProfessors().size();i++){
            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
            for(int j=0;j<i; j++) {
                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
            }
            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
       }
    }

    @Test
    void testCreateModel3playerPrincipiante() {
        Model model=new Model(3,"PRINCIPIANT");
        model.getPlayers().add(new Player("pippo","192.168.0.1", model));
        model.getPlayers().add(new Player("paperino","192.168.0.2", model));
        model.getPlayers().add(new Player("pluto","192.168.0.3",model));

        //assert Model
        assertEquals(3,model.getPlayers().size());
        assertEquals(GameMode.PRINCIPIANT,model.getGameMode());
        assertEquals(3,model.getNumberOfPlayers());
        assertNull(model.getTeams());
        assertEquals(model.getPlayers().get(0),model.getcurrentPlayer());


        //assert player 1
        assertEquals("pippo",model.getPlayers().get(0).getNickname());
        assertEquals("192.168.0.1",model.getPlayers().get(0).getIp());
        assertEquals(10,model.getPlayers().get(0).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(0).getChoosedCard());
        assertEquals(0,model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
        assertEquals(9,model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
        assertEquals(6,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
       assertTrue(TowerColor.BLACK==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.GREY==model.getPlayers().get(0).getSchoolBoard().getTowerColor());

        //assert player 3
        assertEquals("paperino",model.getPlayers().get(1).getNickname());
       assertEquals("192.168.0.2",model.getPlayers().get(1).getIp());
        assertEquals(10,model.getPlayers().get(1).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(1).getChoosedCard());
        assertEquals(0,model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
        assertEquals(9,model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE) || TowerColor.GREY==model.getPlayers().get(1).getSchoolBoard().getTowerColor()) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(6,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());

        //assert player 2
        assertEquals("pluto",model.getPlayers().get(2).getNickname());
        assertEquals("192.168.0.3",model.getPlayers().get(2).getIp());
        assertEquals(10,model.getPlayers().get(2).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(2).getChoosedCard());
        assertEquals(0,model.getPlayers().get(2).getSchoolBoard().getDinnerTable().size());
        assertEquals(9,model.getPlayers().get(2).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE) || TowerColor.GREY==model.getPlayers().get(2).getSchoolBoard().getTowerColor()) && !(model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(1).getSchoolBoard().getTowerColor())) && !(model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(6,model.getPlayers().get(2).getSchoolBoard().getNumOfTowers());


        //assert CentreTable
        //assert clouds
        assertEquals(3,model.getTable().getClouds().size());
        for(int i=0;i<model.getTable().getClouds().size();i++)
        {
           assertEquals(4,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
           assertEquals(4,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
        }
        //assert Island and mother nature position
        assertEquals(12,model.getTable().getIslands().size());
       for(int i=0;i<model.getTable().getIslands().size();i++){
            if(i==0){

                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
               assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            else if(i==6){
                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            else{
                assertEquals(1,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            assertEquals(0,model.getTable().getIslands().get(i).getNumberOfTowers());
           assertNull(model.getTable().getIslands().get(i).getTowerColor());
            assertFalse(model.getTable().getIslands().get(i).isBlocked());
        }
        //assert bag
        assertEquals(81,model.getTable().getBag().size());
        //assert professor
       assertEquals(5,model.getTable().getProfessors().size());
        for(int i=0; i<model.getTable().getProfessors().size();i++){
            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
            for(int j=0;j<i; j++) {
                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
            }
            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
        }
    }
    @Test
    void testCreateModel4playerPrincipiante() {
        Model model=new Model(4,"PRINCIPIANT");
        model.getPlayers().add(new Player("pippo","192.168.0.1",1, model));
        model.getPlayers().add(new Player("paperino","192.168.0.2", 2,model));
       model.getPlayers().add(new Player("pluto","192.168.0.3",1,model));
        model.getPlayers().add(new Player("minnie","192.168.0.4",2,model));

        //assert Model
        assertEquals(4,model.getPlayers().size());
        assertEquals(GameMode.PRINCIPIANT,model.getGameMode());
        assertEquals(4,model.getNumberOfPlayers());
        assertEquals(2,model.getTeams().size());

        //assert team
        assertEquals(1,model.getTeams().get(0).getTeamNumber());
        assertEquals(2,model.getTeams().get(1).getTeamNumber());
        assertEquals(model.getPlayers().get(0),model.getTeams().get(0).getPlayer1());
        assertEquals(model.getPlayers().get(1),model.getTeams().get(1).getPlayer1());
        assertEquals(model.getPlayers().get(2),model.getTeams().get(0).getPlayer2());
        assertEquals(model.getPlayers().get(3),model.getTeams().get(1).getPlayer2());

        assertEquals(model.getPlayers().get(0),model.getcurrentPlayer());


        //assert player 1
        assertEquals("pippo",model.getPlayers().get(0).getNickname());
        assertEquals("192.168.0.1",model.getPlayers().get(0).getIp());
        assertEquals(10,model.getPlayers().get(0).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(0).getChoosedCard());
        assertEquals(0,model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
        assertEquals(7,model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertTrue(TowerColor.BLACK==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE==model.getPlayers().get(0).getSchoolBoard().getTowerColor());

       //assert player 2
       assertEquals("paperino",model.getPlayers().get(1).getNickname());
        assertEquals("192.168.0.2",model.getPlayers().get(1).getIp());
        assertEquals(10,model.getPlayers().get(1).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(1).getChoosedCard());
        assertEquals(0,model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
       assertEquals(7,model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
       assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
       assertEquals(8,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());

       //assert player 3
        assertEquals("pluto",model.getPlayers().get(2).getNickname());
        assertEquals("192.168.0.3",model.getPlayers().get(2).getIp());
        assertEquals(10,model.getPlayers().get(2).getAvailableCards().getCardsList().size());
        assertNull(model.getPlayers().get(2).getChoosedCard());
        assertEquals(0,model.getPlayers().get(2).getSchoolBoard().getDinnerTable().size());
        assertEquals(7,model.getPlayers().get(2).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && (model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
        assertEquals(8,model.getPlayers().get(2).getSchoolBoard().getNumOfTowers());

        //assert player 4
       assertEquals("minnie",model.getPlayers().get(3).getNickname());
        assertEquals("192.168.0.4",model.getPlayers().get(3).getIp());
        assertEquals(10,model.getPlayers().get(3).getAvailableCards().getCardsList().size());
       assertNull(model.getPlayers().get(3).getChoosedCard());
        assertEquals(0,model.getPlayers().get(3).getSchoolBoard().getDinnerTable().size());
        assertEquals(7,model.getPlayers().get(3).getSchoolBoard().getEntranceSpace().size());
        assertTrue((model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && (model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(1).getSchoolBoard().getTowerColor())));
        assertEquals(8,model.getPlayers().get(3).getSchoolBoard().getNumOfTowers());


        //assert CentreTable
        //assert clouds
        assertEquals(4,model.getTable().getClouds().size());
        for(int i=0;i<model.getTable().getClouds().size();i++)
        {
            assertEquals(3,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
        }
        //assert Island and mother nature position
        assertEquals(12,model.getTable().getIslands().size());
        for(int i=0;i<model.getTable().getIslands().size();i++){
           if(i==0){

                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            else if(i==6){
                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
            }
            else{
                assertEquals(1,model.getTable().getIslands().get(i).getInhabitants().size());
            }
          assertEquals(0,model.getTable().getIslands().get(i).getNumberOfTowers());
            assertNull(model.getTable().getIslands().get(i).getTowerColor());
           assertFalse(model.getTable().getIslands().get(i).isBlocked());
        }
        //assert bag
        assertEquals(80,model.getTable().getBag().size());
       //assert professor
       assertEquals(5,model.getTable().getProfessors().size());
       for(int i=0; i<model.getTable().getProfessors().size();i++){
           assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
           for(int j=0;j<i; j++) {
              assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
            }
            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
        }
    }

    @Test
    void testDeck() {
        Deck deck=new Deck();
        assertEquals(10,deck.getCardsList().size());
        AssistantCard card=new AssistantCard(1,1);
        assertTrue(deck.inDeck(card));
        assertFalse(deck.remove(card));
        assertFalse(deck.inDeck(card));
        assertEquals(9,deck.getCardsList().size());
        AssistantCard card2 = new AssistantCard(2, 1);
        assertTrue(deck.inDeck(card2));
        assertFalse(deck.remove(card2));
        assertFalse(deck.inDeck(card2));
        assertEquals(8,deck.getCardsList().size());
        AssistantCard card3 = new AssistantCard(3, 2);
        assertTrue(deck.inDeck(card3));
        assertFalse(deck.remove(card3));
        assertFalse(deck.inDeck(card3));
        assertEquals(7,deck.getCardsList().size());
        AssistantCard card6 = new AssistantCard(6, 3);
        assertTrue(deck.inDeck(card6));
        assertFalse(deck.remove(card6));
        assertFalse(deck.inDeck(card6));
        assertEquals(6,deck.getCardsList().size());
        AssistantCard card4 = new AssistantCard(4, 2);
        assertTrue(deck.inDeck(card4));
        assertFalse(deck.remove(card4));
        assertFalse(deck.inDeck(card4));
        assertEquals(5,deck.getCardsList().size());
        AssistantCard card5 = new AssistantCard(5, 3);
        assertTrue(deck.inDeck(card5));
        assertFalse(deck.remove(card5));
        assertFalse(deck.inDeck(card5));
        assertEquals(4,deck.getCardsList().size());
        AssistantCard card9 = new AssistantCard(9, 5);
        assertTrue(deck.inDeck(card9));
        assertFalse(deck.remove(card9));
        assertFalse(deck.inDeck(card9));
        assertThrows(IllegalArgumentException.class,()-> assertFalse(deck.remove(card3)));
        assertEquals(3,deck.getCardsList().size());
        AssistantCard card10 = new AssistantCard(10, 5);
        assertTrue(deck.inDeck(card10));
        assertFalse(deck.remove(card10));
        assertFalse(deck.inDeck(card10));
        assertEquals(2,deck.getCardsList().size());
        AssistantCard card7 = new AssistantCard(7, 4);
        assertTrue(deck.inDeck(card7));
        assertFalse(deck.remove(card7));
        assertFalse(deck.inDeck(card7));
        assertEquals(1,deck.getCardsList().size());
        AssistantCard card8 = new AssistantCard(8, 4);
        assertTrue(deck.inDeck(card8));
        assertTrue(deck.remove(card8));
        assertFalse(deck.inDeck(card8));
        assertEquals(0,deck.getCardsList().size());
    }
    @Test
    void StudentTest(){
        StudentSet set=new StudentSet();
        for(PeopleColor color:PeopleColor.values()){
            assertEquals(0,set.numStudentsbycolor(color));
        }
    }
    @Test
    void extractSomeStudent() {
        StudentSet set=new StudentSet(2,2,2,2,2);
        assertEquals(10,set.size());
        set.removestudent(2,PeopleColor.RED);
        set.removestudent(3,PeopleColor.GREEN);
        set.removestudent(1,PeopleColor.YELLOW);
        set.addstudents(2,PeopleColor.PINK);
        set.addstudents(27,PeopleColor.BLUE);
        set.addstudents(1,PeopleColor.BLUE);
        assertEquals(0,set.numStudentsbycolor(PeopleColor.RED));
        assertEquals(0,set.numStudentsbycolor(PeopleColor.GREEN));
        assertEquals(1,set.numStudentsbycolor(PeopleColor.YELLOW));
        assertEquals(30,set.numStudentsbycolor(PeopleColor.BLUE));
        assertEquals(4,set.numStudentsbycolor(PeopleColor.PINK));
        assertEquals(35,set.size());
        StudentSet set2=new StudentSet(1,1,1,1,1);
        set2.setAllStudentTozero();
        set2.addstudents(2,PeopleColor.RED);
        StudentSet set3=new StudentSet(2,0,0,0,0);
        assertEquals(set2, set3);
    }

    @Test
    void numStudentsColor() {
        StudentSet bag=new StudentSet(2,2,2,2,2);
        StudentSet set=new StudentSet();

        set.setStudentsRandomly(1,bag);

        assertEquals(1,set.size());
        assertEquals(9,bag.size());

        set.setStudentsRandomly(9,bag);
        assertEquals(0,bag.size());
        assertEquals(10,set.size());
        assertThrows(IllegalArgumentException.class,()-> set.setStudentsRandomly(1,bag));
    }
    @Test
    void SimulateGame2Player(){
        Model model=new Model(2,"PRINCIPIANT");
        model.getPlayers().add(new Player("paperino","192.168.0.2", model));
        model.getPlayers().add(new Player("pippo","192.168.0.1", model));

        model.getTable().setDebug(true);
        model.getPlayers().get(0).setDebug(true);
        model.getPlayers().get(1).setDebug(true);

        model.getPlayers().get(1).setChoosedCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(0));
        model.getPlayers().get(0).setChoosedCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(1));

        assertEquals("pippo",model.getPlayers().get(1).getNickname());
        model.schedulePlayers();
        assertEquals("pippo",model.getPlayers().get(0).getNickname());

        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addstudents(2,PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addstudents(2,PeopleColor.YELLOW);
        model.getPlayers().get(0).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"pippo");
            }
        }
        model.getPlayers().get(0).getSchoolBoard().load_dinner(PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(0),PeopleColor.YELLOW,2);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"pippo");
            }
        }
        assertEquals(2,model.getTable().getIslands().get(2).getInhabitants().size());

        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addstudents(2,PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addstudents(3,PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(1),PeopleColor.YELLOW,2);
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"pippo");
            }
        }
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.YELLOW);
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.YELLOW);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"paperino");
            }
        }

        model.getTable().mother(2);

        assertEquals(model.getTable().getMotherNaturePosition(),2);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getInhabitants().size(),3);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).player_influence(model.getPlayers(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect()).getNickname(),"paperino");

        model.getTable().getIslands().get(3).getInhabitants().addstudents(2,PeopleColor.YELLOW);
        model.getTable().getIslands().get(2).controllIsland(model.getPlayers().get(1));
        model.getTable().getIslands().get(3).controllIsland(model.getPlayers().get(1));
        model.getTable().getIslands().get(3).placeTower();
        model.getTable().getIslands().get(2).placeTower();

        assertEquals(6,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(1,model.getTable().getIslands().get(3).getNumberOfTowers());
        assertEquals(1,model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(3).getTowerColor());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(2).getTowerColor());

        model.getTable().mergeIsland(2,3);

        assertEquals(11,model.getTable().getIslands().size());
        assertEquals(2,model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(6,model.getTable().getIslands().get(2).getInhabitants().size());

        model.getTable().conquestIsland(2,model.getPlayers(),model.getPlayers().get(0));
        assertEquals(8,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(6,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(model.getPlayers().get(0).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(2).getTowerColor());

        assertEquals(model.player_winner(),model.getPlayers().get(0).getNickname());

        String view="\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + 0 + "    " + "" +"\n\n" + model.getTable().toString(false) +
                "-----------------PLAYER---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ model.printplayers("pippo") +
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ "ciao"+ "\n";
        assertEquals(model.toString("pippo","ciao"),view);

        view="-----------------------------------------TABLE----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "\n----------------ISLANDS---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.getTable().printIslands() + "------------------BAG-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    SIZE : " + model.getTable().getBag().size() + "    " + model.getTable().getBag().toString() +
                "\n----------------CLOUDS----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.getTable().printClouds() +
                 "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" +
                "---------------PROFESSORS-------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                model.getTable().printProfessors() + "\n\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n";

        assertEquals(model.getTable().toString(false),view);

    }

    @Test
    void SimulateGame4Player(){
        Model model=new Model(4,"PRINCIPIANT");
        model.getPlayers().add(new Player("paperino","192.168.0.2",1, model));
        model.getPlayers().add(new Player("pippo","192.168.0.1", 2,model));
        model.getPlayers().add(new Player("paperino2","192.168.0.2", 1,model));
        model.getPlayers().add(new Player("pippo2","192.168.0.1",2, model));

        model.getTable().setDebug(true);
        model.getPlayers().get(0).setDebug(true);
        model.getPlayers().get(1).setDebug(true);

        model.getPlayers().get(1).setChoosedCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(0));
        model.getPlayers().get(0).setChoosedCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(1));
        model.getPlayers().get(2).setChoosedCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(5));
        model.getPlayers().get(3).setChoosedCard(model.getPlayers().get(0).getAvailableCards().getCardsList().get(6));

        assertEquals("pippo",model.getPlayers().get(1).getNickname());
        model.schedulePlayers();
        assertEquals("pippo",model.getPlayers().get(0).getNickname());

        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addstudents(2,PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().addstudents(2,PeopleColor.YELLOW);
        model.getPlayers().get(0).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getPlayers().get(0).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"pippo");
            }
        }
        model.getPlayers().get(0).getSchoolBoard().load_dinner(PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(0),PeopleColor.YELLOW,2);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"pippo");
            }
        }
        assertEquals(2,model.getTable().getIslands().get(2).getInhabitants().size());

        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addstudents(2,PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().addstudents(3,PeopleColor.YELLOW);
        model.getTable().loadIsland(model.getPlayers().get(1),PeopleColor.YELLOW,2);
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.RED);
        model.getTable().checkProfessor(PeopleColor.RED, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"pippo");
            }
        }
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.YELLOW);
        model.getPlayers().get(1).getSchoolBoard().load_dinner(PeopleColor.YELLOW);
        model.getTable().checkProfessor(PeopleColor.YELLOW, model.getPlayers());
        for(int i=0;i<model.getTable().getProfessors().size();i++){
            if(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW)){
                assertEquals(model.getTable().getProfessors().get(i).getHeldBy().getNickname(),"paperino");
            }
        }

        model.getTable().mother(2);

        assertEquals(model.getTable().getMotherNaturePosition(),2);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getInhabitants().size(),3);
        assertEquals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).team_influence(model.getTeams(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect()).getPlayer1().getNickname(),"paperino");

        model.getTable().getIslands().get(3).getInhabitants().addstudents(2,PeopleColor.YELLOW);
        model.getTable().getIslands().get(2).controllIsland(model.getPlayers().get(1));
        model.getTable().getIslands().get(3).controllIsland(model.getPlayers().get(1));
        model.getTable().getIslands().get(3).placeTower();
        model.getTable().getIslands().get(2).placeTower();

        assertEquals(6,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(1,model.getTable().getIslands().get(3).getNumberOfTowers());
        assertEquals(1,model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(3).getTowerColor());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(2).getTowerColor());

        model.getTable().mergeIsland(2,3);

        assertEquals(11,model.getTable().getIslands().size());
        assertEquals(2,model.getTable().getIslands().get(2).getNumberOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(2).getTowerColor());
        assertEquals(6,model.getTable().getIslands().get(2).getInhabitants().size());

        model.getTable().conquestIsland(2,model.getTeams(),model.getTeams().get(0));
        assertEquals(6,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
        assertEquals(model.getPlayers().get(1).getSchoolBoard().getTowerColor(),model.getTable().getIslands().get(2).getTowerColor());

        String winner= model.getTeams().get(0).getPlayer1().getNickname() + "+" + model.getTeams().get(0).getPlayer2().getNickname();
        assertEquals(model.team_winner(),winner);

        String view="\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + 0 + "    " +  ""  +"\n\n" +
                model.getTable().toString(false) +
                 "-----------------TEAM-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + model.printteam("pippo") +
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ "ciao"+ "\n" ;
        assertEquals(model.toString("pippo","ciao"),view);

    }
}