package commandP;

import java.io.PrintWriter;

/**
 * Created by Daric on 11/7/2016.
 */
public class DateC implements Icommands {

    private PrintWriter out;

    public DateC(PrintWriter tout){
        this.out = tout;
    }

    public String perform(){
        return "";
    }


    public String getName(){
        return "Date";
    }
}
