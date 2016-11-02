package client;
//imports
import java.io.*;
import java.net.*;
import javax.swing.*;

//prompts user for ip address and port then attempts to connect
public class Client {
    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog( "Enter IP Address of a machine that is\n" + "running the server on port 9090:");    //to prompt user for sever address
        Socket s = new Socket(serverAddress, 9090);             //creates socket to connect to server
        System.out.println("Connection Success");
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));       //creates a buffered reader for input from server
        String ans = input.readLine();                      //gets input
        System.out.println("Data from server received");
        JOptionPane.showMessageDialog(null, ans);           //shows the imput recieved from server0
        s.close();
        System.exit(0);
    }
}
