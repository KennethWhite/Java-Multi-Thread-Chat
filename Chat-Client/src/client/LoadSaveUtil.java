package client;

/*
       The purpose of this class is load and save all data written to file
       if the file does not exist, it is created. if no data is in the file the value is retrieved as null
       I am definitely going to improve this
 */

import logging.SetupLogger;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadSaveUtil {

    private static Logger LOGGER = SetupLogger.startLogger(LoadSaveUtil.class.getName());
    //the strings are just used for reference
    public static String serverFilename = "savedServers.txt";
    public static String userSettingFilename = "userPref.txt";



//retrieves the property object associated with the file name
    public static Properties getPropertyObject(String filename) {
        Properties savedProperty = new Properties();
        ;
        try {
            
            FileInputStream fin = new FileInputStream(getFile(filename));
            savedProperty.load(fin);
            fin.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving " + filename +  " file property object: " + e.getMessage(), e);
        }
        return savedProperty;
    }


//saves the property object to the file
    public static boolean savePropertyObject(Properties objToSave, String filename) {
        boolean saveSuccessful = false;
        try {
            FileOutputStream fout = new FileOutputStream(getFile(filename));                              //retrieves saved values or empty saved Servers.txt file and creates an output stream
            objToSave.store(fout, "Saved Servers");
            fout.close();
            saveSuccessful = true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while saving " + filename +  " file property object: " + e.getMessage(), e);
        }
        return saveSuccessful;
    }



//creates or returns the file of the string passed
    private static File getFile(String filename) {
        File dataDirectory = new File("Data");
        File userData = new File(dataDirectory, filename);
        try {
            if (!userData.exists()) {                                         //create new file if FNF
                dataDirectory.mkdir();
                userData.createNewFile();
                System.out.println("new " + filename + " created");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving " + filename +  " file: " + e.getMessage(), e);
        }
        return userData;
    }

}
