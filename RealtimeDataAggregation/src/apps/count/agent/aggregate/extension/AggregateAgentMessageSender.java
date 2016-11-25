/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.extension;

import rda.extension.agent.exec.ExtensionPutMessageQueue;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;

/**
 *
 * @author kaeru
 */
public class AggregateAgentMessageSender extends ExtensionPutMessageQueue{
    private static final String AGENT_TYPE = "aggregateagent";

    public AggregateAgentMessageSender() {
    }

    public AggregateAgentMessageSender(AgentKey agentKey, List data) {
        super(agentKey, data);
    }
    
    @Override
    public String send(AgentClient client, String agID, List data) {
        try {
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});
            
            AggregateAgentMessageSender executor = new AggregateAgentMessageSender(agentKey, data);
            
            Object reply = client.execute(agentKey, executor);
            
            String msg = "Update Agent : Reply is " + reply;
            
            System.out.println(msg+" "+agID+" = "+data);
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }
    }
}
