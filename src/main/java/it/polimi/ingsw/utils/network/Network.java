package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class Network implements ActionListener{
    private static NetworkHandler connection;
    private static boolean gotConnect;
    private static boolean serverListening = false;
    private static Network instance = null;
    private static ClientModel clientModel;

    // elementi grafici "virtuali" che astraggono i dettagli implementativi
    private static JButton serverButton,clientButton, sendButton;
    private static JTextField IPAddress,port,textToSend;
    private static JTextArea textReceived;
    private static JButton disconnectButton;
    private static boolean disconnectedClient = false;




    public synchronized void actionPerformed(ActionEvent event){
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

    public static synchronized JTextArea checkNewMessages() {
        if (instance.textReceived == null){
            instance.textReceived = new JTextArea();
        }
        return instance.textReceived;
    }

    public static void setupServer(String port){
            instance = new Network();
            // una volta avviato il server in ascolto su una porta
            // non posso rifare il setup (instance non è null)
            instance.port.setText(port);
            instance.serverButton.doClick();
            serverListening = true;


    };

    public static void setupClient(String ip,String port){
        instance = new Network();
        Network.IPAddress.setText(ip);
        Network.port.setText(port);
        Network.clientButton.doClick();
    };

    public synchronized static void disconnect(){
        instance.disconnectButton.doClick();
    }

    public synchronized static boolean isConnected() {
        return instance.gotConnect;
    }

    public synchronized static boolean send(String message) throws InterruptedException {
        return connection.sendText(message);
    }

    public synchronized static String getMyIp(){
        return instance.connection.getMyAddress();
    }

    public synchronized static boolean isServerListening() {
        return serverListening;
    }

    public synchronized static  boolean disconnectedClient() {
        synchronized ((Object) disconnectedClient){
            return disconnectedClient;
        }

    }

    public synchronized static void setDisconnectedClient(boolean disconnectedClient) {
        synchronized ((Object) disconnectedClient){
            Network.disconnectedClient = disconnectedClient;
        }
    }

    public static void setClientModel(ClientModel clientModel) {
        Network.clientModel = clientModel;
    }

    public static ClientModel getClientModel() {
        return clientModel;
    }
}

