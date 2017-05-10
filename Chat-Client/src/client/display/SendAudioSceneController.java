package client.display;


import client.Client;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;
import java.util.ResourceBundle;

public class SendAudioSceneController implements Initializable{

    private Client client;
    private TextFlow messageArea;
    private ProgressBar progressBar;

    public void initialize(URL location, ResourceBundle resources) {
        client = Client.getClient();
    }

    /**
     * This method serves as a way to initialize an instance of this controller, and pass objects from calling class
     * @param root the scene to display to user
     * @param textField the chat window text field
     */
    public  void setup(Parent root, TextFlow textField){
        this.messageArea = textField;
        Stage audioStage = new Stage();
        audioStage.initModality(Modality.WINDOW_MODAL);
        audioStage.initStyle(StageStyle.DECORATED);
        audioStage.setTitle("Send Audio");
        audioStage.setScene(new Scene(root));
        audioStage.show();
    }



    /**
     * records audio
     */
    public void recordBtnHandler() {
        Text nt = new Text("Recorded like old program!\n");
        nt.setFill(Color.LIME);
        nt.setFont(Font.font("Monaco", 15));
        messageArea.getChildren().add(nt);

        //TODO popup window to aquire 'title' of recorded audio clip
        String title = "Testing...123";

        client.voiceLine(title);
    }

    /**
     * plays the recorded audio
     */
    public void listenBtnHandler() {
        Text nt = new Text("Cannot listen yet!\n");
        nt.setFill(Color.BLUE);
        nt.setFont(Font.font("Comic Sans MS", 20));
        messageArea.getChildren().add(nt);

    }

    /**
     * sends the audio to server
     */
    public void sendBtnHandler() {
        Text nt = new Text("Sends like old program!\n");
        nt.setFill(Color.RED);
        nt.setFont(Font.font("Verdana", 25));
        messageArea.getChildren().add(nt);

        //TODO get file from the *fileSearcher* and put into sendLine()

        client.sendLine("Testing...123");
    }

}
