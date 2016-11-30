/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.reader;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageFactory;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author kaeru
 */
public abstract class AgentReader implements AgentExecutor, Serializable{

    public AgentReader() {
        // TODO 自動生成されたコンストラクター・スタブ
    }

    AgentKey agentKey;
    String msgtype;
    public AgentReader(AgentKey agentKey, String msgtype) {
        // TODO 自動生成されたコンストラクター・スタブ
        this.agentKey = agentKey;
        this.msgtype = msgtype;
    }

    @Override
    public Object complete(Collection<Object> results) {
        // TODO 自動生成されたメソッド・スタブ 
        Object[] ret = results.toArray();
        return ret[0];
    }

    @Override
    public Object execute() {
        // TODO 自動生成されたメソッド・スタブ
        try{
            AgentManager agentManager = AgentManager.getAgentManager();

            MessageFactory factory = MessageFactory.getFactory();
            Message msg = factory.getMessage(msgtype);
            
            //System.out.println(agentKey+"-"+agentManager.exists(agentKey));

            Object ret = agentManager.sendMessage(agentKey, msg);

            //System.out.println(agentManager.getRegionName()+" - "+ret);
            
            return ret;
        }catch(IllegalAccessException | InstantiationException e){
            return e;
        }
    }

    public abstract Object read(AgentClient client, String agID);
}
