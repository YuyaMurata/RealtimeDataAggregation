/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.deploy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import rda.agent.client.AgentConnection;
import rda.agent.deploy.DeployStrategy;

/**
 *
 * @author kaeru
 */
public class AppCountDeployStrategy extends DeployStrategy{
	private Integer pattern;
	private String idRule;
	private Map<String, TreeMap> aggregateAgentToServerMap = new HashMap();
	
	public AppCountDeployStrategy(Integer pattern, String idRule, List agents) {
		super();
		this.pattern = pattern;
		this.idRule = idRule.split("#")[0];
		super.setAgentList(this.idRule, agents);
	}

	@Override
	public void createDeployPattern(Map deployMap) {
		List servers = (List) deployMap.get(idRule);
		Integer amountAgents = (Integer) super.agentMap.get(idRule);
		TreeMap map = new TreeMap();
		Integer th = 0;
		map.put(th, servers.get(0));
		for(int i=1; i < servers.size(); i++){
			th = th + (amountAgents / servers.size());
			if(i < (amountAgents % servers.size())) th += 1;
			map.put(th, servers.get(i));
		}
		aggregateAgentToServerMap.put(idRule, map);
	}

	@Override
	public AgentConnection getDeployServer(Object agID) {
		String agRule = agID.toString().split("#")[0];
		Integer agNo = Integer.parseInt(agID.toString().split("#")[1]);
		
		TreeMap map = aggregateAgentToServerMap.get(agRule);
		return (AgentConnection) map.floorEntry(agNo).getValue();
	}
	
	public String toString(){
		if(pattern == 0)
			return "AgentCloning : "+aggregateAgentToServerMap;
		else
			return "AgentTyping : "+aggregateAgentToServerMap;
	}
}
