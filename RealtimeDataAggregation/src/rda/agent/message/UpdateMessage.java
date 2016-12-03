/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.message;

import com.ibm.agent.exa.Message;
import java.util.List;

/**
 *
 * @author kaeru
 */
public class UpdateMessage extends Message {
    private long time;
    public List msgdata;
    
    public void setParams(List msgdata) {
        this.msgdata = msgdata;
        this.time = System.currentTimeMillis();
    }
    
    public long latency(){
        return System.currentTimeMillis() - time;
    }
}
