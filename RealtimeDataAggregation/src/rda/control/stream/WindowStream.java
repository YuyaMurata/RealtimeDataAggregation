/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.stream;

import rda.extension.agent.exec.ExtensionPutMessageQueue;
import com.ibm.agent.exa.client.AgentClient;
import rda.agent.client.AgentConnection;
import rda.control.flow.Window;
import rda.control.flow.WindowController;

/**
 *
 * @author kaeru
 */
public class WindowStream extends Thread{
    private WindowController flow;
    private AgentConnection agcon;
    private ExtensionPutMessageQueue sender;
    
    public WindowStream(WindowController flow, AgentConnection agcon, ExtensionPutMessageQueue sender) {
        this.flow = flow;
        this.agcon = agcon;
        this.sender = sender;
    }
    
    private static Boolean runnable;
    public static void setRunnable(Boolean state){
        runnable = state;
        WindowController.setRunnable(state);
    }
    
    @Override
    public void run() {
        while(runnable){
            //Get Window
            Window window = flow.get();
            if(window == null) continue;
            
            //Update
            AgentClient client = agcon.getClient();
            String msg = sender.send(client, window.id, window.unpack());
            agcon.returnConnection(client);
        }
    }
    
}
