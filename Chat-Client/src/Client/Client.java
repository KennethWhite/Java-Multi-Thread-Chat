package Client;

/**
 * Created by CW-Aspire on 11/1/2016.
 */

//imports
import java.io.*;
import java.net.*;
import javax.swing.*;

//prompts user for ip address and port then attempts to connect
public class Client {
    public void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog( "Enter IP Address of a machine that is\n" + "running the server on port 9090:");    //to prompt user for sever address
        Socket s = new Socket(serverAddress, 9090);             //creates socket to connect to server
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));       //creates a buffered reader for input from server
        String ans = input.readLine();                      //gets input
        JOptionPane.showMessageDialog(null, ans);           //shows the imput recieved from server
        System.exit(0);
    }
}
