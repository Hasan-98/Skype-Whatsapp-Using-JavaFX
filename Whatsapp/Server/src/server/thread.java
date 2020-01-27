/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author saada
 */
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
public class thread extends Thread
{
        static int request = 0;
	Socket connectionSocket;
        String email;
        Vector<Socket> connectedSockets;
        Vector connectedClients;
        Message message;
	User user;
	Friend friend;
        boolean online;
        
        DataOutputStream outToClient;
        Vector<Vector<String>> Groups;
	thread(Socket Socket1, String email, Vector connectedSockets, Vector connectedClints, Message message, User user, Friend friend, DataOutputStream outToClient, Vector<Vector<String>> Groups )
	{
		connectionSocket = Socket1;
                this.email = email;
                this.connectedSockets = connectedSockets;
                this.connectedClients = connectedClints;
                this.message = message;
                this.user = user;
                this.friend = friend;
                online = true;
                this.outToClient = outToClient;
                this.Groups = Groups;
	}
	public void run()
	{
            online = true;
              
               try{
                   //CanvasFrame frame=new CanvasFrame("server");
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		while(online)
		{
                    String method=  inFromClient.readLine();
			try
			{
                        if(method.equals("EXIT"))
                        {
                            synchronized(this)
                            {
                                System.out.println("Exit Request");
                                user.offline(email);
                                online = false;
                            }
                        }
                        else if(method.equals("ADDFRIEND"))
                        {
                            synchronized(this)
                            {
                                //System.out.println("Add Friend Request");
                                String friendUserName = inFromClient.readLine();
                                int id1=user.getUserId(friendUserName);
                                int id2=user.getUserId(email);
                                //System.out.println(id1);
                                //System.out.println(id2);
                                if (id1>0 && id2>0 && id1!=id2)
                                {
                                    boolean inserted = friend.insert(id1, id2);if(inserted)
                                        outToClient.writeBytes("True"+"\n");
                                    else
                                        outToClient.writeBytes("FALSE"+"\n");
                                }
                                else
                                        outToClient.writeBytes("FALSE"+"\n");
                            }                                
                        }
                        else if(method.equals("GROUPCHAT"))
                        {
                            synchronized(this)
                            {
                                System.out.println("Group Chat Request");
                                String count="";
                                count = inFromClient.readLine();// count =  inFromServer.readLine();
                                Vector<String> Group = new Vector();
                                //System.out.println("count: "+count);
                                for(int i=0;i<Integer.parseInt(count);i++)
                                {
                                    String friend = inFromClient.readLine();
                                    Group.add(friend);
                                    //System.out.println(Group.get(i));
                                }
                                Groups.add(Group);
                            }
                        }
                        else if(method.equals("VIDEO"))
                        {
                            
                            synchronized(this)
                            {
                                System.out.println("Video Request");
                                String to =inFromClient.readLine();
                                System.out.println("in video");
                                int bytesRead;
                                DataInputStream clientData = new DataInputStream(connectionSocket.getInputStream());

                                String fileName = clientData.readUTF();
                                //OutputStream output = new FileOutputStream((fileName));
                                long size = clientData.readLong();
                                long sendSize = size;
                                byte[] buffer = new byte[100024];
                                clientData.read(buffer, 0, (int)size);
                                //while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
                                {
                                    //output.write(buffer, 0, bytesRead);
                                    //size -= bytesRead;
                                }
                                //output.close();
                        //opencv_core.IplImage image=cvLoadImage(fileName);
                        
                          // frame.showImage(image);
                                System.out.println("File "+fileName+" received from " + email);

                                for(int i=0;i<connectedClients.size();i++)
                                {
                                    if (connectedClients.get(i).equals(to))
                                    {

                                        //File myFile = new File(fileName);
                                        //byte[] mybytearray = new byte[(int) myFile.length()];

                                        //FileInputStream fis = new FileInputStream(myFile);
                                        //BufferedInputStream bis = new BufferedInputStream(fis);
                                        //System.out.println("file read by server");
                                        //DataInputStream dis = new DataInputStream(bis);
                                        //dis.read(mybytearray, 0, mybytearray.length);  
                                        //bis.close();
                                        System.out.println("Socket found");

                                        //Sending file name and file size to the server
                                        //DataOutputStream dos = new DataOutputStream(os);
                                        //outToClient1.writeUTF(myFile.getName());
                                        
                                        DataOutputStream outToClient1 = new DataOutputStream(connectedSockets.get(i).getOutputStream());
                                        //if (request == 0)
                                        {
                                            outToClient1.writeBytes("VIDEO"+"\n");
                                           // request=1;
                                        }
                                        outToClient1.writeBytes(email+"\n");
                                        
                                        //long imageSize = mybytearray.length;
                                        System.out.println("size :"+sendSize);
                                        outToClient1.writeBytes(sendSize+"\n");
                                        outToClient1.write(buffer);
                                        //outToClient1.write(buffer, 0, (int)sendSize);    
                                        outToClient1.flush();  

                                    }
                                }
                            }
                            request = 0;
                        }
                        else if(method.equals("SENDPORT"))
                        {
                            synchronized(this)
                            {
                            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                            String to =inFromClient.readLine();
                            System.out.println("TO: "+to);
                            for(int i=0;i<connectedClients.size();i++)
                            {
                                if (connectedClients.get(i).equals(to))
                                {
                                    outToClient.writeBytes("PORT"+"\n");
                                    
                                    //outToClient.write(connectedSockets.get(i).getLocalPort());
                                    outToClient.writeBytes(connectedSockets.get(i).getLocalPort()+"\n");
                                    
                                    DataOutputStream outToClient1 = new DataOutputStream(connectedSockets.get(i).getOutputStream());

                                    outToClient1.writeBytes("VOICECALL"+'\n');
                                    outToClient1.writeBytes(email+'\n');
                                }
                            }
                                
                            }
                            
                            
                        }
                        else if(method.equals("SENDFRIENDS"))
                        {
                            synchronized(this)
                            {
                                //System.out.println("Send Friend Request");
                                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                                Vector<String> onlineFriends = new Vector();
                                try {
                                    onlineFriends = user.getFriend(email);
                                } catch (SQLException ex) {
                                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                outToClient.writeBytes("FRIENDS"+"\n");
                                String count = Integer.toString(onlineFriends.size());
                                outToClient.writeBytes(count+"\n");
                                //System.out.println("size: "+onlineFriends.size());
                                for (int i=0;i<onlineFriends.size();i++)
                                {
                                    //System.out.println(onlineFriends.get(i));
                                    outToClient.writeBytes(onlineFriends.get(i)+"\n");
                                }
                                //Send groups
                                int noOfGroups = 0;
                                
                                //calculate noOfGroups
                                for(int i = 0; i < Groups.size(); ++i)
                                {
                                    if(Groups.get(i).contains(email))
                                    {
                                        ++noOfGroups;
                                    }
                                    /*for(int j = 0; j < Groups.get(i).size(); ++j)
                                    {
                                        System.out.println("Memeber names: " + Groups.get(i).get(j));
                                    }
                                   */
                                }
                                //send Groups
                                outToClient.writeBytes(Integer.toString(noOfGroups)+'\n');
                                if(noOfGroups > 0)
                                {
                                    //ObjectOutputStream OOS = new ObjectOutputStream(connectionSocket.getOutputStream());
                                    for(int i = 0; i < Groups.size(); ++i)
                                    {
                                        if(Groups.get(i).contains(email))
                                        {
                                            //send subgroup
                                            outToClient.writeBytes(Integer.toString(Groups.get(i).size())+'\n');
                                            for(int j = 0; j < Groups.get(i).size(); ++j)
                                            {
                                                outToClient.writeBytes(Groups.get(i).get(j)+'\n');
                                            }
                                        }
                                    }                                
                                }
                                Vector<String> offlineFriends = new Vector();
                                try {
                                    offlineFriends = user.getFriendOffline(email);
                                } catch (SQLException ex) {
                                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                count = Integer.toString(offlineFriends.size());
                                outToClient.writeBytes(count+"\n");
                                //System.out.println("size: "+onlineFriends.size());
                                //System.out.println("OFFLINE FRIEND: " + count);
                                for (int i=0;i<offlineFriends.size();i++)
                                {
                                    //System.out.println(onlineFriends.get(i));
                                    String a = offlineFriends.get(i);
                                    System.out.println(a);
                                 
                                    outToClient.writeBytes(a+"\n");
                                }
                            }
                        }
                        else if(method.equals("FILE"))
                        {
                            synchronized(this)
                            {
                                System.out.println("File Request");
                                String to =inFromClient.readLine();
                                int bytesRead;

                                DataInputStream clientData = new DataInputStream(connectionSocket.getInputStream());

                                String fileName = clientData.readUTF();
                                OutputStream output = new FileOutputStream((fileName));
                                long size = clientData.readLong();
                                byte[] buffer = new byte[1024];
                                while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
                                {
                                    output.write(buffer, 0, bytesRead);
                                    size -= bytesRead;
                                }

                                System.out.println("File "+fileName+" received from " + email);

                                for(int i=0;i<connectedClients.size();i++)
                                {
                                    if (connectedClients.get(i).equals(to))
                                    {

                                        DataOutputStream outToClient1 = new DataOutputStream(connectedSockets.get(i).getOutputStream());
                                        outToClient1.writeBytes("FILE"+"\n");
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
                                        outToClient1.writeUTF(myFile.getName());
                                        outToClient1.writeLong(mybytearray.length);
                                        outToClient1.write(mybytearray, 0, mybytearray.length);
                                        outToClient1.flush();                                  

                                    }
                                }
                            }
                            
                        }
                        else if(method.equals("MSG"))
                        {
                            synchronized(this)
                            {
                                System.out.println("MSG Request");
                                Msg msg1 = new Msg();
                                msg1.from=inFromClient.readLine();
                                msg1.to=inFromClient.readLine();
                                msg1.text=inFromClient.readLine();
                                    System.out.println("TO: "+msg1.to);
                                    System.out.println("FROM: "+msg1.from);
                                    System.out.println("TXT: "+msg1.text);
                                    System.out.println("Connected Clients: " + Integer.toString(connectedClients.size()));
                                String[] TO = null;
                                boolean isGroupMsg = true;
                                if(msg1.to.contains(","))
                                {
                                    TO = msg1.to.split(",");
                                }
                                else
                                {
                                    TO = new String [1];
                                    TO[0] = msg1.to;
                                    isGroupMsg = false;
                                }
                                for(int i=0, j = 0;i<connectedClients.size();i++)
                                {
                                    if (connectedClients.get(i).equals(TO[j]))
                                    {
                                        DataOutputStream outToClient1 = new DataOutputStream(connectedSockets.get(i).getOutputStream());

                                        outToClient1.writeBytes("MSG"+'\n');
                                        if(!isGroupMsg)
                                        {
                                            outToClient1.writeBytes(msg1.from+'\n');
                                            outToClient1.writeBytes(msg1.to+'\n');
                                            outToClient1.writeBytes(msg1.text+'\n');
                                            break;
                                        }
                                        else
                                        {
                                            if(!msg1.from.equals(TO[j]))
                                            {
                                                outToClient1.writeBytes(msg1.to+'\n');  //it is from
                                                outToClient1.writeBytes(TO[j]+'\n');
                                                outToClient1.writeBytes(msg1.from +": "+msg1.text+'\n');  
                                                System.out.println("TO: "+msg1.to);
                                                System.out.println("FROM: "+TO[j]);
                                                System.out.println("TXT: "+msg1.text);
                                            }
                                        }

                                        
                                        ++j;
                                    
                                    }
                                }
                            }
                        }       
                    }
                    catch (IOException e)
                    {

                    } catch (SQLException ex) {
                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally
                {
                    /*try {
                            connectionSocket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                }            
            }
                
               } catch (IOException ex) {
                Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
