/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kaeru
 */
public class DestinationAgentTable implements Serializable{
    private Map<Object, List<Object>> destTable;
    private List<Object> idList;
    private Integer size = 10;
    
    public DestinationAgentTable(List agentList, Integer size){
        this.size = size;
        destTable = createTable(agentList);
        idList = agentList;
    }
    
    private Map createTable(List<Object> agentList){
        Map<Object, List> table = new ConcurrentHashMap<>();
        
        for(int i=0; i < agentList.size(); i++){
            Object agListID = agentList.get(i % size);
            if(table.get(agListID) == null){
                table.put(agentList.get(i), new ArrayList<>());
            }
                
            List destAgList = table.get(agListID);
            destAgList.add(agentList.get(i));
            
            table.put(agListID, destAgList);
        }
        
        return table;
    }

    public Object getDestAgentID(Object id){
        Integer hashID = Math.abs(id.hashCode());
        
        List destAgList = destTable.get(idList.get(hashID % destTable.size()));
        
        return destAgList.get(hashID % destAgList.size());
    }
    
    public Object getDestAgentID(Object id, Object uid){
        Integer hashID = Math.abs(id.hashCode());
        Integer uhashID = Math.abs(uid.hashCode());
        
        List destAgList = destTable.get(idList.get(hashID % destTable.size()));
        
        return destAgList.get(uhashID % destAgList.size());
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Destination Agent Table {\n");
        for(Object id : destTable.keySet()){
            sb.append("\t");
            sb.append(id);
            sb.append(":");
            sb.append(destTable.get(id));
            sb.append("\n");
        }
        sb.append("}");
        
        return sb.toString();
    }
}
