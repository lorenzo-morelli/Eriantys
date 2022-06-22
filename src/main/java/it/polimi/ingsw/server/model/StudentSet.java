package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;
import java.util.Random;

public class StudentSet {
    private int numOfRedStudents;
    private int numOfYellowStudents;
    private int numOfBlueStudents;
    private int numOfPinkStudents;
    private int numOfGreenStudents;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public StudentSet() {
        this.numOfRedStudents = 0;
        this.numOfGreenStudents = 0;
        this.numOfBlueStudents = 0;
        this.numOfPinkStudents = 0;
        this.numOfYellowStudents = 0;
    }

    public StudentSet(int numOfRedStudents, int numOfYellowStudents, int numOfBlueStudents, int numOfPinkStudents, int numOfGreenStudents) {
        this.numOfRedStudents = numOfRedStudents;
        this.numOfGreenStudents = numOfGreenStudents;
        this.numOfBlueStudents = numOfBlueStudents;
        this.numOfPinkStudents = numOfPinkStudents;
        this.numOfYellowStudents = numOfYellowStudents;
    }

    public void setAllStudentTozero() {
        this.numOfRedStudents = 0;
        this.numOfGreenStudents = 0;
        this.numOfBlueStudents = 0;
        this.numOfPinkStudents = 0;
        this.numOfYellowStudents = 0;
    }

    public PeopleColor estractRandomStudent() {
        if (this.size() > 0) {
            int rnd = new Random().nextInt(this.size());
            if (numOfRedStudents != 0 && rnd < numOfRedStudents) {
                removestudent(1, PeopleColor.RED);
                return PeopleColor.RED;
            }
            if (numOfGreenStudents != 0 && rnd < numOfRedStudents + numOfGreenStudents) {
                removestudent(1, PeopleColor.GREEN);
                return PeopleColor.GREEN;
            }
            if (numOfBlueStudents != 0 && rnd < numOfRedStudents + numOfGreenStudents + numOfBlueStudents) {
                removestudent(1, PeopleColor.BLUE);
                return PeopleColor.BLUE;
            }
            if (numOfPinkStudents != 0 && rnd < numOfRedStudents + numOfGreenStudents + numOfBlueStudents + numOfPinkStudents) {
                removestudent(1, PeopleColor.PINK);
                return PeopleColor.PINK;
            } else {
                removestudent(1, PeopleColor.YELLOW);
                return PeopleColor.YELLOW;
            }
        }
        throw new IllegalArgumentException();
    }

    public int numStudentsbycolor(PeopleColor color) {
        switch (color) {
            case BLUE:
                return numOfBlueStudents;
            case RED:
                return numOfRedStudents;
            case YELLOW:
                return numOfYellowStudents;
            case PINK:
                return numOfPinkStudents;
            case GREEN:
                return numOfGreenStudents;
        }
        throw new IllegalArgumentException();
    }

    public int size() {
        return numStudentsbycolor(PeopleColor.BLUE) + numStudentsbycolor(PeopleColor.RED) + numStudentsbycolor(PeopleColor.YELLOW) + numStudentsbycolor(PeopleColor.PINK) + numStudentsbycolor(PeopleColor.GREEN);
    }

    public void removestudent(int n, PeopleColor color) {
        if (numStudentsbycolor(color) < n) {
            addstudents(-numStudentsbycolor(color), color);
            return;
        }
        addstudents(-n, color);
    }

    public void removestudentinBag(int n, PeopleColor color, StudentSet bag) {
        if (numStudentsbycolor(color) < n) {
            addstudents(-numStudentsbycolor(color), color);
            bag.addstudents(numStudentsbycolor(color), color);
            return;
        }
        addstudents(-n, color);
        bag.addstudents(n, color);
    }


    public void addstudents(int n, PeopleColor color) {
        switch (color) {
            case BLUE:
                numOfBlueStudents += n;
                break;
            case RED:
                numOfRedStudents += n;
                break;
            case YELLOW:
                numOfYellowStudents += n;
                break;
            case PINK:
                numOfPinkStudents += n;
                break;
            case GREEN:
                numOfGreenStudents += n;
                break;
        }
    }

    public void setStudentsRandomly(int n, StudentSet bag) {
        for (int i = 0; i < n; i++) {
            addstudents(1, bag.estractRandomStudent());
        }
    }

    @Override
    public String toString() {
        return ANSI_RED + "RED=" + numOfRedStudents + ANSI_RESET + " , " +
                ANSI_YELLOW + "YELLOW=" + numOfYellowStudents + ANSI_RESET + " , " +
                ANSI_BLUE + "BLUE=" + numOfBlueStudents + ANSI_RESET + " , " +
                ANSI_PURPLE + "PINK=" + numOfPinkStudents + ANSI_RESET + " , " +
                ANSI_GREEN + "GREEN=" + numOfGreenStudents + ANSI_RESET +
                '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentSet)) return false;
        StudentSet that = (StudentSet) o;
        return this.numOfRedStudents == that.numOfRedStudents
                && this.numOfYellowStudents == that.numOfYellowStudents
                && this.numOfBlueStudents == that.numOfBlueStudents
                && this.numOfPinkStudents == that.numOfPinkStudents
                && this.numOfGreenStudents == that.numOfGreenStudents;
    }

    public int getNumOfBlueStudents() {
        return numOfBlueStudents;
    }

    public int getNumOfGreenStudents() {
        return numOfGreenStudents;
    }

    public int getNumOfPinkStudents() {
        return numOfPinkStudents;
    }

    public int getNumOfRedStudents() {
        return numOfRedStudents;
    }

    public int getNumOfYellowStudents() {
        return numOfYellowStudents;
    }

    public boolean contains(ArrayList<PeopleColor> colors) {
        int red = 0, pink = 0, green = 0, yellow = 0, blue = 0;

        for (PeopleColor color : colors) {
            switch (color) {
                case YELLOW:
                    yellow++;
                    break;
                case PINK:
                    pink++;
                    break;
                case BLUE:
                    blue++;
                    break;
                case GREEN:
                    green++;
                    break;
                case RED:
                    red++;
                    break;
            }
        }
        if (red > numStudentsbycolor(PeopleColor.RED)) return true;
        if (yellow > numStudentsbycolor(PeopleColor.YELLOW)) return true;
        if (pink > numStudentsbycolor(PeopleColor.PINK)) return true;
        if (green > numStudentsbycolor(PeopleColor.GREEN)) return true;
        return blue > numStudentsbycolor(PeopleColor.BLUE);
    }
}
