package logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;


public class MyLogger {
    /*
    *
     * Filehandlers are connected to their respective log files, with APPEND set to TRUE
     * Filehandlers/Levels;
     *      Level.SEVERE    Writes to the error log, 'ErrorLog.txt'
     *      Level.INFO      Writes to general log, 'Log.txt'
     *      Level.FINER     Writes to the debug log, 'Debug.txt'
     *
     * All handlers are then added to the logger, which is returned
     */

    static Logger logger;
    public Handler[] fileHandlers;
    Formatter plainText;

    /**
     * A CTOR that sets up the logger with handlers/filters
     */
    public MyLogger() {//starts a default logger for info, debug, error logs.
        try {
            File dir = new File("out/LogFiles");
            if (dir.mkdirs()) {
                System.out.println("Directory out/LogFiles was created.");
            }

            Formatter format = new SimpleFormatter();//uses default format
            logger = Logger.getLogger("");//creates root logger

            //this disables parent handlers in the root logger, prevents writing to console twice when logging Level.INFO
            // and higher levels
            logger.setUseParentHandlers(false);

            //creates file/console handlers
            FileHandler errOut = new FileHandler("./out/LogFiles/ErrorLog.log", true);
            FileHandler genLog = new FileHandler("./out/LogFiles/Log.log", true);
            FileHandler debugLog = new FileHandler("./out/LogFiles/Debug.log", true);
            Handler consoleHandler = new ConsoleHandler();//logs all Level.INFO by default

            errOut.setFilter(new SFilter(Level.SEVERE));
            genLog.setFilter(new SFilter(Level.INFO));
            debugLog.setFilter(new SFilter(Level.FINER));

            Handler handlerArray[] = new Handler[]{errOut, genLog, debugLog, consoleHandler};

            for (int i = 0; i < handlerArray.length; i++) {
                handlerArray[i].setFormatter(format);
                logger.addHandler(handlerArray[i]);
            }

        } catch (IOException ex) {
            System.out.println("Error creating log files");
            System.out.println(ex.getMessage() + "\n");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error setting up logger");
            System.out.println(ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    /**
     *Insures the logger has been created, returns a reference to this logger.
     * @return a reference this classes logger
     */
    private static Logger getLogger(){
        if(logger == null){
            new MyLogger();//call CTOR for logger
            return logger;
        }
        else{
            return logger;
        }
    }

    /**
     * Overloaded method that calls default log method on this classes Logger
     * @param lvl LEVEL of the log
     * @param msg String message to log
     * @param objs Array of Objects to be added to log
     */
    public static void log(Level lvl, String msg, Object objs){
        if(msg == null && objs == null){
            System.out.println("Error, null params on call to MyLogger.log()");
        }
        getLogger().log(lvl, msg, objs);
    }
    /**
     * Overloaded method that calls default log method on this classes Logger
     * @param lvl LEVEL of the log
     * @param msg String message to log
     */
    public static void log(Level lvl, String msg){
        if(msg == null){
            System.out.println("Error, null params on call to MyLogger.log()");
        }
        getLogger().log(lvl, msg);
    }

    /**
     * This method properly closes the Handlers of logger
     */
    public static void closeLogger(){
        if(logger != null){
            Handler[] handlers = logger.getHandlers();
            logger.log(Level.INFO, "User exit: ", handlers);
            for(int i = 0; i < handlers.length; i++){
                handlers[i].close();
            }
        }

    }
}