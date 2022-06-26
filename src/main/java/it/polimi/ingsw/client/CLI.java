package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.cliController.ClientController;
import it.polimi.ingsw.client.view.cli.CliView;

/**
 * Launcher for CLI View
 */
public class CLI {
    public static void main(String[] args) throws Exception {
//        Console console = System.console();
//        if (console == null && !GraphicsEnvironment.isHeadless()) {
//            String filename = CLI.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
//            try {
//                File batch = new File("CLI-launcher.bat");
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
            new ClientController(new CliView());
        }
//    }
}
