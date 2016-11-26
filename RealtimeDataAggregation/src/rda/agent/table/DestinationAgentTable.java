/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kaeru
 */
public class DestinationAgentTable {
    private static DestinationAgentTable table = new DestinationAgentTable();
    public static DestinationAgentTable getInstance(){
        return table;
    }
    
    private static Integer size = 10;
    public static void setParameter(Integer indexSize){
        size = indexSize;
    }
    
    private Map<Object, List<Object>> destTable;
    private List<Object> idList;
    public void createTable(List<Object> agentList){
        destTable = new ConcurrentHashMap<>();
        idList = new ArrayList<>();
        for(int i=0; i < agentList.size(); i++){
            Object agListID = agentList.get(i % size);
            if(destTable.get(agListID) == null){
                idList.add(agListID);
                destTable.put(agentList.get(i), new ArrayList<>());
            }
                
            List destAgList = destTable.get(agListID);
            destAgList.add(agentList.get(i));
            
            destTable.put(agListID, destAgList);
        }
    }
    
    public Object getDestAgentID(Object id){
        Integer hashID = id.hashCode();
        List destAgList = destTable.get(idList.get(hashID % size));
        return destAgList.get(hashID % destAgList.size());
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
