package commandP;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by Daric on 11/6/2016.
 */
public class NullCommand implements Icommands{

    private ObjectOutputStream out;
    private String text;

    public NullCommand(String text, ObjectOutputStream out){
        this.out = out;
        this.text = text;
    }

    public String perform() {
        try {
            out.writeObject("MESSAGE " + "Command not recognized: " + text);
        }
        catch(IOException ioe){
            //TODO
        }
        return null;
    }
    @Override
    public String help(){
        return "/NullCommand is used behind the scenes to handle unknown commands.";
    }

    public String getName(){
        return "NullCommand";
    }
}
