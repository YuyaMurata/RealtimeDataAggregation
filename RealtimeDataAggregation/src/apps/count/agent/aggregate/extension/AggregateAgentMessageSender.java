/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.extension;

import rda.extension.agent.sender.ExtensionPutMessageQueue;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import rda.agent.client.AgentConnection;

/**
 *
 * @author murata
 */
public class AggregateAgentMessageSender extends ExtensionPutMessageQueue {
	public AggregateAgentMessageSender() {
	}

	public AggregateAgentMessageSender(List data) {
		super(data);
	}

	@Override
	public String send(AgentConnection server, List data) {
		try {
			AgentClient client = server.getClient();
			
			AggregateAgentMessageSender executor = new AggregateAgentMessageSender(data);

			Object reply = client.execute(executor);

			String msg = "Update Agent : Reply is " + reply;

			server.returnConnection(client);
				
			return msg;
		} catch (AgentException ex) {
			return ex.toString();
		}
	}
}
