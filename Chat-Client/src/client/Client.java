package client;

//imports
 import logging.SetupLogger;
 import java.io.*;
 import java.net.*;
 import javax.sound.sampled.AudioFileFormat.Type;
 import javax.sound.sampled.*;
 import javax.swing.*;
 import javax.sound.sampled.AudioSystem;
 import java.util.Properties;
 import java.util.logging.Handler;
 import java.util.logging.Level;
 import java.util.logging.Logger;


//prompts user for ip address and port then attempts to connect
public class Client {



    private Properties settings;
    private BufferedReader in;
    private PrintWriter out;
    private OutputStream audioOut;
    private BufferedInputStream audioIn;
    private ProgGui gui;
    private static final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());

     //initializes the client class
     public static void main(String[] args){
         try {
             Client client = new Client();
             client.run();
         }
         catch (ConnectException e){
             LOGGER.log(Level.SEVERE, "Server refused connection, or server is down: " + e.getMessage(), e);
             //display message to client, call run again
         }

         catch(Exception e){
             LOGGER.log(Level.SEVERE, "An unexpected fatal error occurred while running client: " + e.getMessage(), e);
         }
         finally{
             Handler[] handlers = LOGGER.getHandlers();
             for(int i = 0; i < handlers.length; i++){
                 handlers[i].close();
             }
         }
     }
     


//the object Client.java
     private Client() {

     }

 

//Prompt for and return the address of the server before a user connects.
     private String getServerAddress() {
         return JOptionPane.showInputDialog(null, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
     }

     private String getName() {
         return JOptionPane.showInputDialog(null, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
     }

    private String getAddr(){
        try {
            int reply = JOptionPane.showConfirmDialog(null, "Would you like connect to this computer?\n(No prompts for IP address)", "Load Server?", JOptionPane.YES_NO_OPTION);
            String svr;
            if (reply == JOptionPane.YES_OPTION) {
                System.out.println("Load server list");
                svr = "localhost";                                                                                                      //.rm this will load a JPane containing all saved servers
            } else {
                svr = getServerAddress();
            }
            return svr;
        }
        catch(Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private String loadSvrList(){
        //create pane with scroll box to load recent/saved servers. return server selected
        return null;
    }
 
     //Main looped used to update client from server and vise versa
     private void run() throws IOException {
 
         // Make connection and initialize streams
         String serverAddress = getAddr();
         Socket typeS = new Socket(serverAddress, 9001);//creates socket connection to server...for text
         Socket audioS = new Socket(serverAddress, 9002);//creates another socket connection to server..for audio
         in = new BufferedReader(new InputStreamReader(typeS.getInputStream()));       //buffered reader to recieve from serverfor text
         out = new PrintWriter(typeS.getOutputStream(), true);                       //printwriter to write to server
         audioIn = new BufferedInputStream(audioS.getInputStream());                //input stream to recieve from server for audio
         audioOut = audioS.getOutputStream();                                       //output stream to send audio out


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
                         //will prevent endless loop if server goes downs
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
         while (cont) {
            try{
                //for typing
                String line = in.readLine();

                 if (line != null) {
                     if (line.startsWith("SUBMITNAME")) {
                         out.println(getName());
                     } else if (line.startsWith("NAMEACCEPTED")) {
                        gui = new ProgGui();                                       //waits till fully connected to server before loading gui
                        gui.getTextField().setEditable(true);
                        gui.setOut(out);
                        gui.setAudioOut(audioOut);
                        audioT.start();        //this starts the above declared thread that listens for audio, after everything is in working order with GUI
                    } else if (line.startsWith("MESSAGE")) {
                        gui.getMessageArea().append(line.substring(8) + "\n");
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

     }//end run

  }
}
  