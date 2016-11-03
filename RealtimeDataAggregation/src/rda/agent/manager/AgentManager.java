/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.manager;

import java.util.Map;
import rda.agent.client.AgentConnection;

/**
 *
 * @author kaeru
 */
public abstract class AgentManager {
    public abstract void setDestinationServer();
    public abstract void setDestinationAgent();
    
    public abstract AgentConnection getDestinationAgent();
    
    public abstract void setAgentParameter(Map param);
    public abstract void createAgent();
    
    public abstract void shutdown();
}
