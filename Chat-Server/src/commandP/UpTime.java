package commandP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by Daric on 11/9/2016.
 */
public class UpTime implements Icommands {

    private long connTime;
    private ObjectOutputStream out;

    public UpTime(ObjectOutputStream out, long connTime){
        this.connTime = connTime;
        this.out = out;
    }

    public String perform(){
        long min = ((System.currentTimeMillis() - connTime) / 1000)/60;
        long sec = ((System.currentTimeMillis() - connTime) / 1000) - 60*min;
        long hour = min / 60;
        min = min %60;
        String temp = "MESSAGE Server has been running for " + hour + " hours " + min + " minutes " + sec + " seconds\n";
        try {
            out.writeObject(temp);
        }
        catch(IOException ioe){
            //TODO
        }
        return null;
    }

    @Override
    public String help(){
        return "/UpTime - Returns how long the server has been up for.";
    }

    public String getName(){
        return "uptime";
    }
}
