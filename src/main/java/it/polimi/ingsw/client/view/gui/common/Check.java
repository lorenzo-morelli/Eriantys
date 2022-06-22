package it.polimi.ingsw.client.view.gui.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    public static boolean isValidIp(String ip) {
        String IP_REGEX =
                "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern IP_PATTERN = Pattern.compile(IP_REGEX);

        if (ip == null) {
            return false;
        }
        Matcher matcher = IP_PATTERN.matcher(ip);
        return matcher.matches();
    }

    public static boolean isValidPort(String port) {
        String PORT_REGEX=
                "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);
        if (port == null) {
            return false;
        }
        Matcher matcher = PORT_PATTERN.matcher(port);
        return matcher.matches();
    }
}
