package client;

import logging.SetupLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadSave {
    private static Properties userPref = new Properties();
    //private static final Logger LOGGER = SetupLogger.startLogger(Save.class.getName());       //not used until start logger error fixed

    public static boolean updatePref(Object obj, Object objData){
        boolean isSuccessful = false;
        userPref.put(obj,objData);
        return isSuccessful;

    }

    public static boolean savePref(){
        boolean saveSuccessful = false;
        try {
            FileOutputStream fout = new FileOutputStream(getPrefFile());
           userPref.store(fout,"Properties");
            fout.close();
            saveSuccessful = true;
        }
         catch(Exception e){
             //LOGGER.log(Level.SEVERE, "An unexpected error occurred while saving to user preferences file: " + e.getMessage(), e);
             System.out.println("An unexpected error occurred while saving to user preferences file: " + e.getMessage());
             e.printStackTrace();
         }
        return saveSuccessful;
    }

    private static File getPrefFile(){
        File userDataDirectory = new File("usrData");
        File userData = new File(userDataDirectory,"usrPref.dat");
        try {
            if(!userData.exists()){
                    userDataDirectory.mkdir();
                    userData.createNewFile();
                }
        }

        catch (Exception e) {
            //LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving user preferences file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while retrieving user preferences file: " + e.getMessage());
        }

       return userData;
    }
}
