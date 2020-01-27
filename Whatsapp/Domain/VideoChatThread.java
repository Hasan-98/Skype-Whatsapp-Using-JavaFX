/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeChat;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saada
 */
public class VideoChatThread  extends Thread
{
    BufferedReader inFromServer;
    Socket S;
    
   
    
    public VideoChatThread(BufferedReader inFromServer, Socket S) throws IOException
    {
        this.inFromServer = inFromServer;//new BufferedReader(new InputStreamReader(S.getInputStream()));
        this.S = S;
    }
    public void run()
    {
       synchronized(this)
                    {
                        FileOutputStream output  = null;
           try {
               String From  = inFromServer.readLine();
               int bytesRead;
               System.out.println(From);
               InputStream in = S.getInputStream();
               DataInputStream clientData = new DataInputStream(in);
               String fileName;// = clientData.readUTF();
               fileName = "aa.jpeg";
               output = new FileOutputStream(fileName);
               long size = clientData.readLong();
               byte[] buffer = new byte[1024];
               while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1)
               {
                   output.write(buffer, 0, bytesRead);
                   size -= bytesRead;
               }
               System.out.println("image recved");
                CanvasFrame frame=new CanvasFrame("Video Chat");
               opencv_core.IplImage image=cvLoadImage(fileName);
               frame.showImage(image);
               System.out.println("SHOWING IMAGE");
               frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
               
               // mediaPlayer.play();
           } catch (FileNotFoundException ex) {
               Logger.getLogger(VideoChatThread.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(VideoChatThread.class.getName()).log(Level.SEVERE, null, ex);
           } finally {
               try {
                   output.close();
               } catch (IOException ex) {
                   Logger.getLogger(VideoChatThread.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
                    }
    }
}
