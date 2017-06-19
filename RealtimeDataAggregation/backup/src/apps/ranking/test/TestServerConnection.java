/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.test;

import apps.ranking.agent.rank.profile.RankAgentProfile;
import apps.ranking.agent.user.profile.UserAgentProfile;
import apps.ranking.appuser.UserProfile;
import apps.ranking.manager.RankingAgentManager;
import apps.ranking.property.AppRankingProperty;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.table.DestinationAgentTable;
import rda.property.RDAProperty;
import rda.server.ServerConnectionManager;

/**
 *
 * @author kaeru
 */
public class TestServerConnection {
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
        
        //Server
        List ruleList = new ArrayList();
        ruleList.add(userAgentProf.getAgentIDRule());
        ruleList.add(rankAgentProf.getAgentIDRule());
        System.out.println(ruleList);
        
        RDAProperty prop = RDAProperty.getInstance();
        ServerConnectionManager scManager = ServerConnectionManager.getInstance();
        scManager.createServerConnection(prop.getAllParameter());
        Map deployRule = new HashMap();
        deployRule.putAll(prop.getAllParameter());
        deployRule.put(ServerConnectionManager.paramID.AGENTTYPE_LIST, ruleList);
        scManager.agentDeployServer(deployRule);
        
        System.out.println(scManager.getDeployAllServer());
    }
}
