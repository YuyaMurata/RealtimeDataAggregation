/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.test;

import apps.count.agent.aggregate.deploy.AppCountDeployStrategy;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.agent.aggregate.table.DestinationSubTable;
import apps.count.appuser.UserProfile;
import apps.count.manager.AggregateAgentManager;
import apps.count.property.AppCountProperty;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import rda.agent.client.AgentConnection;
import rda.agent.deploy.DeployStrategy;
import rda.agent.profile.AgentProfileGenerator;
import rda.property.RDAProperty;
import rda.server.ServerConnectionManager;

/**
 *
 * @author kaeru
 */
public class CompServerDealAndTable {
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
		UserProfile.setParameter(approp.getAllParameter());
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
		scManager.createAgeMap(100);
		
		//Deploy Strategy Initialize
		System.out.println("Agent in Servers : ");
		DeployStrategy deploy = new AppCountDeployStrategy(approp.getAllParameter(), agIDLists);
		scManager.setDeployStrategy(deploy);
		System.out.println(deploy);
		
		//Destination Table
		Map<AgentConnection, DestinationSubTable> serverMap = new HashMap();
		for(AgentConnection server : scManager.getAllServer()){
			DestinationSubTable table = new DestinationSubTable(scManager.getServerInfo(server));
			serverMap.put(server, table);
			System.out.println(table);
		}
		//DestinationAppTable table = new DestinationAppTable(aggregateAgentProf.registerIDList(), 10);
		//table.createAgeTable(100);	//Max Age 100
		//System.out.println(table.toString());
		
		// userID -> server -> desttable -> age -> agID
		Object map1 = userLists.stream()
				.collect(Collectors.groupingBy(user -> 
						serverMap.get(scManager.getDealServer(user, (Integer) userProf.generate(user).get(UserProfile.profileID.AGE))).getDestAgentID(user, (Integer) userProf.generate(user).get(UserProfile.profileID.AGE))));
		
		for(Object key : ((Map)map1).keySet()){
			List user = (List) ((Map)map1).get(key);
			System.out.print(key + ":");
			for(Object u : user){
				Map prof = userProf.generate(u);
				System.out.print(prof.get(UserProfile.profileID.ID)+"("+prof.get(UserProfile.profileID.AGE)+"), ");
			}
			System.out.println();
			System.out.print(key + ":");
			for(Object u : user){
				Map prof = userProf.generate(u);
				System.out.print(((AgentConnection)scManager.getDealServer(prof.get(UserProfile.profileID.ID), (int) prof.get(UserProfile.profileID.AGE))).getHost()+",");
			}
			System.out.println();
		}
		
		System.out.println("");
		
		//Deal DistributedServer
		System.out.println("\nDeal Servers : ");
		for (Object id : userLists) {
			int age = (int) userProf.generate(id).get(UserProfile.profileID.AGE);
			AgentConnection host = scManager.getDealServer(id, age);
			System.out.println("ID:" + id + " age="+age+" ,srv=" +host.getHost()+" -> "+serverMap.get(host).getDestRootAgentID(age)+" -> "+serverMap.get(host).getDestAgentID(id, age));
		}
	}
}
