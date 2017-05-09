package client.display;

import client.Client;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatSceneController implements Initializable{


    /**
     * Objects injected from fxml
     */
    public TextField inputField;
    public TextFlow messageArea;
    public ProgressBar recordProgress;
    private Client client;

    @Override
    /**
     * initializes communication services and event handler for the input field
     */
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

    /**
     * opens separate send audio menu when button pressed
     */
    public void sendAudioBtnHandler() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./SendAudioScene.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            SendAudioSceneController ctlr = (SendAudioSceneController) fxmlLoader.getController();                      //we get an instance of our controller
            ctlr.setup(root1, messageArea);                                                                             //call custom creat method

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
