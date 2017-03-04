package client;

//imports
 import client.display.ChatSceneController;
 import javafx.application.Application;
 import javafx.application.Platform;
 import javafx.beans.property.SimpleStringProperty;
 import javafx.concurrent.Service;
 import javafx.concurrent.Task;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.control.TextArea;
 import javafx.scene.control.TextField;
 import javafx.scene.layout.BorderPane;
 import javafx.stage.Stage;
 import logging.SetupLogger;
 import java.io.*;
 import java.net.*;
 import javax.sound.sampled.*;
 import javax.swing.*;

 import javax.sound.sampled.AudioSystem;

 import java.time.LocalDateTime;
 import java.time.format.*;

 import java.util.Properties;
 import java.util.Scanner;
 import java.util.logging.Handler;
 import java.util.logging.Level;
 import java.util.logging.Logger;



//prompts user for ip address and port then attempts to connect

    public class Client extends Application{

        private static Client client;

        public static Client getClient() {
            return client;
        }

//initializes the program
        public static void main(String[] args){
                client = new Client();
                launch(args);

                Handler[] handlers = LOGGER.getHandlers();
                for(int i = 0; i < handlers.length; i++){
                    handlers[i].close();

            }


        }

        private BufferedReader in;
        private PrintWriter out;
        private OutputStream audioOut;
        private BufferedInputStream audioIn;
        private ProgGui gui;
        private Stage window;
        private static final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());
        private String userName = "";


        @Override
        public void start(Stage primaryStage) throws Exception {
            client.window = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("display/ServerScene.fxml"));                          //loads the server scene from fxml file
            primaryStage.setTitle("Chat Client");
            primaryStage.setScene(new Scene(root, 800, 400));
            primaryStage.show();

        }



//if the name is already used, reprompt
        private String getName() {
            return JOptionPane.showInputDialog(null, "Username: \""  + userName +  "\" is taken.\nEnter a new Username", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
        }


//used to connect client to server. returns if connection success
        public boolean connect(String serverAddress){
            Socket typeS = null;//creates socket connection to server...for text
            try {
                typeS = new Socket(serverAddress, 9001);
                Socket audioS = new Socket(serverAddress, 9002);//creates another socket connection to server..for audio
                this.in = new BufferedReader(new InputStreamReader(typeS.getInputStream()));       //buffered reader to recieve from serverfor text
                this.out = new PrintWriter(typeS.getOutputStream(), true);                       //printwriter to write to server
                this.audioIn = new BufferedInputStream(audioS.getInputStream());                //input stream to recieve from server for audio
                this.audioOut = audioS.getOutputStream();
            }
            catch (Exception e) {
                System.out.println("Invalid server");
                //e.printStackTrace();
                return false;
            }

            return true;
        }


//set clients name
        public void setName(String newUsername){
            userName = newUsername;
        }


        public void runChatService(TextField inputField, TextArea messgaeArea){                                     //this method creates and starts the service
            Service chatService = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {

                            Thread audioT = new Thread(new Runnable() {
                                public void run(){
                                    boolean cont = true;
                                    while (cont) {

                                        //for sounds
                                        try {
                                            AudioInputStream ais = AudioSystem.getAudioInputStream(audioIn);   //waits here for audio input coming from server
                                            Clip clip = AudioSystem.getClip();
                                            clip.open(ais);
                                            clip.start();
                                            clip.drain();
                                        } catch(SocketException e){
                                            //will prevent endless loop if server goes down
                                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                                            cont = false;
                                        }
                                        catch (Exception e){
                                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                                        }
                                    }//end while
                                }//end run
                            });//end audio thread

                            boolean cont = true;
                            int namesFailed = 0;
                            while (cont) {

                                try{
                                    //for typing
                                    String line = in.readLine();

                                    if (line != null) {

                                        if (line.startsWith("SUBMITNAME")) {
                                            if(namesFailed < 1) {
                                                out.println(userName);                                //if the send useername to server, if bad prompt user
                                                namesFailed++;
                                            }
                                            else {
                                                out.println(getName());
                                            }
                                        } else if (line.startsWith("NAMEACCEPTED")) {
//
                                        } else if (line.startsWith("MESSAGE")) {
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messgaeArea.appendText(line.substring(8) + "\n");
                                                }
                                            });
                                            //gui.getMessageArea().append(line.substring(8) + "\n");
                                            AudioEffects.play("boop.wav");
                                        }
                                    }
                                }//end try
                                catch(SocketException e){
                                    //will prevent endless loop if server goes downs
                                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                                    cont = false;
                                }
                                catch (Exception e){
                                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                                }

                            }//end while
                            return null;
                        }

                    };
                }
            };
            chatService.start();
        }

        public PrintWriter getPrintWriter(){
            return this.out;
        }


}
  