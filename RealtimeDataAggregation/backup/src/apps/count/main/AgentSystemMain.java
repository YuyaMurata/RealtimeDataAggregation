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
import apps.count.agent.aggregate.table.DestinationAppTable;
import apps.count.agent.aggregate.updator.UpdateAggregateAgent;
import apps.count.manager.AggregateAgentManager;
import apps.count.property.AppCountProperty;
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
import rda.agent.deploy.DeployStrategy;
import rda.agent.profile.AgentProfileGenerator;
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

		//CountSystem Property
		AppCountProperty approp = AppCountProperty.getInstance();
		UserProfile.setParameter((Integer) approp.getParameter(UserProfile.paramID.USER_MODE),
				(Long) approp.getParameter(UserProfile.paramID.USER_SEED));

		//Create User ID
		List userLists = agBench.getUserList();
		AgentProfileGenerator userProf = new AgentProfileGenerator(new UserProfile(userLists));
		System.out.println("NumberOfUsers = "+userProf.registerIDList().size());

		//Create Agent ID
		List agIDLists = manager.getAgentList();
		AgentProfileGenerator agentProf = new AgentProfileGenerator(new AggregateAgentProfile(agIDLists));
		System.out.println(agentProf.toString());

		//Destination Table
		DestinationAppTable table = new DestinationAppTable(agentProf.registerIDList(), 10);
		table.createAgeTable(100);	//Max Age 100
		System.out.println(table.toString());

		//Server - AgentClient
		RDAProperty prop = RDAProperty.getInstance();
		ServerConnectionManager scManager = ServerConnectionManager.getInstance();
		scManager.createServerConnection();
		DeployStrategy strategy = new AppCountDeployStrategy((String) approp.getParameter(AggregateAgentManager.paramID.ID_RULE), agIDLists);
		scManager.setDeployStrategy(strategy);
		System.out.println(strategy);

		//Init Parameter
		String agentIDRule = (String) approp.getParameter(AggregateAgentManager.paramID.ID_RULE);
		Integer agentMode = (Integer) approp.getParameter(AggregateAgentManager.paramID.AGENT_MODE);
		CreateAggregateAgent creator = new CreateAggregateAgent();
		UpdateAggregateAgent updator = new UpdateAggregateAgent();
		Map param = prop.getAllParameter();
		param.put(AgentSystemInitializer.paramID.AGENT_TYPE, agentIDRule.split("#")[0]);
		param.put(AgentSystemInitializer.paramID.AGENT_CREATOR, creator);
		param.put(AgentSystemInitializer.paramID.AGENT_PROFILE, agentProf);
		param.put(AgentSystemInitializer.paramID.AGENT_UPDATOR, updator);
		param.put(AgentSystemInitializer.paramID.AGENT_MODE, agentMode);

		//Extension Initialize
		AgentSystemInitializer agInit = new AgentSystemInitializer();
		for(Object con : (List)scManager.getDeployAllServer().get("A")){
			param.put(AgentSystemInitializer.paramID.HOST_NAME, ((AgentConnection)con).getHost());
			AgentClient client = ((AgentConnection)con).getClient();
			Object msg = agInit.initalize(client, param);
			System.out.println(msg);
			((AgentConnection)con).returnConnection(client);
		}
		
		//Create Agent 変更
		for (Object agID : agentProf.registerIDList()) {
			AgentConnection con = scManager.getDistributedServer(agID);
			AgentClient client = con.getClient();
			
			Map setter = agentProf.generate(agID);
			String msgc = creator.create(client, setter);
			System.out.println("Create " + agID + " = " + msgc);
			
			con.returnConnection(client);
		}
		
		//Start AgentSystem
		AgentSystemLaunch agLaunch = new AgentSystemLaunch();
		for(Object con : (List)scManager.getDeployAllServer().get("A")){
			AgentClient client = ((AgentConnection)con).getClient();
			
			String msg = agLaunch.launch(client);
			System.out.println(msg);
			
			((AgentConnection)con).returnConnection(client);
		}

		//Update Test
		WindowStream window = new WindowStream(
			prop.getAllParameter(),
			new AggregateAgentMessageSender(table, userProf));
		window.start();
		
		//Start Benchmark
		Map<Object, Integer> dataLog = new HashMap();
		Long totalData = 0L;
		Long start = System.currentTimeMillis();
		try {
			while (true) {
				UserData user = agBench.bench();
				if (user == null) {
					continue;
				}

				Integer age = (Integer) userProf.generate(user.id).get(UserProfile.profileID.AGE);
				AgentConnection server = scManager.getDealServer(user.id, age);

				if (dataLog.get(server.getHost()) == null) {
					dataLog.put(server.getHost(), 0);
				}
				dataLog.put(server.getHost(), dataLog.get(server.getHost()) + 1);

				window.in(server, user);
				System.out.println("userID="+user.id+" -> "+server.getHost());
				
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
		for(Object con : (List)scManager.getDeployAllServer().get("A")){
			AgentClient client = ((AgentConnection)con).getClient();
			String msg = agShutdown.shutdown(client);
			System.out.println(msg);
		}

		//Log
		System.out.println("Benchmark Create Data:");
		for (Object agID : dataLog.keySet()) {
			System.out.println(agID + ":" + dataLog.get(agID));
		}
		
		//Read Test
		System.out.println("AggregateAgent Read Data:");
		ReadAggregateAgent reader = new ReadAggregateAgent();
		Long total = 0L;
		for (Object agID : agentProf.registerIDList()) {
			AgentConnection con = scManager.getDistributedServer(agID);
			AgentClient client = con.getClient();
			
			Object d = reader.read(client, agID);
			System.out.println("Read " + agID + " = " + d);
			total = (Long)((List<Object>) d).get(1) + total;
			
			con.returnConnection(client);
		}

		//Total Time
		System.out.println(total + "/" + totalData + "," + (stop - start));

		//Delete Test
		//Dispose deletor = new Dispose();
		//deletor.delete(client);

		//Client
		//ag.returnConnection(client);

	}
}
