package server;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Daric on 5/11/2017.
 */
public class Audio implements Runnable {

    private Socket audioS;
    private ArrayList<OutputStream> audioWriters;

    public Audio(Socket audioS, ArrayList<OutputStream> audioWriters){
        this.audioS = audioS;
        this.audioWriters = audioWriters;
    }

    @Override
    public void run() {

        boolean looper = true;

        while (looper) {

            try {
                //??make the declaration outside the loop??
                InputStream iin = new BufferedInputStream(audioS.getInputStream());
                AudioInputStream ais = AudioSystem.getAudioInputStream(iin);    //waits on this line for incoming audio
                ais.mark(100000);
                for (OutputStream outs : audioWriters) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outs);
                    ais.reset();
                }
            } catch (Exception e) {
                //LOGGER.log(Level.SEVERE, e.getMessage(), e);TODO
                //TODO close stuff
                looper = false;
            }
            finally {
                //finalmethod TODO
                //TODO close stuff
            }

        }

    }//end run

}//end class
