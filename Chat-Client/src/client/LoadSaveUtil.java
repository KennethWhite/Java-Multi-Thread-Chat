package client;

/*
       The purpose of this class is load and save all data written to file
       if the file does not exist, it is created. if no data is in the file the value is retrieved as null
 */

import logging.SetupLogger;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadSaveUtil {

    private static Logger LS_LOGGER = Logger.getLogger("client.Client.LS_LOGGER");
    //the strings are just used for reference
    public static String serverFilename = "savedServers.txt";
    public static String userSettingFilename = "userPref.txt";



    /**
     * retrieves the property object associated with the file name. if it doesn't exist, create one
     * @param filename  The filename to the file that the properties object is saved
     * @return returns the Properties object in the file, or an empty one
     */
    public static Properties getPropertyObject(String filename) {
        Properties savedProperty = new Properties();
        ;
        try {
            
            FileInputStream fin = new FileInputStream(getFile(filename));
            savedProperty.load(fin);
            fin.close();
            LS_LOGGER.log(Level.FINER, "Test logger");
        } catch (Exception e) {
            LS_LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving " + filename +  " file property object: " + e.getMessage(), e);
        }
        return savedProperty;
    }




    /**
     * saves the properties object to the file
     * @param objToSave the properties object to the file
     * @param filename location to be saved
     * @return save successful
     */
    public static boolean savePropertyObject(Properties objToSave, String filename) {
        boolean saveSuccessful = false;
        try {
            FileOutputStream fout = new FileOutputStream(getFile(filename));                              //retrieves saved values or empty saved Servers.txt file and creates an output stream
            objToSave.store(fout, "Saved Servers");
            fout.close();
            saveSuccessful = true;
        } catch (Exception e) {
            LS_LOGGER.log(Level.SEVERE, "An unexpected error occurred while saving " + filename +  " file property object: " + e.getMessage(), e);
        }
        return saveSuccessful;
    }





    /**
     * returns file of string passed in, if it doesn't exist it is created
     * @param filename location of file
     * @return file
     */
    private static File getFile(String filename) {
        File dataDirectory = new File("Data");
        File fileToRetrieve = new File(dataDirectory, filename);
        try {
            if (!fileToRetrieve.exists()) {                                         //create new file if FNF
                dataDirectory.mkdir();
                fileToRetrieve.createNewFile();
                System.out.println("new " + filename + " created");
            }
        } catch (Exception e) {
            LS_LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving " + filename +  " file: " + e.getMessage(), e);
        }
        return fileToRetrieve;
    }

}
