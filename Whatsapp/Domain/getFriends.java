/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author saada
 */
public class getFriends extends Thread 
{
    final Button b1;

    getFriends(Button b1) throws IOException
    {
        this.b1 = b1;
    }
    public void run()
    {
        while (true)
        {
            synchronized(this)
            {   
                if(!LoginController.onGoingCall)
                {
                    Thread_UpdateDisplay t = new Thread_UpdateDisplay(2, null, null, -1, b1, null);
                    Platform.runLater(t);
                }
            }
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(getFriends.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        }
    }
}