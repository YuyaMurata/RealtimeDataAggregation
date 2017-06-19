/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import rda.agent.client.AgentConnection;
import rda.agent.deploy.DeployStrategy;
import rda.agent.table.DestinationAgentTable;
import rda.property.RDAProperty;

/**
 *
 * @author kaeru
 */
public class ServerConnectionManager {
	public enum paramID {
		AMOUNT_SERVERS, AMOUNT_REGIONS, HOSTNAME_RULE, SERVER_PORT, APP_CLASS,
		DEPLOY_PATTERN, AGENTTYPE_LIST, DEPLOY_BALANCE
	}

	private static ServerConnectionManager manager = new ServerConnectionManager();

	private ServerConnectionManager() {
	}

	public static ServerConnectionManager getInstance() {
		return manager;
	}

	private List server;
	private RDAProperty prop;
	public void createServerConnection() {
		//Load Property
		prop = RDAProperty.getInstance();

		//Set Server
		server = new ArrayList();
		for (int i = 0; i < (Integer) prop.getParameter(paramID.AMOUNT_SERVERS); i++) {
			String host = (String) prop.getParameter(paramID.HOSTNAME_RULE) + (i +1);
			server.add(new AgentConnection(
					(Integer) prop.getParameter(AgentConnection.paramID.POOL_SIZE),
					new String[]{
						host + ":" + prop.getParameter(paramID.SERVER_PORT),
						(String) prop.getParameter(paramID.APP_CLASS),
						"agent"
					}
			));
		}

		//Set Localhost
		server.add(new AgentConnection(
			(Integer) prop.getParameter(AgentConnection.paramID.POOL_SIZE),
			new String[]{
				"localhost:2809",
				(String) prop.getParameter(paramID.APP_CLASS),
				"agent"
			}
		));
	}

	//消す予定　DeployStrategyで記述
	private Map deployMap;
	public void agentDeployServer(Map deployRule) {
		deployMap = new HashMap();
		List cluster = server.subList(0, server.size() - 1);
		if ((int) deployRule.get(paramID.DEPLOY_PATTERN) == 0) {
			//Agent Cloning - Server Pattern
			for (Object idrule : (List) deployRule.get(paramID.AGENTTYPE_LIST)) {
				deployMap.put(idrule, cluster);
			}
		} else {
			//Agent Typing - Server Pattern
			String[] numAgServer = ((String) deployRule.get(paramID.DEPLOY_BALANCE)).split(":");
			Integer i = 0;

			//AgentType = 1
			if (((List) deployRule.get(paramID.AGENTTYPE_LIST)).size() == 1) {
				deployMap.put(((List) deployRule.get(paramID.AGENTTYPE_LIST)).get(0), cluster);
			}

			//AgentType > 2
			for (Object idrule : (List) deployRule.get(paramID.AGENTTYPE_LIST)) {
				List agServerList = new ArrayList();
				int n = Integer.valueOf(numAgServer[i]);
				for (int j = 0; j < n; j++) {
					agServerList.add(cluster.get(0));
					cluster.remove(0);
				}
				deployMap.put(idrule, agServerList);
				i++;
			}
		}
	}
	
	//UserIDによるサーバー分散
	public AgentConnection getDealServer(Object id, int age){
		//return idDeal(id);
		return ageDeal(age);
	}
	
	//IDで分散
	private AgentConnection idDeal(Object id){
		int index = Math.abs(id.hashCode()) % getAllServer().size();
		return (AgentConnection) getAllServer().get(index);
	}
	
	//年齢で分散するための処理
	private TreeMap ageMap = new TreeMap();
	public void createAgeMap(int maxAge){
		for(int i =0; i < getAllServer().size(); i++){
			int n = maxAge / getAllServer().size();
			ageMap.put(i*n, getAllServer().get(i));
		}
	}
	
	//年齢で分散
	private AgentConnection ageDeal(int age){
		return (AgentConnection) ageMap.floorEntry(age).getValue();
	}
	
	//配置戦略の適用
	private DeployStrategy strategy;
	public void setDeployStrategy(DeployStrategy strategy){
		strategy.createDeployPattern(
				(int) prop.getParameter(ServerConnectionManager.paramID.DEPLOY_PATTERN), 
				getAllServer(), 
				(int) prop.getParameter(DestinationAgentTable.paramID.DEST_TABLE_SIZE));
		this.strategy = strategy;
	}

	//Localhostの取得
	public AgentConnection getLocalServer() {
		return (AgentConnection) server.get(server.size() - 1);
	}

	//エージェント間通信時の通信サーバーの取得
	public AgentConnection getDistributedServer(Object agID) {
		return null;//(AgentConnection) strategy.getDeployServer(agID);
	}

	//エージェントタイプによる全サーバの取得(ローカルを含む)
	public Map getDeployAllServer() {
		return deployMap;//Strategyから取得できるように変更
	}
	
	//全サーバの取得(ローカルを含まない)
	public List getAllServer() {
		return server.subList(0, server.size()-1);
	}
	
	//戦略の出力
	public String getDeployAllServerToString() {
		return strategy.toString();
	}
}
