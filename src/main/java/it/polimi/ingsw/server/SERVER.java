package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;

/**
 * This class is used to easily start a command line interface of the server,
 * its task is to instantiate a new ServerController
 */
public class SERVER {

    public static void main(String[] args) {
//        Console console = System.console();
//        if (console == null && !GraphicsEnvironment.isHeadless()) {
//            String filename = CLI.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
//            try {
//                File batch = new File("SERVER-launcher.bat");
//                if (!batch.exists()) {
//                    PrintWriter writer = new PrintWriter(batch);
//                    writer.println("@echo off");
//                    writer.println("java -jar " + filename);
//                    writer.println("exit");
//                    writer.flush();
//                }
//                Runtime.getRuntime().exec("cmd /c start \"\" " + batch.getPath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
            try {
                new ServerController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//    }
}
