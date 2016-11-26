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
public class AgentSystemLaunch implements AgentExecutor, Serializable {
    public AgentSystemLaunch() {
    }
    

    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        String msg = extension.startAgentSystem();
        
        AgentManager am = AgentManager.getAgentManager();
        String regionName = am.getRegionName();
        
        return regionName + " : " + msg;
    }
    
    public String launch(AgentClient client){
        try {
            AgentSystemLaunch executor = new AgentSystemLaunch();

            Object reply = client.execute(executor);

            String msg = "Start AgentSystemExtension : Reply is " + reply;
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }
    }
}
