package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represent the basic student accumulator, that is,
 * it maps an integer for each possible color
 *
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 * @author Lorenzo Morelli
 */
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

    public void setAllStudentToZero() {
        this.numOfRedStudents = 0;
        this.numOfGreenStudents = 0;
        this.numOfBlueStudents = 0;
        this.numOfPinkStudents = 0;
        this.numOfYellowStudents = 0;
    }

    public PeopleColor extractRandomStudent() {
        if (this.size() > 0) {
            int rnd = new Random().nextInt(this.size());
            if (numOfRedStudents != 0 && rnd < numOfRedStudents) {
                removeStudent(1, PeopleColor.RED);
                return PeopleColor.RED;
            }
            if (numOfGreenStudents != 0 && rnd < numOfRedStudents + numOfGreenStudents) {
                removeStudent(1, PeopleColor.GREEN);
                return PeopleColor.GREEN;
            }
            if (numOfBlueStudents != 0 && rnd < numOfRedStudents + numOfGreenStudents + numOfBlueStudents) {
                removeStudent(1, PeopleColor.BLUE);
                return PeopleColor.BLUE;
            }
            if (numOfPinkStudents != 0 && rnd < numOfRedStudents + numOfGreenStudents + numOfBlueStudents + numOfPinkStudents) {
                removeStudent(1, PeopleColor.PINK);
                return PeopleColor.PINK;
            } else {
                removeStudent(1, PeopleColor.YELLOW);
                return PeopleColor.YELLOW;
            }
        }
        throw new IllegalArgumentException();
    }

    public int numStudentsByColor(PeopleColor color) {
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
        return numStudentsByColor(PeopleColor.BLUE) + numStudentsByColor(PeopleColor.RED) + numStudentsByColor(PeopleColor.YELLOW) + numStudentsByColor(PeopleColor.PINK) + numStudentsByColor(PeopleColor.GREEN);
    }

    public void removeStudent(int n, PeopleColor color) {
        if (numStudentsByColor(color) < n) {
            addStudents(-numStudentsByColor(color), color);
            return;
        }
        addStudents(-n, color);
    }

    public void removeStudentInBag(int n, PeopleColor color, StudentSet bag) {
        if (numStudentsByColor(color) < n) {
            addStudents(-numStudentsByColor(color), color);
            bag.addStudents(numStudentsByColor(color), color);
            return;
        }
        addStudents(-n, color);
        bag.addStudents(n, color);
    }


    public void addStudents(int n, PeopleColor color) {
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
            addStudents(1, bag.extractRandomStudent());
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
        if (red > numStudentsByColor(PeopleColor.RED)) return true;
        if (yellow > numStudentsByColor(PeopleColor.YELLOW)) return true;
        if (pink > numStudentsByColor(PeopleColor.PINK)) return true;
        if (green > numStudentsByColor(PeopleColor.GREEN)) return true;
        return blue > numStudentsByColor(PeopleColor.BLUE);
    }
}
