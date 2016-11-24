/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.stream;

import apps.count.agent.aggregate.extension.ExtensionPutMessageQueue;
import com.ibm.agent.exa.client.AgentClient;
import rda.agent.client.AgentConnection;
import rda.control.flow.Window;
import rda.control.flow.WindowController;

/**
 *
 * @author kaeru
 */
public class WindowStream implements Runnable{
    private WindowController flow;
    private AgentConnection agcon;
    
    public WindowStream(WindowController flow, AgentConnection agcon) {
        this.flow = flow;
        this.agcon = agcon;
    }
    
    private static Boolean runnable;
    public static void setRunnable(Boolean state){
        runnable = state;
    }
    
    @Override
    public void run() {
        ExtensionPutMessageQueue agUpdate = new ExtensionPutMessageQueue();
        
        while(runnable){
            //Get Window
            Window window = flow.get();
            
            //Update
            AgentClient client = agcon.getClient();
            agUpdate.update(client, window.id, window.unpack());
            agcon.returnConnection(client);
        }
    }
    
}
