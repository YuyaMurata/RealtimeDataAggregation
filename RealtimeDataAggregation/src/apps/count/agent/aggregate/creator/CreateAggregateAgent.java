/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.creator;

import apps.count.manager.AggregateAgentManager;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.creator.AgentCreator;

/**
 *
 * @author kaeru
 */
public class CreateAggregateAgent extends AgentCreator{
    private final String AGENT_TYPE = "aggregateagent";
    
    public CreateAggregateAgent() {
        super("initAggregateAgent");
    }
    
    public CreateAggregateAgent(AgentKey agentKey, List state){
        super(agentKey, state);
    }
    
    @Override
    public void create(Map setter) {
        try {
            AgentConnection ag = AggregateAgentManager.getInstance().getDestinationAgent();            
            AgentClient client = ag.getClient();
            
            String agID = (String)setter.get("ID");
            agentKey = new AgentKey(AGENT_TYPE,new Object[]{agID});
            
            //Create Agent
            List msgdata = new ArrayList();
            msgdata.add("Aggregate Conditios :"+agID);
            CreateAggregateAgent executor = new CreateAggregateAgent(agentKey, msgdata);
            
            Object reply = client.execute(agentKey, executor);
            
            System.out.println("Agent[" + agentKey + "] was created. Reply is [" + reply + "]");
            
            ag.returnConnection(client);
            
            //return mq;
        } catch (AgentException e) {
            //return null;
        }
    }
}
