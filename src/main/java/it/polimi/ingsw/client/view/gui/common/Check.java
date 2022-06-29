package it.polimi.ingsw.client.view.gui.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    public static boolean isValidIp(String ip) {
        String IP_REGEX =
                "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." +
                        "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." +
                        "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." +
                        "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
        Pattern IP_PATTERN = Pattern.compile(IP_REGEX);

        if (ip == null) {
            return true;
        }

        Matcher matcher = IP_PATTERN.matcher(ip);

        return !matcher.matches();
    }

    public static boolean isValidPort(String port)
    {
        String PORT_REGEX=
                "^([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
        Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);
        if (port == null) {
            return true;
        }

        Matcher matcher = PORT_PATTERN.matcher(port);

        return !matcher.matches();
    }
}