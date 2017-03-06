package client;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.EOFException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Casey on 1/29/2017.
 */

public class ServerScene extends BorderPane{
    final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());
    private ListView listView = getServerList();
    private BorderPane serverSceneLayout;
    private Client client;
    private Stage window;

    public ServerScene(Stage primaryStage, Client client){
        this.serverSceneLayout =  makeListLayout();
        this.window = primaryStage;
        this.client = client;
    }

//merges the different layouts int a borderpane
    private BorderPane makeListLayout(){
        BorderPane serverListLayout = new BorderPane();     //create layout
        serverListLayout.setRight(getSideBar());
        serverListLayout.setCenter(listView);
        return serverListLayout;
    }


//get side bar
    private VBox getSideBar(){
        LoadSaveObject savedServers = new LoadSaveObject();

        VBox side = new VBox();
        side.setPadding(new Insets(10,10,10,10));                                           //sets padding for elements inside Vertical box(top/right/bottom/left)
        side.setSpacing(20);

    //create elements
        side.setAlignment(Pos.TOP_CENTER);
        TextField usrNameInput = new TextField();
        usrNameInput.setPromptText("Username");                                            //username text field
        usrNameInput.setText(savedServers.getUserName());

        Text errorText = new Text();                                                       //error text field
        errorText.setFill(Paint.valueOf("#ff0000"));

        Button newBtn = new Button("new");
        newBtn.setOnAction(e -> addressInput());                                            //new address button


        Button deleteBtn = new Button("delete");                                            //delete button
        deleteBtn.setOnAction(e -> {
            String selected = (String) listView.getSelectionModel().getSelectedItem();
            if(!selected.equals("localhost")) {
                if(!savedServers.removeServer(selected))
                    errorText.setText("failed to remove " + selected);
                else{
                    errorText.setText("Item was removed");
                    savedServers.saveServerList();
                }
                listView.getItems().remove(selected);
                savedServers.saveServerList();
            }
            else
                errorText.setText("Cannot delete localhost");
        });


        Button connectBtn = new Button("connect");                                     //connect button
        connectBtn.setOnAction(e -> {
            String usrname = usrNameInput.getText();

            /*usrname = usrname.replaceAll("\\s", "_");
            usrNameInput.clear();*/

            if(usrname == null)
                usrname = "";
            usrname = usrname.replaceAll("\\s", "_");                                    //ensure no whitespac

            usrNameInput.setText(usrname);
            LoadSaveObject saveUsername = new LoadSaveObject();
            savedServers.setUserName(usrname);

            if(usrname.length()==0 || usrname.isEmpty()) {
                errorText.setText("You must have a username");
            }
            else if(listView.getSelectionModel().getSelectedItem() == null)
                errorText.setText("You must select a server");
            else {
                if(client.connect(listView.getSelectionModel().getSelectedItem().toString())){
                   errorText.setText("The server connected");
                    try {
                        window.close();
                        client.run();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // client
                }
                else{
                    errorText.setText("The server cannot be connected to at this time");
                }
            }
        });

        side.getChildren().addAll(usrNameInput,newBtn,deleteBtn,connectBtn,errorText);
        return side;
    }//end getSideBar



//get list of servers
    private ListView getServerList(){
        ListView<String> temp = new ListView<>();
        LoadSaveObject loadServer = new LoadSaveObject();
        temp.getItems().add("localhost");
        temp.getItems().addAll(loadServer.getServers());                                           //load servers from file here
        return  temp;
    }


//getAddress
    private String addressInput(){
        try {

            Stage stage = new Stage();
                                                                                        //creates grid layout
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            //grid.setVgap(5);
            grid.setHgap(5);

            //Defining the text field and area
                Text txt = new Text("Enter the new Ip address");
                GridPane.setConstraints(txt, 0, 0);                                     //put text field at grid location
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
                        LoadSaveObject saveServer = new LoadSaveObject();
                        saveServer.updateSavedServers(address , address);
                        listView.getItems().addAll(address);
                        saveServer.saveServerList();
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

    public BorderPane getServerSceneLayout() {
        return serverSceneLayout;
    }
}
