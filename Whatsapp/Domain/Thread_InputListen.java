/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeChat;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author umarc
 */
public class Thread_InputListen extends Thread {

    BufferedReader inFromServer;
    Socket S;
    Label l1;
    protected Vector<VBox> chatScreens;
    protected VBox chat;
    protected VBox vBox1;
    Button b1;
    
        
        String musicFile = "ping.mp3";     // Bell tone

        Media sound = new Media(new File(musicFile).toURI().toString());
    //DataInputStream inFromServer;
    
    public Thread_InputListen(BufferedReader inFromServer, Socket S, Label l1, Vector<VBox> chatScreens, MediaPlayer mediaPlayer, Button b1, VBox chat, VBox vBox1) {
        this.inFromServer = inFromServer;
        this.S = S;
        this.l1 = l1;
        this.chatScreens = chatScreens;
        //this.mediaPlayer = mediaPlayer;
        this.b1 = b1;
        this.chat = chat;
        this.vBox1 = vBox1;
    }
    
    @Override
    public void run()
    {

        try {
            inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
            Thread friendthread = new getFriends(b1);////////////////////////////////////////
            friendthread.start();////////////////////////////////////////
            //System.out.println("Socket: " + S.getInetAddress().toString());
            //System.out.println("Port: " + S.getPort());
            //inFromServer = new DataInputStream(S.getInputStream());
            //Msg msg1 = new Msg();
            String method;
            while(true)
            {
                method = inFromServer.readLine();
                
                //method = inFromServer.readLine();
                //Label recvMsg = new Label();
                if(method.equals("MSG"))
                {
                    
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                    synchronized(this)
                    {
                        Msg msg1 = new Msg();
                        Label recvMsg = new Label();
                        //System.out.println("I m in reciving box");
                        msg1.from=inFromServer.readLine().trim();
                        msg1.to=inFromServer.readLine().trim();
                        msg1.text=inFromServer.readLine().trim();
                        System.out.println("\nMSG Receiced:\n");
                        System.out.println("From: "+msg1.from);
                        System.out.println("To: "+msg1.to);
                        System.out.println("MSG: "+msg1.text);

                        recvMsg.setText(msg1.text);
                        recvMsg.setWrapText(true);
                        recvMsg.setAlignment(Pos.CENTER_LEFT);
                        recvMsg.getStyleClass().add("recv");
                        for (int i=0;i<chatScreens.size();i++)
                        {
                            if (chatScreens.get(i).getId().equals(msg1.from))
                            {
                                Node b =vBox1.getChildren().get(i);
                                b.setEffect(new Bloom(0.1));
                                Thread a = new Thread_UpdateDisplay(0, chatScreens, recvMsg, i, null, null);
                                Platform.runLater(a);
                                //chatScreens.get(i).getChildren()
                                //.add(recvMsg);
                            }
                        }
                    }
                    //break;
                }
                else if(method.equals("PORT"))
                {
                    synchronized(this)
                    {
                        System.out.println("reading port");
                        String reply = inFromServer.readLine();
                        int port = Integer.parseInt(reply);
                        System.out.println("port: "+port);
                    }
                }
                
                else if(method.equals("VOICE"))
                {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("Accept Call.fxml"));
                    System.out.println("OH no");
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Call "+l1.getText());
                    stage.setScene(scene);
                    stage.show();
                    
                }
                else if(method.equals("VIDEO"))
                {
                    //synchronized(this)
                    {
                        //System.out.println("ITS A VIDEO");
                        //VideoChatThread VCT = new VideoChatThread(inFromServer, S);
                        //VCT.start();
                       
                       
                       
                    CanvasFrame frame=new CanvasFrame("yes");
                    synchronized(this)
                    {
                        System.out.println("starting while");
                        //while(method.equals("VIDEO"))
                        //while(true)
                        {
                            
                            String From  = inFromServer.readLine();
                            System.out.println(From);
                            String sizeString = inFromServer.readLine();
                            int bytesRead;
                            InputStream in = S.getInputStream();

                            DataInputStream clientData = new DataInputStream(in);

                            String fileName;// = clientData.readUTF();
                            //long sizeLong = in.read();
                            int size = Integer.parseInt(sizeString);
                            System.out.println("size String: "+size);
                            System.out.println("size Int: "+size);
                            byte[] buffer = new byte[100024];
                           // if (size > 0)
                            {
                                //in
                                //inFromServer.read
                                fileName = "aa.JPEG";
                                FileOutputStream output = new FileOutputStream(fileName);
                                System.out.println("1");
                                BufferedOutputStream bos = new BufferedOutputStream(output);
                                System.out.println("2");
                                clientData.read(buffer);
                                //clientData.read(buffer, 0, size);
                                System.out.println("3");
                                //System.out.println("BR: "+bR);
                                   //bos.write(buffer);
                                bos.write(buffer, 0, size);
                                bos.close();
                                //while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
                                {
                                    //output.write(buffer, 0, bytesRead);
                                    //size -= bytesRead;
                                }   
                                //output.close();
                                System.out.println("image recved");
                                 //CanvasFrame frame=new CanvasFrame("yes");
                                opencv_core.IplImage image=cvLoadImage(fileName);
                                //if(image!=null)
                                {
                                
                                  frame.showImage(image);
                                   System.out.println("SHOWING IMAGE");

                                }
                                
                               // method = inFromServer.readLine();
                                //System.out.println("method: "+method);

                                
                                }
                            }
                    //frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

                       // mediaPlayer.play();
                    }
                       
                       
                       
             
                       
                       
                       
                    }                    
                }
                else if(method.equals("FILE"))
                {
                    synchronized(this)
                    {
                        
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                        String From  = inFromServer.readLine();
                        int bytesRead;
                        //System.out.println(From);
                        InputStream in = S.getInputStream();

                        DataInputStream clientData = new DataInputStream(in);

                        String fileName = clientData.readUTF();
                        System.out.println("file: "+fileName);
                        FileOutputStream output = new FileOutputStream((fileName));
                        long size = clientData.readLong();
                        System.out.println("size: " +size); 
                                
                        byte[] buffer = new byte[100024];
                        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
                        {
                            output.write(buffer, 0, bytesRead);
                            size -= bytesRead;
                        }   
                        Label recvMsg = new Label();
                        recvMsg.setText("File Received "+fileName );
                        recvMsg.setWrapText(true);
                        recvMsg.setAlignment(Pos.CENTER_LEFT);
                        recvMsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        recvMsg.setStyle("-fx-background-color: white; -fx-text-fill: black;");

                        for (int i=0;i<chatScreens.size();i++)
                        {
                            if (chatScreens.get(i).getId().equals(l1.getText()))///////// set button id for correct msg display
                            {
                                Thread a = new Thread_UpdateDisplay(0, chatScreens, recvMsg, i, null, null);
                                Platform.runLater(a);
                                //chatScreens.get(i).getChildren()
                                //.add(recvMsg);
                            }
                        }
                    }
                }
                else if(method.equals("FRIENDS"))
                {
                    synchronized(this)
                    {
                        String count="";
                        count = inFromServer.readLine();
                        Vector<String> onlineFriend = new Vector();
                        Vector<String> offlineFriend = new Vector();
                        //System.out.println("count: "+count);
                            //.out.println("online");
                        for(int i=0;i<Integer.parseInt(count);i++)
                        {
                            String friend = inFromServer.readLine();
                            onlineFriend.add(friend);
                            //.out.println(onlineFriend.get(i));
                        }
                        String groupCounts = inFromServer.readLine();
                        //.out.println("group count: "+groupCounts);
                        Vector<Vector<String>> groups= groups = new Vector<Vector<String>>();
                        if(Integer.parseInt(groupCounts) > 0)
                        {
                            //ObjectInputStream OIS = new ObjectInputStream(S.getInputStream());
                           
                            for(int i =0; i < Integer.parseInt(groupCounts);++ i)
                            {
                                groups.add(new Vector<String>());
                                
                                //receive subGroups
                                String subGroupCount = inFromServer.readLine();
                                String allClients = new String("");
                                for(int j = 0; j < Integer.parseInt(subGroupCount); ++j)
                                {
                                    String temp = inFromServer.readLine();
                                    allClients += temp;
                                    groups.get(i).add(temp);
                                    if(j != Integer.parseInt(subGroupCount) - 1)
                                    {
                                        allClients += ",";
                                    }
                                }
                                onlineFriend.add(allClients);
                                //Button button = new Button(allClients);
                                //button.setMaxWidth(Double.MAX_VALUE);
                                //button.setId(allClients);
                                //Thread T = new Thread_UpdateDisplay(1, null, null, -1, button, vBox1);
                                //Platform.runLater(T);
                                //vBox1.getChildren().add(button);
                            }
                        }
                        //System.out.println("Online friends");
                        /*for(int i = 0; i < onlineFriend.size();++i)
                        {
                            System.out.println(onlineFriend.get(i));
                        }*/

                        count = inFromServer.readLine();
                        //System.out.println("offline Friends");
                        for(int i=0;i<Integer.parseInt(count);i++)
                        {
                            String friend = inFromServer.readLine();
                            offlineFriend.add(friend);
                            System.out.println(offlineFriend.get(i));
                        }
                        
                        for (int i=0;i<chatScreens.size();i++)
                        {
                            for (int j=0;j<offlineFriend.size();j++)
                            {
                                String id = "VBox[id=";
                                id+=offlineFriend.get(j)+"]";
                                if (id.equals(chatScreens.get(i).toString()))
                                {
                                    //if(l1!=null)
                                        //l1.setText("");
                                    Node b = vBox1.getChildren().get(i);
                                    b.setVisible(false);
                                    Node cs =chatScreens.get(i);
                                    cs.setVisible(false);
                                }
                              
                            }
                        }
                        
                        for(int i=0;i<onlineFriend.size();i++)
                        {
                            boolean notAlreadyOnline = true;
                              
                            //String name = inFromServer.readLine();
                                 
                            for(int j = 0; j < chatScreens.size();j++)
                            {
                                if (onlineFriend.get(i).equals(chatScreens.get(j).getId()) && j <= chatScreens.size())
                                {
                                    notAlreadyOnline = false;
                                    Node b = vBox1.getChildren().get(j);
                                    b.setVisible(true);
                                    Node cs =chatScreens.get(j);
                                    cs.setVisible(true);
                                }
                            }                     
                            
                            if(notAlreadyOnline)
                            {

                                Button button = new Button(onlineFriend.get(i));
                                button.setMaxWidth(Double.MAX_VALUE);
                                button.setId(onlineFriend.get(i));
                                button.getStyleClass().add("button1");
                                VBox chatScreen = new VBox();
                                chatScreen.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                                chatScreen.setId(onlineFriend.get(i));

                                //chatScreen.getChildren().add(new Label(Integer.toString(i)));
                                button.setOnAction(new EventHandler<ActionEvent>() 
                                {
                                    @Override
                                    public void handle(ActionEvent e) 
                                    {
                                        //button.setEffect(new );
                                    button.setEffect(null);
                                        l1.setText(button.getId());
                                        for (int i=0;i<chatScreens.size();i++)
                                        {
                                            if (chatScreens.get(i).getId().equals(l1.getText()))
                                            {
                                                chat.getChildren().clear();
                                                chat.getChildren().add(chatScreens.get(i));
                                                //chat = chatScreens.get(i);
                                            }
                                        }
                                    }
                                });
                                chatScreens.add(chatScreen);
                                Thread a = new Thread_UpdateDisplay(1, null, null, 0, button, vBox1);
                                Platform.runLater(a);
                        }
                    }
                    }
                }
            }
        } catch (IOException ex) {
            //Logger.getLogger(Thread_InputListen.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }    
}
