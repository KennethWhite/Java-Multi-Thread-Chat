package client;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.DataLine.Info;
import java.io.*;
import java.lang.annotation.Target;

/**
 * Created by Daric on 11/27/2016.
 *
 * sender class/object? that accepts a record object
 *
 */
public class Record {

    private Type type;
    private File wavFile;
    private TargetDataLine mic;
    private AudioFormat format;
    private Info info;
    private AudioInputStream audioInStream;

    public Record(){
        try{
            type = AudioFileFormat.Type.WAVE;
            wavFile = new File("RecentAudio.wav");
            format = new AudioFormat(8000.0f, 16, 1, true, true);
            if(!wavFile.exists()){
                wavFile.createNewFile();
            }
            //info = new DataLine.Info(TargetDataLine.class, format);
        }
        catch(Exception e){
            //TODO
        }
    }

    public void startRec(){
        try {
            //if (!AudioSystem.isLineSupported(info)) {
            //TODO...exit
            //}

            mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
            mic.start();

            audioInStream = new AudioInputStream(mic);

            AudioSystem.write(audioInStream, type, wavFile);//make output streams to everyone for live audio??

        }
        catch(Exception e){
            //TODO
        }
    }

    public void stopRec(){
        mic.stop();
        mic.flush();
        mic.close();
        try{
            audioInStream.close();
        }
        catch(Exception e){
            //TODO
        }
    }
}
