/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.deploy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public abstract class DeployStrategy {
	public Map agentTypeMap = new HashMap();
	public void setAgentList(String idRule, List agents){
		agentTypeMap.put(idRule, agents);
	};
	
	public abstract Map createDeployPattern(int strategy, List servers, int size);
	
	public abstract Map getServerMap();
	public abstract Map getAgentMap();
	
	public abstract Map getAgents();
}
