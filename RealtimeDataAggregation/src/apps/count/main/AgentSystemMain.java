/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.agent.aggregate.creator.CreateAggregateAgent;
import apps.count.agent.aggregate.extension.AggregateAgentMessageSender;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.appuser.UserProfile;
import apps.count.agent.aggregate.reader.ReadAggregateAgent;
import apps.count.agent.aggregate.updator.UpdateAggregateAgent;
import apps.count.manager.AggregateAgentManager;
import apps.count.property.AppCountProperty;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import bench.template.UserData;
import bench.time.TimeOverEvent;
import com.ibm.agent.exa.client.AgentClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.deletor.Dispose;
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
        
        //RankingSystem Property
        AppCountProperty approp = AppCountProperty.getInstance();
        
        //Create User ID
        List userLists = agBench.getUserList();
        AgentProfileGenerator userProf = new AgentProfileGenerator(new UserProfile(userLists));
        System.out.println(userProf.toString());
        
        //Create Agent ID
        List agIDLists = manager.getAgentList();
        AgentProfileGenerator agentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
        System.out.println(agentProf.toString());
        
        //Destination Table
        DestinationAgentTable table = new DestinationAgentTable(agentProf.registerIDList(), 10);
        System.out.println(table.toString());
        
        //Server - AgentClient
        RDAProperty prop = RDAProperty.getInstance();
        ServerConnectionManager scManager = ServerConnectionManager.getInstance();
        scManager.createServerConnection(prop.getAllParameter());
        AgentConnection ag = scManager.getLocalServer();
        AgentClient client = ag.getClient();
        
        //Init Parameter
        String agentIDRule = (String) approp.getParameter(AggregateAgentManager.paramID.ID_RULE);
        CreateAggregateAgent creator = new CreateAggregateAgent();
        UpdateAggregateAgent updator = new UpdateAggregateAgent();
        Map param = prop.getAllParameter();
        param.put(AgentSystemInitializer.paramID.AGENT_TYPE, agentIDRule.split("#")[0]);
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, creator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, agentProf);
        param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, updator);
        
        //Extension Initialize
        AgentSystemInitializer agInit = new AgentSystemInitializer();
        Object msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Create Agent
        for(Object agID : agentProf.registerIDList()){
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
                prop.getAllParameter(),
                ag,
                agUpdate);
        window.start();

        //Start Benchmark
        Map<Object, Integer> dataLog = new HashMap();
        Long totalData = 0L;
        Long start = System.currentTimeMillis();
        try {
            while (true) {
                UserData user = agBench.bench();
                if(user== null){
                    continue;
                }
                
                Integer age = (Integer) userProf.generate(user.id).get(UserProfile.profileID.AGE) / 10;
                Object agID = table.getDestAgentID(age);
                
                if(dataLog.get(age) == null) dataLog.put(age, 0);
                dataLog.put(age, dataLog.get(age)+1);
                
                //System.out.println(agID+" - "+user.toString());
                
                window.in(agID, user);
                totalData++;
            }
        } catch (TimeOverEvent ex) {
            //ex.printStackTrace();
        }
        Long stop = System.currentTimeMillis();
        
        //Stop AgentSystem
        WindowStream.setRunnable(false);
        AgentSystemShutdown agShutdown = new AgentSystemShutdown();
        msg = agShutdown.shutdown(client);
        System.out.println(msg);

        //Read Test
        ReadAggregateAgent reader = new ReadAggregateAgent();
        Long total = 0L;
        for(Object agID : agentProf.registerIDList()){
            Object d = reader.read(client, agID);
            System.out.println("Read "+agID+" = "+d);
            total = ((List<Long>)d).get(0) + total;
        }
        
        //Log
        for(Object age : dataLog.keySet())
            System.out.println(age+":"+dataLog.get(age));
        
        //Total Time
        System.out.println(total+"/"+totalData+","+(stop-start));
        
        //Delete Test
        Dispose deletor = new Dispose();
        deletor.delete(client);
        
        //Client
        ag.returnConnection(client);
        
    }
}
