/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.manager;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.soliddb.extension.Extension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import rda.agent.clone.AgentCloning;
import rda.agent.creator.AgentCreator;
import rda.agent.mq.AgentMessageQueue;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.table.DestinationTable;
import rda.agent.updator.AgentUpdator;
import rda.extension.agent.comm.AgentIntaractionComm;
import rda.extension.agent.exec.AgentSystemInitializer;

/**
 *
 * @author kaeru
 */
public class AgentSystemExtension implements Extension {

	private static AgentSystemExtension extention = new AgentSystemExtension();

	public static AgentSystemExtension getInstance() {
		return extention;
	}

	AgentKey extensionAgentKey;

	public AgentSystemExtension() {
	}

	// リージョン名
	String regionName;

	@Override
	public void initialize(String serverTypeName, Properties params) throws Exception {
	}

	@Override
	public void primaryChanged() throws Exception {
	}

	@Override
	public void regionChanged(int serverRole, String regionName) throws Exception {
		// リージョン名のセット
		this.regionName = regionName;

		if (serverRole == Extension.ROLE_PRIMARY) {
			// ロールがエージェント実行環境のプライマリであるかをチェック
			// し，そうであればサービスを開始する．
			// ただし，サンプルの構成では，かならずプライマリである．
			startService();
		}
	}

