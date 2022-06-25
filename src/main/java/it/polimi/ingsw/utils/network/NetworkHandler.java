package it.polimi.ingsw.utils.network;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Java class that abstracts the low-level details of TCP sockets providing the programmer with simple
 * * methods for connecting to a socket, sending and receiving messages, and various utilities
 */

public class NetworkHandler {
    private final int port;
    private String serverIP = null;
    private String incomingText = null;
    private final ReentrantLock incomingTextLock = new ReentrantLock();
    private SocketConnection connection = null;
    transient ActionListener actionListener = null;
    // Methods

    /**
     * @param text Text you want to send via socket
     * @return Returns true if it was sent successfully
     */
    public boolean sendText(String text) {
        if (connection != null) {
            return connection.sendText(text);
        }
        return false;
    }

    /**
     * Read the incoming text from the socket
     * Should only be called after ActionEvent has been triggered
     *
     * @return Return the received text, otherwise an empty string
     */
    public String readText() {
        if (connection != null) {
            return incomingText;
        } else {
            return "";
        }
    }

    /**
     * Disconnect all open sockets
     * The server will disconnect all clients before closing its socket
     * Clients will simply close their socket
     */
    public void disconnect() {
        if (connection != null) {
            connection.closeConnection();
            connection = null;
        }
    }

