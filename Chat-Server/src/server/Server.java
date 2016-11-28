package server;

import commandP.*;//changed
import logging.*;


//imports
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;



//Multithread chat room server
public class Server {
    private static long timeConnection = System.currentTimeMillis();

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
        //view logging.setupLogger for details
        private static final Logger LOGGER = SetupLogger.startLogger(Handler.class.getName());




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

                    synchronized (names) {                                              //synchronized means no other changes can be made to 'names' while this thread is active
                        if (!names.contains(name)) {                                    //adds name to list if it doesnt already exist
                            names.add(name);
                            LOGGER.log(Level.INFO, "Added client to server: " + name);   //logs each client to file
                            break;
                        }
                    }
                }


                out.println("NAMEACCEPTED");
                writers.add(out);                                                       //adds printwriter to ArrayList


                while (true) {
                    String input = in.readLine();
                    if (shouldParse(input)) {
                        input = parse(input);                                           //parses input for commands
                    }
                    else if(isData(input)){
                        out.println("MESSAGE Data received but invalid/no destination");          // pass data back to client
                        input = null;
                    }
                    if (input != null && !input.equals("")) {
                        System.out.println(name + ": " + input);
                        for (PrintWriter writer : writers) {
                            writer.println("MESSAGE " + name + ": " + input);           // sends each client is sent the message
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                    LOGGER.log(Level.INFO, "Removing client: " + name);
                }
                if (out != null) {
                    writers.remove(out);
                }

                try {
                    socket.close();
                    java.util.logging.Handler[] handlers = LOGGER.getHandlers();
                    for(int i = 0; i < handlers.length; i++){
                        handlers[i].close();
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "ERROR IN FINALLY BLOCK:\n " + e.getMessage() +
                            "\nList of clients: \n{1}", new Object[]{e, names});
                }

            }
        }//end run


        private static boolean shouldParse(String s) {
            if (!s.equals(null) && !s.isEmpty() && s.substring(0, 1).equals("/")) {
                return true;
            }
            return false;
        }

        //Method will be used to perform user commands
        private String parse(String s){

            String temp = s.toLowerCase();
            CommandFactory cF = new CommandFactory();   //could make this a Handler attribute***
            Icommands curCommand = cF.getCommand(temp, out, timeConnection);

            return curCommand.perform();

        }

        private static boolean isData(String s){                                                        //sometimes throws error. IDK why
            if (!s.equals(null) && s.length() > 1 && s.substring(0, 2).equals(".*")) {
                return true;
            }
                return false;
        }
    }//end Handler

}//end class