	@Override
	public void roleChanged(int serverRole) throws Exception {
		if (serverRole == Extension.ROLE_PRIMARY) {
			// ロールがエージェント実行環境のプライマリであるかをチェック
			// し，そうであればサービスを開始する．
			// ただし，サンプルの構成では，かならずプライマリである．
			startService();
		}
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void start(int serverRole, String regionName) throws Exception {
		// リージョン名のセット
		this.regionName = regionName;

		if (serverRole == Extension.ROLE_PRIMARY) {
			// ロールがエージェント実行環境のプライマリであるかをチェック
			// し，そうであればサービスを開始する．
			// ただし，サンプルの構成では，かならずプライマリである．
			startService();
		}
	}

	private void startService() {
		System.out.println("Start AgentSystem Extension Region-" + regionName);

		System.out.println("************  ************   **********     ************");
		System.out.println("************  ************  ************  ************");
		System.out.println("         ***           ***                    ***              **            ***        ");
		System.out.println("         ***           ************    ********                  ***        ");
		System.out.println("         ***           ************        ********              ***        ");
		System.out.println("         ***           ***                    **                ***          ***        ");
		System.out.println("         ***           ************   ************           ***        ");
		System.out.println("         ***           ************      **********            ***       ");
	}
	
	private String name;
	private Integer mode, region;
	private Map initMap;
	private DestinationTable table;
	public String initAgentSystem(Map param) {
		try {
			initMap = param;
			name = (String) param.get(AgentSystemInitializer.paramID.HOST_NAME);
			mode = (Integer)param.get(AgentCloning.paramID.AGENT_MODE);
			table = (DestinationTable) param.get(AgentSystemInitializer.paramID.DEST_TABLE);
			table.setTableInfo((AgentProfileGenerator) param.get(AgentSystemInitializer.paramID.USER_PROFILE));
			region = 2;
			
			AgentMessageQueue.setParameter(param);
			
			//AgentCreate
			StringBuilder sb = new StringBuilder();
			for(Object id : table.getAgents()){
				String msg = createAgent(id);
				if(msg.equals("")) continue;
				sb.append(msg);
				sb.append("\n");
				System.out.println(msg);
			}
			
			return "<"+name+">[Success AgentSystem Initialize !] - " + AgentMessageQueue.getParameter()+"::Table\n"+table+"\n::Create\n"+sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	private Map<Object, AgentMessageQueue> agentMap = new HashMap();

	public String createAgent(Object agID) {
		Map setter = ((AgentProfileGenerator) initMap.get(AgentSystemInitializer.paramID.AGENT_PROFILE)).generate(agID);
		String msg = ((AgentCreator) initMap.get(AgentSystemInitializer.paramID.AGENT_CREATOR)).create(agID, setter);

		return msg;
	}
	
	//MQからのみ実行されるはず
	public String createCloneAgent(Object agID) {
		//Create CloneID
		Map idPair = AgentCloning.cloning(agID);
		if(idPair == null) return "";
		
		//Cloning Agent
		Map setter = ((AgentProfileGenerator) initMap.get(AgentSystemInitializer.paramID.AGENT_PROFILE)).generate(idPair.get("root"));
		String msg = ((AgentCreator) initMap.get(AgentSystemInitializer.paramID.AGENT_CREATOR)).create(idPair.get("clone"), setter);
		
		//transfer original task
		//upadte table
		table.updateTable(idPair.get("root"), idPair.get("clone"));

		return msg;
	}

	public void registeAgent(Object agID) {
		if (agentMap.containsKey(agID)) {
			return;
		}

		AgentMessageQueue agmq = new AgentMessageQueue(agID, (AgentUpdator) initMap.get(AgentSystemInitializer.paramID.AGENT_UPDATOR));
		agentMap.put(agID, agmq);
	}

	/*public Boolean updateAgent(Object agID, List data) {
		AgentMessageQueue agmq = (AgentMessageQueue) agentMap.get(agID);
		Boolean result = agmq.put(data);

		return result;
	}*/
	
	public List putDataToMQ(List data){
		List nokori = new ArrayList();
		
		Map<Object, List> map = table.repack(data);
		for(Object agID : map.keySet()){
			if(agentMap.get(agID) != null){
				AgentMessageQueue agmq = (AgentMessageQueue) agentMap.get(agID);
				Boolean result = agmq.put(map.get(agID));
					
				if(!result){
					nokori.addAll(map.get(agID));
					System.out.println("Load MQ:"+agID+"!! - "+createCloneAgent(agID));
				}
			}
		}
		
		return nokori;
	}
	
	public Boolean updateAgent(List data) {
		try{
			List nokori = putDataToMQ(data);
			
			if(getMode() == 1)
				while(!nokori.isEmpty()){
					nokori = putDataToMQ(nokori);
				}
		}catch(Exception e){
			System.out.println("Repack周りのError!");
			e.printStackTrace();
		}
		
		//後で修正
		Boolean result = true;
		return result;
	}

	private AgentIntaractionComm intaraction;

	public String setAgentIntaraction(Map intaractionMap) {
		//ServerConnectionManager manager = ServerConnectionManager.getInstance();
		//manager.createServerConnection(intaractionMap);
		//WindowStream window = new WindowStream(
		//		intaractionMap,
		//		manager.getLocalServer(),
		//		(ExtensionPutMessageQueue) intaractionMap.get(AgentIntaractionComm.paramID.WINDOW));
		//intaraction = new AgentIntaractionComm(
		//		window,
		//		(DestinationAgentTable) intaractionMap.get(AgentIntaractionComm.paramID.AGENT_TABLE)
		//);

		return "<"+name+">Set Agent Intaraction [" + intaraction.toString() + "]";
	}

	public AgentIntaractionComm getAgentIntaraction() {
		return intaraction;
	}

	public String startAgentSystem() {
		AgentMessageQueue.runnable = true;

		agentMap.values().stream()
				.map(e -> new Thread(e))
				.forEach(t -> ((Thread) t).start());

		return "<"+name+">[Success AgentSystem Start !]";
	}

	public String stopAgentSystem() {
		AgentMessageQueue.runnable = false;
		if (intaraction != null) {
			intaraction.quit();
		}

		for (AgentMessageQueue agmq : agentMap.values()) {
			List dummy = new ArrayList();
			dummy.add("quit");
			agmq.put(dummy);
		}

		return "<"+name+">[Success AgentSystem Shutdown !]";
	}
	
	public String getName(){
		return name;
	}
	
	public Integer getMode(){
		return mode;
	}
	
	public DestinationTable getTable(){
		return table;
	}
	
	public String getRegion(Object agID){
		if(((String)agID).contains("-"))
			agID = ((String)agID).split("-")[0];
		int hash = (Math.abs(agID.hashCode()) + 1) % region;
		return "ag"+hash;
	}
}
