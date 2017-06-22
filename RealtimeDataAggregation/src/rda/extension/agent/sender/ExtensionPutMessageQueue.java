/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.sender;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import rda.agent.client.AgentConnection;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public abstract class ExtensionPutMessageQueue implements AgentExecutor, Serializable {

	public ExtensionPutMessageQueue() {
	}

	List data;

	public ExtensionPutMessageQueue(List data) {
		this.data = data;
	}

	@Override
	public Object complete(Collection<Object> results) {
		return results;
	}

	@Override
	public Object execute() {
		AgentSystemExtension extension = AgentSystemExtension.getInstance();

		//Boolean result = extension.updateAgent(agentKey.getValue(0), data);
		Boolean result = extension.updateAgent(data);

		String msg = " : Update Agent = " + result;

		if (!result) {
			System.out.println(msg);
		}

		return msg;
	}

	public abstract String send(AgentConnection server, List data);
}
