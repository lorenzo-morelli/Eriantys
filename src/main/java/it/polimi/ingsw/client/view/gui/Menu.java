package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.common.SendModelAndGetResponse;
import it.polimi.ingsw.client.view.common.SetConnection;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.client.view.gui.GuiView.currNode;
import static it.polimi.ingsw.client.view.common.Check.isValidIp;
import static it.polimi.ingsw.client.view.common.Check.isValidPort;

public class Menu implements Initializable {
    private final GuiView guiView = new GuiView();
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
    @FXML
    private Label connected = new Label();
    @FXML
    private ImageView loading = new ImageView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.notice.setText("");
        this.connected.setText("");
        currNode = playButton;
        loading.setVisible(false);
    }

    /**
     * This event goes to the next FXML page with the setup connection.
     */
    @FXML
    private void play() throws IOException {
        this.guiView.changeScene("SetupConnection");
    }

    /**
     * This event terminates the program.
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /**
     * This event is used to set up the connection and to connect to the server.
     * The fields which must be filled are the nickname, the ip and the port for the connection.
     * Some checks about the validity of these fields are done with some conditions at the beginning.
     */
    @FXML
    private void connect() throws IOException, InterruptedException {
        currNode = notice;
        String nickname = this.nicknameField.getText();
        String ip = this.ipField.getText();
        String port = this.portField.getText();

        if (nickname.equals("") || ip.equals("") || port.equals("")) {
            this.notice.setText("FAILURE: make sure to fill all the fields!");
        } else if (this.nicknameField.getText().length() > 13) {
            this.notice.setText("FAILURE: nickname must be less than 13 characters!");
        } else if (ip.contains(" ") || port.contains(" ")) {
            this.notice.setText("FAILURE: ip and port can't contain any spaces!");
        } else if (isValidIp(ip) || isValidPort(port)) {
            this.notice.setText("FAILURE: ip or port format not valid!");
        } else {
            SetConnection.setConnection(nickname, ip, port, this.guiView.getClientModel());
            if (Network.isConnected()) {
                this.connected.setText("CONNECTED!");
                this.notice.setText("Waiting for the server to give a response...");
                ClientModel model = SendModelAndGetResponse.sendAndGetModel(this.guiView.getClientModel());

                if (model != null) {
                    this.guiView.setClientModel(model);
                } else {
                    Network.disconnect();
                    this.notice.setText("Server didn't respond, disconnection...");
                    TimeUnit.SECONDS.sleep(5);
                    System.exit(0);
                }
                if (this.guiView.getClientModel().getAmFirst() == null) {
                    this.notice.setText("FAILURE: Nickname already taken");
                } else if (this.guiView.getClientModel().getAmFirst()) {
                    this.guiView.changeScene("SetupGame");
                } else {
                    loading.setVisible(true);
                    ConnectionToServer connection = new ConnectionToServer();
                    connection.connect(false);
                }
            } else {
                this.notice.setText("FAILURE: impossible to connect to server!");
            }
        }
    }
}
