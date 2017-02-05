package client;

/**
 * Created by Kenny on 1/28/2017.
 */


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.*;
import java.io.*;
import javax.sound.sampled.*;

public class AudioEffects {
    static ArrayList sounds = loadAudio();
    private static boolean soundEnabled = true;//will be set true/false in gui
    private static float masterGain = -17.0f; // Reduces volume by 35 decibels (note: decibels are logarithmic in scale)

    public static void play(String name) {
        try {
            if(isSoundEnabled()) {
                File audioFile = new File("./sounds/" + name); //opens the specified file

                if (!sounds.contains(name)) {//checks to see if the string is a valid file
                    throw new FileNotFoundException("File " + name + " was not found in ./sounds.");
                }
                AudioInputStream stream;
                AudioFormat format;
                DataLine.Info info;
                Clip clip;
                FloatControl gain;

                stream = AudioSystem.getAudioInputStream(audioFile);
                format = stream.getFormat();
                info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                //returns a control for the master gain of the clip
                gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(masterGain); //sets the gain(volume)

                clip.start();
            }

        } catch (Exception e) {
            Client.getLOGGER().log(Level.FINER, e.getMessage(), e);
        }
    }


    //method shell to read all .wav file names from /sound folder
    private static ArrayList<String> loadAudio(){
        File folder = new File("./sounds");
        File[] listOfFiles = folder.listFiles();
        String temp;
        ArrayList sounds = new ArrayList(25);

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                temp = listOfFiles[i].getName();
                if(temp.substring(temp.length()-4).equals(".wav")){
                    sounds.add(listOfFiles[i].getName());

                }
            } else if (listOfFiles[i].isDirectory()) {
                //do nothing
            }
        }
        return sounds;
    }


    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public static float getMasterGain() {
        return masterGain;
    }

    public static void setMasterGain(float masterGain) {
        AudioEffects.masterGain = masterGain;
    }
}