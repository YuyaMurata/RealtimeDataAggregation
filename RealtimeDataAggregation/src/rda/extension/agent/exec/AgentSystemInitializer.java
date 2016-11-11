/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.exec;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentSystemInitializer implements AgentExecutor, Serializable {

    public static enum paramID{
        REGION_NAME, AGENT_PROFILE, AGENT_CREATOR
    }

    public AgentSystemInitializer() {
    }
    
    Map param;
    public AgentSystemInitializer(Map param) {
        this.param = param;
    }

    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    /**
     * 全エージェントにDISPOSEメッセージを送信する．
     */
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        String msg = extension.initAgentSystem(param);
        
        return msg;
    }
    
    public String initalize(AgentClient client, Map param){
        try {
            AgentSystemInitializer executor = new AgentSystemInitializer(param);
            
            String regionName = (String) param.get(paramID.REGION_NAME);

            Object reply;
            if(regionName.equals(""))
                reply = client.execute(executor);
            else
                reply = client.executeAt(regionName, executor);
            
            String msg = "Initialize [" + regionName + "] was initialized. Reply is [" + reply + "]";
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }
    }
}
