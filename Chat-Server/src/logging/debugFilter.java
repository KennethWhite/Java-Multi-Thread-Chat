package logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Kenny on 11/13/2016.
 */
public class debugFilter implements Filter{
    @Override
    public boolean isLoggable(LogRecord record) {
        if (record.getLevel().intValue() == Level.FINER.intValue()) {
            //returns true if the level is of the level FINER
            return true;
        } else {
            return false;
        }

    }
}
