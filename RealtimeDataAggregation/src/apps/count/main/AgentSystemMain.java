/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.agent.aggregate.creator.CreateAggregateAgent;
import apps.count.agent.aggregate.reader.ReadAggregateAgent;
import apps.count.agent.aggregate.updator.UpdateAggregateAgent;
import apps.count.manager.AggregateAgentManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.deletor.Dispose;

/**
 *
 * @author kaeru
 */
public class AgentSystemMain {
    public static void main(String[] args) {
        //Test
        AggregateAgentManager manager = AggregateAgentManager.getInstance();
        manager.setDestinationAgent();
        
        //Create Test
        CreateAggregateAgent creator = new CreateAggregateAgent();
        for(int i=0; i < 10; i++){
            Map setter = new HashMap();
            setter.put("ID", "Agent#00"+i);
            creator.create(setter);
        }
        
        //Update Test
        UpdateAggregateAgent updator = new UpdateAggregateAgent();
        for(int i=0; i < 10; i++){
            List msgdata = new ArrayList();
            List data = new ArrayList();
            for(int j=0; j < i; j++) data.add(1);
            
            msgdata.add(data);
            
            updator.update("Agent#00"+i, data);
        }
        
        //Read Test
        ReadAggregateAgent reader = new ReadAggregateAgent();
        for(int i=0; i < 10; i++){
            long d = (long) reader.read(manager.getDestinationAgent(), "Agent#00"+i);
            System.out.println("Agent#00"+i+" = "+d);
        }
        
        //Delete Test
        Dispose deletor = new Dispose();
        deletor.delete(manager.getDestinationAgent());
    }
}
