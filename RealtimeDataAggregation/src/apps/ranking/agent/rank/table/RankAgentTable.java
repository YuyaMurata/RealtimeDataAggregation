package apps.ranking.agent.rank.table;

import java.util.List;
import rda.agent.table.DestinationAgentTable;

public class RankAgentTable extends DestinationAgentTable{

    public RankAgentTable(List agentList, Integer size) {
        super(agentList, size);
    }

    @Override
    public Object getDestAgentID(Object id) {
        Integer hashID = Math.abs(id.hashCode());
        
        List destAgList = destTable.get(idList.get(hashID % destTable.size()));
        
        return destAgList.get(hashID % destAgList.size());
    }
    
}
