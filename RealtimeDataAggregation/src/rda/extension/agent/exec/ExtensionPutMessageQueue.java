/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.exec;

import com.ibm.agent.exa.AgentKey;
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
public abstract class ExtensionPutMessageQueue implements AgentExecutor, Serializable{
    
    public ExtensionPutMessageQueue() {
    }
    
    AgentKey agentKey;
    List data;
    public ExtensionPutMessageQueue(AgentKey agentKey, List data) {
        this.agentKey = agentKey;
        this.data = data;
    }
    
    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        
        Boolean result = extension.updateAgent(agentKey.getValue(0), data);
        
        String msg = agentKey.getValue(0) + " : Update Agent = "+result;
        
        return msg;
    }
    
    public abstract String send(AgentClient client, String agID, List data);
}
