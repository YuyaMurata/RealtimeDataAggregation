/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.stream;

import rda.extension.agent.sender.ExtensionPutMessageQueue;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import rda.agent.client.AgentConnection;
import rda.control.flow.Window;
import rda.control.flow.WindowController;
import rda.control.thread.WindowThread;

/**
 *	
 * @author kaeru
 */
public class WindowStream extends Thread {
	private static int serialID = 0;
	private WindowController flow;
	private ExtensionPutMessageQueue sender;
	private String name;
	
	private ExecutorService exeService = Executors.newFixedThreadPool(16);
	//private Integer poolsize;

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
			
			//Update
			//System.out.println("Host::"+server.getHost());
			//Long start = System.currentTimeMillis();
			
			//String msg = sender.send(server, window.unpack());
			exeService.execute(new WindowThread(server, sender, window.unpack()));
			
			//Long stop = System.currentTimeMillis();
			//System.out.println((stop-start)+",[ms]");
			
			total+=window.unpack().size();
		}
		
		//終了処理
		try{
			exeService.shutdown();
			if(!exeService.awaitTermination(1, TimeUnit.SECONDS))
				exeService.shutdownNow();
		}catch(InterruptedException e){
			System.out.println("awaitTermination interrupted: " + e); 
			exeService.shutdownNow();
		}
		
		
		System.out.println("WindowStream total send data = "+total);
		
	}
	
	@Override
	public String toString(){
		return this.name;
	}

}
