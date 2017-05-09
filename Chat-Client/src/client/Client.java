package client;
//change
//imports
 import client.display.ChatSceneController;
 import javafx.application.Application;
 import javafx.application.Platform;
 import javafx.concurrent.Service;
 import javafx.concurrent.Task;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.control.TextArea;
 import javafx.scene.text.Text;
 import javafx.scene.text.TextFlow;
 import javafx.stage.Stage;
 import logging.MyLogger;

 import java.io.*;
 import java.net.*;
 import javax.sound.sampled.*;
 import javax.swing.*;
 import javax.sound.sampled.AudioSystem;
 import java.util.Properties;
 import java.util.logging.Level;


public class Client extends Application{

    private static Client client;
    public static Client getClient() {
            return client;
        }

//main
    public static void main(String[] args){
        client = new Client();
        launch(args);
    }//endMain


    /**
     * client class private variables
     */
    private BufferedReader in;
    private PrintWriter out;

    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;

    private OutputStream audioOut;
    private BufferedInputStream audioIn;
    private Stage window;
    private String userName = "";
    private Record recording;



    @Override
    /**
     * initializes fx gui by loading from fxml file
     */
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("display/ServerScene.fxml"));                          //loads the server scene from fxml file
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.setOnCloseRequest(e -> closeClient());
        primaryStage.show();
    }



    /**
     * closes logger files when the user closes the fx window
     */
    private void closeClient(){
        MyLogger.closeLogger();//closes logger properly
        this.window.close();
    }




    /**
     * is used to re-prompt the user if the name is already taken on server
     * @return new name to attempt connection with
     */
        private String getName() {
            return JOptionPane.showInputDialog(null, "Username: \""  + userName +  "\" is taken.\nEnter a new Username", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
        }




    /**
     * used to connect the client to a server, and have validated name
     * @param serverAddress the address of the server to attempt connection
     * @return successful connection to server
     */
        public boolean connect(String serverAddress){
            Socket typeS = null;//creates socket connection to server...for text
            try {
                typeS = new Socket(serverAddress, 9001);
                Socket audioS = new Socket(serverAddress, 9002);//creates another socket connection to server..for audio
                //this.in = new BufferedReader(new InputStreamReader(typeS.getInputStream()));       //buffered reader to recieve from serverfor text
                //this.out = new PrintWriter(typeS.getOutputStream(), true);                       //printwriter to write to server

                InputStream intemp = typeS.getInputStream();
                OutputStream outtemp = typeS.getOutputStream();
                this.objectOut = new ObjectOutputStream(outtemp);
                this.objectIn = new ObjectInputStream(intemp);

                this.audioIn = new BufferedInputStream(audioS.getInputStream());                //input stream to recieve from server for audio
                this.audioOut = audioS.getOutputStream();
                this.recording = new Record();

                //in.readLine();
                //out.println(userName);
                objectIn.readObject();
                objectOut.writeObject(userName);

                Task myTask = new Task() {  //this task ensure valid name
                    @Override
                    protected Object call() throws Exception {
                        while(((String)objectIn.readObject()).startsWith("SUBMITNAME")){
                            userName = getName();
                            objectOut.writeObject(userName);
                        }
                        Properties userPrefObj = LoadSaveUtil.getPropertyObject(LoadSaveUtil.userSettingFilename);
                        userPrefObj.setProperty("USERNAME",userName);
                        LoadSaveUtil.savePropertyObject(userPrefObj,LoadSaveUtil.userSettingFilename);
                        return null;
                    }
                };
                myTask.run();

            }
            catch (Exception e) {
                MyLogger.log(Level.INFO, "Failed to connect to server: " + serverAddress, serverAddress);
                //e.printStackTrace();
                return false;
            }
            MyLogger.log(Level.INFO, "Connected to server: " + serverAddress, serverAddress);
            return true;
        }




    /**
     * declares and runs the service that transfers text communication
     * @param messagearea a reference to the gui text area so text can be appended
     */
        public void runChatService(TextFlow messagearea){                                     //this method creates and starts the service
            Service chatService = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {

                            boolean cont = true;
                            int namesFailed = 0;
                            while (cont) {

                                try{
                                    //for typing
                                    String line = (String)objectIn.readObject();

                                    if (line != null && line.startsWith("MESSAGE")) {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                messagearea.getChildren().add(new Text(line.substring(8) + "\n"));
                                            }
                                        });
                                        AudioEffects.play("boop.wav");
                                    }
                                }//end try
                                catch(SocketException e){
                                    //will prevent endless loop if server goes down
                                    MyLogger.log(Level.SEVERE, e.getMessage(), e);
                                    cont = false;
                                }
                                catch (Exception e){
                                    MyLogger.log(Level.SEVERE, e.getMessage(), e);
                                }

                            }//end while
                            return null;
                        }//end call
                    };//end new task
                }//end create task
            };//end service

            chatService.start();
        }


    /**
     * creates and starts the service that sends and recieves audio
     */
    public void runAudioService(){
        Service voiceService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        boolean cont = true;
                        while (cont) {
                            //for sounds
                            try {
                                AudioInputStream ais = AudioSystem.getAudioInputStream(audioIn);   //waits here for audio input coming from server
                                Clip clip = AudioSystem.getClip();
                                clip.open(ais);
                                clip.start();
                                clip.drain();
                            } catch(SocketException e){
                                //will prevent endless loop if server goes down
                                MyLogger.log(Level.SEVERE, e.getMessage(), e);
                                cont = false;
                                System.out.println("wrong");
                            }
                            catch (Exception e){
                                MyLogger.log(Level.SEVERE, e.getMessage(), e);
                                System.out.println("wrong2");
                            }
                        }//end while
                       return null;
                    }//end call
                };//end new task
            }//end create task
        };//end new service


        voiceService.start();
    }


    /**
     * sends a string message to the server
     */
    public void sendToServer(String strToSend){
        try {
            objectOut.writeObject(strToSend);
        }
        catch(IOException ioe){
            MyLogger.log(Level.SEVERE, ioe.getMessage(), ioe);
        }
    }

    /**
     * sets the username
     * @param newUsername new username
     */
    public void setName(String newUsername){
        userName = newUsername;
    }



    public void sendLine(String wavToSend){

        File audioFile = recording.getAudioFile(wavToSend);


        try {
            FileInputStream fin = new FileInputStream(audioFile);
            OutputStream os = audioOut;
            byte buffer[] = new byte[2048];
            int count;
            while((count = fin.read(buffer)) != -1){
                os.write(buffer, 0, count);
            }
        }
        catch(FileNotFoundException fnfe){
            MyLogger.log(Level.SEVERE, fnfe.getMessage(), fnfe);
        }
        catch(IOException ioe){
            MyLogger.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

    }//end sendLine


    //maybe move to LoadSave class**********************************************
    public void voiceLine(String title) {

        recording.startRec(title);

    }

}
