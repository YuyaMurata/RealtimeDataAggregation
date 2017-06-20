/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.table;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author kaeru
 */
public class DestinationSubTable {
	private TreeMap ageMap = new TreeMap();
	public void createAgeTable(Object[] serverInfo){
		Map agents = (Map) serverInfo[0];
		List<Integer> range = (List<Integer>) serverInfo[1];
		
		List topAgents = (List) agents.get("top");
		List bottomAgents = (List) agents.get("bottom");
		
		int th = (range.get(1) - range.get(0)) / topAgents.size();
		for(int i=0; i < topAgents.size(); i++)
			ageMap.put(range.get(0)+i*th, topAgents.get(i));
	}
	
	public Object getDestAgentID(Object uid, Integer age){
		//Integer hashID = Math.abs(uid.hashCode());
		//System.out.println(age +" = "+ageMap.floorEntry(age).getValue());
		Object destAg = ageMap.floorEntry(age).getValue();
		return destAg;
	}
}
