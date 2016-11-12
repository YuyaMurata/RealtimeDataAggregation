/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.exec;

import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import java.util.Map;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentSystemCreator {
    public static enum paramID{
        REGION_NAME, AGENT_LISTS
    }
    
    public String creator(AgentClient client, Map param){
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        
        StringBuilder sb = new StringBuilder();
        for(String agID : (List<String>)param.get(paramID.AGENT_LISTS)){
            String msg = extension.createAgent(client, agID);
            
            sb.append(msg);
            sb.append("\n");
        }
        
        return "Creator AgentSystemExtension : Reply is " + sb.toString();
    }
}
