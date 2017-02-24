package client;


import logging.SetupLogger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgGui extends JFrame implements ActionListener {

    //added********************
    private DataOutputStream dOut;
    private OutputStream audioOut;

    private PrintWriter out;
    private static final Logger LOGGER = SetupLogger.startLogger(Client.class.getName());
//panels
    private JFrame frame = new JFrame("KDC chat");
    private JPanel main = new JPanel();
    private JPanel buttons = new JPanel();
    private SettingsGui settings = new SettingsGui();

//fields
    private JTextField textField = new JTextField();
    private JTextArea messageArea = new JTextArea(20,80);



//buttons
    private JLabel Ideas = new JLabel("games tab?   Friends/groups tab?");//.rm
    private JButton saveConv_btn = new JButton("Save Conversation");
    private JButton settings_btn = new JButton("Settings");

    //added**********************
    private JButton recordVoice = new JButton("R"); //want image
    private JButton sendVoice = new JButton("S");//want image


    //ctor
    public ProgGui() {

        //initialize gui
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.settings.setVisible(false);
        this.frame.setVisible(true);

        messageArea.setBackground(Color.LIGHT_GRAY);
        messageArea.setLineWrap(true);
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        main.setBorder(new EmptyBorder(3,3,3,3));
        DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));

        //added******************
        recordVoice.addActionListener(this);//assuming this works as a general template
        sendVoice.addActionListener(this);//^^

        saveConv_btn.addActionListener(this);
        settings_btn.addActionListener(this);
        textField.addActionListener(this);

        buttons.add(Ideas);//.rm
        buttons.add(Box.createRigidArea(new Dimension(50, 0)));//.rm
        buttons.add(saveConv_btn);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));

        //added***************
        buttons.add(recordVoice);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));//assume this is correct
        buttons.add(sendVoice);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));//^^

        buttons.add(settings_btn);
        main.add(buttons);
        main.add(new JScrollPane(messageArea));
        main.add(textField);                            //adds input box to bottom of the frame

        frame.add(main);

        frame.pack();                                                              //ensures the content fits in the frame
    }


    @Override
    public void actionPerformed(ActionEvent e) {                                    //all button input for txt communication

        if (e.getSource() == settings_btn) {                //settings gui
            main.setVisible(false);
            buttons.setVisible(false);
            frame.add(settings);
            settings.setVisible(true);
        }
        else if (e.getSource() == textField) {          //sends input sends to server
            this.out.println(textField.getText());
            textField.setText("");
        }
        else if (e.getSource() == saveConv_btn) {           //saves conversationto file
            saveConv();
        }
        else if (e.getSource() == recordVoice)  {//added*******************
            voiceLine();//??????? move ?????
        }
        else if (e.getSource() == sendVoice){//^^
            sendLine();//?????? move????
        }
    }//end actionListener

//maybe move to LoadSave class*******************************
    private void sendLine(){

        File audioFile = new File("RecentAudio.wav");


        try {
            FileInputStream fin = new FileInputStream(audioFile);
            OutputStream os = audioOut;
            byte buffer[] = new byte[2048];//weird array thing*
            int count;
            while((count = fin.read(buffer)) != -1){
                os.write(buffer, 0, count);
            }
        }
        catch(Exception e){
            //TODO
        }

    }//end sendLine


//maybe move to LoadSave class**********************************************
    private void voiceLine() {

        final Record line = new Record();
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);//change time if needed
                } catch (InterruptedException ie) {
                    //TODO
                }
                line.stopRec();
            }
        });
        stopper.start();
        line.startRec();

    }


