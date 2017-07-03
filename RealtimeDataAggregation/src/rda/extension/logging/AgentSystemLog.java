/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.logging;

import java.util.Map;
import java.util.TreeMap;
import rda.agent.mq.AgentMessageQueue;

/**
 *
 * @author kaeru
 */
public class AgentSystemLog extends Thread{
	Map<Object, AgentMessageQueue> map;
	Long period;
	
	public AgentSystemLog(Long time, Map agmqMap) {
		this.map = agmqMap;
		this.period = time;
	}
	
	private void logging(){
		TreeMap t = new TreeMap();
		t.putAll(map);
		
		StringBuilder sb = new StringBuilder("MQ:{");
		for(Object id : map.keySet()){
			sb.append(id);
			sb.append("=");
			sb.append(map.get(id).getSize());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		System.out.println(sb);
	}
	
	@Override
	public void run(){
		System.out.println("Logging Start!");
		while(AgentMessageQueue.runnable){
			logging();
			
			try {
				Thread.sleep(period);
			} catch (InterruptedException ex) {
			}
		}
		System.out.println("Logging Stop!");
	}
}
