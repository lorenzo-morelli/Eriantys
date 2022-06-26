package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;

/**
 * Launcher for the Server
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