//maybe move to LoadSave class
    private void saveConv(){
        String fileName = JOptionPane.showInputDialog("Enter file name");
        if(fileName != null && !fileName.equals("")) {
            try {
                File file1 = new File("SavedConversations\\");                                       //made it save to central folder
                file1.mkdirs();
                File file2 = new File(file1, fileName + ".txt");
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                BufferedWriter temp = new BufferedWriter(new FileWriter(file2, false));
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String convo = getConvo();
                temp.write("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n" +
                            convo + "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + (dateFormat.format(new Date())));
                temp.close();
            } catch (Exception ex) {
                System.out.println("Error in save conversation Event Handler");
            }
        }
    }

    private String getConvo() {
        String temp;
        temp = messageArea.getText();
        if (temp == null) {
            temp = "";
        }
        return temp;
    }


//getters and setters
    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public void setMessageArea(JTextArea messageArea) {
        this.messageArea = messageArea;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void setdOut(DataOutputStream dOut)  {this.dOut = dOut;}

    public void setAudioOut(OutputStream audioOut){this.audioOut = audioOut;}






 //settings class
    private class SettingsGui extends JPanel implements ActionListener{

//create labels and buttons
        JButton back_btn = new JButton("back");

        JLabel setting1 = new JLabel("Setting 1");
        JRadioButton setting1_btn = new JRadioButton();

        JLabel setting2 = new JLabel("Setting 2");
        JSpinner setting2_btn = new JSpinner();

         JLabel setting3 = new JLabel("Setting 3");
         JCheckBox setting3_btn = new JCheckBox("");

        JLabel setting4 = new JLabel("Setting 4");
        JToggleButton setting4_btn = new JToggleButton("Toggle");

        JLabel setting5 = new JLabel("Setting 5");
        String[] moreSettings = {"option1","option2","another one","DJ Khaled"};//.rm
        JComboBox setting5_btn = new JComboBox(moreSettings);

        JButton save_btn = new JButton("save");

//ctor
        public SettingsGui(){
            //settings.setVisible(true);
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            GridLayout layout = new GridLayout(7,2);    //row,col
            layout.setHgap(500);
            layout.setVgap(10);
            this.setLayout(layout);

            this.setFont(new Font("Courier",Font.ITALIC,20));                                                 //Dont know how to change font, but can change modifier and size

//add events
            this.back_btn.addActionListener(this);
            this.setting1_btn.addActionListener(this);
            this.setting3_btn.addActionListener(this);
            this.setting4_btn.addActionListener(this);
            this.setting5_btn.addActionListener(this);
            this.save_btn.addActionListener(this);

//add items to panel
            this.add(back_btn);
            this.add(new JLabel());
            this.add(setting1);
            this.add(setting1_btn);
            this.add(setting2);
            this.add(setting2_btn);
            this.add(setting3);
            this.add(setting3_btn);
            this.add(setting4);
            this.add(setting4_btn);
            this.add(setting5);
            this.add(setting5_btn);
            this.add(new JLabel());
            this.add(save_btn);

            loadSettings();
        }


//applies saved values to buttons
        private void loadSettings(){
            try {

                LoadSaveObject loadingObj = new LoadSaveObject();
                Properties usrSettings = loadingObj.getUserSettingsProperty();
                if(usrSettings.isEmpty()){
                    return;
                }
            //for booleans
                setting1_btn.setSelected(Boolean.valueOf(usrSettings.getProperty("setting 1")));
                setting3_btn.setSelected(Boolean.valueOf(usrSettings.getProperty("setting 3")));
                setting4_btn.setSelected(Boolean.valueOf(usrSettings.getProperty("setting 4")));

                    //for Strings
                    setting5_btn.setSelectedItem(usrSettings.getProperty("setting 5"));

                    //for ints
                    //setting2_btn.setValue(Integer.parseInt(usrSettings.getProperty("setting 2")));

                    if(!usrSettings.isEmpty()){
                    System.out.println("The user settings have been successfully loaded from file");//.rm  for debug
                   // LOGGER.log(Level.CONFIG, "User settings were loaded from file:\n");
                }
            }
            catch(Exception e){
                LOGGER.log(Level.SEVERE, "Error occurred loading preferences:\n" + e.getMessage() , e);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            LoadSaveObject settingsSave = new LoadSaveObject();

            if(e.getSource() == back_btn){
                main.setVisible(true);
                buttons.setVisible(true);
                this.setVisible(false);
            }
            else if(e.getSource() == setting1_btn){
                settingsSave.updateUserSettings("setting 1",String.valueOf(setting1_btn.isSelected()));
            }
            else if(e.getSource() == setting3_btn){
                settingsSave.updateUserSettings("setting 3",String.valueOf(setting3_btn.isSelected()));
            }
            else if(e.getSource() == setting4_btn){
                settingsSave.updateUserSettings("setting 4",String.valueOf(setting4_btn.isSelected()));
            }
            else if(e.getSource() == setting5_btn){
                //System.out.printf("Setting number 5 is : %s\n",setting5_btn.getSelectedItem());//.rm                  the event for JComboBox obj happens as soon as gui is displayed. data is passed to properties obj in  when save button is pressed
            }
            else if(e.getSource() == save_btn) {
                try {
                    settingsSave.updateUserSettings("setting 2",String.valueOf(setting2_btn.getValue()));
                    settingsSave.updateUserSettings("setting 5",setting5_btn.getSelectedItem());
                    //if(settingsSave.saveSettings())                                                                                                         //.rmwhen this method is called it saves all the settings to a file
                        JOptionPane.showConfirmDialog(null, "Save successful", "Your preferences have been saved", JOptionPane.CLOSED_OPTION);      //.rm need better icon obj
                    //else
                        JOptionPane.showConfirmDialog(null, "Save unsuccessful", "An error has occurred. Your preferences have not been saved );", JOptionPane.CLOSED_OPTION);

                }
                catch(Exception ex){
                    messageArea.append("WARNING: Error occurred saving to file: \n" + ex.getMessage());
                    LOGGER.log(Level.SEVERE, "Error occurred writing to file:\n" + ex.getMessage() , ex);
                }
            }
        }
    }
}



