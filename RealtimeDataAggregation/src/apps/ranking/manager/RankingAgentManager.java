/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.manager;

import apps.count.property.AppCountProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.manager.AgentManager;

/**
 *
 * @author kaeru
 */
public class RankingAgentManager extends AgentManager{
    public enum paramID{
        ID_RULE, AMOUNT_AGENTS
    }
    
    private static RankingAgentManager manager = new RankingAgentManager();
    
    private List agentLists;
    private RankingAgentManager() {
        AppCountProperty prop = AppCountProperty.getInstance();
        preparedAgentSystem(
                (Integer)prop.getParameter(paramID.AMOUNT_AGENTS), 
                (String) prop.getParameter(paramID.ID_RULE));
    }
    
    private void preparedAgentSystem(Integer n, String rule){
        agentLists = new ArrayList();
        for(int i=0; i < n; i++)
            agentLists.add(rule+i);
    }
    
    public static RankingAgentManager getInstance(){
        return manager;
    }
    
    public List getAgentList(){
         if(agentLists.isEmpty())
            System.out.println("Do not set parameters!");
        return agentLists;
    }
    
    @Override
    public void setDestinationServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private AgentConnection ag;
    @Override
    public void setDestinationAgent() {
        //Test
        this.ag = new AgentConnection(8, new String[]{"localhost:2809", "apps.count", "agent"});
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
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