    /**
     * Get your computer's ip address
     *
     * @return Returns computer's IP address
     */
    public String getMyAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> niAddresses = networkInterface.getInetAddresses();
                while (niAddresses.hasMoreElements()) {
                    InetAddress inetAddress = niAddresses.nextElement();
                    if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignored) {

        }
        return "127.0.0.1";
    }

    /**
     * Open a socket connection
     * Server - Open a socket connection and wait for some client to arrive
     * Client - Open the socket and connect to the server
     * Once connected, both will start a thread to listen to incoming clients or incoming messages
     *
     * @return Returns true if socket connection was successfully
     */
    public boolean connect() {
        // First check to see if you can make a socket connection
        connection = new SocketConnection(serverIP, port, this);
        if (connection.openConnection()) {
            return true;
        } else {
            connection = null;
            return false;
        }
    }

    private synchronized void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
    }

    private void postActionEvent() {
        ActionListener listener = actionListener;
        if (listener != null) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Network Message"));
        }
    }

    /**
     * Server Mode TcpSocket Costruttore
     *
     * @param port     TCP Port you want to use for your connection
     * @param listener Swing/AWT program's ActionListener.  Usually "this"
     */
    public NetworkHandler(int port, ActionListener listener) {
        this.addActionListener(listener);
        this.port = port;
    }

    /**
     * Client Mode TcpSocket Constructor
     *
     * @param serverIP Hostname or IP address of the server you want to connect to
     * @param port     TCP Port you want to use for your connection
     * @param listener Swing/AWT program's ActionListener.  Usually "this"
     */
    public NetworkHandler(String serverIP, int port, ActionListener listener) {
        this.addActionListener(listener);
        this.port = port;
        this.serverIP = serverIP;
    }

    /********************************************************************
     * SocketConnection Class
     * Creates a socket connection in either server mode or client
     * Server opens a server socket and listens for incoming connections
     * If a connection is made, make an appropriate client object
     * and starts listening for data
     * Client opens a socket and starts listening for data
     * *****************************************************************/

    private class SocketConnection implements Runnable, ActionListener {
        final NetworkHandler parentSocket;
        final int port;
        final String serverIP;
        String incomingText = "";
        ServerSocket serverSocketObject = null;
        Socket socketObject = null;
        PrintWriter outBuffer = null;
        BufferedReader inBuffer = null;
        Vector<ClientConnection> clientConnections = new Vector<>();
        boolean blnListenForClients = true;

        final Timer timer;

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == timer) {
                //System.out.println("Heartbeat");
                this.sendText("Heartbeat");
            }
        }

        public boolean sendText(String text) {
            if (serverIP == null || serverIP.equals("")) {
                // Server mode sending text needs to send to all clients
                // It therefore goes through the vector
                // and uses each object's sendText method.
                ////System.out.println("Sending message to all "+port connections.size()+" clients: "+text);
                int i;
                for (i = 0; i < clientConnections.size(); i++) {
                    clientConnections.get(i).sendText(text);
                }
                if (i == 0) {
                    System.out.println("No one connected");
                }
                // restarting Heartbeat after last message that was sent.
                // No need to send heartbeat if there are lots of network messages being sent
                timer.restart();
                return true;
            } else {
                // Client mode is much easier.
                ////System.out.println("Sending message: "+text);
                // First check if connection is down
                if (socketObject != null) {
                    if (outBuffer.checkError()) {
                        closeConnection();
                        return false;
                    } else {
                        outBuffer.println(text);
                        // restarting Heartbeat after last message that was sent.
                        // No need to send heartbeat if there are lots of network messages being sent
                        timer.restart();
                        return true;
                    }

                }
                return false;
            }
        }

        // This might be called buy two areas simultaneously!
        // Might be called by the disconnecting while loop in the run method
        // Might be called by the disconnect method.
        public void removeClient(ClientConnection clientConnection) {
            if (clientConnection.socketObject != null) {
                System.out.println("Trying to close server connection to client");
                // Since two methods might be running this code simultaneously
                // Some objects might be null
                // So catch the null pointer exception
                // the first method that accesses this should close everything correctly
                try {
                    clientConnection.socketObject.shutdownInput();
                    clientConnection.socketObject.shutdownOutput();
                    clientConnection.socketObject.close();
                    clientConnection.outBuffer.close();
                    clientConnection.inBuffer.close();
                    clientConnection.socketObject = null;
                    clientConnection.inBuffer = null;
                    clientConnection.outBuffer = null;
                    clientConnection.incomingText = null;
                    System.out.println("Done closing server connection to client");
                    clientConnections.remove(clientConnection);
                    System.out.println("Server removed a client connection.  Current Size: " + clientConnections.size());
                    //Network.setDisconnectedClient(true);

                } catch (NullPointerException ignored) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void run() {
            if (serverIP == null || serverIP.equals("")) {
                // Server
                // while loop to listen for incoming clients
                // When a client connects, create a socket object
                // Add that socket object to a vector
                // Spawn a thread with that socket object
                // One thread for each client
                while (blnListenForClients) {
                    try {
                        socketObject = serverSocketObject.accept();
                        ClientConnection interconnection = new ClientConnection(this.parentSocket, this.socketObject, this);
                        clientConnections.addElement(interconnection);
                        Thread t1 = new Thread(interconnection);
                        t1.start();
                        System.out.println("Server accepted a client connection:  Current Size: " + clientConnections.size());
                    } catch (IOException e) {
                        blnListenForClients = false;
                    }
                }
            } else {
                // Client
                // Already connected to a server and have a socket object
                // while loop to listen for incoming data
                while (incomingText != null) {
                    try {
                        incomingText = inBuffer.readLine();
                        if (incomingText != null && !incomingText.equals("Heartbeat")) {
                            this.parentSocket.incomingTextLock.lock();
                            this.parentSocket.incomingText = incomingText;
                            this.parentSocket.postActionEvent();
                            this.parentSocket.incomingTextLock.unlock();
                        }
                    } catch (IOException ignored) {
                    }
                }
                System.out.println("[Server is Closing]");
                closeConnection();
            }
        }

        public void closeConnection() {
            // If server, kill all client sockets then close the server-socket
            if (serverIP == null || serverIP.equals("")) {
                blnListenForClients = false;
                while (clientConnections.size() > 0) {
                    removeClient(clientConnections.get(0));
                    //System.out.println("Trying to remove all clients");
                }
                try {
                    serverSocketObject.close();
                } catch (IOException ignored) {
                }
                serverSocketObject = null;
                clientConnections = null;
                timer.stop();
            } else {
                // If client, just kill the socket
                // This might be called buy two areas simultaneously!
                // Might be called by the disconnecting while loop in the run method
                // Might be called by the disconnect method.
                if (socketObject != null) {
                    System.out.println("Trying to close the client connection");
                    try {
                        // Since two methods might be running this code simultaneously
                        // Some objects might be null
                        // So catch the null pointer exception
                        // the first method that accesses this should close everything correctly
                        try {
                            socketObject.shutdownInput();
                            socketObject.shutdownOutput();
                            socketObject.close();
                            outBuffer.close();
                            inBuffer.close();
                            socketObject = null;
                            inBuffer = null;
                            outBuffer = null;
                            incomingText = null;
                            System.out.println("Done closing client connection");
                        } catch (NullPointerException ignored) {
                        }
                    } catch (IOException ignored) {
                    }
                }
                timer.stop();
            }
        }

        public boolean openConnection() {
            if (serverIP == null || serverIP.equals("")) {
                // Server style connection.
                // Open Port
                // Create a server-socket object
                try {
                    serverSocketObject = new ServerSocket(port);
                } catch (IOException e) {
                    return false;
                }
                Thread t1 = new Thread(this);
                t1.start();
                System.out.println("Server port opened.  Listening to incoming client connections");
                // Heartbeat start
            } else {
                // Client style connection.
                // Open port
                // Create a socket object
                try {
                    socketObject = new Socket(serverIP, port);
                    outBuffer = new PrintWriter(socketObject.getOutputStream(), true);
                    inBuffer = new BufferedReader(new InputStreamReader(socketObject.getInputStream()));
                } catch (IOException e) {
                    return false;
                }
                Thread t1 = new Thread(this);
                t1.start();
                System.out.println("Client connected to server.  Listening for incoming data");
                // Heartbeat start
            }
            timer.start();
            return true;
        }

        public SocketConnection(String serverIP, int port, NetworkHandler parentSocket) {
            this.serverIP = serverIP;
            this.port = port;
            this.parentSocket = parentSocket;
            // Heartbeat to verify if socket is connected
            // Shouldn't be real time heartbeat check/keep-alive
            // Therefore will beat every 10 seconds
            // Anyone wanting to real time disconnect should use the disconnect method
            timer = new Timer(10000, this);

        }
    }

    private class ClientConnection implements Runnable {
        final NetworkHandler parentSocket;
        final SocketConnection socketConnection;
        String incomingText = "";
        Socket socketObject;
        PrintWriter outBuffer = null;
        BufferedReader inBuffer = null;

        public void run() {
            try {
                inBuffer = new BufferedReader(new InputStreamReader(socketObject.getInputStream()));
                outBuffer = new PrintWriter(socketObject.getOutputStream(), true);
            } catch (IOException ignored) {
            }
            while (incomingText != null) {
                try {
                    incomingText = inBuffer.readLine();
                    if (incomingText != null && !incomingText.equals("Heartbeat")) {
                        // Send to all other clients except for this receiving one
                        for (int counter = 0; counter < socketConnection.clientConnections.size(); counter++) {
                            if (socketConnection.clientConnections.get(counter) != this) {
                                /*
                                  If heartbeat arrive from clients, send it to the server
                                 */
                                socketConnection.clientConnections.get(counter).sendText(incomingText);
                            }
                        }
                        // Then set text
                        this.parentSocket.incomingTextLock.lock();
                        this.parentSocket.incomingText = incomingText;
                        this.parentSocket.postActionEvent();
                        this.parentSocket.incomingTextLock.unlock();
                    }
                } catch (IOException ignored) {
                }
            }
            System.out.println("reading while loop done");
            socketConnection.removeClient(this);
        }

        public void sendText(String text) {
            if (outBuffer.checkError()) {
                socketConnection.removeClient(this);
            } else {
                outBuffer.println(text);
            }
        }

        public ClientConnection(NetworkHandler parentSocket, Socket socketObject, SocketConnection socketConnection) {
            this.socketConnection = socketConnection;
            this.socketObject = socketObject;
            this.parentSocket = parentSocket;
        }
    }
}
