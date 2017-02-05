package data;

import java.util.ArrayList;

/**
 * Created by Kenny on 2/5/2017.
 */
public class Message {

    String msg;
    ArrayList data;

    public Message(String t, ArrayList data){
        if(t == null || data == null){
            throw new NullPointerException("Parameter null on call to Message ctor. " + "String: " + t + ", Data: "+ data.toString());
        }
        msg = t;
        this.data = data;
    }
    public Message(String t){
        if(t == null ){
            throw new NullPointerException("String null on call to Message ctor." );
        }
        msg = t;

    }
    public Message(ArrayList data){
        if(data == null){
            throw new NullPointerException("Data null on call to Message ctor.");
        }
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList getData() {
        return data;
    }
}
