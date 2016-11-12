/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.agent.aggregate.creator.CreateAggregateAgent;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.agent.aggregate.reader.ReadAggregateAgent;
import apps.count.agent.aggregate.updator.UpdateAggregateAgent;
import apps.count.manager.AggregateAgentManager;
import com.ibm.agent.exa.client.AgentClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.deletor.Dispose;
import rda.agent.profile.AgentProfileGenerator;
import rda.extension.agent.exec.AgentSystemCreator;
import rda.extension.agent.exec.AgentSystemInitializer;

/**
 *
 * @author kaeru
 */
public class AgentSystemMain {
    public static void main(String[] args) {
        //Test
        AggregateAgentManager manager = AggregateAgentManager.getInstance();
        manager.setDestinationAgent();
        
        //Create Agent ID
        List agIDLists = new ArrayList();
        for(int i=0; i < 10; i++)
            agIDLists.add("Agent#00"+i);
        
        //Profile
        AgentProfileGenerator agentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
        
        //Client
        AgentConnection ag = manager.getDestinationAgent();
        AgentClient client = ag.getClient();
        
        //Extension Initialize
        Map param = new HashMap();
        AgentSystemInitializer agInit = new AgentSystemInitializer();
        CreateAggregateAgent creator = new CreateAggregateAgent();
        param.put(AgentSystemInitializer.paramID.REGION_NAME, "");
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, creator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, agentProf);
        Object msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Create Agent
        AgentSystemCreator agCreate = new AgentSystemCreator();
        param = new HashMap();
        param.put(AgentSystemCreator.paramID.AGENT_LISTS, agIDLists);
        agCreate.creator(client, param);
        
        //Client
        ag.returnConnection(client);
        
        /*//Update Test
        UpdateAggregateAgent updator = new UpdateAggregateAgent();
        for(String agID : (List<String>)agIDLists){
            List msgdata = new ArrayList();
            List data = new ArrayList();
            data.add(1);
            
            msgdata.add(data);
            
            updator.update(agID, msgdata);
        }
        
        //Read Test
        ReadAggregateAgent reader = new ReadAggregateAgent();
        for(String agID : (List<String>)agIDLists){
            Object d = reader.read(manager.getDestinationAgent(), agID);
            System.out.println("Read "+agID+" = "+d);
        }
        
        //Delete Test
        Dispose deletor = new Dispose();
        deletor.delete(manager.getDestinationAgent());
        */
    }
}
