/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.user.updator;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
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
    
    public UpdateAggregateAgent(AgentKey agentKey, List data) {
        super(agentKey, MESSAGE_TYPE, data);
    }
    
    @Override
    public void update(AgentClient client, Object agID, List data) {
        if(data == null) return;
        
        try {    
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});
                
            UpdateAggregateAgent executor = new UpdateAggregateAgent(agentKey, data);
            
            Object reply = client.execute(agentKey, executor);
            
            //System.out.println("Update "+agID+" = "+reply);
            
        } catch (AgentException e) {
            e.printStackTrace();
        } 
    }

    @Override
    public void update(Object agID, List data) {
        AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

        //Update Agent
        UpdateAggregateAgent executor = new UpdateAggregateAgent(agentKey, data);

        Object reply = executor.execute();
        
        String msg = "Update [" + agentKey + "] was updated. Reply is [" + reply + "]";
    }
    
}
