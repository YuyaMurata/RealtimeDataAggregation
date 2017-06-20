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
import rda.agent.table.DestinationTable;

/**
 *
 * @author kaeru
 */
public class AggregateAgentMessageSender extends ExtensionPutMessageQueue {
	private static final String AGENT_TYPE = "aggregateagent";
	private transient DestinationTable table;
	private transient AgentProfileGenerator prof;
	
	public AggregateAgentMessageSender(DestinationTable table, AgentProfileGenerator prof) {
		this.table = table;
		this.prof = prof;
	}

	public AggregateAgentMessageSender(AgentKey agentKey, List data) {
		super(agentKey, data);
	}

	@Override
	public String send(AgentClient client, List data) {
		Map<Object, List> agpack = repack(data);
		
		StringBuilder sb = new StringBuilder();
		for(Object agID : agpack.keySet())
			try {
				AgentKey agentKey = new AgentKey(AGENT_TYPE, new Object[]{agID});

				AggregateAgentMessageSender executor = new AggregateAgentMessageSender(agentKey, agpack.get(agID));

				Object reply = client.execute(agentKey, executor);

				String msg = "Update Agent : Reply is " + reply;

				sb.append(msg);
				sb.append(",\n");
			} catch (AgentException ex) {
				return ex.toString();
			}
		
		return sb.toString();
	}
	
	private Map repack(List<UserData> data){
		Object map = data.stream()
				.collect(Collectors.groupingBy(user -> table.getDestAgentID(user.id,
						(Integer) prof.generate(user.id).get(UserProfile.profileID.AGE))));
		
		/*System.out.println("Repack::");
		for(UserData user : data){
			System.out.println("\t"+user+"("+prof.generate(user.id).get(UserProfile.profileID.AGE)+") -> "+table.getDestAgentID(user.id,(Integer) prof.generate(user.id).get(UserProfile.profileID.AGE)));
		}
		System.out.println("");
		*/
		return (Map) map;
	}
}
