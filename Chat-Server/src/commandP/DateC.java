package commandP;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by Daric on 11/9/2016.
 */
public class DateC implements Icommands {
    private PrintWriter out;

    public DateC(PrintWriter out){
        this.out = out;
    }

    public String perform(){
        Date date = new Date();
        out.println("MESSAGE " +date.toString());
        return null;
    }

    public String getName(){
        return "date";
    }
}
