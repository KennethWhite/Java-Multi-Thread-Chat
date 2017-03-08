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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatSceneController implements Initializable{


    /**
     * Objects injected from fxml
     */
    public TextField inputField;
    public TextArea messageArea;
    public ProgressBar recordProgress;
    public Label statusBar;
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
    public void sendAudioBtnHandler(){
        try {
            Stage audioWindow = new Stage();
            Parent root =  FXMLLoader.load(getClass().getResource("SendAudioScene.fxml"));        //i think this needs moved to directory that contains no java files
            audioWindow.setTitle("Chat Client");
            audioWindow.setScene(new Scene(root));
            audioWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * records audio
     */
    public void recordBtnHandler(){
        client.voiceLine(this);
    }

    /**
     * plays the recorded audio
     */
    public void listenBtnHandler(){

    }

    /**
     * sends the audio to server
     */
    public void sendBtnHandler(){
        client.sendLine(this);
    }

    public void notifyClient(String message){
        this.statusBar.setText(message);
    }


}
