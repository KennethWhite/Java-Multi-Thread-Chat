package client;

//imports
 import javafx.scene.layout.Pane;

 import java.awt.*;
 import java.awt.event.*;
 import java.awt.Dimension;
 import java.io.*;
 import java.net.*;
 import javax.swing.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;


//prompts user for ip address and port then attempts to connect
public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private ProgGui gui;


     //initializes the client class
     public static void main(String[] args) throws Exception {
         Client client = new Client();
         client.run();
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
         int reply = JOptionPane.showConfirmDialog(null,"Would you like to load a server?\n(hardcoded until implemented)","Load Server?",JOptionPane.YES_NO_OPTION);
        String svr;
        if(reply == JOptionPane.YES_OPTION){
            System.out.println("Load server list");
            svr = "10.0.0.217";                                                                                                      ///////change this address to the one you connect to often for testing so its easier to connect until server list is made\\\\\\\
        }
        else{
            svr = getServerAddress();
        }
        return  svr;
    }

    private String loadSvrList(){

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
  