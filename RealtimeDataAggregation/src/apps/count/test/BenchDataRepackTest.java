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
import bench.template.UserData;
import bench.time.TimeOverEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import rda.agent.deploy.DeployStrategy;
import rda.agent.profile.AgentProfileGenerator;
import rda.control.flow.WindowController;
import rda.property.RDAProperty;
import rda.server.ServerConnectionManager;

/**
 *
 * @author kaeru
 */
public class BenchDataRepackTest {
	public static void main(String[] args) {
		AggregateAgentManager manager = AggregateAgentManager.getInstance();
		AgentBenchmark agBench = AgentBenchmark.getInstance();

		//Benchmark Initializer
		BenchmarkProperty bprop = BenchmarkProperty.getInstance();
		agBench.setParameter(bprop.getParameter());
		
		//RDAProperty
		RDAProperty prop = RDAProperty.getInstance();
		int size = (int) prop.getParameter(WindowController.paramID.WINDOW_SIZE);
		
		//ApplicationProperty
		AppCountProperty approp = AppCountProperty.getInstance();
		
		//Create User ID
		List userLists = agBench.getUserList();
		UserProfile.setParameter(approp.getAllParameter());
		AgentProfileGenerator userProf = new AgentProfileGenerator(new UserProfile(userLists));
		System.out.println(userProf.toString());

		//Create Agent ID
		List agIDLists = manager.getAgentList();
		AgentProfileGenerator agentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
		System.out.println(agentProf.toString());
		
		//Server
		List ruleList = new ArrayList();
		ruleList.add(agentProf.getAgentIDRule());
		System.out.println(ruleList);
		ServerConnectionManager scManager = ServerConnectionManager.getInstance();
		scManager.createServerConnection(prop.getAllParameter());
		scManager.createAgeMap(100);
		
		//Deploy Strategy Initialize
		System.out.println("Agent in Servers : ");
		DeployStrategy deploy = new AppCountDeployStrategy(approp.getAllParameter(), agIDLists);
		scManager.setDeployStrategy(deploy);
		System.out.println(deploy);
		
		//Table
		DestinationSubTable table = new DestinationSubTable(scManager.getServerInfo());
		table.setTableInfo(userProf);
		
		//Bench Start
		Integer totalData = 0;
		List window = new ArrayList();
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
				
				if(userAgeMap.get(age) == null) userAgeMap.put(age, 0);
				userAgeMap.put(age, userAgeMap.get(age)+1);
				Object agID = table.getDestAgentID(user.id, age);
				
				if(agentMap.get(agID) == null) agentMap.put(agID, 0);
				agentMap.put(agID, agentMap.get(agID)+1);
				
				if(window.size() < size)
					window.add(user);
				else{
					long startRepack = System.currentTimeMillis();
					
					Map<Object, List> pack = table.repack(window);
					window = new ArrayList();
					
					long stopRepack = System.currentTimeMillis();
					for(Object id : pack.keySet()){
						System.out.print("["+id+"="+pack.get(id).size()+"],");
					}
					System.out.println("repack time="+(stopRepack-startRepack));
				}
				
				//System.out.println("userID:"+user.id+" -> "+agID);

				//System.out.println(agID+" - "+user.toString());
				totalData++;
			}
		} catch (TimeOverEvent ex) {
			//ex.printStackTrace();
		}
		Long stop = System.currentTimeMillis();
		
		System.out.println("user age Total");
		System.out.println(userAgeMap.size());
		for(Object age : userAgeMap.keySet()){
			System.out.println(age+","+userAgeMap.get(age));
		}
		
		display(userAgeMap);
		
		System.out.println("agent Total");
		System.out.println(agentMap.size());
		for(Object id : agentMap.keySet()){
			System.out.println(id+","+agentMap.get(id));
		}
		
		System.out.println("Benchmark Time = "+(stop-start)+" [ms]");
		System.out.println("Total = "+totalData);
	}
	
	//Display
	public static void display(Map<Object, Integer> map){
		for(Object age : map.keySet()){
			System.out.print(age+","+map.get(age)+" ");
			int l = map.get(age) / 50 ;
			for(int i=0; i < l; i++)
				System.out.print("*");
				
			System.out.println("");
		}
	}
}
