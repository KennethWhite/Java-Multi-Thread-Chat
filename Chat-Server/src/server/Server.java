package server;

import commandP.*;//changed
import logging.*;


//imports
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;



//Multithread chat room server
public class Server {
    private static long timeConnection = System.currentTimeMillis();

    //The ports that the server listens on.
    private static final int PORT = 9001;
    private static final int PORT2 = 9002;

    //arrayList of all the names in use
    private static ArrayList<String> names = new ArrayList<>();

    //List of all printwriters for the clients
    private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

    //List of all outstreams related to audio for the clients
    private static ArrayList<OutputStream> audioWriters = new ArrayList<>();

    private static final Logger LOGGER = SetupLogger.startLogger(Server.class.getName());


    public static void main(String[] args) throws Exception {

        System.out.println("The chat server is running.");
        ServerSocket listener = null;
        ServerSocket listener2 = null;
        try {
            listener = new ServerSocket(PORT);//Socket listens on a port for text
            listener2 = new ServerSocket(PORT2);//socket listens on port for audio
            while (true) {

                Socket typeSocket = listener.accept();//accepts connection to 'text' port
                Socket audioSocket = listener2.accept();//accepts connection to 'audio' port
                Runnable connectionHandler = new Handler(typeSocket, audioSocket);
                new Thread(connectionHandler).start();

            }
        }
            catch(Exception ex){
                System.out.println("Error in main: "+ex.getMessage());
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
         finally {
            if(listener != null)
                listener.close();
            if(listener2 != null)
                listener2.close();
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
        private Socket typeS;
        private Socket audioS;
        private BufferedReader in;
        private PrintWriter out;
        private OutputStream audioOut;
        private DataInputStream audioIn;
        //view logging.setupLogger for details




        public Handler(Socket typeS, Socket audioS) {
            this.typeS = typeS; //stores socket passed from main method
            this.audioS = audioS;
        }


        public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(typeS.getInputStream()));
                out = new PrintWriter(typeS.getOutputStream(), true);
                audioIn = new DataInputStream(audioS.getInputStream());
                audioOut = audioS.getOutputStream();
                while (true) {
                    out.println("SUBMITNAME");//requests a name from the client
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    out.println("Added " + name + " to the chat.");
                    synchronized (names) {                                              //synchronized means no other changes can be made to 'names' while this thread is active
                        if (!names.contains(name)) {                                    //adds name to list if it doesnt already exist
                            names.add(name);
                            LOGGER.log(Level.INFO, "Added client to server: " + name);   //logs each client to file
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED");
                out.println("MESSAGE Type /help for a list of server commands");
                writers.add(out);                                                       //adds printwriter to ArrayList
                audioWriters.add(audioOut);

                /*
                creates a thread that consists of code to run and listen for incoming audio connected to the
                corresponding port
                 */
                Thread audioT = new Thread(new Runnable(){
                    public void run(){
                        while (true) {

                            try {
                                //??make the declaration outside the loop??
                                InputStream iin = new BufferedInputStream(audioS.getInputStream());
                                AudioInputStream ais = AudioSystem.getAudioInputStream(iin);    //waits on this line for incoming audio
                                ais.mark(100000);
                                for (OutputStream outs : audioWriters) {
                                    AudioSystem.write(ais, Type.WAVE, outs);
                                    ais.reset();
                                }
                            } catch (Exception e) {
                                //LOGGER.log(Level.SEVERE, e.getMessage(), e);TODO
                            }
                            finally {
                                //finalmethod TODO
                            }

                        }
                    }//end run
                });//end audioT thread

                //starts the audio thread
                audioT.start();

                //loop for text
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

            }
            catch(SocketException e){
                System.out.println(e);
                //explicitly handling socket exception w/o logging so that it no longer dominates error log
            }
            catch (IOException e) {
                System.out.println(e );
                LOGGER.log(Level.SEVERE, e.getMessage(), e);

            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                    out.println("Removing client: " + name);
                    LOGGER.log(Level.INFO, "Removing client: " + name);
                }
                if (out != null) {
                    writers.remove(out);
                }

                try {
                    typeS.close();
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
