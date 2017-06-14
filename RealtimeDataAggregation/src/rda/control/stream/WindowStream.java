/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.stream;

import rda.extension.agent.exec.ExtensionPutMessageQueue;
import com.ibm.agent.exa.client.AgentClient;
import java.util.HashMap;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.control.flow.Window;
import rda.control.flow.WindowController;

/**
 *
 * @author kaeru
 */
public class WindowStream extends Thread {

	private WindowController flow;
	private AgentConnection agcon;
	private ExtensionPutMessageQueue sender;
	private String name;

	public WindowStream(Map param, AgentConnection agcon, ExtensionPutMessageQueue sender) {
		this.flow = new WindowController(param);
		this.name = "WS-<"+agcon+">";
		this.agcon = agcon;
		this.sender = sender;

		setRunnable(true);
		this.flow.start();
	}

	private static Boolean runnable;

	public static void setRunnable(Boolean state) {
		runnable = state;
		WindowController.setRunnable(state);
	}

	public void in(Object id, Object data) {
		flow.pack(id, data);
	}

	@Override
	public void run() {
		AgentClient client = agcon.getClient();
		Map verifMap = new HashMap();
		while (runnable) {
			//Get Window
			Window window = flow.get();
			if (window == null) {
				continue;
			}

			//Update
			String msg = sender.send(client, window.id, window.unpack());
			if(verifMap.get(window.id) == null) verifMap.put(window.id, 0);
			verifMap.put(window.id, (Integer)verifMap.get(window.id) + window.unpack().size());
		}
		System.out.println(agcon.getHost()+"-\n\t"+verifMap);
		agcon.returnConnection(client);
	}
	
	public String toString(){
		return this.name;
	}

}
