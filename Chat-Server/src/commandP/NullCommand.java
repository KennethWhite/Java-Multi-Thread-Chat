package commandP;

import java.io.PrintWriter;

/**
 * Created by Daric on 11/6/2016.
 */
public class NullCommand implements Icommands{

    private PrintWriter out;
    private String text;

    public NullCommand(String text, PrintWriter out){
        this.out = out;
        this.text = text;
    }

    public String perform() {
        out.println("MESSAGE " +"Command not recognized: " + text);
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
