package server;

import logging.MyLogger;
import utils.Parsing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Daric on 5/11/2017.
 */
public class Messages implements  Runnable{

    private long timeConnection;
    private String name;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private ArrayList<ObjectOutputStream> objectWriters;

    public Messages(long time, String name, ObjectInputStream in, ObjectOutputStream out, ArrayList<ObjectOutputStream> writers){
        this.timeConnection = time;
        this.name = name;
        this.objectIn = in;
        this.objectOut = out;
        this.objectWriters = writers;
    }


    @Override
    public void run() {

        boolean looper = true;

        Object input = null;

        //loop for text
        while (looper) {

            String strIn = null;

            try {

                input = objectIn.readObject();

                if (input instanceof String) {

                    strIn = (String) input;

                    if (Parsing.shouldParse(strIn)) {
                        strIn = Parsing.parse(strIn, objectOut, timeConnection);                                           //parses input for commands
                    }
                    if (strIn != null && !strIn.equals("")) {
                        System.out.println(name + ": " + strIn);

                        for (ObjectOutputStream writer : objectWriters) {
                            String temp = "MESSAGE " + name + ": " + strIn;
                            writer.writeObject(temp);
                        }
                    }
                } else {
                    //TODO game data handling
                }



            }
            catch(ClassNotFoundException cnfe){
                //TODO
                looper = false;
            }
            catch (IOException e) {
                System.out.println(e );
                MyLogger.log(Level.SEVERE, e.getMessage(), e);
                looper = false;
            }
        }

    }
}
