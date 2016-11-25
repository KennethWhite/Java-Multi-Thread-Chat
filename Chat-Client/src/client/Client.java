package client;

//imports
 import logging.SetupLogger;
 import java.io.*;
 import java.net.*;
 import javax.swing.*;
 import java.util.logging.Level;
 import java.util.logging.Logger;


//prompts user for ip address and port then attempts to connect
public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private ProgGui gui;
    private static final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());

     //initializes the client class
     public static void main(String[] args){
         try {
             Client client = new Client();
             client.run();
         }
         catch(Exception e){
             LOGGER.log(Level.SEVERE, "An unexpected fatal error occurred while running client: " + e.getMessage(), e);
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
            int reply = JOptionPane.showConfirmDialog(null, "Would you like to load a server?\n(No prompts for IP address)", "Load Server?", JOptionPane.YES_NO_OPTION);
            String svr;
            if (reply == JOptionPane.YES_OPTION) {
                System.out.println("Load server list");
                svr = "localhost";                                                                                                      ///////change this address to the one you connect to often for testing so its easier to connect until server list is made\\\\\\\
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

         while (true) {
             String line = in.readLine();
             if (line != null) {
                 if (line.startsWith("SUBMITNAME")) {
                     out.println(getName());
                 } else if (line.startsWith("NAMEACCEPTED")) {
                     gui = new ProgGui();                                       //waits till fully connected to server before loading gui
                     gui.getTextField().setEditable(true);
                     gui.setOut(out);
                 } else if (line.startsWith("MESSAGE")) {
                     gui.getMessageArea().append(line.substring(8) + "\n");
                 } else if (line.startsWith(".*")) {
                     gui.getMessageArea().append("Identify what to do with the data");                   //possibly sub ifs for separate games. each with a print writer passed.
                 }
             }
         }
     }//end run
  }
  