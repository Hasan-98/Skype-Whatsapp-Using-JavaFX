/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saada
 */
public class VideoToClint extends Thread
    {
    
    
    String email, fileName;
    Socket connectionSocket;
    VideoToClint(String email, String fileName,	Socket connectionSocket)
    {
        this.email = email;
        this.fileName = fileName;
        this.connectionSocket = connectionSocket;
    }
    @Override
    public synchronized  void run()
    {
        try {
            DataOutputStream outToClient1 = new DataOutputStream(connectionSocket.getOutputStream());
            outToClient1.writeBytes("VIDEO"+"\n");
            outToClient1.writeBytes(email+"\n");
            
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
            
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            System.out.println("file read by server");
            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);
            
            System.out.println("Socket found");
            
            //Sending file name and file size to the server
            //DataOutputStream dos = new DataOutputStream(os);
            //outToClient1.writeUTF(myFile.getName());
            outToClient1.writeLong(mybytearray.length);
            outToClient1.write(mybytearray, 0, mybytearray.length);    
            outToClient1.flush();
        } catch (IOException ex) {
            Logger.getLogger(VideoToClint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
