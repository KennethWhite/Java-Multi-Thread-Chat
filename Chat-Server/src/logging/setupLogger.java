package logging;

import server.Server;

import java.util.logging.*;


/**
 * Created by Kenny on 11/13/2016.

 */

public class SetupLogger {
    /*
    *
     * Filehandlers are connected to their respective log files, with APPEND set to TRUE
     * Filehandlers/Levels;
     *      Level.SEVERE    Writes to the error log, 'ErrorLog.xml'
     *      Level.INFO      Writes to general log, 'Log.xml'
     *      Level.FINER     Writes to the debug log, 'Debug.xml'
     *
     * All handlers are then added to the logger, which is returned
     */


    public static Logger startLogger(String name) {//starts a default logger for info, debug, error logs.
        try {
            if(name == null){
                throw new IllegalArgumentException("Parameter name null on call to startLogger!");
            }


            Logger ret = Logger.getLogger(name);
            FileHandler errOut = new FileHandler("./out/LogFiles/ErrorLog.txt", true);
            FileHandler genLog = new FileHandler("./out/LogFiles/Log.txt", true);
            FileHandler debugLog = new FileHandler("./out/LogFiles/Debug.txt", true);


            errOut.setFilter(new SFilter(Level.SEVERE));
            genLog.setFilter(new SFilter(Level.INFO));
            debugLog.setFilter(new SFilter(Level.FINER));

            ret.addHandler(errOut);
            ret.addHandler(genLog);
            ret.addHandler(debugLog);

            return ret;
        } catch (Exception ex) {
            System.out.println("Error creating log files");
            System.out.println(ex.getMessage() +"\n"); ex.printStackTrace();
            return null;
        }
    }

    //starts logger with filehandlers pointing to specified Files, boolean writing in append mode, and filtering at passed in levels.
    //eg creates a filehandler that writes to docs[0] in append[0] write mode and filters based on level at lvls[0];
    public static Logger startLogger(String name, String[] docs, Boolean[] append, Level[] lvls) {
        try {
            if(name == null){
                throw new IllegalArgumentException("Parameter name null on call to startLogger!");
            }
            if(docs.length != append.length || docs.length != lvls.length || append.length != lvls.length){
                throw new IllegalArgumentException("Length of parameter arrays do not match on call to startLogger()" );
            }

            Logger ret = Logger.getLogger(name);
            FileHandler temp;
            for(int i = 0; i < docs.length; i++){
                temp = new FileHandler(docs[i], append[i]);
                temp.setFilter(new SFilter(lvls[i]));
                ret.addHandler(temp);
            }

            return ret;
        } catch (Exception ex) {
            System.out.println("Error creating log files");
            System.out.println(ex.getMessage() +"\n"); ex.printStackTrace();
            return null;
        }
    }

}//end class
