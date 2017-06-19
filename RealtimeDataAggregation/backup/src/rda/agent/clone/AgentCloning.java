package rda.agent.clone;

import rda.extension.agent.manager.AgentSystemExtension;



public class AgentCloning {

	public static String cloning(String sourceID, Object originalState) {
		AgentSystemExtension extension = AgentSystemExtension.getInstance();
		
		if (extension.getMode() == 0) {
			return "";
		}

		System.out.println(">> Start Agent Cloning");

		IDManager id = manager.getIDManager();
		String originalID = id.getOrigID(sourceID);

		//Clone
		String cloneID = manager.createCloneAgent(originalID, originalState);
		id.regID(originalID, cloneID);

		System.out.println(">> Agent Cloning New Copy From " + originalID);

		return cloneID;
	}

	public static String delete(String deleteID) {
		AgentSystemExtension extension = AgentSystemExtension.getInstance();
		
		if (extension.getMode() == 0) {
			return "";
		}

		System.out.println(">> Start Agent Delete");

		IDManager id = manager.getIDManager();
		String originalID = id.getOrigID(deleteID);

		//Delete
		id.deleteID(originalID, deleteID);

		System.out.println(">> Agent Cloning Delete " + deleteID);

		return deleteID;
	}
}
