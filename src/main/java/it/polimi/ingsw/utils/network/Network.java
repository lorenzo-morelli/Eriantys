package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.client.model.ClientModel;

import java.awt.event.*;
import javax.swing.*;

/**
 * The network class is a helper class to abstract the low level code
 * typical of a TCP socket, making available to the developer a series
 * of methods to initialize the connection in a completely intuitive way.
 *
 * @author fernandomorea
 */
public class Network implements ActionListener {
    private static NetworkHandler connection;
    private static boolean gotConnect;
    private static boolean serverListening = false;
    private static ClientModel clientModel;
    private static JButton serverButton;
    private static JButton clientButton;
    private static JTextField IPAddress;
    private static JTextField port;
    private static JTextArea textReceived;
    private static JButton disconnectButton;
    private static boolean disconnectedClient = false;


    /**
     * This is the main network controller that is called when a network action is
     * listened
     *
     * @param event the event that caused the network response
     */
    public synchronized void actionPerformed(ActionEvent event) {
        if (event.getSource() == clientButton) {
            connection = new NetworkHandler(IPAddress.getText(), Integer.parseInt(port.getText()), this);
            gotConnect = connection.connect();
            if (gotConnect) {
                System.out.println("[Connected to server]");
            }

        } else if (event.getSource() == serverButton) {
            connection = new NetworkHandler(Integer.parseInt(port.getText()), this);
            gotConnect = connection.connect();
            if (gotConnect) {
                System.out.println("[Listening for Clients]");
            }
        } else if (event.getSource() == connection) {
            textReceived.setText(connection.readText());

        } else if (event.getSource() == disconnectButton) {
            connection.disconnect();
        }

    }

    /**
     * This is the main network class and is an abstraction of
     * the underlying NetworkHandler (much deeper level of TCP socket)
     */
    private Network() {
        serverButton = new JButton();
        serverButton.addActionListener(this);
        clientButton = new JButton();
        clientButton.addActionListener(this);
        JButton sendButton = new JButton();
        sendButton.addActionListener(this);
        IPAddress = new JTextField();
        port = new JTextField();
        //textReceived = new JTextArea();
        disconnectButton = new JButton();
        disconnectButton.addActionListener(this);
    }

    /**
     * This method is used to check if new messages are arrived from the network
     */
    public static synchronized JTextArea checkNewMessages() {
        if (textReceived == null) {
            textReceived = new JTextArea();
        }
        return textReceived;
    }

    /**
     * This method is used to set up the server side connection
     *
     * @param port the port on which the server has to listen
     */
    public static void setupServer(String port) {
        new Network();
        Network.port.setText(port);
        serverButton.doClick();
        serverListening = true;


    }

    /**
     * This method is used to set up the client side connection
     *
     * @param port the port on which the server has to listen
     * @param ip   the ip of the server
     */
    public static void setupClient(String ip, String port) {
        new Network();
        Network.IPAddress.setText(ip);
        Network.port.setText(port);
        Network.clientButton.doClick();
    }

    /**
     * Used to disconnect from the socket
     */
    public synchronized static void disconnect() {
        disconnectButton.doClick();
    }

    /**
     * Used to check whether we are connected or not
     */
    public synchronized static boolean isConnected() {
        return gotConnect;
    }

    /**
     * Used to send messages over the network
     *
     * @param message the message to be sent
     */

    public synchronized static boolean send(String message) {
        return connection.sendText(message);
    }

    /**
     * Used to get your ip address
     */
    public synchronized static String getMyIp() {
        return connection.getMyAddress();
    }

    /**
     * Used to check whether the server is listening or not
     */
    public synchronized static boolean isServerListening() {
        return serverListening;
    }

    /**
     * Used to check whether the client is disconnected or not
     */
    public synchronized static boolean disconnectedClient() {
        synchronized ((Object) disconnectedClient) {
            return disconnectedClient;
        }

    }

    /**
     * Used to set the disconnected status
     *
     * @param disconnectedClient true or false
     */
    public synchronized static void setDisconnectedClient(boolean disconnectedClient) {
        synchronized ((Object) disconnectedClient) {
            Network.disconnectedClient = disconnectedClient;
        }
    }

    /**
     * Used to store the clientModel just in case
     */
    public static void setClientModel(ClientModel clientModel) {
        Network.clientModel = clientModel;
    }

    /**
     * Used get the clientModel
     *
     * @return the clientModel stored
     */
    public static ClientModel getClientModel() {
        return clientModel;
    }
}

