package client.display;

import client.Client;
import client.LoadSaveUtil;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class ServerSceneController implements Initializable{


//gui variables injected from fxml file.
    public ListView listView;
    public Text errorText;
    public TextField userNameInput;

    private Client client;


    @Override//runs when gui is created. loads user info
    public void initialize(URL location, ResourceBundle resources) {
        loadServerList();
        client = Client.getClient();
    }


//load servers from file on startup
    private void loadServerList(){
        Properties userPrefObj = LoadSaveUtil.getPropertyObject(LoadSaveUtil.userSettingFilename);
        Properties savedServerObj = LoadSaveUtil.getPropertyObject(LoadSaveUtil.serverFilename);
        userNameInput.setText(userPrefObj.getProperty("USERNAME"));
        listView.getItems().add("localhost");
        listView.getItems().addAll(savedServerObj.values());
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
            if(address != null && !address.isEmpty() &&  address.length() > 3 && !address.equals(" ") && !address.equals("Invalid IP address")) {           //if meta-valid address update and save the saved server file using property obj
                Properties savedServerObj = LoadSaveUtil.getPropertyObject(LoadSaveUtil.serverFilename);
                savedServerObj.put(address , address);
                listView.getItems().addAll(address);
                LoadSaveUtil.savePropertyObject(savedServerObj, LoadSaveUtil.serverFilename);
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
        String selectedServer = (String) listView.getSelectionModel().getSelectedItem();
        errorText.setFill(Paint.valueOf("#ff0000"));
        if(selectedServer != null) {
            if (selectedServer.equals("localhost")) {
                errorText.setText("Cannot delete localhost");
                return;
            }
            listView.getItems().remove(selectedServer);
            Properties savedServerObj = LoadSaveUtil.getPropertyObject(LoadSaveUtil.serverFilename);
            savedServerObj.remove(selectedServer);
            LoadSaveUtil.savePropertyObject(savedServerObj,LoadSaveUtil.serverFilename);
        }
        else
            errorText.setText("You must select a server to delete");

        errorText.setFill(Paint.valueOf("#000000"));
    }



//handles when the user tries to connect
    public void connectBtnHandler(){
        String username = userNameInput.getText();

        if(username == null || username.length()==0 || username.isEmpty()) {
            errorText.setFill(Paint.valueOf("#ff0000"));
            errorText.setText("You must have a username");
        }
        else if(listView.getSelectionModel().getSelectedItem() == null) {
            errorText.setFill(Paint.valueOf("#ff0000"));
            errorText.setText("You must select a server");
        }
        else {
            try {
                client.setName(username);
                if (client.connect(listView.getSelectionModel().getSelectedItem().toString()))      {                    //if the connection was successful close fx and start main service
                    username = username.replaceAll("\\s", "_");                                    //ensure no whitespace\
                    Properties userSettingsObj = LoadSaveUtil.getPropertyObject(LoadSaveUtil.userSettingFilename);
                    userSettingsObj.put("USERNAME", username);
                    Stage mainStage = (Stage) errorText.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("ChatScene.fxml"));                            //this line switches to the chat scene
                    mainStage.setScene(new Scene(root));
                    mainStage.show();
            }
            else {
                    errorText.setText("cannot connect");
                }
        }
            catch (Exception ex) {
                ex.printStackTrace();
            }
    }
        errorText.setFill(Paint.valueOf("#000000"));
    }






}
