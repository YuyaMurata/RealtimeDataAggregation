/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.profile.AgentProfile;

/**
 *
 * @author kaeru
 */
public class AggregateAgentProfile extends AgentProfile {

	@Override
	public Object getAttribute(Object agID) {
		return null;
	}

	public static enum paramID {
		ID, MESSAGE_DATA
	}

	public AggregateAgentProfile(List agIDLists) {
		super(agIDLists);
	}

	@Override
	public Map initProfile(List agIDLists) {
		Map map = new HashMap();

		for (Object agID : agIDLists) {
			Map profParam = new HashMap();

			//ID
			profParam.put(paramID.ID, agID);

			//MESSAG_DATA
			List msgdata = new ArrayList();
			msgdata.add("Aggregate Conditios :" + agID);
			profParam.put(paramID.MESSAGE_DATA, msgdata);

			map.put(agID, profParam);
		}

		return map;
	}

}
