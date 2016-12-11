package apps.count.agent.aggregate.table;

import java.util.List;
import rda.agent.table.DestinationAgentTable;

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
