package client.display;

import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inputField.setOnAction(e -> {
            if (e.getSource() == KeyCode.ENTER)  {
                String text = inputField.getText();
                System.out.println(text);
                messageArea.appendText(text);
                inputField.setText("");
            }
        });
    }


}
