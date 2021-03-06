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
	public Map agentMap = new HashMap();
	public void setAgentList(String idRule, List agents){
		agentMap.put(idRule, agents);
	};
	
	public abstract Map createDeployPattern(int strategy, List server, int size);
	
	public abstract String getServerMap();
}
