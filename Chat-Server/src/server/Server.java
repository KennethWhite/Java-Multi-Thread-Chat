package server;

import commandP.*;//changed
import logging.*;
import utils.*;


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
    private static UsernameTrie names = new UsernameTrie();                                                      //!?!?!?!?!??! map of handlers with name as key

    //List of all printwriters for the clients
    private static ArrayList<ObjectOutputStream> objectWriters = new ArrayList<>();

    //List of all outstreams related to audio for the clients
    private static ArrayList<OutputStream> audioWriters = new ArrayList<>();

    //soon-to-be table with...key: name of user & value: handler associated with that user
    //private static Hashtable<String, Handler> users = new Hashtable<>();


    public static void main(String[] args) throws Exception {

        System.out.println("The chat server is running.");

        ServerSocket listener = null;
        ServerSocket listener2 = null;

        try {

            listener = new ServerSocket(PORT);                                          //Socket listens on a port for text
            listener2 = new ServerSocket(PORT2);                                        //socket listens on port for audio

            while (true) {

                Socket typeSocket = listener.accept();                                  //accepts connection to 'text' port
                Socket audioSocket = listener2.accept();                                //accepts connection to 'audio' port
                Runnable connectionHandler = new Handler(typeSocket, audioSocket);
                new Thread(connectionHandler).start();

            }
        }
        catch(java.net.BindException ex){
            MyLogger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch(Exception ex){
            System.out.println("Error in main: "+ex.getMessage());
            MyLogger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        finally {
            if(listener != null)
                listener.close();
            if(listener2 != null)
                listener2.close();
        }
    }


    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    private static class Handler extends Thread {

        private String name;
        private Socket typeS;
        private Socket audioS;

        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;

        private OutputStream audioOut;


        public Handler(Socket typeS, Socket audioS) {
            this.typeS = typeS;                                                             //stores socket passed from main method
            this.audioS = audioS;
        }


        public void run() {
            try {

                objectOut = new ObjectOutputStream(typeS.getOutputStream());
                objectIn = new ObjectInputStream(typeS.getInputStream());

                audioOut = audioS.getOutputStream();


                boolean notAccepted = true;

                while (notAccepted) {
                    objectOut.writeObject("SUBMITNAME");                                    //requests a name from the client
                    try {
                        name = (String)objectIn.readObject();
                    }
                    catch(ClassNotFoundException cnfe){
                        //TODO
                    }
                    if (!name.equals("null") && !name.equals("")) {

                        synchronized (names) {                                              //synchronized means no other changes can be made to 'names' while this thread is active
                            if (!names.contains(name)) {                                    //adds name to list if it doesnt already exist
                                names.insert(name);
                                MyLogger.log(Level.INFO, "Added client to server: " +
                                                            name);                          //logs each client to file
                                notAccepted = false;
                            }

                        }
                    }
                }

                for(ObjectOutputStream writer : objectWriters){
                    writer.writeObject("MESSAGE SERVER: Added " + name + " to chat.");
                }

                objectOut.writeObject("NAMEACCEPTED");
                objectOut.writeObject("MESSAGE Type /help for a list of server commands");
                objectWriters.add(objectOut);                                               //adds printwriter to ArrayList
                audioWriters.add(audioOut);


                /*
                Create the Runnable instance of Audio to insert into the Thread to run.
                 */
                Runnable audioLoop = new Audio(audioS, audioWriters);
                Thread audioT = new Thread(audioLoop);
                audioT.start();

                /*
                Create the Runnable instance of Messages to insert into the Thread to run.
                 */
                Runnable messageLoop = new Messages(timeConnection, name, objectIn, objectOut, objectWriters);
                Thread messagesT = new Thread(messageLoop);
                messagesT.start();

                /*
                Stays in the run method until the client exits to ensure no loss of messenging/audio...result in error
                 */
                messagesT.join();

            }
            catch(InterruptedException ie){
                //TODO
            }
            catch(SocketException e){
                System.out.println(e);  //??why
                //TODO explicitly handling socket exception w/o logging so that it no longer dominates error log
            }
            catch (IOException e) {
                System.out.println(e );     //why??
                MyLogger.log(Level.SEVERE, e.getMessage(), e);
            } finally {

                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {

                    names.remove(name);

                    MyLogger.log(Level.INFO, "Removing client: " + name);
                }
                if (objectOut != null) {

                    for(ObjectOutputStream writer : objectWriters){
                        try {
                            writer.writeObject("MESSAGE SERVER: Removing client " + name + " from chat.");
                        }
                        catch(IOException ioe){
                            //TODO
                        }
                    }

                    objectWriters.remove(objectOut);
                }

                try {

                    typeS.close();
                    MyLogger.closeLogger();

                } catch (IOException e) {
                    MyLogger.log(Level.SEVERE, "ERROR IN FINALLY BLOCK:\n " + e.getMessage() +
                            "\nList of clients: \n{1}", new Object[]{e, names});
                }

            }

        }//end run

    }//end Handler

}//end class
