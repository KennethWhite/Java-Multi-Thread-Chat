package client;

//imports
 import logging.SetupLogger;
 import java.io.*;
 import java.net.*;
 import javax.swing.*;
 import java.time.LocalDateTime;
 import java.time.format.*;
 import java.util.Calendar;
 import java.util.Properties;
 import java.util.logging.Handler;
 import java.util.logging.Level;
 import java.util.logging.Logger;



//prompts user for ip address and port then attempts to connect
    public class Client {
    private Properties settings;
    private BufferedReader in;
    private PrintWriter out;
    private ProgGui gui;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalDateTime time;


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
                LOGGER.log(Level.INFO ,"Load server list");
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
         Socket socket = new Socket(serverAddress, 9001);                               //creates socket connection to server
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));       //buffered reader to recieve from server
         out = new PrintWriter(socket.getOutputStream(), true);                         //printwriter to write to server



     //listening loop
         boolean cont = true;
         while (cont) {
             try {
                 String line = in.readLine();
                 time = LocalDateTime.now();

             if (line != null) {
                 if (line.startsWith("SUBMITNAME")) {
                     out.println(getName());
                 } else if (line.startsWith("NAMEACCEPTED")) {
                     gui = new ProgGui();                                       //waits till fully connected to server before loading gui
                     gui.getTextField().setEditable(true);
                     gui.setOut(out);
                 } else if (line.startsWith("MESSAGE")) {
                     gui.getMessageArea().append(dtf.format(time) + " : "  + line.substring(8) + "\n");
                 } else if (line.startsWith(".*")) {
                     gui.getMessageArea().append("Identify what to do with the data");
                 }
             }
             }//end try
             catch(SocketException e){
                 //will prevent endless loop if server goes down
                 LOGGER.log(Level.SEVERE, e.getMessage(), e);
                 cont = false;
             }
             catch (Exception e){
                 LOGGER.log(Level.SEVERE, e.getMessage(), e);
             }
     }//end run
  }
}
  