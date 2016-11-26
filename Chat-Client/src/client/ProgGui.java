package client;


import logging.SetupLogger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgGui extends JFrame implements ActionListener {
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
    private JButton settings_btn = new JButton("Setings");


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
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));

        saveConv_btn.addActionListener(this);
        settings_btn.addActionListener(this);
        textField.addActionListener(this);

        buttons.add(Ideas);//.rm
        buttons.add(Box.createRigidArea(new Dimension(50, 0)));//.rm
        buttons.add(saveConv_btn);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
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
            System.out.println("Settings and such");
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
    }//end actionListener




//maybe move logic to another class other than gui?
    private void saveConv(){
        String filePath = JOptionPane.showInputDialog("Enter folder name");
        String fileName = JOptionPane.showInputDialog("Enter file name");
        try {
            File file1 = new File("SavedConversations\\" + filePath);                                       //made it save to central folder
            file1.mkdirs();
            File file2 = new File(file1, fileName+".txt");;
            if (!file2.exists()) {
                file2.createNewFile();
            }
            BufferedWriter temp = new BufferedWriter(new FileWriter(file2, true));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String convo = getConvo();
            temp.write("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n" +
                    convo + "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + (dateFormat.format(new Date())));
            temp.close();
//append or overwrite???

        } catch (Exception ex) {
            System.out.println("Error in save conversation Event Handler");
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

            setSettings();
        }

        private void setSettings(){

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == back_btn){
                main.setVisible(true);
                buttons.setVisible(true);
                this.setVisible(false);
            }
            else if(e.getSource() == setting1_btn){
                System.out.printf("Setting number 1 is : %s\n",setting1_btn.isSelected());
                LoadSave.updatePref("setting 1",String.valueOf(setting1_btn.isSelected()));
            }
            else if(e.getSource() == setting2_btn){
                System.out.printf("Setting number 2 is : %d\n",(int)setting2_btn.getValue());//.rm                      cannot create event listener for spinner obj
            }
            else if(e.getSource() == setting3_btn){
                System.out.printf("Setting number 3 is : %s\n",setting3_btn.isSelected());
                LoadSave.updatePref("setting 3",String.valueOf(setting3_btn.isSelected()));
            }
            else if(e.getSource() == setting4_btn){
                System.out.printf("Setting number 4 is : %s\n",setting4_btn.isSelected());
                LoadSave.updatePref("setting 4",String.valueOf(setting4_btn.isSelected()));
            }
            else if(e.getSource() == setting5_btn){
                System.out.printf("Setting number 5 is : %s\n",setting5_btn.getSelectedItem());
                LoadSave.updatePref("setting 5",String.valueOf(setting5_btn.getSelectedItem()));
            }
            else if(e.getSource() == save_btn) {
                try {
                    JOptionPane.showConfirmDialog(this, "properties saving is not yet implemented", "Warning", JOptionPane.CLOSED_OPTION);
                    LoadSave.updatePref("setting 2",String.valueOf(setting2_btn.getValue()));
                    if(LoadSave.savePref())
                        JOptionPane.showConfirmDialog(null, "Save successful", "Your preferences have been saved", JOptionPane.OK_OPTION);      //.rm need better icon obj
                    else
                        JOptionPane.showMessageDialog(null, "Save unsuccessful", "An error has occurred. Your preferences have not been saved );", JOptionPane.OK_OPTION);

                }
                catch(Exception ex){
                    messageArea.append("WARNING: Error occurred saving to file: \n" + ex.getMessage());
                    LOGGER.log(Level.SEVERE, "Error occurred writing to file:\n" + ex.getMessage() , ex);
                }
            }
        }
    }
}



