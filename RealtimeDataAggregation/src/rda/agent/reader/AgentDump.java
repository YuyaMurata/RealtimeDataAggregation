/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.reader;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageFactory;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentDump implements AgentExecutor, Serializable {
	public static String MESSAGE_TYPE;
	
	public AgentDump() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	String msgtype;

	public AgentDump(String msgtype) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.msgtype = msgtype;
	}

	@Override
	public Object complete(Collection<Object> results) {
		// TODO 自動生成されたメソッド・スタブ 
		Object[] ret = results.toArray();
		return ret[0];
	}

	@Override
	public Object execute() {
		// TODO 自動生成されたメソッド・スタブ
		try {
			AgentSystemExtension extension = AgentSystemExtension.getInstance();
			
			AgentManager agentManager = AgentManager.getAgentManager();

			MessageFactory factory = MessageFactory.getFactory();
			Message msg = factory.getMessage(msgtype);

			//System.out.println(agentKey+"-"+agentManager.exists(agentKey));
			Object ret = "<"+extension.getName()+">,"+agentManager.sendMessage(msg);

			//System.out.println(agentManager.getRegionName()+" - "+ret);
			return ret;
		} catch (IllegalAccessException | InstantiationException e) {
			return e;
		}
	}

	public String dump(AgentClient client){
		AgentDump executor = new AgentDump();
		try {
			Object reply = client.execute(executor);
			
			String msg = "AgentDump : "+reply;
			return msg;
		} catch (AgentException ex) {
			ex.printStackTrace();
			return ex.toString();
		}
	}
}
