package it.polimi.ingsw.utils.gui.controllers;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.gui.GUI;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.management.timer.Timer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.common.Check.isValidIp;
import static it.polimi.ingsw.utils.common.Check.isValidPort;

public class MenuController implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private ParametersFromNetwork response;

    @FXML
    private TextField nicknameField = new TextField();
    @FXML
    private TextField ipField = new TextField();
    @FXML
    private TextField portField = new TextField();
    @FXML
    private Label notice = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.nicknameField.setText("morel");
        this.ipField.setText("127.0.0.1");
        this.portField.setText("5555");
        this.notice.setText("");
    }

    public void play(MouseEvent mouseEvent) throws IOException {
        this.gui.changeScene("SetupConnection", mouseEvent);
    }

    public void exit() {
        System.exit(0);
    }

    public void connect(MouseEvent mouseEvent) throws IOException, InterruptedException {
        String nickname = this.nicknameField.getText();
        String ip = this.ipField.getText();
        String port = this.portField.getText();

        if (nickname.equals("") || ip.equals("") || port.equals("")) {
            this.notice.setText("FAILURE: porcodio inserisci tutti i campi testa di minchiAAAAAAAAA");
        } else if (this.nicknameField.getText().length() > 13) {
            this.notice.setText("FAILURE: nickname must be less than 13 characters!");
        } else if (ip.contains(" ") || port.contains(" ")) {
            this.notice.setText("FAILURE: ip and port can't contain any spaces!");
        } else if (!isValidIp(ip) || !isValidPort(port)) {
            this.notice.setText("FAILURE: ip or port format not valid!");
        } else {
            this.gui.getClientModel().setNickname(nickname);
            this.gui.getClientModel().setIp(ip);
            this.gui.getClientModel().setPort(port);
            Network.setupClient(ip, port);
            this.gui.getClientModel().setMyIp(Network.getMyIp());

            if (Network.isConnected()) {
                boolean responseReceived = false;
                while (!responseReceived) {
                    Network.send(gson.toJson(this.gui.getClientModel()));
                    response = new ParametersFromNetwork(1);
                    response.enable();
                    while (!response.parametersReceived()) {
                        // Non ho ancora ricevuto una risposta dal server
                    }
                    if (gson.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == this.gui.getClientModel().getClientIdentity()) {
                        responseReceived = true;
                    }
                }

                this.gui.setClientModel(gson.fromJson(response.getParameter(0), ClientModel.class));
                if (this.gui.getClientModel().getAmIfirst()) {
                    System.out.println("primooo");
                    this.gui.changeScene("SetupGame", mouseEvent);
                }
                else {
                    this.gui.changeScene("Lobby", mouseEvent);
                }
            } else {
                this.notice.setText("FAILURE: impossible to connect to server!");
            }
        }
    }


}
