package logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Kenny on 11/13/2016.
 */
public class infoLogFilter implements Filter{
        @Override
        public boolean isLoggable(LogRecord record) {
            if(record.getLevel().intValue() == Level.INFO.intValue()){
                //returns true if the level is INFO
                return true;
            }
            else {
                return false;
            }
        }
}
