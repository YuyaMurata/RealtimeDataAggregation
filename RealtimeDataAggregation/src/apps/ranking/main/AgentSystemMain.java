/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.main;

import apps.ranking.agent.rank.creator.CreateRankAgent;
import apps.ranking.agent.rank.extension.RankAgentMessageSender;
import apps.ranking.agent.rank.profile.RankAgentProfile;
import apps.ranking.agent.rank.reader.ReadRankAgent;
import apps.ranking.agent.rank.updator.UpdateRankAgent;
import apps.ranking.agent.user.creator.CreateUserAgent;
import apps.ranking.agent.user.extension.UserAgentMessageSender;
import apps.ranking.agent.user.profile.UserAgentProfile;
import apps.ranking.agent.user.reader.ReadUserAgent;
import apps.ranking.agent.user.updator.UpdateUserAgent;
import apps.ranking.appuser.UserProfile;
import apps.ranking.manager.RankingAgentManager;
import apps.ranking.property.AppRankingProperty;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import bench.template.UserData;
import bench.time.TimeOverEvent;
import com.ibm.agent.exa.client.AgentClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.deletor.Dispose;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.table.DestinationAgentTable;
import rda.control.stream.WindowStream;
import rda.extension.agent.comm.AgentIntaractionComm;
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
        
        //RankingSystem Property
        AppRankingProperty approp = AppRankingProperty.getInstance();
        
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
        DestinationAgentTable userAgentTable = new DestinationAgentTable(userAgentProf.registerIDList(), userAgentProf.registerIDList().size());
        System.out.println(userAgentTable.toString());
        
        //Destination RankAgent Table
        DestinationAgentTable rankAgentTable = new DestinationAgentTable(rankAgentProf.registerIDList(), 10);
        System.out.println(rankAgentTable.toString());
        
        //Server - AgentClient
        RDAProperty prop = RDAProperty.getInstance();
        ServerConnectionManager scManager = ServerConnectionManager.getInstance();
        scManager.createServerConnection(prop.getAllParameter());
        AgentConnection ag = scManager.getLocalServer();
        AgentClient client = ag.getClient();
        
        //Init UserAgent Parameter
        String userIDRule = (String) approp.getParameter(RankingAgentManager.paramID.USERID_RULE);
        CreateUserAgent userCreator = new CreateUserAgent();
        UpdateUserAgent userUpdator = new UpdateUserAgent();
        Map param = prop.getAllParameter();
        param.put(AgentSystemInitializer.paramID.AGENT_TYPE, userIDRule.split("#")[0]);
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, userCreator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, userAgentProf);
        param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, userUpdator);
        
        //Extension Initialize
        AgentSystemInitializer agInit = new AgentSystemInitializer();
        Object msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Init RankAgent Parameter
        String rankIDRule = (String) approp.getParameter(RankingAgentManager.paramID.RANKID_RULE);
        CreateRankAgent rankCreator = new CreateRankAgent();
        UpdateRankAgent rankUpdator = new UpdateRankAgent();
        param.put(AgentSystemInitializer.paramID.AGENT_TYPE, rankIDRule.split("#")[0]);
        param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, rankCreator);
        param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, rankAgentProf);
        param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, rankUpdator);
        
        //Extension Initialize
        msg = agInit.initalize(client, param);
        System.out.println(msg);
        
        //Create UserAgent
        for(Object agID : userAgentProf.registerIDList()){
            Map setter = userAgentProf.generate(agID);
            String msgc = userCreator.create(client, setter);
            System.out.println("Create "+agID+" = "+msgc);
        }
        
        //Create RankAgent
        for(Object agID : rankAgentProf.registerIDList()){
            Map setter = rankAgentProf.generate(agID);
            String msgc = rankCreator.create(client, setter);
            System.out.println("Create "+agID+" = "+msgc);
        }
        
        //Communication Set
        Map commMap = new HashMap();
        commMap.putAll(prop.getAllParameter());
        commMap.put(AgentIntaractionComm.paramID.WINDOW, new RankAgentMessageSender());
        commMap.put(AgentIntaractionComm.paramID.AGENT_TABLE, rankAgentTable);
        msg = AgentIntaractionComm.setExtensionAgentIntaraction(client, commMap);
        System.out.println(msg);
        
        //Start AgentSystem
        AgentSystemLaunch agLaunch = new AgentSystemLaunch();
        msg = agLaunch.launch(client);
        System.out.println(msg);
        
        //UpdateUserAgent Test
        UserAgentMessageSender agUserAgentUpdate = new UserAgentMessageSender();
        WindowStream window = new WindowStream(
                prop.getAllParameter(),
                ag,
                agUserAgentUpdate);
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
                
                //Test RankAgent
                /*for(Object agID : rankAgentProf.registerIDList()){
                    List rankData = new ArrayList();
                    rankData.add(user);
                    rankUpdator.update(client, agID, rankData);
                }*/
                
                Object id = userProf.generate(user.id).get(UserProfile.profileID.ID);
                Object agID = userAgentTable.getDestAgentID(id);
                
                window.in(agID, user);
                totalData++;
            }
        } catch (TimeOverEvent ex) {
            //ex.printStackTrace();
        }
        Long stop = System.currentTimeMillis();
        
        //Test TimeStop
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        
        //Stop AgentSystem
        WindowStream.setRunnable(false);
        AgentSystemShutdown agShutdown = new AgentSystemShutdown();
        msg = agShutdown.shutdown(client);
        System.out.println(msg);

        //Read UserAgent Test
        ReadUserAgent userReader = new ReadUserAgent();
        Long total = 0L;
        for(Object agID : userAgentProf.registerIDList()){
            Object d = userReader.read(client, agID);
            System.out.println("Read "+agID+" = "+d);
            total = ((List<Long>)d).get(0) + total;
        }
        
        //Read RankAgent Test
        ReadRankAgent rankReader = new ReadRankAgent();
        for(Object agID : rankAgentProf.registerIDList()){
            Object d = rankReader.read(client, agID);
            System.out.println("Read "+agID+" = "+d);
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
