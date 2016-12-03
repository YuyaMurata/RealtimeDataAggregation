/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.main;

import apps.ranking.agent.rank.profile.RankAgentProfile;
import apps.ranking.agent.user.creator.CreateUserAgent;
import apps.ranking.agent.user.extension.UserAgentMessageSender;
import apps.ranking.agent.user.profile.UserAgentProfile;
import apps.ranking.agent.user.reader.ReadUserAgent;
import apps.ranking.agent.user.updator.UpdateUserAgent;
import apps.ranking.appuser.UserProfile;
import apps.ranking.manager.RankingAgentManager;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import bench.template.UserData;
import bench.time.TimeOverEvent;
import com.ibm.agent.exa.client.AgentClient;
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
        RankingAgentManager manager = RankingAgentManager.getInstance();
        AgentBenchmark agBench = AgentBenchmark.getInstance();
        
        //Benchmark Initializer
        BenchmarkProperty bprop = BenchmarkProperty.getInstance();
        agBench.setParameter(bprop.getParameter());
        
        //Create User ID
        List userLists = agBench.getUserList();
        AgentProfileGenerator userProf = new AgentProfileGenerator(new UserProfile(userLists));
        System.out.println(userProf.toString());
        
        //Create UserAgent ID
        AgentProfileGenerator userAgentProf = new AgentProfileGenerator(new UserAgentProfile(userLists, userProf));
        System.out.println(userAgentProf.toString());
        
        //Create RankAgent ID
        List agIDLists = manager.getAgentList();
        AgentProfileGenerator rankAgentProf = new AgentProfileGenerator(new RankAgentProfile(agIDLists));
        System.out.println(rankAgentProf.toString());
        
        //Destination UserAgent Table
        DestinationAgentTable.setParameter(userAgentProf.registerIDList().size());
        DestinationAgentTable userAgentTable = new DestinationAgentTable(userAgentProf.registerIDList());
        System.out.println(userAgentTable.toString());
        
        //Destination UserAgent Table
        DestinationAgentTable.setParameter(10);
        DestinationAgentTable rankAgentTable = new DestinationAgentTable(rankAgentProf.registerIDList());
        System.out.println(rankAgentTable.toString());
        
        //Server - AgentClient
        ServerConnectionManager scManager = ServerConnectionManager.getInstance();
        AgentConnection ag = scManager.getLocalServer();
        AgentClient client = ag.getClient();
        
        //Init Parameter
        RDAProperty prop = RDAProperty.getInstance();
        CreateUserAgent creator = new CreateUserAgent();
        UpdateUserAgent updator = new UpdateUserAgent();
        Map param = prop.getAllParameter();
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, creator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, userAgentProf);
        param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, updator);
        
        //Extension Initialize
        AgentSystemInitializer agInit = new AgentSystemInitializer();
        Object msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Create Agent
        for(Object agID : userAgentProf.registerIDList()){
            Map setter = userAgentProf.generate(agID);
            String msgc = creator.create(client, setter);
            System.out.println("Create "+agID+" = "+msgc);
        }
        
        //Start AgentSystem
        AgentSystemLaunch agLaunch = new AgentSystemLaunch();
        msg = agLaunch.launch(client);
        System.out.println(msg);
        
        //Update Test
        UserAgentMessageSender agUpdate = new UserAgentMessageSender();
        WindowStream window = new WindowStream(
                prop.getWindowParameter(),
                ag,
                agUpdate);
        window.start();

        //Start Benchmark
        Long totalData = 0L;
        Long start = System.currentTimeMillis();
        try {
            while (true) {
                UserData user = agBench.bench();
                if(user== null){
                    //System.out.println(ag.toString());
                    continue;
                }
                
                Object id = userProf.generate(user.id).get(UserProfile.profileID.ID);
                Object agID = userAgentTable.getDestAgentID(id);
                
                System.out.println(agID+" - "+user.toString());
                
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
        ReadUserAgent reader = new ReadUserAgent();
        Long total = 0L;
        for(Object agID : userAgentProf.registerIDList()){
            Object d = reader.read(client, agID);
            System.out.println("Read "+agID+" = "+d);
            total = ((List<Long>)d).get(0) + total;
        }
        
        //Total Time
        System.out.println(total+"/"+totalData+","+(stop-start));
        
        //Delete Test
        Dispose deletor = new Dispose();
        deletor.delete(client);
        
        //Client
        ag.returnConnection(client);
        
    }
}
