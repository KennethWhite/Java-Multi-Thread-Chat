package client;

import client.display.ChatSceneController;
import logging.MyLogger;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.DataLine.Info;
import java.io.*;
import java.util.logging.Level;

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
    private boolean isGoodState;
    private AudioInputStream audioInStream;
    private long recTime;

    public Record() {

        wavFile = new File("ChatWavFiles");
        type = AudioFileFormat.Type.WAVE;
        format = new AudioFormat(8000.0f, 16, 1, true, true);
        recTime = 5000;
        try {
            if (!wavFile.exists()) {
                wavFile.mkdir();
            }
            isGoodState = true;
        }
        catch(SecurityException se){
            MyLogger.log(Level.SEVERE, se.getMessage(), se);
            isGoodState = false;
            return;
        }

    }

    public boolean state(){
        return this.isGoodState;
    }

    public void setTime(long time){
        this.recTime = time;
    }

    public void startRec(String title){ //TODO make button stop rec or time

        File newWavFile = new File(wavFile.getName() + "\\" + title);

        try {

            Thread stopper = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(recTime);//change time if needed
                        stopRec();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        MyLogger.log(Level.SEVERE, ie.getMessage(), ie);
                        isGoodState = false;
                        stopRec();
                    }
                }
            });

            mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
            mic.start();

            stopper.start();
            audioInStream = new AudioInputStream(mic);

            /*
            Cleanup.cleanAudioFiles(10);
             */

            AudioSystem.write(audioInStream, type, newWavFile);    //make output streams to everyone for live audio??
        }
        catch(LineUnavailableException lue){
            format = new AudioFormat(8000.0f, 16, 1, true, true);
            MyLogger.log(Level.SEVERE, lue.getMessage(), lue);
            isGoodState = false;
        }
        catch(IOException ioe){
            MyLogger.log(Level.SEVERE, ioe.getMessage(), ioe);
            isGoodState = false;
        }
    }

    public void stopRec(){
        mic.stop();
        mic.flush();
        mic.close();
        try{
            audioInStream.close();
        }
        catch(IOException ioe){
            MyLogger.log(Level.SEVERE, ioe.getMessage(), ioe);
            //TODO dont know if this matters
        }
    }

    public File getAudioFile(String fileName){

        if(fileName == null){
            //TODO if string is null
        }

        File temp = new File(wavFile.getName() + "\\" + fileName);

        if(!temp.exists()){
            //TODO if file does not exist anymore
        }

        return temp;
    }
}
