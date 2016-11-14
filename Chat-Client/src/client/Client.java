package client;

//imports
 import java.awt.event.*;
 import java.io.*;
 import java.net.*;
 import javax.swing.*;

  
 //prompts user for ip address and port then attempts to connect
  public class Client {
     //obj vars
     private BufferedReader in;
     private PrintWriter out;
     private JFrame frame = new JFrame("KDC chat");
     private JTextField textField = new JTextField(40);
     private JTextArea messageArea = new JTextArea(8, 40);
  
     //initializes the client class
     public static void main(String[] args) throws Exception {
         Client client = new Client();
         client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         client.frame.setVisible(true);
         client.run();
      }
     


//the object Client.java
     private Client() {
 
         // Layout GUI
         textField.setEditable(false);                                              //denies use of input box until name is verified
         messageArea.setEditable(false);                                            //wont display messages till ^^
         frame.getContentPane().add(textField, "North");                            //adds input box to top of the frame
         frame.getContentPane().add(new JScrollPane(messageArea), "Center");        //adds input box to center of the frame
         frame.pack();                                                              //ensures the content fits in the frame
 
         // Add Listeners for keys press
         textField.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {   //for 'enter' key
                 out.println(textField.getText());           //takes input sends to server
                 textField.setText("");                      //clears txt box
             }
         }
    ); }
 

//Prompt for and return the address of the server before a user connects.
     private String getServerAddress() {
         return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
     }
 

//takes input for a name when user first connects
     private String getName() {
         return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
     }
 
 //Main looped used to update client from server and vise versa
     private void run() throws IOException {
 
         // Make connection and initialize streams
         String serverAddress = getServerAddress();
         Socket socket = new Socket(serverAddress, 9001);           //creates socket connection to server
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));       //buffered reader to recieve from server
         out = new PrintWriter(socket.getOutputStream(), true);                         //printwriter to write to server

    //listening loop
         while (true) {
             String line = in.readLine();
             if (line.startsWith("SUBMITNAME")) {
                 out.println(getName());
             } else if (line.startsWith("NAMEACCEPTED")) {
                 textField.setEditable(true);
             } else if (line.startsWith("MESSAGE")) {
                 messageArea.append(line.substring(8) + "\n");
             }

         }
     }
 

  }
  