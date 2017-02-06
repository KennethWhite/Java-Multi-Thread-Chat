package client;

//imports
 import javafx.application.Application;
 import javafx.scene.Scene;
 import javafx.scene.layout.BorderPane;
 import javafx.stage.Stage;
 import logging.SetupLogger;
 import java.io.*;
 import java.net.*;
 import java.awt.*;
 import javax.sound.sampled.*;
 import javax.swing.*;

 import javax.sound.sampled.AudioSystem;

 import java.time.LocalDateTime;
 import java.time.format.*;
 import java.util.Calendar;

 import java.util.Properties;
 import java.util.logging.Handler;
 import java.util.logging.Level;
 import java.util.logging.Logger;



//prompts user for ip address and port then attempts to connect

    public class Client extends Application{

        private Properties settings;
        private BufferedReader in;
        private PrintWriter out;
        private OutputStream audioOut;
        private BufferedInputStream audioIn;
        private ProgGui gui;
        private Stage window;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime time;


         private static final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());
        private static Client client = new Client();

         //initializes the client class
         public static void main(String[] args){
                 launch(args);

                 Handler[] handlers = LOGGER.getHandlers();
                 for(int i = 0; i < handlers.length; i++){
                     handlers[i].close();
             }
         }
     


        @Override
        public void start(Stage primaryStage) throws Exception {
            this.window = primaryStage;
            BorderPane serverList = new ServerScene(primaryStage, client).getServerSceneLayout();
            Scene serverScene = new Scene(serverList,600,300);
            window.setScene(serverScene);
            window.show();
        }


     private String getName() {
         return JOptionPane.showInputDialog(null, "Username is taken. Enter a new Username", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
     }

    private String getAddr(){
        try {
            int reply = JOptionPane.showConfirmDialog(null, "Would you like connect to this computer?\n(No prompts for IP address)", "Load Server?", JOptionPane.YES_NO_OPTION);
            String svr;
            if (reply == JOptionPane.YES_OPTION) {
                svr = "localhost";                                                                                                      //.rm this will load a JPane containing all saved servers
            } else {
                svr = JOptionPane.showInputDialog(null, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
            }
            return svr;
        }
        catch(Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }


        public static Logger getLOGGER() {
            return LOGGER;
        }

        public boolean connect(String serverAddress){
            Socket typeS = null;//creates socket connection to server...for text
            try {
                typeS = new Socket(serverAddress, 9001);
                Socket audioS = new Socket(serverAddress, 9002);//creates another socket connection to server..for audio
                in = new BufferedReader(new InputStreamReader(typeS.getInputStream()));       //buffered reader to recieve from serverfor text
                out = new PrintWriter(typeS.getOutputStream(), true);                       //printwriter to write to server
                audioIn = new BufferedInputStream(audioS.getInputStream());                //input stream to recieve from server for audio
                audioOut = audioS.getOutputStream();
            }
            catch (Exception e) {
                System.out.println("Invalid server");
                //e.printStackTrace();
                return false;
            }

            return true;
        }

        //Main looped used to update client from server and vise versa
     public void run(String username) throws IOException {
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
                             out.println(username);                                //a switch from fx to swing if the name entered in fx pane was already used
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
  