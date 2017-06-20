/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.stream;

import rda.extension.agent.exec.ExtensionPutMessageQueue;
import com.ibm.agent.exa.client.AgentClient;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.control.flow.Window;
import rda.control.flow.WindowController;

/**
 *
 * @author kaeru
 */
public class WindowStream extends Thread {
	private static int serialID = 0;
	private WindowController flow;
	private ExtensionPutMessageQueue sender;
	private String name;

	public WindowStream(Map param, ExtensionPutMessageQueue sender) {
		this.flow = new WindowController(param);
		this.name = "WS-<"+(serialID++)+">";
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
		int total = 0;
		while (runnable) {
			//Get Window
			Window window = flow.get();
			if (window == null) {
				continue;
			}
			
			AgentConnection server = (AgentConnection) window.id;
			AgentClient client = server.getClient();
			
			//Update
			System.out.println("Host::"+server.getHost());
			String msg = sender.send(client, window.unpack());
			System.out.println("");
			total+=window.unpack().size();
			
			server.returnConnection(client);
		}
		
		System.out.println("WindowStream total send data = "+total);
	}
	
	public String toString(){
		return this.name;
	}

}
