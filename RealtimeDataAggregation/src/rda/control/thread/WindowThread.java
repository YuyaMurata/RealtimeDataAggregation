/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.thread;

import java.util.List;
import rda.agent.client.AgentConnection;
import rda.extension.agent.sender.ExtensionPutMessageQueue;

/**
 *
 * @author kaeru
 */
public class WindowThread implements Runnable{
	AgentConnection server;
	ExtensionPutMessageQueue sender;
	List sendData;
	
	public WindowThread(AgentConnection server, ExtensionPutMessageQueue sender, List sendData) {
		this.server = server;
		this.sender = sender;
		this.sendData = sendData;
	}
	
	@Override
	public void run() {
		String msg = sender.send(server, sendData);
	}
	
}
