package server;

/**
 * Created by CW-Aspire on 11/1/2016.
 * Design is from: http://cs.lmu.edu/~ray/notes/javanetexamples/#chat
 */

//imports
import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Because this is just a teaching example to illustrate a simple
 * chat server, there are a few features that have been left out.
 * Two are very useful and belong in production code:
 *
 *     1. The protocol should be enhanced so that the client can
 *        send clean disconnect messages to the server.
 *
 *     2. The server should do some logging.
 */

//Multithread chat room server
public class Server {


    //The port that the server listens on.
     private static final int PORT = 9001;

    //arrayList of all the names in use
    private static ArrayList<String> names = new ArrayList<>();

    //List of all printwriters for the clients
    private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();


    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);//Socket listens on a port
        try {
            while (true) {
                new Handler(listener.accept()).start(); //creates handler threads for each client
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;


        public Handler(Socket socket) {
            this.socket = socket; //stores socket passed from main method
        }


        public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");//requests a name from the client
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    //synchronized means no other changes can be made to 'names' while this thread is active
                    synchronized (names) {
                        if (!names.contains(name)) {//adds name to list if it doesnt already exist
                            names.add(name);
                            break;
                        }
                    }
                }


                out.println("NAMEACCEPTED");//displays to the client
                writers.add(out);//adds printwriter to ArrayList


                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {//Iterates through all the printwriters
                        writer.println("MESSAGE " + name + ": " + input);//each client is sent the message
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
