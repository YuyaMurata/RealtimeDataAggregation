package apps.ranking.agent.user.table;

import java.util.List;
import rda.agent.table.DestinationAgentTable;

public class UserAgentTable extends DestinationAgentTable{

    public UserAgentTable(List agenList, Integer size) {
        super(agenList, size);
    }

    @Override
    public Object getDestAgentID(Object id) {
        List destAgList = destTable.get(id);
        return destAgList.get(0);
    }
}
