/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import rda.agent.client.AgentConnection;
import rda.agent.deploy.DeployStrategy;

/**
 *
 * @author kaeru
 */
public class AppCountDeployStrategy extends DeployStrategy{
	/*private Integer pattern, size;
	private String idRule;
	private Map<String, TreeMap> aggregateAgentToServerMap = new HashMap();
	private List agents;*/
	
	/*public AppCountDeployStrategy(Integer pattern, String idRule, List agents, Integer size) {
		super();
		this.pattern = pattern;
		this.size= size;
		this.idRule = idRule.split("#")[0];
		this.agents = agents;
		super.setAgentList(this.idRule, agents);
	}*/

	//@Override
	/*public void createDeployPattern(Map deployMap) {
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
	}*/
	
	private int pattern;
	public Map createDeployPattern(int strategy, List server, List agents, int size){
		this.pattern = strategy;
		
		List top, bottom;
		if(agents.size() > size){
			top = agents.subList(0, size);
			bottom = agents.subList(size, agents.size());
		}else{
			top = agents;
			bottom = null;
		}
		
		List divTop = divideList(top, server.size());
		System.out.println(divTop);
		List divBottom = null;
		if(bottom != null){
			divBottom = divideList(bottom, server.size());
			System.out.println(divBottom);
		}
		
		Map serverCreateMap = new HashMap();
		for(int i = 0; i < server.size(); i++){
			Map map = new HashMap();
			map.put("top", divTop.get(i));
			if(bottom != null)
				map.put("bottom", divBottom.get(i));
			serverCreateMap.put(server.get(i), map);
		}
		
		return serverCreateMap;
	}
	
	private List<List> divideList(List xs, int div){
		int q =xs.size() / div;
		int m = xs.size() % div;
		return IntStream.range(0, div).collect(
			ArrayList::new,
			(acc, i) -> {
				int fr = acc.stream().mapToInt(List::size).sum();
				int to = fr + q + ((i < m)? 1: 0);
				acc.add(xs.subList(fr, to));
			},
			ArrayList::addAll
		);
	}
	
	public String toString(){
		if(pattern == 0)
			return "AgentCloning : ";//+aggregateAgentToServerMap;
		else
			return "AgentTyping : ";//+aggregateAgentToServerMap;
	}
}
