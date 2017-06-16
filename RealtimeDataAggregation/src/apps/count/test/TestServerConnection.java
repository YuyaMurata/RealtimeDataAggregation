/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.test;

import apps.count.agent.aggregate.deploy.AppCountDeployStrategy;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.appuser.UserProfile;
import apps.count.manager.AggregateAgentManager;
import apps.count.property.AppCountProperty;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.deploy.DeployStrategy;
import rda.agent.profile.AgentProfileGenerator;
import rda.property.RDAProperty;
import rda.server.ServerConnectionManager;

/**
 *
 * @author kaeru
 */
public class TestServerConnection {

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

		//Create UserAgent ID
		List agIDLists = manager.getAgentList();
		AgentProfileGenerator aggregateAgentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
		System.out.println(aggregateAgentProf.toString());

		//Server
		List ruleList = new ArrayList();
		ruleList.add(aggregateAgentProf.getAgentIDRule());
		System.out.println(ruleList);
		
		//RDA
		RDAProperty prop = RDAProperty.getInstance();
		ServerConnectionManager scManager = ServerConnectionManager.getInstance();
		scManager.createServerConnection(prop.getAllParameter());
		Map deployRule = new HashMap();
		deployRule.putAll(prop.getAllParameter());
		
		//Deploy
		/*DeployStrategy deploy = new AppCountDeployStrategy((int) deployRule.get(ServerConnectionManager.paramID.DEPLOY_PATTERN), 
												(String) approp.getParameter(AggregateAgentManager.paramID.ID_RULE), 
												agIDLists, 10);
		deployRule.put(ServerConnectionManager.paramID.AGENTTYPE_LIST, ruleList);
		scManager.agentDeployServer(deployRule);
		scManager.setDeployStrategy(deploy);
		
		//
		System.out.println(scManager.getDeployAllServer());
		System.out.println(deploy.toString());
		System.out.println("Agent ins Servers : ");
		for(Object agID : agIDLists){
			System.out.println("agID="+agID+" server="+deploy.getDeployServer(agID));
		}*/
		
		System.out.println("Agent in Servers : ");
		DeployStrategy deploy = new AppCountDeployStrategy();
		Map map = deploy.createDeployPattern((int) deployRule.get(ServerConnectionManager.paramID.DEPLOY_PATTERN), scManager.getAllServer(), agIDLists, 8);
		System.out.println(map);
		
		System.out.println("\nDeal Servers : ");
		for(Object id : agIDLists)
			System.out.println("ID:"+id+" srv="+scManager.getDealServer(id));
	}
}
