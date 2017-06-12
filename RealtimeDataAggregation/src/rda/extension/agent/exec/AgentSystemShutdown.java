/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.exec;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentSystemShutdown implements AgentExecutor, Serializable {

    public static enum paramID{
        REGION_NAME, AGENT_PROFILE, AGENT_CREATOR, AGENT_UPDATOR
    }

    public AgentSystemShutdown() {
    }
    

    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        String msg = extension.stopAgentSystem();
        
        AgentManager am = AgentManager.getAgentManager();
        String regionName = am.getRegionName();
        
        return regionName + " : " + msg;
    }
    
    public String shutdown(AgentClient client){
        try {
            AgentSystemShutdown executor = new AgentSystemShutdown();

            Object reply = client.execute(executor);

            String msg = "<"+client + ">Stopped AgentSystemExtension : Reply is " + reply;
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }
    }
}
