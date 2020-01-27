/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeChat;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.sun.management.jmx.Trace.send;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * FXML Controller class
 *
 * @author saada
 */

public class LoginController implements Initializable  
{
    /*public class SaveSocket
    {
       
        public void set(Socket s)
        {
            System.out.println(s);
            S = s;
            System.out.println(S);
        }
    }*/
        private static  Socket S;
        @FXML
        private Button signIn;
        @FXML
        private PasswordField password;
        @FXML
        private TextField email;
        @FXML
        private Label label1;
        @FXML
        private Label label2;
        @FXML
        private Button signUp;
        @FXML
        private PasswordField SUpassword;
        @FXML
        private TextField SUemail;
        @FXML
        private TextField SUname;
        @FXML
        private Label label3;
        @FXML
        private Label label4;
        @FXML
        private Label label5;
        @FXML
        private Label label6;
        private boolean initializeInputThread;
        Thread_InputListen TIL;
        private static String userName;
        private Socket clientSocket, save;// = new Socket("localhost", 6789);
        private DataOutputStream outToServer;
        private BufferedReader inFromServer ;
        static boolean onGoingCall;
        static String copyOfL1Label;
       
        @FXML
        private void handleButtonSignIn(ActionEvent event) throws IOException 
        {
            onGoingCall = false;    //no call on signIN
            Stage stage;
            Parent root;
            
            if(!email.getText().trim().equals(""))
            {
            label1.setText("");
                if(!password.getText().trim().equals(""))
                {
                    label2.setText("");
                    String Semail = email.getText(), Spassword = password.getText();
                    
                    clientSocket = new Socket("localhost", 6789);
                    System.out.println(clientSocket);
                    //Save.set(clientSocket);
                    S=clientSocket;
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String method = "signin", reply; 
                    userName = Semail;
                    outToServer.writeBytes(method + "\n");
                    outToServer.writeBytes(Semail + "\n");
                    outToServer.writeBytes(Spassword + "\n");
                    reply = inFromServer.readLine();
                    if(reply.equals("True"))
                    {
                        
                        stage = (Stage) signIn.getScene().getWindow();
                        
                        root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                        stage.setOnCloseRequest(event1 -> {
                            
                        try 
                        {
                            outToServer.writeBytes("EXIT"+'\n');
                        } 
                        catch (IOException ex) 
                        {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        });
                        
                        
                        
                        Scene sc = new Scene(root);
                        
                        sc.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                        //sc.getStylesheets().add("stylesheet.css");
                        stage.setScene(sc);
                        stage.show();
                        
                        
                        
                        
                    }
                    else
			label1.setText("Login Failed");
                }
                else
                    label2.setText("Password field is empty");
            }
            else
            label1.setText("Email field is empty");
        }
        
        
        @FXML    
        private void handleButtonSignUP(ActionEvent event) throws IOException 
        {
            if(!SUname.getText().trim().equals(""))
            {
            label3.setText("");
                if(!SUemail.getText().trim().equals(""))
                {
                    label4.setText("");
                    if(!SUpassword.getText().trim().equals(""))
                    {
                    label5.setText("");
                    clientSocket = new Socket("localhost", 6789);
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                   
                    String method = "signup";
                    String reply="", email = SUemail.getText(), password = SUpassword.getText(), name = SUname.getText();
                    outToServer.writeBytes(method + "\n");
                    outToServer.writeBytes(name + "\n");
                    outToServer.writeBytes(email + "\n");
                    outToServer.writeBytes(password + "\n");
                    reply = inFromServer.readLine();
                    if(reply.equals("True"))
			label6.setText("You successful created an account");
                    else
			label6.setText("SignUp Failed");
                    clientSocket.close();
                    }
                    else
                        label5.setText("Password field is empty");
                }
                else
                    label4.setText("Email field is empty");
            }
            else
                label3.setText("Name field is empty");
        }
        
        
       
        
        @FXML
        private void handleButtonChat(ActionEvent event) 
        {
            
            
        }
        
        
        @FXML
        private Button b1 = new Button();
        @FXML
        private Button send = new Button();
        @FXML        
        private VBox vBox1;
        @FXML
        private TextField msgTxt;
        @FXML
        private VBox chat;
        @FXML
        private VBox chatScreen;
        @FXML
        private Label myName;
        @FXML
        private Label l1;
        Vector<VBox> chatScreens = new Vector<VBox>();
        Vector<Button> chatButtons = new Vector<Button>();
        
        
        
        static boolean threadStarted = true;
        @FXML
        private void handleButton(ActionEvent event) throws IOException ,ClassNotFoundException ///// ye wala function
        {
            
            
            
            myName.setAlignment(Pos.CENTER);
            myName.setText(userName);
            
            inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
            outToServer = new DataOutputStream(S.getOutputStream());
            outToServer.writeBytes("SENDFRIENDS"+'\n');
                     
        }
        
        String musicFile = "ping.mp3";     // Bell tone

        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        
        @FXML
        private void handleButtonSend(ActionEvent event) throws IOException
        {            
            //Msg msg1 = new Msg();
            if(!initializeInputThread)
            {
                //inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
                /*final Thread_InputListen */
                TIL = new Thread_InputListen(inFromServer, S, l1, chatScreens, mediaPlayer, b1, chat, vBox1);
                //Platform.runLater(TIL);
                TIL.start();

                initializeInputThread = true;
                //System.out.println("Thread Started");
            }
            if(!msgTxt.getText().trim().equals("") && !l1.getText().trim().equals(""))
            {
                //msg1.from = userName;
                //msg1.to = l1.getText();
               // msg1.text = msgTxt.getText();
                outToServer = new DataOutputStream(S.getOutputStream());
                outToServer.writeBytes("MSG"+'\n');
                outToServer.writeBytes(userName+'\n');
                outToServer.writeBytes(l1.getText()+'\n');
                String saveMsg =  msgTxt.getText();
                outToServer.writeBytes(msgTxt.getText()+'\n');
                msgTxt.setText("");
                System.out.println("msg send");
                Label recvMsg = new Label(), sendMsg = new Label();
                recvMsg.setTextAlignment(TextAlignment.LEFT);
                sendMsg.setTextAlignment(TextAlignment.LEFT);
                sendMsg.setAlignment(Pos.CENTER_RIGHT);
                sendMsg.setWrapText(true);
                //recvMsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
               
                sendMsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                sendMsg.getStyleClass().add("send");
                
                HBox hBox=new HBox();
                hBox.getChildren().add(sendMsg);
                hBox.setAlignment(Pos.BASELINE_RIGHT);
                for (int i=0;i<chatScreens.size();i++)
                {
                    if (chatScreens.get(i).getId().equals(l1.getText()))
                    {
                        sendMsg.setText(saveMsg);
                        //sendMsg.setWrapText(true);
                        //sendMsg.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                        chatScreens.get(i).getChildren().add(hBox);
                        //chat.getChildren().add(sendMsg);
                        //chat = chatScreens.get(i);
                        
                    }
                }
                //ObjectOutputStream outToServerObject = new ObjectOutputStream(S.getOutputStream());
                //outToServerObject.writeObject(msg1);
                /*
                inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
                Msg msg1 = new Msg();
                String method;
                method = inFromServer.readLine();
                if(method.equals("MSG"))
                {
                    msg1.from=inFromServer.readLine();
                    msg1.to=inFromServer.readLine();
                    msg1.text=inFromServer.readLine();
                    System.out.println("\nMSG Receiced:\n");
                    System.out.println("From: "+msg1.from);
                    System.out.println("To: "+msg1.to);
                    System.out.println("MSG: "+msg1.text);

                    recvMsg.setText(msg1.text);

                    mediaPlayer.play();

                    recvMsg.setWrapText(true);
                    recvMsg.setAlignment(Pos.CENTER_LEFT);
                    for (int i=0;i<chatScreens.size();i++)
                    {
                        if (chatScreens.get(i).getId().equals(l1.getText()))
                        {
                            chatScreens.get(i).getChildren().add(recvMsg);
                        }
                    }
                    
                }  */ 
            }
            
        }
        
        @FXML
        private Button addFriend;
        @FXML
        private Button cancel;
        @FXML 
        private TextField friendUserName;
        @FXML
        private void handleButtonAddFriendWindow(ActionEvent event) throws IOException
        { 
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AddFriend.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
        }
        @FXML
        private Label friendAdded;
        @FXML
        private void handleButtonAddFriend(ActionEvent event) throws IOException
        { 
            inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
            outToServer = new DataOutputStream(S.getOutputStream());
            outToServer.writeBytes("ADDFRIEND"+'\n');
            String FUN = friendUserName.getText();
            friendUserName.setText("");
            outToServer.writeBytes(FUN+'\n');
            String reply = inFromServer.readLine();
            if(reply.equals("True"))
                friendAdded.setText("You successfully added a new friend");
            else
                friendAdded.setText("Error");
            
        }
        
        @FXML
        private Button attachFile;
        @FXML
        private void handleButtonAttachFile(ActionEvent event) throws IOException
        {   
            Stage stage = (Stage) attachFile.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            File myFile = fileChooser.showOpenDialog(stage);
            if(myFile != null)
            {
                System.out.println(myFile.getName());
            
                myFile.createNewFile();
                //inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));//////////
                outToServer = new DataOutputStream(S.getOutputStream());
                outToServer.writeBytes("FILE"+'\n');
                outToServer.writeBytes(l1.getText()+'\n');
                
                //File myFile = new File(fileName);
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                //bis.read(mybytearray, 0, mybytearray.length);

                DataInputStream dis = new DataInputStream(bis);
                dis.readFully(mybytearray, 0, mybytearray.length);

                OutputStream os = S.getOutputStream();

                //Sending file name and file size to the server
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(myFile.getName());
                dos.writeLong(mybytearray.length);
                dos.write(mybytearray, 0, mybytearray.length);
                dos.flush();
                Label sendMsg = new Label();
                //sendMsg.setTextAlignment(TextAlignment.RIGHT);
                sendMsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                for (int i=0;i<chatScreens.size();i++)
                {
                    if (chatScreens.get(i).getId().equals(l1.getText()))
                    {
                        sendMsg.setText("File Sent "+myFile.getName());
                        sendMsg.setAlignment(Pos.CENTER_RIGHT);
                        sendMsg.setWrapText(true);
                        sendMsg.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                        chatScreens.get(i).getChildren().add(sendMsg);
                        //chat.getChildren().add(sendMsg);
                        //chat = chatScreens.get(i);
                        
                    }
                }

                
            }
        }
    /**
     * Initializes the controller class.
     */
       
    
    @FXML
    private Button StartGroupChat;
    @FXML
    private TextField GroupChatClients;
    @FXML
    private void handleButtonStartGroupChat(ActionEvent event) throws IOException
    {     
        String allClients = GroupChatClients.getText();
        String []clients = allClients.split(",");
        int size = 0;
        if(clients[0].equals(""))
            size=0;
        else
            size=clients.length + 1;
        System.out.println(size);
        if(size>0)
        {
            for(int i=0;i<size-1;i++)
            {   
                clients[i] = clients[i].replaceAll("\\s+","");
                System.out.println(clients[i]); 
            }
         outToServer = new DataOutputStream(S.getOutputStream());
            outToServer.writeBytes("GROUPCHAT"+'\n');
            outToServer.writeBytes(size+"\n");
            for(int i=0;i<size - 1;i++)
            {   
                outToServer.writeBytes(clients[i]+"\n");
            }
            
            outToServer.writeBytes(myName.getText()+"\n");
        }
    } 
        
    
    @FXML
    private Button StartVideoCall;
    
    opencv_core.IplImage img=null;
    File myFile=null;
    byte[] mybytearray=null;
    FileInputStream fis=null;
    BufferedInputStream bis =null;
    DataInputStream dis =null;
    OutputStream os =null;
    DataOutputStream dos=null;
    OpenCVFrameGrabber grabber=new OpenCVFrameGrabber(0);
    
    
    @FXML
    private void handleButtonStartVideoCall(ActionEvent event) throws IOException, FrameGrabber.Exception
    { 
                grabber.setImageWidth(150);
                grabber.setImageHeight(150);
                
        grabber.start();
        
        
        
            while(true)// blocked duw to this while loop
            {
                img=grabber.grab();
                if(img!=null)
                {
                    cvSaveImage("image.jpeg",img);
                    myFile = new File("image.jpeg");
	            mybytearray = new byte[(int) myFile.length()];
                    
                    fis = new FileInputStream(myFile);
	            bis = new BufferedInputStream(fis);
	            //bis.read(mybytearray, 0, mybytearray.length);

	            dis = new DataInputStream(bis);
	            dis.readFully(mybytearray, 0, mybytearray.length);

	            os = S.getOutputStream();
                    
	            dos = new DataOutputStream(os);
                    System.out.println("Sending video request");
                           
                    dos.writeBytes("VIDEO"+"\n");
                    dos.writeBytes(l1.getText()+'\n');
                    
	            dos.writeUTF(myFile.getName());

	            
                    long imageSize = mybytearray.length;
                    System.out.println(imageSize);
	            dos.writeLong(imageSize);
	            dos.write(mybytearray, 0, mybytearray.length);
	            dos.flush();
                }
            }
    }
    
    private AudioFormat getAudioFormat() {
    float sampleRate = 16000.0F;
    int sampleInbits = 16;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = false;
    return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
}
 
    boolean stopaudioCapture = false;   
ByteArrayOutputStream byteOutputStream;
AudioFormat adFormat;
TargetDataLine targetDataLine;
AudioInputStream InputStream;
SourceDataLine sourceLine;
Graphics g;

private void captureAudio() {
    try {
        adFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(adFormat);
        targetDataLine.start();

        Thread captureThread = new Thread(new CaptureThread());
        captureThread.start();
    } catch (Exception e) {
        StackTraceElement stackEle[] = e.getStackTrace();
        for (StackTraceElement val : stackEle) {
            System.out.println(val);
        }
        System.exit(0);
    }
}
Stage stage = new Stage();
    
    @FXML
    Button voiceCall;
    @FXML
    private void handleButtonStartAudioCall(ActionEvent event) throws IOException, FrameGrabber.Exception
    { 
        if(!l1.getText().trim().equals(""))
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Audiio Call.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            //stage = new Stage();
            stage.setTitle("Call "+l1.getText());
            stage.setScene(scene);
            stage.show();
            
            copyOfL1Label = l1.getText();
        }
    }
    
