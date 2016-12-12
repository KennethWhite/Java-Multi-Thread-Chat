package commandP;
/**
 * Created by Daric on 11/6/2016.
 *
 * This class simulates a coin flip
 */
public class CoinFlip implements Icommands {
    public String perform(){

        int var1 = (int)(Math.random()*100);
        if(var1 % 2 == 0){
            return "Flipping a coin: Heads.";
        }
        else{
            return "Flipping a coin: Tails.";
        }

    }
    @Override
    public String help(){
        return "/CoinFlip - Simulates flipping a coin, will return either heads or tails.";
    }

    public String getName(){

        return "flip";
    }
}
