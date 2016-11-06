/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.reader;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import rda.agent.client.AgentConnection;
import rda.agent.reader.AgentReader;

/**
 *
 * @author kaeru
 */
public class ReadAggregateAgent extends AgentReader {

    private static final String AGENT_TYPE = "aggregateagent";
    private static final String MESSAGE_TYPE = "readAggregateAgent";

    public ReadAggregateAgent() {
    }
    
    public ReadAggregateAgent(AgentKey agentKey) {
        super(agentKey, MESSAGE_TYPE);
    }

    @Override
    public Object read(AgentConnection ag, String agID) {
        try {
            AgentClient client = ag.getClient();

            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

            ReadAggregateAgent executor = new ReadAggregateAgent(agentKey);

            Object reply = client.execute(agentKey, executor);

            ag.returnConnection(client);

            return reply;
        } catch (AgentException e) {
            return null;
        }
    }
}
