/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.thread;

import java.util.List;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class ExtensionPutThread implements Runnable{
	private AgentSystemExtension extension;
	private List data;
	
	public ExtensionPutThread(List data) {
		extension = AgentSystemExtension.getInstance();
		this.data = data;
	}
	
	@Override
	public void run() {
		try{
			List nokori = extension.putDataToMQ(data);
			if(extension.getMode() == 1)
				while(!nokori.isEmpty()){
					nokori = extension.putDataToMQ(nokori);
				}
		}catch(Exception e){
			System.out.println("Repack周りのError!");
			e.printStackTrace();
		}
	}
	
}
