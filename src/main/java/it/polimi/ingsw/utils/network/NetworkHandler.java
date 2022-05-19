package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.utils.stateMachine.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.AWTEventMulticaster;
import javax.swing.Timer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.NetworkInterface;
import java.net.Inet4Address;
import java.net.SocketException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classe java che astrae i dettagli di basso livello delle socket TCP fornendo al programmatore semplici
 * metodi per connettersi a una socket, inviare e ricevere messaggi, e varie utilita'
*/

public class NetworkHandler {
    // Properta' della connessione
    private int port = 1337;
    private String serverIP = null;
    private String incomingText = null;
    private ReentrantLock incomingTextLock = new ReentrantLock();
    private SocketConnection connection = null;
    transient ActionListener actionListener = null;
    // Methods
    /**
     * @param text Testo che vuoi inviare tramite socket
     * @return Ritorna true se e' stato inviato correttamente
     */
    public boolean sendText(String text){
        if(connection != null){
            return connection.sendText(text);
        }
        return false;
    }
    /**
     * Leggi il testo in arrivo dalla socket
     * Dovrebbe essere chiamato solo dopo che ActionEvent e' stato triggherato
     *
     * @return Ritorna il testo ricevuto, in caso contrario una stringa vuota
     */
    public String readText(){
        if(connection != null){
            return incomingText;
        }else{
            return "";
        }
    }
    /**
     * Disconnetti tutte le socket aperte
     * Il server scolleghera' tutti i client prima di chiudere la sua socket
     * I client chiuderanno semplicemente la loro socket
     */
    public void disconnect(){
        if(connection != null){
            connection.closeConnection();
            connection = null;
        }
    }
    /**
     * Ottieni l'indirizzo ip del tuo computer
     *
     * @return Returns computer's IP address
     */
    public String getMyAddress(){
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration<InetAddress> niAddresses = networkInterface.getInetAddresses();
                while(niAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) niAddresses.nextElement();
                    if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {

        }
        return "127.0.0.1";
    }
    /**
     * Ottieni il nome di rete (HostName) del tuo computer
     *
     * @return Returns computer's Hostname
     */
    public String getMyHostname(){
        try{
            return InetAddress.getLocalHost().getHostName();
        }catch(UnknownHostException e){
            return "localhost";
        }
    }
    /**
     * Apri una connessione socket
     * Server - Apri una connessione socket e attendi l'arrivo di qualche client
     * Client - Apri la socket e connettiti al server
     * Una volta connessi, entrambi starteranno un thread per ascoltare client in arrivo o messaggi in arrivo
     *
     * @return Returns true if socket connection was successfull
     */
    public boolean connect(){
        // First check to see if you can make a socket connection
        connection = new SocketConnection(serverIP, port, this);
        if(connection.openConnection()){
            return true;
        }else{
            connection = null;
            return false;
        }
    }
    private synchronized void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
    }
    private synchronized void removeActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.remove(actionListener, listener);
    }
    private void postActionEvent() {
        ActionListener listener = actionListener;
        if (listener != null) {
            listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Network Message"));
        }
    }
   
    /**
     * Server Mode TcpSocket Costruttore
     *
     * @param port TCP Port you want to use for your connection
     * @param listener Swing/AWT program's ActionListener.  Usually "this"
     */
    public NetworkHandler(int port, ActionListener listener){
        this.addActionListener(listener);
        this.port = port;
    }
    /**
     * Client Mode TcpSocket Constructor
     *
     * @param serverIP Hostname or IP address of the server you want to connect to
     * @param port TCP Port you want to use for your connection
     * @param listener Swing/AWT program's ActionListener.  Usually "this"
     */
    public NetworkHandler(String serverIP, int port, ActionListener listener){
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

    private class SocketConnection implements Runnable, ActionListener{
        NetworkHandler parentSocket = null;
        int port = 1337;
        String serverIP = null;
        String incomingText = "";
        ServerSocket serverSocketObject = null;
        Socket socketObject = null;
        PrintWriter outBuffer = null;
        BufferedReader inBuffer = null;
        String strMyIP;
        String strMyHostname;
        Vector<ClientConnection> clientConnections = new Vector<ClientConnection>();
        boolean blnListenForClients = true;

        Timer timer;
        public void actionPerformed(ActionEvent event){
            if(event.getSource() == timer){
                //System.out.println("Heartbeat");
                this.sendText("Heartbeat");
            }
        }

        public boolean sendText(String text) {
            if (serverIP == null || serverIP.equals("")) {
                // Server mode sending text needs to send to all clients
                // It therefore goes through the vector
                // and uses each object's sendText method.
                ////System.out.println("Sending message to all "+portconnections.size()+" clients: "+text);
                for (int counter = 0; counter < clientConnections.size(); counter++) {
                    clientConnections.get(counter).sendText(text);
                }
                // restarting Heartbeat after last message that was sent.
                // No need to send heartbeat if there are lots of network messages being sent
                timer.restart();
                return true;
            } else {
                // Client mode is much easier.
                ////System.out.println("Sending message: "+text);
                // First check if connecion is down
                if(socketObject != null){
                    if(outBuffer.checkError()){
                        closeConnection();
                        return false;
                    }else{
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
        public void removeClient(ClientConnection clientConnection){
            if(clientConnection.socketObject != null){
                System.out.println("Trying to close server connection to client");
                // Since two methods might be running this code simultaneously
                // Some of the objects might be null
                // So catch the null pointer exception
                // the first method that accesses this should close everything correctly
                try{
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
                    clientConnection = null;
                    System.out.println("Server removed a client connection.  Current Size: " + clientConnections.size());
                    Network.setDisconnectedClient(true);

                }catch(NullPointerException e){
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void run(){
            if(serverIP == null || serverIP.equals("")){
                // Server
                // while loop to listen for incoming clients
                // When a client connects, create a socket object
                // Add that socket object to a vector
                // Spawn a thread with that socket object
                // One thread for each client
                while (blnListenForClients) {
                    try {
                        socketObject = serverSocketObject.accept();
                        ClientConnection singleconnection = new ClientConnection(this.parentSocket, this.socketObject, this);
                        clientConnections.addElement(singleconnection);
                        Thread t1 = new Thread(singleconnection);
                        t1.start();
                        System.out.println("Server accepted a client connection:  Current Size: " + clientConnections.size());
                    } catch (IOException e) {
                        blnListenForClients = false;
                    }
                }
            }else{
                // Client
                // Already connected to a server and have a socket object
                // while loop to listen for incoming data
                while (incomingText != null) {
                    try {
                        incomingText = inBuffer.readLine();
                        if(incomingText != null && !incomingText.equals("Heartbeat")){
                            this.parentSocket.incomingTextLock.lock();
                            this.parentSocket.incomingText = incomingText;
                            this.parentSocket.postActionEvent();
                            this.parentSocket.incomingTextLock.unlock();
                        }
                    } catch (IOException e) {
                    }
                }
                System.out.println("[Il server sta chiudendo]");
                closeConnection();
            }
        }
        public void closeConnection(){
            // If server, kill all client sockets then close the serversocket
            if(serverIP == null || serverIP.equals("")){
                blnListenForClients = false;
                while(clientConnections.size() > 0){
                    removeClient(clientConnections.get(0));
                    //System.out.println("Trying to remove all clients");
                }
                try{
                    serverSocketObject.close();
                }catch(IOException e){
                }
                serverSocketObject = null;
                clientConnections = null;
                timer.stop();
            }else{
                // If client, just kill the socket
                // This might be called buy two areas simultaneously!
                // Might be called by the disconnecting while loop in the run method
                // Might be called by the disconnect method.
                if(socketObject != null){
                    System.out.println("Trying to close the client conneccion");
                    try{
                        // Since two methods might be running this code simultaneously
                        // Some of the objects might be null
                        // So catch the null pointer exception
                        // the first method that accesses this should close everything correctly
                        try{
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
                        }catch(NullPointerException e){
                        }
                    }catch(IOException e){
                    }
                }
                timer.stop();
            }
        }
        public boolean openConnection(){
            if(serverIP == null || serverIP.equals("")){
                // Server style connection.
                // Open Port
                // Create a serversocket object
                try {
                    serverSocketObject = new ServerSocket(port);
                } catch (IOException e) {
                    return false;
                }
                Thread t1 = new Thread(this);
                t1.start();
                System.out.println("Server port opened.  Listening to incoming client connections");
                // Heartbeat start
                timer.start();
                return true;
            }else{
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
                timer.start();
                return true;
            }
        }
        public SocketConnection(String serverIP, int port, NetworkHandler parentSocket){
            this.serverIP = serverIP;
            this.port = port;
            this.parentSocket = parentSocket;
            // Heartbeat to verify if socket is connected
            // Shouldn't be real time heartbeat check/keepalive
            // Therefore will beat every 10 seconds
            // Anyone wanting to real time disconnect should use the disconnect method
            timer = new Timer(10000, this);

        }
    }
    private class ClientConnection implements Runnable{
        NetworkHandler parentSocket = null;
        SocketConnection socketConnection = null;
        String incomingText = "";
        Socket socketObject = null;
        PrintWriter outBuffer = null;
        BufferedReader inBuffer = null;
        public void run(){
            try {
                inBuffer = new BufferedReader(new InputStreamReader(socketObject.getInputStream()));
                outBuffer = new PrintWriter(socketObject.getOutputStream(), true);
            } catch (IOException e) {
            }
            while (incomingText != null) {
                try {
                    incomingText = inBuffer.readLine();
                    if(incomingText != null && !incomingText.equals("Heartbeat")){
                        // Send to all other clients except for this recieving one
                        for (int counter = 0; counter < socketConnection.clientConnections.size(); counter++) {
                            if(socketConnection.clientConnections.get(counter) != this){
                                /**
                                 * Se mi arriva l'heartbit lato client, reinoltra l'heartbit al server
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
                } catch (IOException e) {
                }
            }
            System.out.println("reading while loop done");
            socketConnection.removeClient(this);
        }
        public boolean sendText(String text) {
            if(outBuffer.checkError()){
                socketConnection.removeClient(this);
                return false;
            }else{
                outBuffer.println(text);
                return true;
            }
        }
        public ClientConnection(NetworkHandler parentSocket, Socket socketObject, SocketConnection socketConnection){
            this.socketConnection = socketConnection;
            this.socketObject = socketObject;
            this.parentSocket = parentSocket;
        }
    }
}
