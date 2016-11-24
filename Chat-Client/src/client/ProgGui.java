package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class ProgGui extends JFrame implements ActionListener {
    private PrintWriter out;
//pannels
    private JFrame frame = new JFrame("KDC chat");
    private JPanel main = new JPanel();
    private JPanel buttons = new JPanel();

//fields
    private JTextField textField = new JTextField(20);
    private JTextArea messageArea = new JTextArea(20, 80);

//buttons
    private JButton saveConv = new JButton("Save Convo");
    private JButton settings = new JButton("Setings");


    //ctor
    public ProgGui() {

        //initialize gui
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        textField.setEditable(false);                                              //denies use of input box until name is verified
        messageArea.setEditable(false);                                            //wont display messages till ^^
        saveConv.setEnabled(true);//change***
        messageArea.setBackground(Color.LIGHT_GRAY);

        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));

        saveConv.addActionListener(this);
        settings.addActionListener(this);
        textField.addActionListener(this);

        buttons.add(saveConv);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(settings);
        main.add(buttons);
        main.add(new JScrollPane(messageArea), "Center");
        main.add(textField, "South");                            //adds input box to bottom of the frame
        //main.
        frame.add(main);
        frame.pack();                                                              //ensures the content fits in the frame
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == settings) {                //sets
            System.out.println("Settings and such");
        }
        else if (e.getSource() == textField) {          //sends input sends to server
            this.out.println(textField.getText());
            textField.setText("");
        }
        else if (e.getSource() == saveConv) {           //saves conversationto file
            saveConv();
        }
    }//end actionListener


    private void saveConv(){                                                                //maybe move logic to another class other than gui?
        String filePath = JOptionPane.showInputDialog("Enter file path");
        String fileName = JOptionPane.showInputDialog("Enter file name");
        try {
            filePath += "\\savedconvo";                                                     //maybe change so the convos saved are in a centralized folder rather than individual
            File file1 = new File(filePath);
            file1.mkdirs();
            File file2 = new File(file1, fileName);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            BufferedWriter temp = new BufferedWriter(new FileWriter(file2, true));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String convo = getConvo();
            temp.write("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n" +
                    convo + "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + (dateFormat.format(new Date())));
            temp.close();//append or overwrite???

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
}
