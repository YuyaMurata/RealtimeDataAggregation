/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.agent.aggregate.creator.CreateAggregateAgent;
import apps.count.agent.aggregate.extension.AggregateAgentMessageSender;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.agent.aggregate.profile.UserProfile;
import apps.count.agent.aggregate.reader.ReadAggregateAgent;
import apps.count.agent.aggregate.updator.UpdateAggregateAgent;
import apps.count.manager.AggregateAgentManager;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import bench.template.UserData;
import bench.time.TimeOverEvent;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.table.DestinationAgentTable;
import rda.control.stream.WindowStream;
import rda.extension.agent.exec.AgentSystemInitializer;
import rda.extension.agent.exec.AgentSystemLaunch;
import rda.extension.agent.exec.AgentSystemShutdown;
import rda.property.RDAProperty;
import rda.server.ServerConnectionManager;

/**
 *
 * @author kaeru
 */
public class AgentSystemMain {
    public static void main(String[] args) {
        //Test
        AggregateAgentManager manager = AggregateAgentManager.getInstance();
        AgentBenchmark agBench = AgentBenchmark.getInstance();
        
        //Benchmark Initializer
        BenchmarkProperty bprop = BenchmarkProperty.getInstance();
        agBench.setParameter(bprop.getParameter());
        
        //Create User ID
        List userLists = agBench.getUserList();
        AgentProfileGenerator userProf = new AgentProfileGenerator(new UserProfile(userLists));
        System.out.println(userProf.toString());
        
        //Create Agent ID
        List agIDLists = manager.getAgentList();
        AgentProfileGenerator agentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
        
        //Destination Table
        DestinationAgentTable table = DestinationAgentTable.getInstance();
        table.createTable(agIDLists);
        System.out.println(table.toString());
        
        //Server - AgentClient
        ServerConnectionManager scManager = ServerConnectionManager.getInstance();
        AgentConnection ag = scManager.getLocalServer();
        AgentClient client = ag.getClient();
        
        //Init Parameter
        RDAProperty prop = RDAProperty.getInstance();
        CreateAggregateAgent creator = new CreateAggregateAgent();
        UpdateAggregateAgent updator = new UpdateAggregateAgent();
        Map param = prop.getAllParameter();
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, creator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, agentProf);
        param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, updator);
        
        //Extension Initialize
        AgentSystemInitializer agInit = new AgentSystemInitializer();
        Object msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Create Agent
        for(String agID : (List<String>)agIDLists){
            Map setter = agentProf.generate(agID);
            String msgc = creator.create(client, setter);
            System.out.println("Create "+agID+" = "+msgc);
        }
        
        //Start AgentSystem
        AgentSystemLaunch agLaunch = new AgentSystemLaunch();
        msg = agLaunch.launch(client);
        System.out.println(msg);
        
        //Update Test
        AggregateAgentMessageSender agUpdate = new AggregateAgentMessageSender();
        WindowStream window = new WindowStream(
                prop.getWindowParameter(),
                ag,
                agUpdate);
        window.start();
        /*for(String userID : (List<String>)userLists){
            Object id = userProf.generate(userID).get(UserProfile.profileID.ID);
            UserData data = new UserData(userID, 1);
            
            Object agID = table.getDestAgentID(id);
            window.in(agID, data);
        }*/
        Long totalData = 0L;
        Long start = System.currentTimeMillis();
        try {
            while (true) {
                UserData user = agBench.bench();
                if(user== null) continue;
                
                Object id = userProf.generate(user.id).get(UserProfile.profileID.ID);
                Object agID = table.getDestAgentID(id);
                
                window.in(agID, user.data);
                totalData++;
            }
        } catch (TimeOverEvent ex) {
            //ex.printStackTrace();
        }
        Long stop = System.currentTimeMillis();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        
        //Stop AgentSystem
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
        
        //Total Time
        System.out.println(totalData+","+(stop-start));
        
        //Client
        ag.returnConnection(client);
        
        /*
        //Delete Test
        Dispose deletor = new Dispose();
        deletor.delete(manager.getDestinationAgent());
        */
    }
}
