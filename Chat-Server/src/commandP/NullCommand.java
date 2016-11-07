package commandP;

/**
 * Created by Daric on 11/6/2016.
 */
public class NullCommand implements Icommands{

    public String perform() {
        return null;
    }

    public String getName(){
        return "NullCommand";
    }
}
