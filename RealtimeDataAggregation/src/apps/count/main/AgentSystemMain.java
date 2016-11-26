/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.agent.aggregate.creator.CreateAggregateAgent;
import apps.count.agent.aggregate.extension.AggregateAgentMessageSender;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.agent.aggregate.reader.ReadAggregateAgent;
import apps.count.agent.aggregate.updator.UpdateAggregateAgent;
import apps.count.manager.AggregateAgentManager;
import bench.template.UserData;
import com.ibm.agent.exa.client.AgentClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.mq.AgentMessageQueue;
import rda.agent.profile.AgentProfileGenerator;
import rda.control.flow.WindowController;
import rda.control.stream.WindowStream;
import rda.extension.agent.exec.AgentSystemInitializer;
import rda.extension.agent.exec.AgentSystemShutdown;

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
        UpdateAggregateAgent updator = new UpdateAggregateAgent();
        
        //Init Parameter
        param.put(AgentSystemInitializer.paramID.REGION_NAME, "");
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, creator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, agentProf);
        param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, updator);
        param.put(AgentMessageQueue.paramID.AGENT_WAIT, 100L);
        param.put(AgentMessageQueue.paramID.QUEUE_WAIT, 100L);
        param.put(AgentMessageQueue.paramID.QUEUE_LENGTH, 1000);
        
        Object msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Create Agent
        for(String agID : (List<String>)agIDLists){
            Map setter = agentProf.generate(agID);
            String msgc = creator.create(client, setter);
            System.out.println("Create "+agID+" = "+msgc);
        }
        
        //Update Test
        AggregateAgentMessageSender agUpdate = new AggregateAgentMessageSender();
        WindowController win = new WindowController(10, 10L);
        WindowStream dataStream = new WindowStream(
                win,
                ag,
                agUpdate);
        WindowStream.setRunnable(true);
        win.start();
        dataStream.start();
        for(String agID : (List<String>)agIDLists){
            String userID = agID;
            UserData data = new UserData(userID, 1);
            
            win.pack(agID, data);
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        
        WindowStream.setRunnable(false);
        AgentSystemShutdown agShutdown = new AgentSystemShutdown();
        msg = agShutdown.shutdown(client);
        System.out.println(msg);

        //Read Test
        ReadAggregateAgent reader = new ReadAggregateAgent();
        for(String agID : (List<String>)agIDLists){
            Object d = reader.read(client, agID);
            System.out.println("Read "+agID+" = "+d);
        }
        
        //Client
        ag.returnConnection(client);
        
        /*
        //Delete Test
        Dispose deletor = new Dispose();
        deletor.delete(manager.getDestinationAgent());
        */
    }
}
