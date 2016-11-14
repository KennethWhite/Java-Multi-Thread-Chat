package commandP;

import java.io.PrintWriter;

/**
 * Created by Daric on 11/9/2016.
 */
public class UpTime implements Icommands {

    private long connTime;
    private PrintWriter out;

    public UpTime(PrintWriter out, long connTime){
        this.connTime = connTime;
        this.out = out;
    }

    public String perform(){
        long min = ((System.currentTimeMillis() - connTime) / 1000)/60;
        long sec = ((System.currentTimeMillis() - connTime) / 1000) - 60*min;
        out.printf("MESSAGE Server has been running for %d minutes %d seconds\n", min,sec);
        return null;
    }

    public String getName(){
        return "uptime";
    }
}
