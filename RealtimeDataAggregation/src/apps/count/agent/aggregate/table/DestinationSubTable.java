/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.table;

import apps.count.appuser.UserProfile;
import bench.template.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.table.DestinationTable;

/**
 *
 * @author kaeru
 */
public class DestinationSubTable extends DestinationTable{

	public DestinationSubTable(Object[] serverInfo) {
		super(serverInfo);
	}
	
	@Override
	public void createTable(Object[] serverInfo){
		super.ageMap = new TreeMap();
		super.agentMap = new ConcurrentHashMap();
		
		Map agents = (Map) serverInfo[0];
		List<Integer> range = (List<Integer>) serverInfo[1];
		
		List topAgents = (List) agents.get("top");
		List bottomAgents = (List) agents.get("bottom");
		super.agentList = new ArrayList();
		super.agentList.addAll(topAgents);
		if(bottomAgents != null)
			super.agentList.addAll(bottomAgents);
		
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
	
	@Override
	public Object getDestAgentID(Object uid, Integer age){
		Integer hashID = Math.abs(uid.hashCode());
		//System.out.println(age +" = "+ageMap.floorEntry(age).getValue());
		Object destAg = ageMap.floorEntry(age).getValue();
		List list = null;
		try{
			list = (List) agentMap.get(destAg);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("destAg="+agentMap.get(destAg));
		}
		return list.get(hashID % list.size());
	}
	
	public Object getDestRootAgentID(Integer age){
		//System.out.println(age +" = "+ageMap.floorEntry(age).getValue());
		Object destAg = ageMap.floorEntry(age).getValue();
		return destAg;
	}
	
	private AgentProfileGenerator prof;
	@Override
	public void setTableInfo(AgentProfileGenerator prof){
		this.prof = prof;
	}
	
	@Override
	public Map<Object, List> repack(List data){
		List<UserData> udata = data;
		Object map = udata.stream()
				.collect(Collectors.groupingBy(user -> getDestAgentID(user.id, (Integer) prof.generate(user.id).get(UserProfile.profileID.AGE))));
		
		/*System.out.println("Repack::");
		for(Object id : ((Map)map).keySet()){
			System.out.println(id+":"+((Map)map).get(id));
		}
		System.out.println("");
		*/
		return (Map<Object, List>) map;
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
