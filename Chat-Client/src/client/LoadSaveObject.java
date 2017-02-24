package client;

/*
       The purpose of this class is load and save all data written to file
       if the file does not exist, it is created. if no data is in the file the value is retrieved as null
       I am definitely going to improve this
 */

import logging.SetupLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadSaveObject {

    private Properties userSettings;
    private Properties savedServers;
    private Logger LOGGER;


    public LoadSaveObject() {
        this.userSettings = loadSettings();
        this.savedServers = loadServers();
        this.LOGGER = SetupLogger.startLogger(LoadSaveObject.class.getName());
    }


/////////////////////////////////////////////////////////////////////////////////////load save servers

//adds the object passed to the properties object
    public void updateSavedServers(Object obj, Object objData){
        savedServers.put(obj,objData);
    }


//saves the values in the properties object to a file
    public boolean saveServerList(){
        boolean saveSuccessful = false;
        try {
            FileOutputStream fout = new FileOutputStream(getUserFile("savedServers.txt"));                              //retrieves saved values or empty saved Servers.txt file and creates an output stream
            savedServers.store(fout,"Saved Servers");
            fout.close();
            saveSuccessful = true;
        }
        catch(Exception e){
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while saving to saved servers file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while saving to saved servers file: " + e.getMessage());
        }
        return saveSuccessful;
    }


    public boolean removeServer(String server){
        boolean wasSuccessful = false;
        if (this.savedServers.contains(server)) {
            this.savedServers.remove(server);
            System.out.println("removed " + server);
            wasSuccessful = true;
        }
        return wasSuccessful;
    }


//loads servers from file and returns array list
    public Properties loadServers(){
        Properties temp = new Properties();
        try{
            FileInputStream fin = new FileInputStream(getUserFile("savedServers.txt"));
            temp.load(fin);
            fin.close();
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while loading from saved servers file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while loading from saved servers file: " + e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }


//loads servers from file and returns array list
    public ArrayList<String> getServers(){
        ArrayList<String> loadedServers = new ArrayList<String>();
        Collection serverKeys = savedServers.values();
        for(Object key : serverKeys){
            loadedServers.add(0,(String) key);
        }

        return loadedServers;
    }


//creates or returns the file of the string passed
    private File getUserFile(String filename){
        File dataDirectory = new File("Data");
        File userData = new File(dataDirectory, filename);
        try {
            if(!userData.exists()){                                         //create new file if FNF
                dataDirectory.mkdir();
                userData.createNewFile();
                System.out.println("new " + filename + " created");
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving user preferences file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while retrieving user preferences file: " + e.getMessage());
        }
        return userData;
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////// User settings and loading methods

    public String getUserName(){
        return userSettings.getProperty("USERNAME");
    }

    public void setUserName(String name){
        userSettings.put("USERNAME",name);
        saveSettings();
    }

    public void updateUserSettings(Object obj, Object objData){                                                         //updates the settings property object
        userSettings.put(obj,objData);
    }


    public boolean saveSettings(){                                                                                      //saves settings to a file
        boolean saveSuccessful = false;
        try {
            FileOutputStream fout = new FileOutputStream(getUserSettingsFile());
            userSettings.store(fout,"User Settings");
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



    public Properties loadSettings(){                                                                                   //loads Settings from file
        Properties usrPref = new Properties();
        try{
            FileInputStream fin = new FileInputStream(getUserSettingsFile());
            usrPref.load(fin);
            fin.close();
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while loading from user preferences file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while loading from user preferences file: " + e.getMessage());
            e.printStackTrace();
        }
        return usrPref;
    }


    private File getUserSettingsFile(){                                                                                  //creates or returns the users preferences.dat file
        File userDataDirectory = new File("Data");
        File userData = new File(userDataDirectory,"userSettings.txt");
        try {
            if(!userData.exists()){
                    userDataDirectory.mkdir();
                    userData.createNewFile();
                    System.out.println("new settings file created");
                }
        }

        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while retrieving user preferences file: " + e.getMessage(), e);
            System.out.println("An unexpected error occurred while retrieving user preferences file: " + e.getMessage());
        }

       return userData;
    }


    public Properties getUserSettingsProperty(){
        return userSettings;
    }
}
