/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.table;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import rda.agent.table.DestinationAgentTable;

/**
 *
 * @author kaeru
 */
public class DestinationAppTable extends DestinationAgentTable{
	
	public DestinationAppTable(List agentList, Integer size) {
		super(agentList, size);
	}
	
	private TreeMap ageMap = new TreeMap();
	public void createAgeTable(int maxAge){
		Integer th = 0;
		Iterator it = super.getDestTable().keySet().iterator();
		for(int i = 0; i < super.getDestTable().size(); i++){
			th = i * (maxAge / super.getDestTable().size());
			Object agID = it.next();
			ageMap.put(th, agID);
		}
	}
	
	public Object getDestAgentID(Object uid, Integer age){
		Integer hashID = Math.abs(uid.hashCode());
		//System.out.println(age +" = "+ageMap.floorEntry(age).getValue());
		List destAgList = (List) getDestTable().get(ageMap.floorEntry(age).getValue());
		
		return destAgList.get(hashID % destAgList.size());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Destination Agent Table {\n");
		for (Object id : ageMap.keySet()) {
			sb.append("\t");
			sb.append(id);
			sb.append(":");
			sb.append(ageMap.get(id));
			sb.append("\n");
		}
		sb.append("}");

		return sb.toString();
	}
}
