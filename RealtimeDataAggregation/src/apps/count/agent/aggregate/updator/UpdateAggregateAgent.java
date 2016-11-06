/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.updator;

import apps.count.manager.AggregateAgentManager;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import rda.agent.client.AgentConnection;
import rda.agent.updator.AgentUpdator;

/**
 *
 * @author kaeru
 */
public class UpdateAggregateAgent extends AgentUpdator{ 
    private static final String AGENT_TYPE = "aggregateagent";
    private static final String MESSAGE_TYPE = "updateAggregateAgent";

    public UpdateAggregateAgent() {
    }
    
    public UpdateAggregateAgent(AgentKey agentKey, String agID, List data) {
        super(agentKey, MESSAGE_TYPE, agID, data);
    }
    
    @Override
    public void update(String agID, List data) {
        if(data == null) return;
        
        try {
            AgentConnection ag = AggregateAgentManager.getInstance().getDestinationAgent();
            AgentClient client = ag.getClient();
            
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});
                
            UpdateAggregateAgent executor = new UpdateAggregateAgent(agentKey, agID, data);
            
            Object reply = client.execute(agentKey, executor);
            
            System.out.println("Update "+agID+" = "+reply);
            
            ag.returnConnection(client);
        } catch (AgentException e) {
            e.printStackTrace();
        } 
    }
    
}
