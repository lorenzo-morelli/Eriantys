package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.utils.stateMachine.Event;

import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class Network implements ActionListener{
    private static NetworkHandler connection;
    private static boolean gotConnect;
    private static boolean serverListening = false;
    private static Network instance = null;

    // elementi grafici "virtuali" che astraggono i dettagli implementativi
    private static JButton serverButton,clientButton, sendButton;
    private static JTextField IPAddress,port,textToSend;
    private static JTextArea textReceived;
    private static JButton disconnectButton;
    private static boolean disconnectedClient = false;




    public void actionPerformed(ActionEvent event){
        if(event.getSource() == clientButton){
            connection = new NetworkHandler(IPAddress.getText(), Integer.parseInt(port.getText()), this);
            gotConnect = connection.connect();
            if(gotConnect){
                System.out.println("[Connected to server]");
            }

        }else if(event.getSource() == serverButton){
            connection = new NetworkHandler(Integer.parseInt(port.getText()), this);
            gotConnect = connection.connect();
            if(gotConnect){
                System.out.println("[Listening for Clients]");
            }
        }else if(event.getSource() == connection){
            textReceived.setText(connection.readText());

        }else if(event.getSource() == disconnectButton){
            connection.disconnect();
        }

    }

    private Network(){
        serverButton = new JButton();
        serverButton.addActionListener(this);
        clientButton = new JButton();
        clientButton.addActionListener(this);
        sendButton = new JButton();
        sendButton.addActionListener(this);
        IPAddress = new JTextField();
        port = new JTextField();
        //textReceived = new JTextArea();
        disconnectButton = new JButton();
        disconnectButton.addActionListener(this);
    }

    public static JTextArea checkNewMessages() {
        if (instance.textReceived == null){
            instance.textReceived = new JTextArea();
        }
        return instance.textReceived;
    }

    public static void setupServer(String port){
        if (instance == null){
            instance = new Network();
            // una volta avviato il server in ascolto su una porta
            // non posso rifare il setup (instance non è null)
            instance.port.setText(port);
            instance.serverButton.doClick();
            serverListening = true;
        }

    };

    public static void setupClient(String ip,String port){
        if (instance == null){
            instance = new Network();
        }
        Network.IPAddress.setText(ip);
        Network.port.setText(port);
        Network.clientButton.doClick();
    };

    public static void disconnect(){
        instance.disconnectButton.doClick();
    }

    public static boolean isConnected() {
        return instance.gotConnect;
    }

    public static void send(String message) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        connection.sendText(message);
    }

    public static String getMyIp(){
        return instance.connection.getMyAddress();
    }

    public static boolean isServerListening() {
        return serverListening;
    }

    public static boolean disconnectedClient() {
        return disconnectedClient;
    }

    public static void setDisconnectedClient(boolean disconnectedClient) {
        Network.disconnectedClient = disconnectedClient;
    }
}

