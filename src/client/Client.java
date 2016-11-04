package client;
  //imports

 import java.awt.event.*;
 import java.io.*;
 import java.net.*;
 import javax.swing.*;
  
 //prompts user for ip address and port then attempts to connect
  public class Client {
  
       /**
      * Runs the client as an application with a closeable frame.
      */
     public static void main(String[] args) throws Exception {
         Client client = new Client();
         client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         client.frame.setVisible(true);
         client.run();
      }
     
     BufferedReader in;
     PrintWriter out;
     JFrame frame = new JFrame("Chatter");
     JTextField textField = new JTextField(40);
     JTextArea messageArea = new JTextArea(8, 40);
 
     public Client() {
 
         // Layout GUI
         textField.setEditable(false);
         messageArea.setEditable(false);
         frame.getContentPane().add(textField, "North");
         frame.getContentPane().add(new JScrollPane(messageArea), "Center");
         frame.pack();
 
         // Add Listeners for keys press
         textField.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 out.println(textField.getText());           //takes input sends to server
                 textField.setText("");                      //clears txt box
             }
         }
    ); }
 
 
     //Prompt for and return the address of the server.
 
     private String getServerAddress() {
         return JOptionPane.showInputDialog(
                 frame,
                 "Enter IP Address of the Server:",
                 "Welcome to the Chatter",
                 JOptionPane.QUESTION_MESSAGE);
     }
 
 
     // Prompt for and return the desired screen name.
 
     private String getName() {
         return JOptionPane.showInputDialog(
                 frame,
                 "Choose a screen name:",
                 "Screen name selection",
                 JOptionPane.PLAIN_MESSAGE);
     }
 
     /**
      * Connects to the server then enters the processing loop.
      */
     private void run() throws IOException {
 
         // Make connection and initialize streams
         String serverAddress = getServerAddress();
         Socket socket = new Socket(serverAddress, 9001);
         in = new BufferedReader(new InputStreamReader(
                 socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream(), true);
 
         // Process all messages from server, according to the protocol.
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
  