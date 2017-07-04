/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.agent.aggregate.creator.CreateAggregateAgent;
import apps.count.agent.aggregate.deploy.AppCountDeployStrategy;
import apps.count.agent.aggregate.extension.AggregateAgentMessageSender;
import apps.count.agent.aggregate.profile.AggregateAgentProfile;
import apps.count.appuser.UserProfile;
import apps.count.agent.aggregate.reader.ReadAggregateAgent;
import apps.count.agent.aggregate.table.DestinationSubTable;
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
import java.util.TreeMap;
import rda.agent.client.AgentConnection;
import rda.agent.profile.AgentProfileGenerator;
import rda.control.stream.WindowStream;
import rda.db.DBAccess;
import rda.extension.agent.exec.AgentSystemInitializer;
import rda.extension.agent.exec.AgentSystemLaunch;
import rda.extension.agent.exec.AgentSystemShutdown;
import rda.property.RDAProperty;
import rda.server.ServerConnectionManager;

/**
 *
 * @author murata
 */
public class AgentSystemMain {

	public static void main(String[] args) {
		//Test
		AggregateAgentManager manager = AggregateAgentManager.getInstance();
		AgentBenchmark agBench = AgentBenchmark.getInstance();

		//Benchmark Initializer
		BenchmarkProperty bprop = BenchmarkProperty.getInstance();
		agBench.setParameter(bprop.getParameter());

		//CountSystem Property
		AppCountProperty approp = AppCountProperty.getInstance();
		UserProfile.setParameter(approp.getAllParameter());

		//Create User ID
		List userLists = agBench.getUserList();
		AgentProfileGenerator userProf = new AgentProfileGenerator(new UserProfile(userLists));
		System.out.println("NumberOfUsers = "+userProf.registerIDList().size());

		//Create Agent ID
		List agIDLists = manager.getAgentList();
		AgentProfileGenerator agentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
		System.out.println(agentProf.toString());

		//Server - AgentClient
		RDAProperty prop = RDAProperty.getInstance();
		ServerConnectionManager scManager = ServerConnectionManager.getInstance();
		scManager.createServerConnection(prop.getAllParameter());
		scManager.setDeployStrategy(new AppCountDeployStrategy(approp.getAllParameter(), agIDLists));
		scManager.createAgeMap(100);
		System.out.println(scManager.getDeployAllServerToString());

		//Init Parameter
		Map initParam = new HashMap();
		initParam.put(AgentSystemInitializer.paramID.AGENT_CREATOR, new CreateAggregateAgent());
		initParam.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, new UpdateAggregateAgent());
		initParam.put(AgentSystemInitializer.paramID.AGENT_PROFILE, agentProf);
		initParam.put(AgentSystemInitializer.paramID.USER_PROFILE, userProf);
		initParam.putAll(prop.getAllParameter());
		initParam.putAll(approp.getAllParameter());
		
		//Extension Initialize
		AgentSystemInitializer agInit = new AgentSystemInitializer();
		for(AgentConnection server : scManager.getAllServer()){
			initParam.put(AgentSystemInitializer.paramID.HOST_NAME, server.getHost());
			initParam.put(AgentSystemInitializer.paramID.DEST_TABLE, new DestinationSubTable(scManager.getServerInfo(server)));
			
			AgentClient client = server.getClient();
			
			Object msg = agInit.initalize(client, initParam);
			System.out.println(msg);
			
			server.returnConnection(client);
		}
		
		//Start AgentSystem
		AgentSystemLaunch agLaunch = new AgentSystemLaunch();
		for(AgentConnection server : scManager.getAllServer()){
			AgentClient client = server.getClient();
			
			String msg = agLaunch.launch(client);
			System.out.println(msg);
			
			server.returnConnection(client);
		}

		//Start Window
		WindowStream window = new WindowStream(
			prop.getAllParameter(),
			new AggregateAgentMessageSender());
		window.start();
		
		//Start Benchmark
		//agBench.setBenchList(scManager, userProf, window.getWindowSize());
		Map<Object, Integer> dataLog = new HashMap();
		Long totalData = 0L;
		Long start = System.currentTimeMillis();
		
		Long pest = 0L;
		Long best = 0L;
		Long lest = 0L;
		Long west = 0L;
		try {
			while (true) {
				Long bstart = System.currentTimeMillis();
				UserData user = agBench.bench();
				best += System.currentTimeMillis() - bstart;
				if (user == null) {
					continue;
				}
				
				//Data
				Long pstart = System.currentTimeMillis();
				Integer age = (Integer) userProf.getAttribute(user.id);
				pest += System.currentTimeMillis() - pstart;
				AgentConnection server = scManager.getDealServer(user.id, age);
				
				/*//Log
				Long lstart = System.currentTimeMillis();
				if (dataLog.get(server.getHost()) == null)
					dataLog.put(server.getHost(), 0);
				dataLog.put(server.getHost(), dataLog.get(server.getHost()) + 1);
				lest += System.currentTimeMillis() - lstart;*/
				
				Long wstart = System.currentTimeMillis();
				
				window.in(server, user);
				//System.out.println("userID="+user.id+" ("+age+") -> "+server.getHost());
				west += System.currentTimeMillis() - wstart;
				
				totalData++;
			}
		} catch (TimeOverEvent ex) {
			//ex.printStackTrace();
		}
		Long stop = System.currentTimeMillis();

		//Test TimeStop
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
		}

		//Stop AgentSystem
		WindowStream.setRunnable(false);
		AgentSystemShutdown agShutdown = new AgentSystemShutdown();
		for(AgentConnection server : scManager.getAllServer()){
			AgentClient client = server.getClient();
			
			String msg = agShutdown.shutdown(client);
			System.out.println(msg);
			
			server.returnConnection(client);
		}

		//Log
		System.out.println("Benchmark Create Data:");
		for (Object agID : dataLog.keySet()) {
			System.out.println(agID + ":" + dataLog.get(agID));
		}
		
		//Read Test
		System.out.println("AggregateAgent Read Data:");
		ReadAggregateAgent reader = new ReadAggregateAgent();
		for (Object agID : agentProf.registerIDList()) {
			AgentConnection server = scManager.getDistributedServer(agID);
			AgentClient client = server.getClient();
			
			Object d = reader.read(client, agID);
			System.out.println("Read " + agID + " = " + d);
			
			server.returnConnection(client);
		}
		
		//DB Dump
		System.out.println("AgentDump:");
		TreeMap map = new TreeMap();
		for(AgentConnection server : scManager.getAllServer()){
			AgentClient client = server.getClient();
			
			Object msg = new DBAccess().dump(client, (String) prop.getParameter(ServerConnectionManager.paramID.APP_CLASS),  "aggregateagent");
			map.putAll((Map) msg);
			//System.out.println(msg);
			
			server.returnConnection(client);
		}
		//Print
		Long total = 0L;
		for(Object id : map.keySet()){
			System.out.println(id+":"+map.get(id));
			total = total + (Long)map.get(id);
		}

		//Total Time
		System.out.println(total + "/" + totalData + "," + (stop - start));
		System.out.println("Bench="+best+"ms Profile="+pest+"ms Log="+lest+"ms Window="+west+"ms");

		//Delete
		/*for(AgentConnection server : scManager.getAllServer()){
			AgentClient client = server.getClient();
			
			Dispose deletor = new Dispose();
			deletor.delete(client);
		
			server.returnConnection(client);
		}*/
	}
}
