/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.test;

import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.appuser.UserProfile;
import apps.count.manager.AggregateAgentManager;
import apps.count.property.AppCountProperty;
import bench.main.AgentBenchmark;
import bench.property.BenchmarkProperty;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import rda.agent.profile.AgentProfileGenerator;

/**
 *
 * @author kaeru
 */
public class AgentIDGrouping {
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
		
		Object map = userLists.stream()
				.collect(Collectors.groupingBy(user -> userProf.generate(user).get(UserProfile.profileID.AGE)));
		
		for(Object key : ((Map)map).keySet()){
			List user = (List) ((Map)map).get(key);
			System.out.print(key + ":");
			for(Object u : user){
				Map prof = userProf.generate(u);
				System.out.print(prof.get(UserProfile.profileID.ID)+"("+prof.get(UserProfile.profileID.AGE)+"), ");
			}
			System.out.println();
		}
	}
}
