/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.deploy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;

/**
 *
 * @author kaeru
 */
public abstract class DeployStrategy {
	//public Map agentMap = new HashMap();
	//public void setAgentList(String idRule, List agents){
	//	agentMap.put(idRule, agents.size());
	//};
	//public abstract void createDeployPattern(Map deployMap);
	public abstract Map createDeployPattern(int strategy, List server, List agents, int size);
	//public abstract AgentConnection getDeployServer(Object agID);
}
