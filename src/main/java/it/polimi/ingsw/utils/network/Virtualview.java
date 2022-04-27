package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.utils.network.NetworkHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Questa classe implementa un'interfaccia grafica molto minimale a riga di comando
 * per simulare lo scambio di messaggi (per il momento stringhe) tra clients e server
 */


public class Virtualview implements ActionListener{
    JFrame frame;
    JPanel panel;
    NetworkHandler connection;
    JButton serverButton;
    JButton clientButton;
    JTextField IPAddress;
    JTextField port;
    JLabel IPAddressLabel;
    JLabel portLabel;
    JTextField textToSend;
    JLabel textToSendLabel;
    JTextArea textReceived;
    JScrollPane scroll;
    JLabel textReceivedLabel;
    JButton disconnectButton;



    public void actionPerformed(ActionEvent event){
        if(event.getSource() == clientButton){
            serverButton.setEnabled(false);
            clientButton.setEnabled(false);
            IPAddress.setEnabled(false);
            port.setEnabled(false);
            connection = new NetworkHandler(IPAddress.getText(), Integer.parseInt(port.getText()), this);
            boolean gotConnect = connection.connect();
            if(gotConnect){
                disconnectButton.setEnabled(true);
                textReceived.append("My Address: "+connection.getMyAddress()+"\n");
                textReceived.append("My Hostname: "+connection.getMyHostname()+"\n");
                textReceived.append("ClientRole" + "\n");
            } else{
                serverButton.setEnabled(true);
                clientButton.setEnabled(true);
                IPAddress.setEnabled(true);
                port.setEnabled(true);
            }

        }else if(event.getSource() == serverButton){
            serverButton.setEnabled(false);
            clientButton.setEnabled(false);
            IPAddress.setEnabled(false);
            port.setEnabled(false);
            connection = new NetworkHandler(Integer.parseInt(port.getText()), this);
            boolean gotConnect = connection.connect();
            if(gotConnect){
                disconnectButton.setEnabled(true);
                textReceived.append("Indirizzo IP di questo pc: "+connection.getMyAddress()+"\n");
                textReceived.append("Nome Computer: "+connection.getMyHostname()+"\n");
                textReceived.append("ServerRole" + "\n");
            }else{
                serverButton.setEnabled(true);
                clientButton.setEnabled(true);
                IPAddress.setEnabled(true);
                port.setEnabled(true);
            }

        }else if(event.getSource() == textToSend){
            if(connection != null){
                connection.sendText(textToSend.getText());
            }
        }else if(event.getSource() == connection){
            textReceived.append(connection.readText() + "\n");
            textReceived.setCaretPosition(textReceived.getDocument().getLength());


        }else if(event.getSource() == disconnectButton){
            serverButton.setEnabled(true);
            clientButton.setEnabled(true);
            IPAddress.setEnabled(true);
            port.setEnabled(true);
            connection.disconnect();
            disconnectButton.setEnabled(false);
        }

    }

    public Virtualview(){
        Font font = new Font("Artis Sans", Font.BOLD, 15);
        frame = new JFrame("Command Line VirtualView Utility");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(600, 600));
        frame.setContentPane(panel);
        frame.pack();
        serverButton = new JButton("Server");
        serverButton.setSize(300, 25);
        serverButton.setLocation(0,0);
        serverButton.addActionListener(this);
        panel.add(serverButton);
        clientButton = new JButton("Client");
        clientButton.setSize(300, 25);
        clientButton.setLocation(300, 0);
        clientButton.addActionListener(this);
        panel.add(clientButton);
        IPAddressLabel = new JLabel("Indirizzo IP");
        IPAddressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        IPAddressLabel.setSize(300, 25);
        IPAddressLabel.setLocation(0, 50);
        panel.add(IPAddressLabel);
        IPAddress = new JTextField("localhost");
        IPAddress.setSize(300,25);
        IPAddress.setLocation(0, 75);
        panel.add(IPAddress);
        portLabel = new JLabel("Porta");
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setSize(300, 25);
        portLabel.setLocation(300, 50);
        panel.add(portLabel);
        port = new JTextField("6112");
        port.setSize(300,25);
        port.setLocation(300, 75);
        panel.add(port);
        textToSendLabel = new JLabel("Scrivi testo da inviare tramite socket TCP (Invio per inviare)");
        textToSendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textToSendLabel.setFont(font);
        textToSendLabel.setSize(600, 25);
        textToSendLabel.setLocation(0, 150);
        panel.add(textToSendLabel);
        textToSend = new JTextField();
        textToSend.setSize(600,25);
        textToSend.setLocation(0, 175);
        textToSend.addActionListener(this);
        panel.add(textToSend);
        textReceivedLabel = new JLabel("Ricezione da socket TCP");
        textReceivedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textReceivedLabel.setSize(600, 25);
        textReceivedLabel.setFont(font);
        textReceivedLabel.setLocation(0, 200);
        panel.add(textReceivedLabel);
        textReceived = new JTextArea();
        textReceived.setEditable(false);
        textReceived.setFont(font);
        textReceived.setForeground(Color.WHITE);
        textReceived.setBackground(Color.BLACK);
        scroll = new JScrollPane(textReceived);
        scroll.setSize(600,350);
        scroll.setLocation(0, 225);
        panel.add(scroll);
        disconnectButton = new JButton("Disconnettiti dalla socket TCP");
        disconnectButton.setFont(font);
        disconnectButton.setSize(600, 25);
        disconnectButton.setLocation(0,575);
        disconnectButton.addActionListener(this);
        disconnectButton.setEnabled(false);
        panel.add(disconnectButton);

        // Needed for Windows. But not in macOS
        // When one closes the window, disconnect any open sockets
        // Seems to do it automatically in macOS but not in windows
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if(connection != null){
                    connection.disconnect();
                }
            }
        });
        frame.setVisible(true);
    }

    public JTextArea getTextArea() {
        return textReceived;
    }
}