package data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Kenny on 2/5/2017.
 */
public class Data {

    public static boolean sendData(Socket s, ArrayList data){
        if(s == null || data == null){
            throw new NullPointerException("Socket or data null on call to sendData()");
        }
        try {
            ObjectOutputStream dataOut = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream dataIn = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));

            dataOut.writeObject(data);;
            dataOut.flush();
            return true;
        }
        catch(IOException e){
            return false;
        }

    }

}
