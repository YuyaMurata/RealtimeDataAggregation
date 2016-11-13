/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.exec;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentSystemUpdator  implements AgentExecutor, Serializable{
    public static enum paramID{
        AGENT_LISTS
    }

    public AgentSystemUpdator() {
    }
    
    String agID;
    List data;
    public AgentSystemUpdator(String agID, List data) {
        this.agID = agID;
        this.data = data;
    }

    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        Boolean result = extension.updateAgent(agID, data);
        
        String msg = agID + " : Update Agent = "+result;
        return msg;
    }
    
    public String updator(AgentClient client, String agID, List data){
        try {
            
            AgentSystemUpdator executor = new AgentSystemUpdator(agID, data);
            String region = client.getRegionNameFor(agID);
                
            Object reply = client.executeAt(region, executor);
            
            String msg = "Update Agent : Reply is " + reply;
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }      
    }
}
