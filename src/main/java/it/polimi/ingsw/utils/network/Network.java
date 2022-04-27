package it.polimi.ingsw.utils.network;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class Network implements ActionListener{
    NetworkHandler connection;
    boolean gotConnect;

    // elementi grafici "virtuali" che astraggono i dettagli implementativi
    JButton serverButton;
    JButton clientButton;
    JTextField IPAddress;
    JTextField port;
    JTextField textToSend;
    JTextArea textReceived;
    JButton disconnectButton;



    public void actionPerformed(ActionEvent event){
        if(event.getSource() == clientButton){
            connection = new NetworkHandler(IPAddress.getText(), Integer.parseInt(port.getText()), this);
            gotConnect = connection.connect();
            if(gotConnect){
                textReceived.append("My Address: "+connection.getMyAddress()+"\n");
                textReceived.append("My Hostname: "+connection.getMyHostname()+"\n");
                textReceived.append("ClientRole" + "\n");
            }

        }else if(event.getSource() == serverButton){
            connection = new NetworkHandler(Integer.parseInt(port.getText()), this);
            gotConnect = connection.connect();
            if(gotConnect){
                textReceived.append("Indirizzo IP di questo pc: "+connection.getMyAddress()+"\n");
                textReceived.append("Nome Computer: "+connection.getMyHostname()+"\n");
                textReceived.append("ServerRole" + "\n");
            }

        }else if(event.getSource() == textToSend){
            if(connection != null){
                connection.sendText(textToSend.getText());
            }
        }else if(event.getSource() == connection){
            textReceived.append(connection.readText() + "\n");
            textReceived.setCaretPosition(textReceived.getDocument().getLength());

        }else if(event.getSource() == disconnectButton){
            connection.disconnect();
        }

    }

    public Network(){
        serverButton = new JButton();
        serverButton.addActionListener(this);
        clientButton = new JButton();
        clientButton.addActionListener(this);
        IPAddress = new JTextField();
        port = new JTextField();
        textToSend = new JTextField();
        textToSend.addActionListener(this);
        textReceived = new JTextArea();
        disconnectButton = new JButton();
        disconnectButton.addActionListener(this);
    }

    public JTextArea getTextArea() {
        return textReceived;
    }

    public void setupServer(String port){
        this.port.setText(port);
        this.serverButton.doClick();
    };

    public void setupClient(String ip,String port){
        this.IPAddress.setText(ip);
        this.port.setText(port);
        this.clientButton.doClick();
    };

    public void disconnect(){
        this.disconnectButton.doClick();
    }

    public boolean isConnected() {
        return gotConnect;
    }
}