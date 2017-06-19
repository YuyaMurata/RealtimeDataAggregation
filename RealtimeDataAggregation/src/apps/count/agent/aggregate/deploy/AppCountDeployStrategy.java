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
import java.util.stream.IntStream;
import rda.agent.deploy.DeployStrategy;

/**
 *
 * @author kaeru
 */
public class AppCountDeployStrategy extends DeployStrategy{
	private String idRule;
	private Map serverToAgentMap;
	private Map agentToServerMap;
	
	public AppCountDeployStrategy(String idRule, List agents) {
		super();
		super.setAgentList(idRule, agents);
		this.idRule = idRule;
	}
	
	private int pattern;
	@Override
	public Map createDeployPattern(int strategy, List servers, int size){
		this.pattern = strategy;
		List agents = (List) super.agentTypeMap.get(this.idRule);
		
		List top, bottom;
		if(agents.size() > size){
			top = agents.subList(0, size);
			bottom = agents.subList(size, agents.size());
		}else{
			top = agents;
			bottom = null;
		}
		
		List divTop = divideList(top, servers.size());
		List divBottom = null;
		if(bottom != null){
			divBottom = divideList(bottom, servers.size());
			System.out.println(divBottom);
		}
		
		Map serverCreateMap = new HashMap();
		Map agentCreateMap = new HashMap();
		for(int i = 0; i < servers.size(); i++){
			Map map = new HashMap();
			map.put("top", divTop.get(i));
			if(bottom != null)
				map.put("bottom", divBottom.get(i));
			
			serverCreateMap.put(servers.get(i), map);
			agentCreateMap.put(divTop.get(i), servers.get(i));
		}
		
		this.serverToAgentMap = serverCreateMap;
		this.agentToServerMap = agentCreateMap;
		
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
			return "AgentCloning : "+serverToAgentMap;
		else
			return "AgentTyping : "+serverToAgentMap;
	}

	@Override
	public Map getServerMap() {
		return this.serverToAgentMap;
	}

	@Override
	public Map getAgentMap() {
		return this.agentToServerMap;
	}
}
