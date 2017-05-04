package commandP;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by Daric on 11/6/2016.
 *
 * This is a nullcommand object.
 */
public class NullCommand implements Icommands{

    private ObjectOutputStream out;
    private String text;

    /**
     * Constructor
     *
     * @param text
     * @param out
     */
    public NullCommand(String text, ObjectOutputStream out){
        this.out = out;
        this.text = text;
    }

    /**
     * NullCommand has no actual perform
     *
     * @return
     */
    public String perform() {
        try {
            out.writeObject("MESSAGE " + "Command not recognized: " + text);
        }
        catch(IOException ioe){
            //TODO
        }
        return null;
    }

    /**
     * Displays what nullCommand does if called.
     *
     * @return
     */
    @Override
    public String help(){
        return "/NullCommand is used behind the scenes to handle unknown commands.";
    }

    /**
     * returns 'NullCommand'
     *
     * @return
     */
    public String getName(){
        return "NullCommand";
    }
}
