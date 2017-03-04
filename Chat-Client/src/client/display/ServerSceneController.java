package client.display;

import client.Client;
import client.LoadSaveObject;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class ServerSceneController implements Initializable{


//gui variables injected from fxml file.
    public ListView listView;
    public Text errorText;
    public TextField userNameInput;

    private Client client;

//runs when gui is created. loads user info
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadServerList();
        client = Client.getClient();
    }


//load servers from file on startup
    private void loadServerList(){
        LoadSaveObject loadServer = new LoadSaveObject();
        userNameInput.setText(loadServer.getUserName());                        //loads username from file
        listView.getItems().add("localhost");
        listView.getItems().addAll(loadServer.getServers());
    }


//prompts user to enter new ip address in a separate window
    public void newBtnHandler(){
        System.out.println("new button pressed");
        Stage stage = new Stage();
        //creates grid layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
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
            if(address != null && !address.isEmpty() &&  address.length() > 3 && !address.equals(" ") && !address.equals("Invalid IP address")) {
                LoadSaveObject saveServer = new LoadSaveObject();
                saveServer.updateSavedServers(address , address);
                listView.getItems().addAll(address);                         //will save the ip to the file and list will reload
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

        stage.setScene(new Scene(grid));
        stage.initModality(Modality.APPLICATION_MODAL);					    //this will block all init(button) events of other windows
        stage.show();
    }



//handles when the delete button is pressed
    public void deleteBtnHandler(){
        LoadSaveObject savedServers = new LoadSaveObject();
        String selected = (String) listView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            if (!selected.equals("localhost")) {
                if (!savedServers.removeServer(selected)) {
                    errorText.setFill(Paint.valueOf("#ff0000"));
                    errorText.setText("failed to remove " + selected);
                } else {
                    errorText.setFill(Paint.valueOf("#000000"));
                    errorText.setText("Item was removed");
                    savedServers.saveServerList();
                }
                listView.getItems().remove(selected);
                savedServers.saveServerList();
            } else {
                errorText.setFill(Paint.valueOf("#ff0000"));
                errorText.setText("Cannot delete localhost");
            }
        }
        else{
            errorText.setFill(Paint.valueOf("#ff0000"));
            errorText.setText("You must select a server to delete");
        }
    }


//handles when the user tries to connect
    public void connectBtnHandler(){
        String username = userNameInput.getText();
        if(username == null)
            username = "";
        username = username.replaceAll("\\s", "_");                                    //ensure no whitespace
        userNameInput.setText(username);
        LoadSaveObject saveUsername = new LoadSaveObject();
        saveUsername.setUserName(username);
        client.setName(username);

        if(username.length()==0 || username.isEmpty()) {
            errorText.setText("You must have a username");
        }
        else if(listView.getSelectionModel().getSelectedItem() == null)
            errorText.setText("You must select a server");
        else {
                try {
                    if(!client.connect(listView.getSelectionModel().getSelectedItem().toString()))                          //if the connection was successful close fx and start main loop
                        errorText.setText("cannot connect");
                    else{
                        errorText.setText("Connected");
                        Stage mainStage = (Stage) errorText.getScene().getWindow();
                        mainStage.close();
                        client.run();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }






}
