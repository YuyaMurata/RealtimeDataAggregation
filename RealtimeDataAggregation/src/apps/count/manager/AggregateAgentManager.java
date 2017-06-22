/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.manager;

import apps.count.property.AppCountProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kaeru
 */
public class AggregateAgentManager {

	public enum paramID {
		ID_RULE, AMOUNT_AGENTS, AGENT_MODE
	}

	private static AggregateAgentManager manager = new AggregateAgentManager();

	private List agentLists;

	private AggregateAgentManager() {
		AppCountProperty prop = AppCountProperty.getInstance();
		preparedAgentSystem(
				(Integer) prop.getParameter(paramID.AMOUNT_AGENTS),
				(String) prop.getParameter(paramID.ID_RULE));
	}

	private void preparedAgentSystem(Integer n, String rule) {
		agentLists = new ArrayList();
		for (int i = 0; i < n; i++) {
			agentLists.add(rule + i);
		}
	}

	public static AggregateAgentManager getInstance() {
		return manager;
	}

	public List getAgentList() {
		if (agentLists.isEmpty()) {
			System.out.println("Do not set parameters!");
		}
		return agentLists;
	}
}
