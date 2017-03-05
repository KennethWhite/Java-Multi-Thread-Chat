package client.display;

import client.Client;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatSceneController implements Initializable{


    public TextField inputField;
    public TextArea messageArea;
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Client.getClient();

        inputField.setOnKeyPressed(e -> {                                                       //allows enter key to send message
            if(e.getCode() == KeyCode.ENTER) {
                client.sendToServer(inputField.getText());
                inputField.setText("");
            }
        });

        client.runChatService(messageArea);                                          //starts the client-server service
        client.runAudioService();

    }


}
