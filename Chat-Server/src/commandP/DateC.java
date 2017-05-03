package commandP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by Daric on 11/9/2016.
 */
public class DateC implements Icommands {
    private ObjectOutputStream out;

    public DateC(ObjectOutputStream out){
        this.out = out;
    }

    public String perform(){
        if(out != null) {
            Date date = new Date();
            try {
                out.writeObject("MESSAGE " + date.toString());
            }
            catch(IOException ioe){
                //TODO
            }
        }
        return null;
    }

    @Override
    public String help(){
        return "/Date - Returns the current date and time of the server. (May vary from local time)";
    }

    public String getName(){
        return "date";

    }
}
