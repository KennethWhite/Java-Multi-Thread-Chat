package utils;

import commandP.CommandFactory;
import commandP.Icommands;

import java.io.ObjectOutputStream;

/**
 * Created by Daric on 5/11/2017.
 */
public class Parsing {


    public static boolean shouldParse(String s) {
        if (!s.equals(null) && !s.isEmpty() && s.substring(0, 1).equals("/")) {
            return true;
        }
        return false;
    }

    //Method will be used to perform user commands
    public static String parse(String s, ObjectOutputStream objectOut, long timeConnection){

        String temp = s.toLowerCase();
        CommandFactory cF = new CommandFactory();   //could make this a Handler attribute***
        Icommands curCommand = cF.getCommand(temp, objectOut, timeConnection);

        return curCommand.perform();

    }

}
