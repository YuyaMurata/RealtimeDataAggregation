/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.user.creator;

import apps.ranking.agent.user.profile.UserAgentProfile;
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
public class CreateUserAgent extends AgentCreator {
    
    private static final String AGENT_TYPE = "useragent";
    private static final String MESSAGE_TYPE = "initUserAgent";

    public CreateUserAgent() {
        super();
    }

    public CreateUserAgent(AgentKey agentKey, Map state) {
        super(agentKey, MESSAGE_TYPE, state);
    }

    @Override
    public String create(AgentClient client, Map setter) {
        try {
            Object agID = setter.get(UserAgentProfile.paramID.ID);
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

            //Create Agent
            Map msgdata = (Map) setter.get(UserAgentProfile.paramID.MESSAG_DATA);
            CreateUserAgent executor = new CreateUserAgent(agentKey, msgdata);

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
        Object agID = setter.get(UserAgentProfile.paramID.ID);
        AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

        //Create Agent
        Map msgdata = (Map) setter.get(UserAgentProfile.paramID.MESSAG_DATA);
        CreateUserAgent executor = new CreateUserAgent(agentKey, msgdata);

        Object reply = executor.execute();
        
        String msg = "Create [" + agentKey + "] was created. Reply is [" + reply + "]";
        
        return msg;
    }
}
