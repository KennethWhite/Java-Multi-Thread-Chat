package client;

import logging.SetupLogger;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadSave { //./rm maybe make non static so doesn't have to be public
    //.rm there could be different Properties objects for different applications like for games or server/client info
    private static Properties userPref = loadPref();
    private static final Logger LOGGER = SetupLogger.startLogger(LoadSave.class.getName());       //.rm not used until start logger FNF error fixed

//updates the properties obj
    public static boolean updatePref(Object obj, Object objData){
        boolean isSuccessful = false;
        userPref.put(obj,objData);
        return isSuccessful;

    }

//saves preferences to a file
    public static boolean savePref(){
        boolean saveSuccessful = false;
        try {
            FileOutputStream fout = new FileOutputStream(getPrefFile());
           userPref.store(fout,"User Properties");
            fout.close();
            saveSuccessful = true;
        }
         catch(Exception e){
             LOGGER.log(Level.SEVERE, "An unexpected error occurred while saving to user preferences file: " + e.getMessage(), e);
             System.out.println("An unexpected error occurred while saving to user preferences file: " + e.getMessage());
             e.printStackTrace();//.rm
         }
        return saveSuccessful;
    }

//loads preferences from file
    public static Properties loadPref(){
        Properties usrPref = new Properties();
        try{
            FileInputStream fin = new FileInputStream(getPrefFile());
            usrPref.load(fin);
            fin.close();
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while loading from user preferences file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while loading from user preferences file: " + e.getMessage());
            e.printStackTrace();//.rm
        }
        return usrPref;
    }

//creates or returns the users preferences.dat file
    private static File getPrefFile(){
        File userDataDirectory = new File("usrPreferences");
        File userData = new File(userDataDirectory,"Pref.dat");             //.rm open as txt
        try {
            if(!userData.exists()){
                    userDataDirectory.mkdir();
                    userData.createNewFile();
                }
        }

        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving user preferences file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while retrieving user preferences file: " + e.getMessage());
        }

       return userData;
    }

    public static Properties getPref(){
        return userPref;
    }
}
