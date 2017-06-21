/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kaeru
 */
public class DestinationSubTable {
	private TreeMap ageMap = new TreeMap();
	private Map agentMap = new ConcurrentHashMap();

	public DestinationSubTable(Object[] serverInfo) {
		createAgeTable(serverInfo);
	}
	
	public void createAgeTable(Object[] serverInfo){
		Map agents = (Map) serverInfo[0];
		List<Integer> range = (List<Integer>) serverInfo[1];
		
		List topAgents = (List) agents.get("top");
		List bottomAgents = (List) agents.get("bottom");
		
		int th = (range.get(1) - range.get(0)) / topAgents.size();
		for(int i=0; i < topAgents.size(); i++){
			ageMap.put(range.get(0)+i*th, topAgents.get(i));
			
			List agentList = new ArrayList();
			agentList.add(topAgents.get(i));
			agentMap.put(topAgents.get(i), agentList);
		}
		
		if(bottomAgents != null){
			for(int i=0; i < bottomAgents.size(); i++){
				List agentList = (List) agentMap.get(topAgents.get(i % topAgents.size()));
				agentList.add(bottomAgents.get(i));
			}
		}
	}
	
	public Object getDestAgentID(Object uid, Integer age){
		Integer hashID = Math.abs(uid.hashCode());
		//System.out.println(age +" = "+ageMap.floorEntry(age).getValue());
		Object destAg = ageMap.floorEntry(age).getValue();
		List agentList = (List) agentMap.get(destAg);
		return agentList.get(hashID % agentList.size());
	}
	
	public Object getDestRootAgentID(Integer age){
		//System.out.println(age +" = "+ageMap.floorEntry(age).getValue());
		Object destAg = ageMap.floorEntry(age).getValue();
		return destAg;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Age-RootAgent:\n");
		for(Object  age :ageMap.keySet()){
			sb.append("\t");
			sb.append(age);
			sb.append(":");
			sb.append(ageMap.get(age));
			sb.append("\n");
		}
		
		sb.append("RootAgent-Agent:\n");
		for(Object  agent :agentMap.keySet()){
			sb.append("\t");
			sb.append(agent);
			sb.append(":");
			sb.append(agentMap.get(agent));
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
