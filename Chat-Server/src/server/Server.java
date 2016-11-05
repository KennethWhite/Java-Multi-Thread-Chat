package server;


//imports
import java.io.*;
import java.net.*;
import java.util.*;


/**

    //things to do
        1. ensure proper disconnect with client at disconnect
        2. The server should do some logging, connection times, names. we could save it to a file.
        3. Implement special character for data communication and server commands
        4. make server gui, settings, logs. we could read them into a "ScrollPane" so you could read it right there.


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
//change
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
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                    else if(shouldParse(input)){
                        input = parse(input);
                    }

                        for (PrintWriter writer : writers) {//Iterates through all the printwriters
                            writer.println("MESSAGE " + name + ": " + input);//each client is sent the message
                            System.out.println(name + ": " + input);
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
            }// end finally
        }//end run

        private static boolean shouldParse(String s){
            if(!s.isEmpty() && s.substring(0,1).equals("/")){
                return true;
            }
            return false;
        }

        //Method will be used to perform user commands
        private String parse(String s){
            String temp = s.toLowerCase();
            int var1;

            //To add a command simply add 'case "command":'
            switch(temp){

                case "/date":
                    Date date = new Date();
                    this.out.println(date.toString());


                case "/flip":
                    var1 = (int)Math.random();
                    if(var1 % 2 == 0){
                        return "Flipping a coin: Heads.";
                    }
                    else{
                        return "Flipping a coin: Tails.";
                    }


                case "/roll":
                    var1 = (int)(Math.random() * 100);
                    return "Rolling (0-99): " + var1 + ".";


                case "/rolldice":
                    var1 = ((int)(Math.random() * 10) +2);
                    if(var1 == 2){
                        return "Rolling two dice: Roll is " + var1 + ", snake eyes!";
                    }
                    else if(var1 == 12){
                        return "Rolling two dice: Roll is " + var1 + ", lucky!";
                    }
                    return "Rolling two dice: Roll is " + var1 + ".";



            }//end switch
            return "Command not recognized: " + temp;


        }
    }//end Handler
}//end class
