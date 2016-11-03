/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.manager;

import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.manager.AgentManager;

/**
 *
 * @author kaeru
 */
public class AggregateAgentManager extends AgentManager{
    private static AggregateAgentManager manager = new AggregateAgentManager();
    public static AggregateAgentManager getInstance(){
        return manager;
    }
    
    @Override
    public void setDestinationServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private AgentConnection ag;
    @Override
    public void setDestinationAgent() {
        //Test
        this.ag = new AgentConnection(8, new String[]{"localhost:2809", "rdarank", "agent"});
    }
    
    @Override
    public AgentConnection getDestinationAgent() {
        return this.ag;
    }

    @Override
    public void setAgentParameter(Map param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createAgent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
