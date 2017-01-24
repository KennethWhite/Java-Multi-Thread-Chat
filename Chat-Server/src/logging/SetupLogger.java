package logging;


import java.io.IOException;

import java.util.logging.*;


import java.io.File;


public class SetupLogger {
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


    public static Logger startLogger(String name) {//starts a default logger for info, debug, error logs.
        try {
            if (name == null) {
                throw new IllegalArgumentException("Parameter name null on call to startLogger!");
            }


            File dir = new File("out/LogFiles");
            if(dir.mkdirs()){
                System.out.println("Directory out/LogFiles was created.");
            }

            File[] array = {
                    new File("./out/LogFiles/ErrorLog.log"),
                    new File("./out/LogFiles/Log.log"),
                    new File("./out/LogFiles/Debug.log"),
                    new File("./out/LogFiles/ErrorLog.log.lck"),
                    new File("./out/LogFiles/Log.log.lck"),
                    new File("./out/LogFiles/Debug.log.lck"),};



            for (int i = 0; i < array.length; i++) {
                try {
                    if (array[i].createNewFile()) {
                        System.out.println(array[i].getName() + " was created.");
                    }
                } catch (IOException ex) {
                    System.out.println("Unexpected error initializing " + array[i].getName());
                    System.out.println(ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
            }


            Formatter format = new SimpleFormatter();//uses default format
            Logger ret = Logger.getLogger(name);

            //this disables parent handlers in the root logger, prevents writing to console twice when logging Level.INFO
            // and higher levels
            ret.setUseParentHandlers(false);

            //creates file/console handlers
            FileHandler errOut = new FileHandler("./out/LogFiles/ErrorLog.log", true);//.rm here we should check if the file exists, if not make one
            FileHandler genLog = new FileHandler("./out/LogFiles/Log.log", true);
            FileHandler debugLog = new FileHandler("./out/LogFiles/Debug.log", true);
            Handler consoleHandler = new ConsoleHandler();//logs all Level.INFO by default

            errOut.setFilter(new SFilter(Level.SEVERE));
            genLog.setFilter(new SFilter(Level.INFO));
            debugLog.setFilter(new SFilter(Level.FINER));

            Handler handlerArray[] = new Handler[]{errOut, genLog, debugLog, consoleHandler};

            for(int i = 0; i < handlerArray.length; i ++){
                handlerArray[i].setFormatter(format);
                ret.addHandler(handlerArray[i]);
            }


            return ret;
        } catch (IOException ex) {
            System.out.println("Error creating log files");
            System.out.println(ex.getMessage() + "\n");
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            System.out.println("Error setting up logger");
            System.out.println(ex.getMessage() + "\n");
            ex.printStackTrace();
            return null;
        }
    }

    //starts logger with filehandlers pointing to specified Files, boolean writing in append mode, and filtering at passed in levels.
    //eg creates a filehandler that writes to docs[0] in append[0] write mode and filters based on level at lvls[0];
    public static Logger startLogger(String name, String[] docs, Boolean[] append, Level[] lvls) {
        try {
            if (name == null) {
                throw new IllegalArgumentException("Parameter name null on call to startLogger!");
            }
            if (docs.length != append.length || docs.length != lvls.length || append.length != lvls.length) {
                throw new IllegalArgumentException("Length of parameter arrays do not match on call to startLogger()");
            }

            Logger ret = Logger.getLogger(name);
            FileHandler temp;
            for (int i = 0; i < docs.length; i++) {
                temp = new FileHandler(docs[i], append[i]);
                temp.setFilter(new SFilter(lvls[i]));
                ret.addHandler(temp);
            }

            return ret;
        } catch (Exception ex) {
            System.out.println("Error creating log files");
            System.out.println(ex.getMessage() + "\n");
            ex.printStackTrace();
            return null;
        }
    }

}//end class
