package rda.agent.creator;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.MessageFactory;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import rda.agent.message.InitMessage;
import rda.extension.agent.manager.AgentSystemExtension;

public abstract class AgentCreator implements AgentExecutor, Serializable {

	public AgentCreator() {
	}

	AgentKey agentKey;
	String msgtype;
	Object state;

	public AgentCreator(AgentKey agentKey, String msgtype, Object state) {
		this.agentKey = agentKey;
		this.msgtype = msgtype;
		this.state = state;
	}

	@Override
	public Object complete(Collection<Object> results) {
		// TODO 自動生成されたメソッド・スタブ
		Object[] ret = results.toArray();
		return ret;
	}

	@Override
	public Object execute() {
		// TODO 自動生成されたメソッド・スタブ
		try {
			AgentManager agentManager = AgentManager.getAgentManager();
			
			//Register Extension
			AgentSystemExtension extension = AgentSystemExtension.getInstance();
			
			//Region Check
			if(!agentManager.getRegionName().equals(extension.getRegion(agentKey.getValue(0))))
				return "";
				
			extension.registeAgent(agentKey.getValue(0));

			if (agentManager.exists(agentKey)) {
				return "agent (" + agentKey + ") already exists";
			}

			agentManager.createAgent(agentKey);

			MessageFactory factory = MessageFactory.getFactory();
			InitMessage msg = (InitMessage) factory.getMessage(msgtype);

			msg.setParams(state);

			Object ret = "<"+extension.getName()+">"+agentManager.sendMessage(agentKey, msg);

			return ret;
		} catch (AgentException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			return e;
		}
	}

	public abstract String create(AgentClient client, Map setter);

	public abstract String create(Object id, Map setter);
}
