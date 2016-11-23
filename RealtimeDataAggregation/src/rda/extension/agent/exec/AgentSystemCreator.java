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
import java.util.Map;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentSystemCreator implements AgentExecutor, Serializable {
    public static enum paramID{
        AGENT_LISTS
    }

    public AgentSystemCreator() {
    }
    
    String agID;
    public AgentSystemCreator(String agID) {
        this.agID = agID;
    }

    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        String msg = extension.createAgent(agID);
        
        return msg;
    }
    
    public static String AGENT_TYPE;
    public String creator(AgentClient client, Map param){
        try {
            StringBuilder sb = new StringBuilder();
            
            for(String id : (List<String>) param.get(paramID.AGENT_LISTS)){
                AgentSystemCreator executor = new AgentSystemCreator(id);
                String region = client.getRegionNameFor(id);
                
                Object reply = client.executeAt(region, executor);
                
                sb.append(reply);
                sb.append("\n");
            }
            
            String msg = "Create AgentSystemExtension : Reply is " + sb.toString();
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }      
    }
}
