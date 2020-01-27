/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeChat;

import java.util.Vector;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *  This thread is FX based and updates VBox of chats
 * @author umarc
 */
public class Thread_UpdateDisplay extends Thread{
    final Vector <VBox> ab;
    final Label l;
    final int i;
    final Button button;
    final VBox vBox1;
    final int index;
    public Thread_UpdateDisplay(int index, Vector<VBox> chatScreens, Label recvMsg, int i, Button button, VBox vBox1) {
       this.ab = chatScreens;
       this.l= recvMsg;
       this.i = i;
       this.button = button;
       this.index = index;
       this.vBox1 = vBox1;
    }
    
    @Override
    public void run()
    {
        
        synchronized(this)
        {
            //System.out.println("Index = " + Integer.toString(index));
            if(index == 0)
                ab.get(i).getChildren().add(l);
            else if(index == 1)
                vBox1.getChildren().add(button);
            else if(index == 2)
                button.fire();
            else if(index == 3)
                vBox1.getChildren().remove(i);
            //else if(index == 4)
                //LoginController.stage = new Stage();
        }
        
    }
}
