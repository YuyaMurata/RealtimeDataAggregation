package rda.agent.clone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.extension.agent.manager.AgentSystemExtension;

public class AgentCloning {
	public enum paramID{
		AGENT_MODE
	}
	
	public static Map cloning(Object sourceID) {
		AgentSystemExtension extension = AgentSystemExtension.getInstance();
		
		if (extension.getMode() == 0) {
			return null;
		}
		
		Map map = new HashMap();

		//Get RootAgent lID
		System.out.println(">> Start Agent Cloning");
		Map<Object, List> agents = extension.getTable().agentMap;
		Object rootID = rootSearch(agents, sourceID);
		
		map.put("root", rootID);
		
		//Clone
		Object cloneID = rootID+"-"+agents.get(rootID).size();
		map.put("clone", cloneID);
		
		System.out.println(">> Agent Cloning New Copy From " + rootID + " -> "+cloneID);
		System.out.println(extension.getTable());
		
		return map;
	}

	public static String delete(String deleteID) {
		/*AgentSystemExtension extension = AgentSystemExtension.getInstance();
		
		if (extension.getMode() == 0) {
			return "";
		}

		System.out.println(">> Start Agent Delete");

		IDManager id = manager.getIDManager();
		String originalID = id.getOrigID(deleteID);

		//Delete
		id.deleteID(originalID, deleteID);

		System.out.println(">> Agent Cloning Delete " + deleteID);
		*/
		return "";//deleteID;
	}
	
	private static Object rootSearch(Map<Object, List> agents, Object agID){
		if(agents.keySet().contains(agID))
			return agID;
		else{
			return agents.keySet().stream().filter(id -> agents.get(id).contains(agID)).findFirst().get();
		}
	}
}
