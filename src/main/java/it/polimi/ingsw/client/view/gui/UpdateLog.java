package it.polimi.ingsw.client.view.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateLog {
    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return "[" + dateFormat.format(date) + "] ";
    }
}
