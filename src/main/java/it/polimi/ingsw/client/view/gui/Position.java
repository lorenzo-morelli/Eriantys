package it.polimi.ingsw.client.view.gui;

public class Position {
    private static int x;
    private static int y;

    /**
     * This method returns the x position for a specific index from 0 to 11, to convert
     * a linear array into the borders of a 4x4 matrix.
     * @param n the position of the linear array, from 0 to 11.
     * @return the x position of the 4x4 matrix.
     */
    public int islandX(int n) {
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

    /**
     * This method returns the y position for a specific index from 0 to 11, to convert
     * a linear array into the borders of a 4x4 matrix.
     * @param n the position of the linear array, from 0 to 11.
     * @return the y position of the 4x4 matrix.
     */
    public int islandY(int n) {
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

    /**
     * This method returns the x position for a specific index from 0 to 3, to convert
     * a linear array into a 2x2 matrix.
     * @param n the position of the linear array, from 0 to 11.
     * @return the x position of the 2x2 matrix.
     */
    public int cloudX(int n) {
        switch (n) {
            case 0: case 2: x = 1; break;
            case 1: case 3: x = 2; break;
        }
        return x;
    }

    /**
     * This method returns the y position for a specific index from 0 to 3, to convert
     * a linear array into a 2x2 matrix.
     * @param n the position of the linear array, from 0 to 11.
     * @return the y position of the 2x2 matrix.
     */
    public int cloudY(int n) {
        switch (n) {
            case 0: case 1: y = 1; break;
            case 2: case 3: y = 2; break;
        }
        return y;
    }
}
