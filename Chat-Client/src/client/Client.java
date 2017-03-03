package client;

//imports
 import javafx.application.Application;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
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

//            BorderPane serverList = new ServerScene(primaryStage, client).getServerSceneLayout();                     //commented section is original
//            Scene serverScene = new Scene(serverList,600,300);
//            primaryStage.setScene(serverScene);
//            primaryStage.show();
        }



//if the name is already used, reprompt
        private String getName() {
            return JOptionPane.showInputDialog(null, "Username is taken. Enter a new Username", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
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

//Main looped used to update client from server and vise versa
     public void run() throws IOException {
         //output stream to send audio out

        /*
        creates a thread that contains code for a loop similar to what is found for 'text' that
        is below this thread declaration
        */
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
                         cont = false;
                     }
                     finally{
                         for(Handler h: LOGGER.getHandlers()){
                             h.close();
                         }
                     }
                 }//end while
             }//end run
         });//end audio thread

         //listening loop
         boolean cont = true;
         int namesFailed = 0;
         while (cont) {

            try{
                //for typing
                String line = in.readLine();

                 if (line != null) {

                     if (line.startsWith("SUBMITNAME")) {
                         if(namesFailed < 1) {
                             out.println(userName);                                //a switch from fx to swing if the name entered in fx pane was already used
                             namesFailed++;
                         }
                         else {
                             out.println(getName());
                         }
                     } else if (line.startsWith("NAMEACCEPTED")) {
                        gui = new ProgGui();                                       //waits till fully connected to server before loading gui
                        gui.getTextField().setEditable(true);
                        gui.setOut(out);
                        gui.setAudioOut(audioOut);
                        audioT.start();        //this starts the above declared thread that listens for audio, after everything is in working order with GUI
                    } else if (line.startsWith("MESSAGE")) {
                        gui.getMessageArea().append(line.substring(8) + "\n");
                        AudioEffects.play("boop.wav");
                    } else if (line.startsWith(".*")) {

                        gui.getMessageArea().append("Identify what to do with the data");
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
     }//end run


}
  