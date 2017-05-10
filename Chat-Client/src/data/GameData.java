package data;

import java.util.ArrayList;

/**
 * Created by Kenny on 2/5/2017.
 */
public class GameData {

    private int processID;
    private Object data;

    public GameData(int pID, Object data){
        this.processID = pID;
        this.data = data;
    }

    public int getProcessID() {
        return processID;
    }

    public Object getData(){
        return data;
    }
}
