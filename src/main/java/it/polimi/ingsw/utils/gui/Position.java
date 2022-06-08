package it.polimi.ingsw.utils.gui;

public class Position {
    private static int x;
    private static int y;

    public static int islandX(int n) {
        switch (n) {
            case 0:
            case 9:
            case 10:
            case 11:
                x = 0;
                break;
            case 1:
            case 8:
                x = 1;
                break;
            case 2:
            case 7:
                x = 2;
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                x = 3;
                break;
        }
        return x;
    }

    public static int islandY(int n) {
        switch (n) {
            case 0:
            case 1:
            case 2:
            case 3:
                y = 0;
                break;
            case 4:
            case 11:
                y = 1;
                break;
            case 5:
            case 10:
                y = 2;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                y = 3;
                break;
        }
        return y;
    }

    public static int cloudX(int n) {
        switch (n) {
            case 0: case 2: x = 1; break;
            case 1: case 3: x = 2; break;
        }
        return x;
    }

    public static int cloudY(int n) {
        switch (n) {
            case 0: case 1: y = 1; break;
            case 2: case 3: y = 2; break;
        }
        return y;
    }
}
