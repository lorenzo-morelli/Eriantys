package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.gui.common.SendModelAndGetResponse;
import it.polimi.ingsw.client.view.gui.common.SetConnection;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.client.view.gui.common.Check.isValidIp;
import static it.polimi.ingsw.client.view.gui.common.Check.isValidPort;

public class Menu implements Initializable {
    private final GUI gui = new GUI();

    @FXML
    private Button playButton = new Button();
    @FXML
    private TextField nicknameField = new TextField();
    @FXML
    private TextField ipField = new TextField();
    @FXML
    private TextField portField = new TextField();
    @FXML
    private Label notice = new Label();
//    private ParametersFromNetwork ack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.nicknameField.setText("morel");
        this.ipField.setText("127.0.0.1");
        this.portField.setText("1234");
        this.notice.setText("");
        this.gui.currNode = playButton;
    }

    public void play(MouseEvent mouseEvent) throws IOException {
        this.gui.changeScene("SetupConnection");
    }

    public void exit() {
        System.exit(0);
    }

    public void connect(MouseEvent mouseEvent) throws IOException, InterruptedException {
        String nickname = this.nicknameField.getText();
        String ip = this.ipField.getText();
        String port = this.portField.getText();

        if (nickname.equals("") || ip.equals("") || port.equals("")) {
            this.notice.setText("FAILURE: make sure to fill all the fields!");
        } else if (this.nicknameField.getText().length() > 13) {
            this.notice.setText("FAILURE: nickname must be less than 13 characters!");
        } else if (ip.contains(" ") || port.contains(" ")) {
            this.notice.setText("FAILURE: ip and port can't contain any spaces!");
        } else if (!isValidIp(ip) || !isValidPort(port)) {
            this.notice.setText("FAILURE: ip or port format not valid!");
        } else {
            SetConnection.setConnection(nickname, ip, port, this.gui.getClientModel());
            if (Network.isConnected()) {
                this.gui.currNode = notice;
                this.notice.setText("In attesa che il server dia una risposta...");
                ClientModel model=SendModelAndGetResponse.sendAndGetModel(this.gui.getClientModel());

                if(model!=null) {
                    this.gui.setClientModel(model);
                }
                else {
                    System.out.println("\n\nServer non ha dato risposta");
                    Network.disconnect();
                    this.gui.currNode = notice;
                    this.notice.setText("Server non ha dato alcuna risposta, mi disconnetto...");
                    TimeUnit.SECONDS.sleep(5);
                    System.exit(0);
                }
                this.gui.currNode = notice;
                if (this.gui.getClientModel().getAmIfirst() == null) {
                    // il nickname inserito è già esistente
                    this.notice.setText("FAILURE: Nickname already taken"); //todo: bugfix
                } else if (this.gui.getClientModel().getAmIfirst()) {
                    System.out.println("primooo");
                    //
                    this.gui.changeScene("SetupGame");
                } else {
                    this.gui.changeScene("Lobby");
                }
            } else {
                this.notice.setText("FAILURE: impossible to connect to server!");
            }
        }
    }


//    public void reinsertNickname(MouseEvent mouseEvent) throws InterruptedException {
//        String nickname = this.nicknameField.getText();
//        if (nickname.equals("") ) {
//            this.notice.setText("FAILURE: porcodio inserisci tutti i campi testa di minchiAAAAAAAAA");
//        } else if (this.nicknameField.getText().length() > 13) {
//            this.notice.setText("FAILURE: nickname must be less than 13 characters!");
//        }
//        else {
//            this.gui.getClientModel().setNickname(nickname);
//            Network.send(gson.toJson(this.gui.getClientModel()));
//            try {
//                // Attendi la ricezione dell'ack
//                checkAck(mouseEvent);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

//    public void checkAck(MouseEvent mouseEvent) throws Exception {
//
//        ack = new ParametersFromNetwork(1);
//        ack.enable();
//        //System.out.println("[Conferma del nickname non ancora ricevuta]");
//        while (!ack.parametersReceived()){
//            // non ho ancora ricevuto l'ack
//        }
//
//        //System.out.println("[Conferma del nickname ricevuta]");
//        ClientModel fromNetwork = gson.fromJson(ack.getParameter(0),ClientModel.class);
//        System.out.println(ack.getParameter(0));
//
//        if(fromNetwork.getAmIfirst() == null){
//            // ancora una volta l'utente ha inserito un nickname già esistente
//            this.gui.changeScene("ReinsertNickname",mouseEvent);
//        }
//        else {
//            // l'utente ha finalmente inserito un nickname valido
//            this.gui.changeScene("Lobby", mouseEvent);
//        }
//
//    }
}
