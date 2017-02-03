package client;


import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logging.SetupLogger;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Casey on 1/29/2017.
 */
public class ServerScene{
    final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());
    private ListView listView = getServerList();
    Stage window;

    public ServerScene(Stage primaryStage){
        this.window = primaryStage;
        Scene serverScene = new Scene(makeListLayout(),600,300);
        window.setScene(serverScene);
        window.show();
    }

    private BorderPane makeListLayout(){
        BorderPane serverListLayout = new BorderPane();     //create layout
        serverListLayout.setRight(getSideBar());
        serverListLayout.setCenter(listView);
        return serverListLayout;
    }


    //get side bar
    private VBox getSideBar(){
        VBox side = new VBox();
        side.setPadding(new Insets(10,10,10,10));           //sets padding for elements inside Vertical box(top/right/bottom/left)
        side.setSpacing(20);

        //create elements
        side.setAlignment(Pos.TOP_CENTER);
        TextField usrNameInput = new TextField();
        usrNameInput.setPromptText("Username");

        Text errorText = new Text();
        errorText.setFill(Paint.valueOf("#ff0000"));

        Button newBtn = new Button("new");
        newBtn.setOnAction(e -> getAddr());          //create action events with lampda function. ((args) -> method body)

        Button deleteBtn = new Button("delete");
        deleteBtn.setOnAction(e -> {
            String selected = (String) listView.getSelectionModel().getSelectedItem();
            if(!selected.equals("localhost")) {
                listView.getItems().remove(selected);
            }
            else
                errorText.setText("Cannot delete localhost");
        });

        Button connectBtn = new Button("connect");
        connectBtn.setOnAction(e -> {
            String usrname = usrNameInput.getText();
            usrname = usrname.replace("\\s", "_");                                                                              //ensure no whitespace
            usrNameInput.clear();
            usrNameInput.setText(usrname);
            if(usrname.length()==0 || usrname.isEmpty()) {
                errorText.setText("You must have a username");
            }
            else if(listView.getSelectionModel().getSelectedItem() == null)
                errorText.setText("You must select a server");
            else
                errorText.setText("wont work yet :(");
        });

        side.getChildren().addAll(usrNameInput,newBtn,deleteBtn,connectBtn,errorText);
        return side;
    }

    //get list of servers
    private ListView getServerList(){
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("localhost");                                            //load here
        return  listView;
    }

    private String getAddr(){
        try {

            Stage stage = new Stage();
                                                                                        //creates grid layout
                GridPane grid = new GridPane();
                grid.setPadding(new Insets(10, 10, 10, 10));
                //grid.setVgap(5);
                grid.setHgap(5);

            //Defining the text field and area
                Text txt = new Text("Enter the new Ip address");
                GridPane.setConstraints(txt, 0, 0);
                grid.getChildren().add(txt);
                grid.setVgap(10);

                TextField ip = new TextField();
                ip.setPrefColumnCount(10);
                ip.getText();
                GridPane.setConstraints(ip, 0, 1);
                grid.getChildren().add(ip);


            //Defining the accept button
                Button okBtn = new Button("Accept");
                okBtn.setOnAction(e -> {
                    String address = ip.getText();
                    if(address != null && !address.isEmpty() &&  address.length() > 3 && !address.equals(" ") && !address.equals("Invalid IP address")) {                   //will save the ip to the file and list will reload
                        listView.getItems().addAll(ip.getText());
                        stage.close();
                    }
                    else
                        ip.setText("Invalid IP address");
                    ip.requestFocus();
                    ip.selectAll();
                });


                GridPane.setConstraints(okBtn, 1, 1);
                grid.getChildren().add(okBtn);
                 ip.requestFocus();

            stage.setScene(new Scene(grid));                                    //set scene
            stage.initModality(Modality.APPLICATION_MODAL);					    //this will block all init(button) events of other windows
            stage.show();
        }
        catch(Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

}
