package logging;

import server.Server;

import java.util.logging.*;


/**
 * Created by Kenny on 11/13/2016.

 */

public class setupLogger {
    /*
    *
     * Filehandlers are connected to their respective log files, with APPEND set to TRUE
     * Filehandlers update their filters like so;
     *      Level.SEVERE    Writes to the error log, 'ErrorLog.txt'
     *      Level.INFO      Writes to general log, 'Log.txt'
     *      Level.FINER     Writes to the debug log, 'Debug.txt'
     *
     * All handlers are then added o the logger, which is returned
     */
    public static Logger startLogger(String name) {
        try {
            if(name == null){
                throw new IllegalArgumentException("Parameter name null on call to startLogger!");
            }
            Logger ret = Logger.getLogger(Server.class.getName());
            FileHandler errOut = new FileHandler("./out/LogFiles/ErrorLog.txt", true);
            FileHandler genLog = new FileHandler("./out/LogFiles/Log.txt", true);
            FileHandler debugLog = new FileHandler("./out/LogFiles/Debug.txt", true);
            errOut.setFilter(new errorFilter());
            genLog.setFilter(new infoLogFilter());
            debugLog.setFilter(new debugFilter());
            ret.addHandler(errOut);
            ret.addHandler(genLog);
            ret.addHandler(debugLog);
            return ret;
        } catch (Exception ex) {
            System.out.println("Error creating log files");
            return Logger.getLogger(Server.class.getName());
        }
    }
}
