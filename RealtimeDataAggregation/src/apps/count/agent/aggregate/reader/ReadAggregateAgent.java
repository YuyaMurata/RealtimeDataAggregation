/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.reader;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.ArrayList;
import java.util.List;
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
    public Object read(AgentClient client, String agID) {
        try {
            Long start = System.currentTimeMillis();
            
            AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

            ReadAggregateAgent executor = new ReadAggregateAgent(agentKey);
            
            List reply = new ArrayList();
            reply.add(client.execute(agentKey, executor));
            
            Long stop = System.currentTimeMillis();
            
            reply.add((stop-start)+" ms");
            
            return reply;
        } catch (AgentException e) {
            return e.toString();
        }
    }
}
