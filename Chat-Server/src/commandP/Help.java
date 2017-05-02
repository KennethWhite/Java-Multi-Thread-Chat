package commandP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by Kenny on 12/9/2016.
 */
public class Help implements Icommands {
    ObjectOutputStream out;
    public Help(ObjectOutputStream out){this.out = out;}
    @Override
    public String perform(){ //returns the list of commands and their description in alphabetical order
        if(out == null){
            throw new IllegalArgumentException("Printwriter null on call to Help.perform()");
        }
        //TODO change to a single write one object socket is set up
        try {
            out.writeObject("MESSAGE " + "*** Commands are not case-sensitive (/rolldice and /RollDice are equivalent) ***");
            out.writeObject("MESSAGE " + "\t" + new CoinFlip().help());
            out.writeObject("MESSAGE " + "\t" + new DateC(null).help());
            out.writeObject("MESSAGE " + "\t" + new DiceRoll().help());
            out.writeObject("MESSAGE " + "\t" + new Help(null).help());
            out.writeObject("MESSAGE " + "\t" + new Roll().help());
            out.writeObject("MESSAGE " + "\t" + new UpTime(null, 0).help());
            out.writeObject("MESSAGE " + "*** End Help ***");
        }
        catch(IOException ioe){
            //TODO
        }
        return null;
    }

    @Override
    public String help(){
        return "/Help - The help command will list descriptions of all registered commands.";
    }

    @Override
    public String getName() {
        return "Help";
    }
}