    @FXML
    Button Call = new Button();    
    @FXML
    Button Cancle = new Button();
    @FXML
    private void handleButtonStartCall(ActionEvent event) throws IOException, FrameGrabber.Exception
    {
        System.out.println("calllllllllllllllllllll");
        Call.setVisible(false);
        Cancle.setVisible(true);
        onGoingCall = true;
        
        
        DataOutputStream oToServer = new DataOutputStream (S.getOutputStream());
        oToServer.writeBytes("VOICE\n");
        oToServer.writeBytes(copyOfL1Label + "\n");
        if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
        try {


            DataLine.Info dataLineInfo = new DataLine.Info( TargetDataLine.class , getAudioFormat() ) ;
            TargetDataLine targetDataLine = (TargetDataLine)AudioSystem.getLine( dataLineInfo  ) ;
            targetDataLine.open( getAudioFormat() );
            targetDataLine.start();
            byte tempBuffer[] = new byte[16000] ;
            int cnt = 0 ;
            int hp=20;
            while( onGoingCall/*hp!=0*/ )
            {
                //hp--;
                System.out.println(hp);
                targetDataLine.read( tempBuffer , 0 , tempBuffer.length );
                OutputStream os = S.getOutputStream();
                os.write(tempBuffer, 0, tempBuffer.length);
                //System.out.println(tempBuffer.toString());
                os.flush();
            }

          }
          catch(Exception e )
          {
            System.out.println(" Exception in sender voice handler " ) ;
            System.exit(0) ;
          }
        }
        
    }
    @FXML
    private void handleButtonCancleCall(ActionEvent event) throws IOException, FrameGrabber.Exception
    {
        onGoingCall = false;
        Stage stage = (Stage) Cancle.getScene().getWindow();
        stage.close();
    }
    @FXML
    Button callAcceptAtRecv = new Button();    
    @FXML
    Button cancleAtRecv = new Button();
    @FXML
    private void handleButtonCancleCallAtRecv(ActionEvent event) throws IOException, FrameGrabber.Exception
    {
        onGoingCall = false;
        Stage stage = (Stage) cancleAtRecv.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleButtonAcceptCallAtRecv(ActionEvent event) throws IOException, FrameGrabber.Exception
    {
        System.out.println("Audio code received");
                    byte b[] = null ;
                    while( true )
                    {
                       b = receiveThruUDP() ; 
                       toSpeaker( b ) ;
                    }
                    
        
    }
    
    class CaptureThread extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {

        byteOutputStream = new ByteArrayOutputStream();
        stopaudioCapture = false;
        try {
            DatagramSocket clientSocket = new DatagramSocket(8786);
            InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
            while (!stopaudioCapture) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                if (cnt > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, IPAddress, 9786);
                    clientSocket.send(sendPacket);
                    byteOutputStream.write(tempBuffer, 0, cnt);
                }
            }
            byteOutputStream.close();
        } catch (Exception e) {
            System.out.println("CaptureThread::run()" + e);
            System.exit(0);
        }
    }
}


    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        send.fire();
        //vBox1 = new VBox();
        b1.setVisible(false);////////////////////////////////////////
    Cancle.setVisible(false);
    }    
    
     private byte[] receiveThruUDP()
    {
       try
       {
        
       byte soundpacket[] = new byte[16000];
       
       InputStream is= S.getInputStream();
       int bytesRead = is.read(soundpacket, 0, soundpacket.length);
       
        return soundpacket;
       }
       catch( IOException e )
       {
        System.out.println(" Unable to send soundpacket using TCP " ) ;   
        return null ;
       } 
 
    }    
    
     public void toSpeaker( byte soundbytes[] )
     {
 
      try{  
        DataLine.Info dataLineInfo = new DataLine.Info( SourceDataLine.class , getAudioFormat() ) ;
          try (SourceDataLine sourceDataLine = (SourceDataLine)AudioSystem.getLine( dataLineInfo )) {
              sourceDataLine.open( getAudioFormat() ) ;
              sourceDataLine.start();
              int cnt = 0;
              sourceDataLine.write( soundbytes , 0, soundbytes.length );
              sourceDataLine.drain() ;
          }
      }
      catch(LineUnavailableException e )
      {
      System.out.println("not working in speakers " ) ;
      }
      
    }   
}
