/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.message;

import com.ibm.agent.exa.Message;

/**
 *
 * @author kaeru
 */
public class InitMessage extends Message{
    //
    public Object msgdata;
    
    public void setParams(Object msgdata) {
        this.msgdata = msgdata;
    }
}
