/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.extension;

import apps.count.appuser.UserProfile;
import bench.template.UserData;
import rda.extension.agent.exec.ExtensionPutMessageQueue;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.client.AgentClient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.table.DestinationAgentTable;

/**
 *
 * @author kaeru
 */
public class AggregateAgentMessageSender extends ExtensionPutMessageQueue {
	private static final String AGENT_TYPE = "aggregateagent";
	private transient DestinationAgentTable table;
	private transient AgentProfileGenerator prof;
	
	public AggregateAgentMessageSender(DestinationAgentTable table, AgentProfileGenerator prof) {
		this.table = table;
		this.prof = prof;
	}

	public AggregateAgentMessageSender(AgentKey agentKey, List data) {
		super(agentKey, data);
	}

	@Override
	public String send(AgentClient client, List data) {
		Map agpack = repack(data);
		StringBuilder sb = new StringBuilder();
		for(Object agID : agpack.keySet())
			try {
				AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

				AggregateAgentMessageSender executor = new AggregateAgentMessageSender(agentKey, data);

				Object reply = client.execute(agentKey, executor);

				String msg = "Update Agent : Reply is " + reply;

				sb.append(msg);
				sb.append(",\n");
			} catch (AgentException ex) {
				return ex.toString();
			}
		return sb.toString();
	}
	
	private Map repack(List data){
		Object map = data.stream()
				.collect(Collectors.groupingBy(user -> table.getDestAgentID(((UserData)user).id, 
						(Integer) prof.generate(user).get(UserProfile.profileID.AGE))));
		return (Map) map;
	}
}
