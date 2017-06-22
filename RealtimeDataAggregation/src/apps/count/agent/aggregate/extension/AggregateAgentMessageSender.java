/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.extension;

import apps.count.agent.aggregate.table.DestinationSubTable;
import apps.count.appuser.UserProfile;
import bench.template.UserData;
import rda.extension.agent.sender.ExtensionPutMessageQueue;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;
import rda.agent.profile.AgentProfileGenerator;

/**
 *
 * @author murata
 */
public class AggregateAgentMessageSender extends ExtensionPutMessageQueue {
	private static final String AGENT_TYPE = "aggregateagent";
	private transient Map<AgentConnection, DestinationSubTable> table;
	private transient AgentProfileGenerator prof;

	public AggregateAgentMessageSender() {
	}
	
	public AggregateAgentMessageSender(Map table, AgentProfileGenerator prof) {
		this.table = table;
		this.prof = prof;
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
