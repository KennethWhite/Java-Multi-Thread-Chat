package logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Kenny on 11/13/2016.
 */
public class errorFilter implements java.util.logging.Filter {
    @Override
    public boolean isLoggable(LogRecord record) {
        if(record.getLevel().intValue() == Level.SEVERE.intValue()){
            //returns true if the level is of the highest priority(SEVERE)
            return true;
        }
        else {
            return false;
        }
    }
}
