/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.rank.creator;

import apps.ranking.agent.rank.profile.RankAgentProfile;
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
public class CreateRankAgent extends AgentCreator {
    
    private static final String AGENT_TYPE = "rankagent";
    private static final String MESSAGE_TYPE = "initRankAgent";

    public CreateRankAgent() {
        super();
    }

    public CreateRankAgent(AgentKey agentKey, List state) {
        super(agentKey, MESSAGE_TYPE, state);
    }

    @Override
    public String create(AgentClient client, Map setter) {
        try {
            Object agID = setter.get(RankAgentProfile.paramID.ID);
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

            //Create Agent
            List msgdata = (List) setter.get(RankAgentProfile.paramID.MESSAG_DATA);
            CreateRankAgent executor = new CreateRankAgent(agentKey, msgdata);

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
        Object agID = setter.get(RankAgentProfile.paramID.ID);
        AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

        //Create Agent
        List msgdata = (List) setter.get(RankAgentProfile.paramID.MESSAG_DATA);
        CreateRankAgent executor = new CreateRankAgent(agentKey, msgdata);

        Object reply = executor.execute();
        
        String msg = "Create [" + agentKey + "] was created. Reply is [" + reply + "]";
        
        return msg;
    }
}
