/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.creator;

import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import java.util.Map;
import rda.agent.creator.AgentCreator;

/**
 *
 * @author kaeru
 */
public class CreateAggregateAgent extends AgentCreator {

    private static final String AGENT_TYPE = "aggregateagent";
    private static final String MESSAGE_TYPE = "initAggregateAgent";

    public CreateAggregateAgent() {
        super();
    }

    public CreateAggregateAgent(AgentKey agentKey, List state) {
        super(agentKey, MESSAGE_TYPE, state);
    }

    @Override
    public String create(AgentClient client, Map setter) {
        try {
            String agID = (String) setter.get(AggregateAgentProfile.paramID.ID);
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

            //Create Agent
            List msgdata = (List) setter.get(AggregateAgentProfile.paramID.MESSAG_DATA);
            CreateAggregateAgent executor = new CreateAggregateAgent(agentKey, msgdata);

            Object reply = client.execute(agentKey, executor);
            
            String msg = "Create [" + agentKey + "] was created. Reply is [" + reply + "]";
            System.out.println(msg);

            return msg;
        } catch (AgentException e) {
            return e.toString();
        }
    }

    @Override
    public String create(Map setter) {
        String agID = (String) setter.get(AggregateAgentProfile.paramID.ID);
        AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

        //Create Agent
        List msgdata = (List) setter.get(AggregateAgentProfile.paramID.MESSAG_DATA);
        CreateAggregateAgent executor = new CreateAggregateAgent(agentKey, msgdata);

        Object reply = executor.execute();
        
        String msg = "Create [" + agentKey.getType()+", "+agentKey.getValues()+","+agentKey.toString() + "] was created. Reply is [" + reply + "]";
        
        return msg;
    }
}
