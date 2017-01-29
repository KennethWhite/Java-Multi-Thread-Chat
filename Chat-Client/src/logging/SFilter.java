package logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Kenny on 11/15/2016.
 */
public class SFilter implements Filter {


        private Level level;


        SFilter(Level var){
            if(var == null){
                throw new IllegalArgumentException("Level param null on call to CTOR");
            }
            this.level = var;
        }

        public void updateFilter(Level in){
            if(in == null){
                throw new IllegalArgumentException("Level param null on call to updateFilter");
            }

            this.level = in;
        }

        @Override
        public boolean isLoggable(LogRecord record) {
            //returns true if the level is of the highest priority(SEVERE)
            return record.getLevel().equals(this.level);
        }
    }


