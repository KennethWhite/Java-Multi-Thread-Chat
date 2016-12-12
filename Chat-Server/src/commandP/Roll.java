package commandP;

/**
 * Created by Daric on 11/6/2016.
 *
 * This class returns a random number between 0 and 99
 */
public class Roll implements Icommands{
    public String perform(){
        int var1 = (int)(Math.random() * 100);
        return "Rolling (0-99): " + var1 + ".";
    }

    @Override
    public String help(){
        return "/Roll - Returns a number between 0 and 99";
    }

    public String getName() {
        return "Roll";
    }
}
