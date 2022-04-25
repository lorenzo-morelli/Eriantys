package it.polimi.ingsw.utils.gui;

import java.awt.*;

public class Gui {
    private static Container con;

    private static final Font titleFont = new Font("Times New Roman", Font.PLAIN,90);
    private static final Font normalFont = new Font("Times New Roman", Font.PLAIN,30);
    private static final Font buttonFont = new Font("Artis Sans", Font.BOLD, 15);


    public static void setContainer(Container con) {
        Gui.con = con;
    }

    public static Container getContainer() {
        return con;
    }

    public static Font getTitleFont() {
        return titleFont;
    }

    public static Font getNormalFont() {
        return normalFont;
    }

    public static Font getButtonFont() {
        return buttonFont;
    }
}
