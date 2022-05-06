package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class StudentSetTest {

    @Test
    void StudentTest(){
        StudentSet set=new StudentSet();
        for(PeopleColor color:PeopleColor.values()){
            assertEquals(0,set.numStudentsbycolor(color));
        }
    }
    @Test
    void estractSomeStudent() {
        StudentSet set=new StudentSet(2,2,2,2,2);
        assertEquals(10,set.size());
        set.removestudent(2,PeopleColor.RED);
        set.removestudent(3,PeopleColor.GREEN);
        set.removestudent(1,PeopleColor.YELLOW);
        set.addstudents(2,PeopleColor.PINK);
        set.addstudents(27,PeopleColor.BLUE);
        assertEquals(0,set.numStudentsbycolor(PeopleColor.RED));
        assertEquals(0,set.numStudentsbycolor(PeopleColor.GREEN));
        assertEquals(1,set.numStudentsbycolor(PeopleColor.YELLOW));
        assertEquals(29,set.numStudentsbycolor(PeopleColor.BLUE));
        assertEquals(4,set.numStudentsbycolor(PeopleColor.PINK));
        assertEquals(34,set.size());
    }

    @Test
    void numStudentsbycolor() throws Exception {
        StudentSet bag=new StudentSet(2,2,2,2,2);
        StudentSet set=new StudentSet();
        ArrayList<PeopleColor> colors=new ArrayList<>();
        Collections.addAll(colors, PeopleColor.values());

        assertEquals(5,colors.size());
        set.setStudentsRandomly(1,bag,colors);
        assertEquals(5,colors.size());

        assertEquals(1,set.size());
        assertEquals(9,bag.size());

        set.setStudentsRandomly(9,bag,colors);
        assertEquals(0,bag.size());
        assertEquals(10,set.size());
        assertEquals(0,colors.size());
        assertThrows(IllegalArgumentException.class,()->{
            set.setStudentsRandomly(1,bag,colors);
        });
    }
    }
