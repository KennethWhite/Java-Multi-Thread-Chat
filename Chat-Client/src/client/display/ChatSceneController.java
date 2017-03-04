package client.display;

import client.Client;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Casey on 3/4/2017.
 */
public class ChatSceneController implements Initializable{


    public TextField inputField;
    public TextArea messageArea;
    public Button sendBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Client.getClient().runChatService(inputField,messageArea);                                          //starts the client-server service

    }

    public void sendBtnHandler(){                                                                           //when send button clicked
        String text = inputField.getText();
        System.out.println(text);
        Client.getClient().getPrintWriter().println(text);
        inputField.setText("");
    }





}
