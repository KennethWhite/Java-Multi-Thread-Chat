package commandP;

/**
 * Created by Daric on 11/6/2016.
 *
 * DiceRoll command which returns a number between 2 and 12
 * to simulate a dice roll.
 */
public class DiceRoll implements Icommands{

    /**
     * Performs the random roll of dice.
     *
     * @return
     */
    public String perform(){
        int var1 = ((int)(Math.random() * 10) +2);
        if(var1 == 2){
            return "Rolling two dice: Roll is " + var1 + ", snake eyes!";
        }
        else if(var1 == 12){
            return "Rolling two dice: Roll is " + var1 + ", lucky!";
        }
        return "Rolling two dice: Roll is " + var1 + ".";
    }

    /**
     * Displays what happens when the DiceRoll command is used.
     *
     * @return
     */
    @Override
    public String help(){
        return "/DiceRoll - Simulates rolling a pair of dice, returns a number between 2 and 12.";
    }

    /**
     * retuns 'DiceRoll'
     *
     * @return
     */
    public String getName(){
        return "DiceRoll";
    }
}
