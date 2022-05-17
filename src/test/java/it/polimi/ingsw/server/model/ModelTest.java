//package it.polimi.ingsw.server.model;
//
//import it.polimi.ingsw.server.model.enums.GameMode;
//import it.polimi.ingsw.server.model.enums.PeopleColor;
//import it.polimi.ingsw.server.model.enums.TowerColor;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ModelTest {
//    @Test
//    void testCreateModel2playerPrincipiant() throws Exception {
//        Model model=Model.createModel(2,"PRINCIPIANT");
//        model.getPlayers().add(new Player("pippo","192.168.0.1", model));
//        model.getPlayers().add(new Player("paperino","192.168.0.2", model));
//
//        //assert Model
//        assertEquals(2,model.getPlayers().size());
//        assertEquals(GameMode.PRINCIPIANT,model.getGameMode());
//        assertEquals(2,model.getNumberOfPlayers());
//        assertEquals(0,model.getTurnNumber());
//        assertNull(model.getTeams());
//        assertEquals(model.getPlayers().get(0),model.getcurrentPlayer());
//
//
//        //assert player 1
//        assertEquals("pippo",model.getPlayers().get(0).getNickname());
//        assertEquals("192.168.0.1",model.getPlayers().get(0).getIp());
//        assertEquals(10,model.getPlayers().get(0).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(0).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
//        assertEquals(7,model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
//        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
//        assertTrue(TowerColor.BLACK==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE==model.getPlayers().get(0).getSchoolBoard().getTowerColor());
//
//        //assert player 2
//        assertEquals("paperino",model.getPlayers().get(1).getNickname());
//        assertEquals("192.168.0.2",model.getPlayers().get(1).getIp());
//        assertEquals(10,model.getPlayers().get(1).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(1).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
//        assertEquals(7,model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
//        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
//        assertEquals(8,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
//
//        //assert CentreTable
//            //assert clouds
//        assertEquals(2,model.getTable().getClouds().size());
//        for(int i=0;i<model.getTable().getClouds().size();i++)
//        {
//            assertEquals(3,model.getTable().getClouds().get(i).getCloudsize());
//            assertEquals(3,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
//        }
//           //assert Island and mother nathure position
//        assertEquals(12,model.getTable().getIslands().size());
//        for(int i=0;i<model.getTable().getIslands().size();i++){
//            if(i==0){
//
//                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
//                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            else if(i==6){
//                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            else{
//                assertEquals(1,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            assertEquals(0,model.getTable().getIslands().get(i).getNumberOfTowers());
//            assertNull(model.getTable().getIslands().get(i).getTowerColor());
//            assertFalse(model.getTable().getIslands().get(i).isBlocked());
//        }
//            //assert bag
//        assertEquals(100,model.getTable().getBag().size());
//            //assert avaible student color
////        assertEquals(5,model.getTable().getAvaiablePeopleColorinBag().size());
////        for(int i=0; i<model.getTable().getAvaiablePeopleColorinBag().size();i++){
////            assertTrue(model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.RED) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.YELLOW) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.GREEN) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.PINK) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.BLUE));
////            for(int j=0;j<i; j++){
////                assertNotEquals(model.getTable().getAvaiablePeopleColorinBag().get(i), model.getTable().getAvaiablePeopleColorinBag().get(j));
////            }
////        }
//           //assert avaible tower color
//        assertEquals(0,model.getTable().getAvaiableTowerColor().size());
//           //assert charachter card
//        assertNull(model.getTable().getCharacterCards());
//           //assert professor
//        assertEquals(5,model.getTable().getProfessors().size());
//        for(int i=0; i<model.getTable().getProfessors().size();i++){
//            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
//            for(int j=0;j<i; j++) {
//                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
//            }
//            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
//        }
//    }
//
//    @Test
//    void testCreateModel3playerPrincipiant() throws Exception {
//        Model model=Model.createModel(3,"PRINCIPIANT");
//        model.getPlayers().add(new Player("pippo","192.168.0.1", model));
//        model.getPlayers().add(new Player("paperino","192.168.0.2", model));
//        model.getPlayers().add(new Player("pluto","192.168.0.3",model));
//
//        //assert Model
//        assertEquals(3,model.getPlayers().size());
//        assertEquals(GameMode.PRINCIPIANT,model.getGameMode());
//        assertEquals(3,model.getNumberOfPlayers());
//        assertEquals(0,model.getTurnNumber());
//        assertNull(model.getTeams());
//        assertEquals(model.getPlayers().get(0),model.getcurrentPlayer());
//
//
//        //assert player 1
//        assertEquals("pippo",model.getPlayers().get(0).getNickname());
//        assertEquals("192.168.0.1",model.getPlayers().get(0).getIp());
//        assertEquals(10,model.getPlayers().get(0).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(0).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
//        assertEquals(9,model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
//        assertEquals(6,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
//        assertTrue(TowerColor.BLACK==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.GREY==model.getPlayers().get(0).getSchoolBoard().getTowerColor());
//
//        //assert player 3
//        assertEquals("paperino",model.getPlayers().get(1).getNickname());
//        assertEquals("192.168.0.2",model.getPlayers().get(1).getIp());
//        assertEquals(10,model.getPlayers().get(1).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(1).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
//        assertEquals(9,model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
//        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE) || TowerColor.GREY==model.getPlayers().get(1).getSchoolBoard().getTowerColor()) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
//        assertEquals(6,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
//
//        //assert player 2
//        assertEquals("pluto",model.getPlayers().get(2).getNickname());
//        assertEquals("192.168.0.3",model.getPlayers().get(2).getIp());
//        assertEquals(10,model.getPlayers().get(2).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(2).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(2).getSchoolBoard().getDinnerTable().size());
//        assertEquals(9,model.getPlayers().get(2).getSchoolBoard().getEntranceSpace().size());
//        assertTrue((model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE) || TowerColor.GREY==model.getPlayers().get(2).getSchoolBoard().getTowerColor()) && !(model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(1).getSchoolBoard().getTowerColor())) && !(model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
//        assertEquals(6,model.getPlayers().get(2).getSchoolBoard().getNumOfTowers());
//
//
//
//        //assert CentreTable
//        //assert clouds
//        assertEquals(3,model.getTable().getClouds().size());
//        for(int i=0;i<model.getTable().getClouds().size();i++)
//        {
//            assertEquals(4,model.getTable().getClouds().get(i).getCloudsize());
//            assertEquals(4,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
//        }
//        //assert Island and mother nathure position
//        assertEquals(12,model.getTable().getIslands().size());
//        for(int i=0;i<model.getTable().getIslands().size();i++){
//            if(i==0){
//
//                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
//                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            else if(i==6){
//                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            else{
//                assertEquals(1,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            assertEquals(0,model.getTable().getIslands().get(i).getNumberOfTowers());
//            assertNull(model.getTable().getIslands().get(i).getTowerColor());
//            assertFalse(model.getTable().getIslands().get(i).isBlocked());
//        }
//        //assert bag
//        assertEquals(81,model.getTable().getBag().size());
//        //assert avaible student color
////        assertTrue(5==model.getTable().getAvaiablePeopleColorinBag().size() || 4==model.getTable().getAvaiablePeopleColorinBag().size());
////        for(int i=0; i<model.getTable().getAvaiablePeopleColorinBag().size();i++){
////            assertTrue(model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.RED) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.YELLOW) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.GREEN) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.PINK) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.BLUE));
////            for(int j=0;j<i; j++){
////                assertNotEquals(model.getTable().getAvaiablePeopleColorinBag().get(i), model.getTable().getAvaiablePeopleColorinBag().get(j));
////            }
//        }
//        //assert avaible tower color
//        assertEquals(0,model.getTable().getAvaiableTowerColor().size());
//        //assert charachter card
//        assertNull(model.getTable().getCharacterCards());
//        //assert professor
//        assertEquals(5,model.getTable().getProfessors().size());
//        for(int i=0; i<model.getTable().getProfessors().size();i++){
//            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
//            for(int j=0;j<i; j++) {
//                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
//            }
//            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
//        }
//    }
//    @Test
//    void testCreateModel4playerPrincipiant() throws Exception {
//        Model model=Model.createModel(4,"PRINCIPIANT");
//        model.getPlayers().add(new Player("pippo","192.168.0.1",1, model));
//        model.getPlayers().add(new Player("paperino","192.168.0.2", 2,model));
//        model.getPlayers().add(new Player("pluto","192.168.0.3",1,model));
//        model.getPlayers().add(new Player("minnie","192.168.0.4",2,model));
//
//        //assert Model
//        assertEquals(4,model.getPlayers().size());
//        assertEquals(GameMode.PRINCIPIANT,model.getGameMode());
//        assertEquals(4,model.getNumberOfPlayers());
//        assertEquals(0,model.getTurnNumber());
//        assertEquals(2,model.getTeams().size());
//
//        //assert team
//        assertEquals(1,model.getTeams().get(0).getTeamNumber());
//        assertEquals(2,model.getTeams().get(1).getTeamNumber());
//        assertEquals(model.getPlayers().get(0),model.getTeams().get(0).getPlayer1());
//        assertEquals(model.getPlayers().get(1),model.getTeams().get(1).getPlayer1());
//        assertEquals(model.getPlayers().get(2),model.getTeams().get(0).getPlayer2());
//        assertEquals(model.getPlayers().get(3),model.getTeams().get(1).getPlayer2());
//
//        assertEquals(1,model.getPlayers().get(0).getNumplayerinteam());
//        assertEquals(1,model.getPlayers().get(1).getNumplayerinteam());
//        assertEquals(2,model.getPlayers().get(2).getNumplayerinteam());
//        assertEquals(2,model.getPlayers().get(3).getNumplayerinteam());
//        assertEquals(model.getPlayers().get(0),model.getcurrentPlayer());
//
//
//        //assert player 1
//        assertEquals("pippo",model.getPlayers().get(0).getNickname());
//        assertEquals("192.168.0.1",model.getPlayers().get(0).getIp());
//        assertEquals(10,model.getPlayers().get(0).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(0).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(0).getSchoolBoard().getDinnerTable().size());
//        assertEquals(7,model.getPlayers().get(0).getSchoolBoard().getEntranceSpace().size());
//        assertEquals(8,model.getPlayers().get(0).getSchoolBoard().getNumOfTowers());
//        assertTrue(TowerColor.BLACK==model.getPlayers().get(0).getSchoolBoard().getTowerColor() || TowerColor.WHITE==model.getPlayers().get(0).getSchoolBoard().getTowerColor());
//
//        //assert player 2
//        assertEquals("paperino",model.getPlayers().get(1).getNickname());
//        assertEquals("192.168.0.2",model.getPlayers().get(1).getIp());
//        assertEquals(10,model.getPlayers().get(1).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(1).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(1).getSchoolBoard().getDinnerTable().size());
//        assertEquals(7,model.getPlayers().get(1).getSchoolBoard().getEntranceSpace().size());
//        assertTrue((model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && !(model.getPlayers().get(1).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
//        assertEquals(8,model.getPlayers().get(1).getSchoolBoard().getNumOfTowers());
//
//        //assert player 3
//        assertEquals("pluto",model.getPlayers().get(2).getNickname());
//        assertEquals("192.168.0.3",model.getPlayers().get(2).getIp());
//        assertEquals(10,model.getPlayers().get(2).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(2).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(2).getSchoolBoard().getDinnerTable().size());
//        assertEquals(7,model.getPlayers().get(2).getSchoolBoard().getEntranceSpace().size());
//        assertTrue((model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && (model.getPlayers().get(2).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(0).getSchoolBoard().getTowerColor())));
//        assertEquals(8,model.getPlayers().get(2).getSchoolBoard().getNumOfTowers());
//
//        //assert player 4
//        assertEquals("minnie",model.getPlayers().get(3).getNickname());
//        assertEquals("192.168.0.4",model.getPlayers().get(3).getIp());
//        assertEquals(10,model.getPlayers().get(3).getAvailableCards().getCardsList().size());
//        assertNull(model.getPlayers().get(3).getChoosedCard());
//        assertEquals(0,model.getPlayers().get(3).getSchoolBoard().getDinnerTable().size());
//        assertEquals(7,model.getPlayers().get(3).getSchoolBoard().getEntranceSpace().size());
//        assertTrue((model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(TowerColor.BLACK) || model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(TowerColor.WHITE)) && (model.getPlayers().get(3).getSchoolBoard().getTowerColor().equals(model.getPlayers().get(1).getSchoolBoard().getTowerColor())));
//        assertEquals(8,model.getPlayers().get(3).getSchoolBoard().getNumOfTowers());
//
//
//
//        //assert CentreTable
//        //assert clouds
//        assertEquals(4,model.getTable().getClouds().size());
//        for(int i=0;i<model.getTable().getClouds().size();i++)
//        {
//            assertEquals(3,model.getTable().getClouds().get(i).getCloudsize());
//            assertEquals(3,model.getTable().getClouds().get(i).getStudentsAccumulator().size());
//        }
//        //assert Island and mother nathure position
//        assertEquals(12,model.getTable().getIslands().size());
//        for(int i=0;i<model.getTable().getIslands().size();i++){
//            if(i==0){
//
//                assertEquals(model.getTable().getIslands().get(i), model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()));
//                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            else if(i==6){
//                assertEquals(0,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            else{
//                assertEquals(1,model.getTable().getIslands().get(i).getInhabitants().size());
//            }
//            assertEquals(0,model.getTable().getIslands().get(i).getNumberOfTowers());
//            assertNull(model.getTable().getIslands().get(i).getTowerColor());
//            assertFalse(model.getTable().getIslands().get(i).isBlocked());
//        }
//        //assert bag
//        assertEquals(80,model.getTable().getBag().size());
//        //assert avaible student color
//        assertTrue(5==model.getTable().getAvaiablePeopleColorinBag().size() || 4==model.getTable().getAvaiablePeopleColorinBag().size());
//        for(int i=0; i<model.getTable().getAvaiablePeopleColorinBag().size();i++){
//            assertTrue(model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.RED) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.YELLOW) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.GREEN) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.PINK) || model.getTable().getAvaiablePeopleColorinBag().get(i).equals(PeopleColor.BLUE));
//            for(int j=0;j<i; j++){
//                assertNotEquals(model.getTable().getAvaiablePeopleColorinBag().get(i), model.getTable().getAvaiablePeopleColorinBag().get(j));
//            }
//        }
//        //assert avaible tower color
//        assertEquals(0,model.getTable().getAvaiableTowerColor().size());
//        //assert charachter card
//        assertNull(model.getTable().getCharacterCards());
//        //assert professor
//        assertEquals(5,model.getTable().getProfessors().size());
//        for(int i=0; i<model.getTable().getProfessors().size();i++){
//            assertTrue(model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.RED) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.YELLOW) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.GREEN) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.PINK) || model.getTable().getProfessors().get(i).getColor().equals(PeopleColor.BLUE));
//            for(int j=0;j<i; j++) {
//                assertNotEquals(model.getTable().getProfessors().get(i).getColor(), model.getTable().getProfessors().get(j).getColor());
//            }
//            assertNull(model.getTable().getProfessors().get(i).getHeldBy());
//        }
//    }
//    @Test
//    void testExcepitionOncreation() throws Exception {
//        assertThrows(IllegalArgumentException.class,()->{
//            Model model1 =Model.createModel(5,"PRINCIPIANT");
//        });
//        assertThrows(IllegalArgumentException.class,()->{
//            Model model2=Model.createModel(1,"PRINCIPIANT");
//        });
//        assertThrows(IllegalArgumentException.class,()->{
//            Model model3=Model.createModel(1,"SIUM");
//        });
//        assertThrows(IllegalArgumentException.class,()->{
//            Model model4=Model.createModel(3,"PRINCIPIANTT");
//        });
//        assertThrows(IllegalArgumentException.class,()->{
//            Model model5=Model.createModel(2,"EXPERCTTT");
//        });
//        assertThrows(IllegalArgumentException.class,()->{
//            Model model6=Model.createModel(5,"SIUM");
//        });
//
//        Model model=Model.createModel(4,"PRINCIPIANT");
//
//        assertThrows(IllegalArgumentException.class,()-> model.getPlayers().add(new Player("pietro","192.168.0.7", 3,model)));
//        assertThrows(IllegalArgumentException.class,()-> model.getPlayers().add(new Player("pietross","192.168.0.9", 0,model)));
//        Player player1=new Player("pippo","192.168.0.1",1, model);
//        Player player2=new Player("paperino","192.168.0.2", 1,model);
//        Player player3=new Player("pluto","192.168.0.3",2,model);
//        Player player4=new Player("minnie","192.168.0.4",2,model);
//
//        model.getPlayers().add(player1);
//        model.getPlayers().add(player2);
//        model.getPlayers().add(player3);
//
//
//        assertThrows(IllegalArgumentException.class,()-> model.getPlayers().add(new Player("pietrodd","192.168.0.8", 1,model)));
//
//        model.getPlayers().add(player4);
//
//        assertThrows(IllegalArgumentException.class,()-> model.getPlayers().add(new Player("pietroddd","192.168.0.10", 2,model)));
//
//        assertEquals(2,model.getTeams().size());
//        assertEquals(player1,model.getTeams().get(0).getPlayer1());
//        assertEquals(player2,model.getTeams().get(0).getPlayer2());
//        assertEquals(player3,model.getTeams().get(1).getPlayer1());
//        assertEquals(player4,model.getTeams().get(1).getPlayer2());
//
//    }
//}
//
