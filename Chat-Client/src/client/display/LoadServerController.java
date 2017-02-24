package client.display;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadServerController implements Initializable{

    public void deleteBtnHandler(){
        System.out.println("delete button pressed");
    }

    public void newBtnHandler(){
        System.out.println("new button pressed");
    }

    public void connectBtnHandler(){
        System.out.println("connect button pressed");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {        //runs when gui is created

        System.out.println("it started");
    }
}
