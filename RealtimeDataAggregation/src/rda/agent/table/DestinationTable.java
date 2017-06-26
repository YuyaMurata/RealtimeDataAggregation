/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import rda.agent.profile.AgentProfileGenerator;

/**
 *
 * @author kaeru
 */
public abstract class DestinationTable implements Serializable {
	public enum paramID{
		DEST_TABLE_SIZE
	}
	
	public TreeMap ageMap;
	public Map agentMap;
	public List agentList;
	
	public DestinationTable(Object[] serverInfo) {
		createTable(serverInfo);
	}
	
	public abstract void createTable(Object[] serverInfo);
	
	public List getAgents(){
		return this.agentList;
	}
	
	public abstract Object getDestAgentID(Object uid, Integer age);
	
	public void updateTable(Object originalID, Object cloneID){
		agentMap.put(originalID, cloneID);
		agentList.add(cloneID);
	}
	
	public abstract void setTableInfo(AgentProfileGenerator prof);
	
	public abstract Map repack(List data);
}
