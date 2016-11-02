package server;

/**
 * Created by CW-Aspire on 11/1/2016.
 */

//imports
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args)throws IOException{
        ServerSocket listener = new ServerSocket(9090);     //creates a listening socket on port 9090 to wait for client connection
        System.out.println("Server awaiting connection");
        try{
            while(true){
                Socket socket = listener.accept();  //accept() waits until a client connects to the server on the given port.
                System.out.println("Connection Success");
                try{
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  //this is the output to the client from the erver
                    out.println("the current date is : " + new Date().toString());
                }
                finally {
                    socket.close(); //make sure to always close sockets after use
                    System.out.println("Connection Closed");
                }//lloyyy
            }
        }
        finally {
            listener.close();       //make sure to always close sockets after usegg
            System.out.println("Server Closed");
        }
    }
}
