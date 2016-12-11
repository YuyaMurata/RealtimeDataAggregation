/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.table;

import java.util.List;
import rda.agent.table.DestinationAgentTable;

/**
 *
 * @author kaeru
 */
public class AggregateAgentTable extends DestinationAgentTable{

    public AggregateAgentTable(List agentList, Integer size) {
        super(agentList, size);
    }
    
    @Override
    public Object getDestAgentID(Object id) {
        Integer hashID = Math.abs(id.hashCode());
        
        List destAgList = destTable.get(idList.get(hashID % destTable.size()));
        
        return destAgList.get(hashID % destAgList.size());
    }
    
}
