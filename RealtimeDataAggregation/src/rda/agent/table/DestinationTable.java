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
import java.util.concurrent.ConcurrentHashMap;
import rda.agent.profile.AgentProfileGenerator;

/**
 *
 * @author kaeru
 */
public abstract class DestinationTable implements Serializable {
	public enum paramID{
		DEST_TABLE_SIZE
	}
	
	private TreeMap ageMap = new TreeMap();
	private Map agentMap = new ConcurrentHashMap();
	
	public DestinationTable(Object[] serverInfo) {
		createTable(serverInfo);
	}
	
	public abstract void createTable(Object[] serverInfo);
	
	public abstract Object getDestAgentID(Object uid, Integer age);
	
	public abstract void setTableInfo(AgentProfileGenerator prof);
	
	public abstract Map repack(List data);
}
