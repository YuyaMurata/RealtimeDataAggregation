package apps.count.agent.aggregate.creator;

import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import java.util.Map;
import rda.agent.creator.AgentCreator;

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
            Object agID = setter.get(AggregateAgentProfile.paramID.ID);
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
        Object agID = setter.get(AggregateAgentProfile.paramID.ID);
        AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

        //Create Agent
        List msgdata = (List) setter.get(AggregateAgentProfile.paramID.MESSAG_DATA);
        CreateAggregateAgent executor = new CreateAggregateAgent(agentKey, msgdata);

        Object reply = executor.execute();
        
        String msg = "Create [" + agentKey + "] was created. Reply is [" + reply + "]";
        
        return msg;
    }
}
