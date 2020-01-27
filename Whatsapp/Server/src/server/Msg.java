/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author saada
 */
public class Msg implements Serializable
{
    public String to, from;
    public String text;
    public Msg(){};
    public Msg(String t, String f, String te)
    {
        to = t;
        from = f;
        text = te;
    }
}
