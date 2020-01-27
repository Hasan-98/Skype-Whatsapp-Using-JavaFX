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
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
public class TCPServer 
{
	public static void main(String argv[]) throws IOException, SQLException
	{
		User user = new User();
		//user.makeTable();
		Friend friend = new Friend();
		//friend.insert(11, 12);
		//friend.makeTable();
		Message message = new Message();
		//message.makeTable();
	
		//message.insert(1, 2, "Bye");
		Vector<Socket> connectedSockets = new Vector<Socket>();
		Vector connectedClients = new Vector();
		String email, password, method;
		ServerSocket welcomeSocket = new ServerSocket(6789);
                Vector<Vector<String>> Groups = new Vector<Vector<String>>();//contains vector of groups
		//System.out.println("Listening.......");
		
		while(true)
		{
			Socket connectionSocket1 = welcomeSocket.accept();
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket1.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket1.getOutputStream());
			email = inFromClient.readLine();	
                        if(email.equals("signin"))
                        {
                            email = inFromClient.readLine();
                            password = inFromClient.readLine();
                            boolean registered = user.verify(email, password);
                            if(registered)
                            {
                                    outToClient.writeBytes("True"+"\n");
                                    user.online(email);
                                    //System.out.println(user.getFriendCount(email));
                                    //outToClient.writeBytes(user.getFriendCount(email)+"\n");
                                    boolean online=true;
                                    for (int i=0;i<connectedClients.size();i++)
                                    {
                                        if(connectedClients.get(i).equals(email))
                                        {
                                            online=false;
                                            connectedSockets.add(i, connectionSocket1);
                                        }
                                    }
                                    if(online)
                                    {
                                        connectedClients.add(email);
                                        connectedSockets.add(connectionSocket1);
                                    }
                                    //System.out.println("online:" + user.getFriendCount(email));
                                    
                                    //
                                    //
                                    //outToClient=null;
                                    thread th = new thread(connectionSocket1, email,connectedSockets, connectedClients, message, user, friend, outToClient, Groups);
                                    th.start();	
                            }
                            else
                            outToClient.writeBytes("False"+"\n");
                        }
                        else
                        {
                            String name;
                            name = inFromClient.readLine();
                            email = inFromClient.readLine();
                            password = inFromClient.readLine();
                            boolean inserted = user.insert(name, email, null, 0, "local host", false, password, null);
                            //System.out.println("ok:" +inserted);
                            if(inserted)
                                    outToClient.writeBytes("True"+"\n");
                            else
                                    outToClient.writeBytes("False"+"\n");
                        }
			//connectedSockets.add(connectionSocket1);
                        
			//connectedClints.add(id1);
			//Socket connectionSocket2 = welcomeSocket.accept();
			//connectedSockets.add(connectionSocket2);
			//connectedClints.add(id2);
                        	
		}
	}
}

    
