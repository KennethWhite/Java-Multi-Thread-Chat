package commandP;

import java.io.PrintWriter;

/**
 * Created by Kenny on 12/9/2016.
 */
public class Help implements Icommands {
    PrintWriter out;
    public Help(PrintWriter out){this.out = out;}
    @Override
    public String perform(){ //returns the list of commands and their description in alphabetical order
        if(out == null){
            throw new IllegalArgumentException("Printwriter null on call to Help.perform()");
        }
        //TODO change to a single write one object socket is set up
        out.println("MESSAGE " + "*** Commands are not case-sensitive (/rolldice and /RollDice are equivalent) ***");
        out.println("MESSAGE " + "\t" + new CoinFlip().help());
        out.println("MESSAGE " + "\t" + new DateC(null).help());
        out.println("MESSAGE " + "\t" + new DiceRoll().help());
        out.println("MESSAGE " + "\t" + new Help(null).help());
        out.println("MESSAGE " + "\t" + new Roll().help());
        out.println("MESSAGE " + "\t" + new UpTime(null, 0).help());
        out.println("MESSAGE " + "*** End Help ***");
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
