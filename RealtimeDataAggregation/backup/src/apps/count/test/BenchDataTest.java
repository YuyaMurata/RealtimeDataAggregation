/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.test;

import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.agent.aggregate.table.DestinationAppTable;
import apps.count.appuser.UserProfile;
import apps.count.manager.AggregateAgentManager;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import bench.template.UserData;
import bench.time.TimeOverEvent;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import rda.agent.profile.AgentProfileGenerator;

/**
 *
 * @author kaeru
 */
public class BenchDataTest {
	public static void main(String[] args) {
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
		System.out.println(agentProf.toString());
		
		//Destination Table
		DestinationAppTable table = new DestinationAppTable(agentProf.registerIDList(), 10);
		table.createAgeTable(100);
		System.out.println(table.toString());
		
		//Bench Start
		Integer totalData = 0;
		Map<Object, Integer> userAgeMap = new TreeMap();
		Map<Object, Integer> agentMap = new TreeMap();
		Long start = System.currentTimeMillis();
		try {
			while (true) {
				UserData user = agBench.bench();
				if (user == null) {
					continue;
				}

				Integer age = (Integer) userProf.generate(user.id).get(UserProfile.profileID.AGE);
				Object agID = table.getDestAgentID(user.id, age);
				
				if(userAgeMap.get(age) == null) userAgeMap.put(age, 0);
				userAgeMap.put(age, userAgeMap.get(age)+1);
				
				if(agentMap.get(agID) == null) agentMap.put(agID, 0);
				agentMap.put(agID, agentMap.get(agID)+1);
				
				//System.out.println("userID:"+user.id+" -> "+agID);

				//System.out.println(agID+" - "+user.toString());
				totalData++;
			}
		} catch (TimeOverEvent ex) {
			//ex.printStackTrace();
		}
		Long stop = System.currentTimeMillis();
		
		System.out.println("user age Total");
		System.out.println(userAgeMap);
		System.out.println("agent Total");
		System.out.println(agentMap);
		System.out.println("Benchmark Time = "+(stop-start)+" [ms]");
		System.out.println("Total = "+totalData);
	}
}
