/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.exec;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentSystemInitializer implements AgentExecutor, Serializable {

	public static enum paramID {
		HOST_NAME, AGENT_TYPE, AGENT_PROFILE, AGENT_CREATOR, AGENT_UPDATOR, AGENT_MODE,
		USER_PROFILE, DEST_TABLE
	}

	public AgentSystemInitializer() {
	}

	Map param;

	public AgentSystemInitializer(Map param) {
		this.param = param;
	}

	@Override
	public Object complete(Collection<Object> results) {
		return results;
	}

	@Override
	public Object execute() {
		AgentSystemExtension extension = AgentSystemExtension.getInstance();
		String msg = extension.initAgentSystem(param);

		AgentManager am = AgentManager.getAgentManager();
		String regionName = am.getRegionName();

		return regionName + " : " + msg;
	}

	public String initalize(AgentClient client, Map param) {
		try {
			AgentSystemInitializer executor = new AgentSystemInitializer(param);

			Object reply = client.execute(executor);

			String msg = "Initialize AgentSystemExtension : Reply is " + reply;

			return msg;
		} catch (AgentException ex) {
			return ex.toString();
		}
	}
}
