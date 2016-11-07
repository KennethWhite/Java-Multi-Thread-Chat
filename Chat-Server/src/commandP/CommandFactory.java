package commandP;

import java.io.PrintWriter;

/**
 * Created by Daric on 11/6/2016.
 *
 * This is a factory class for distributing 'Icommand'
 * objects depending on the String input
 */
public class CommandFactory {
    public Icommands getCommand(String input){

        Icommands temp = null;  //initialize temp for which the icommand will be kept

        switch(input){
            case "/flip": temp = new CoinFlip();
                          break;
            case "/roll": temp = new Roll();
                          break;
            case "/diceroll": temp = new DiceRoll();
                          break;
            default: temp = new NullCommand();
                          break;
        }

        return temp;
    }
}
