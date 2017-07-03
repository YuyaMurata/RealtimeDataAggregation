/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.profile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public class AgentProfileGenerator implements Serializable {

	private AgentProfile prof;

	public AgentProfileGenerator(AgentProfile prof) {
		this.prof = prof;
	}

	public List registerIDList() {
		return prof.getIDList();
	}

	public Map generate(Object agID) {
		return prof.getProfile(agID);
	}

	public Object getAttribute(Object agID) {
		return prof.getAttribute(agID);
	}

	public String getAgentIDRule() {
		return prof.getIDList().get(0).toString().split("#")[0];
	}

	public String toString() {
		return prof.toString();
	}
}
