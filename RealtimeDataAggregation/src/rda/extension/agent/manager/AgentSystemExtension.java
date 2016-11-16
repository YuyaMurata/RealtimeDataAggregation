/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.manager;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.soliddb.extension.Extension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import rda.agent.creator.AgentCreator;
import rda.agent.mq.AgentMessageQueue;
import rda.agent.profile.AgentProfileGenerator;
import rda.agent.updator.AgentUpdator;
import rda.extension.agent.exec.AgentSystemInitializer;

/**
 *
 * @author kaeru
 */
public class AgentSystemExtension implements Extension {

    private static AgentSystemExtension extention = new AgentSystemExtension();

    public static AgentSystemExtension getInstance() {
        return extention;
    }

    AgentKey extensionAgentKey;

    public AgentSystemExtension() {
    }

    // リージョン名
    String regionName;

    @Override
    public void initialize(String serverTypeName, Properties params) throws Exception {
    }

    @Override
    public void primaryChanged() throws Exception {
    }

    @Override
    public void regionChanged(int serverRole, String regionName) throws Exception {
        // リージョン名のセット
        this.regionName = regionName;

        if (serverRole == Extension.ROLE_PRIMARY) {
            // ロールがエージェント実行環境のプライマリであるかをチェック
            // し，そうであればサービスを開始する．
            // ただし，サンプルの構成では，かならずプライマリである．
            startService();
        }
    }

    @Override
    public void roleChanged(int serverRole) throws Exception {
        if (serverRole == Extension.ROLE_PRIMARY) {
            // ロールがエージェント実行環境のプライマリであるかをチェック
            // し，そうであればサービスを開始する．
            // ただし，サンプルの構成では，かならずプライマリである．
            startService();
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void start(int serverRole, String regionName) throws Exception {
        // リージョン名のセット
        this.regionName = regionName;

        if (serverRole == Extension.ROLE_PRIMARY) {
            // ロールがエージェント実行環境のプライマリであるかをチェック
            // し，そうであればサービスを開始する．
            // ただし，サンプルの構成では，かならずプライマリである．
            startService();
        }
    }

    private void startService() {
        System.out.println("Start AgentSystem Extension Region-"+regionName);

        System.out.println("************ ************  **********  ************");
        System.out.println("************ ************ ************ ************");
        System.out.println("    ***      ***          ***       **      ***    ");
        System.out.println("    ***      ************  ********         ***    ");
        System.out.println("    ***      ************    ********       ***    ");
        System.out.println("    ***      ***          **       ***      ***    ");
        System.out.println("    ***      ************ ************      ***    ");
        System.out.println("    ***      ************  **********       ***    ");
    }

    private AgentProfileGenerator agentProf;
    private AgentCreator creator;
    private AgentUpdator updator;
    public String initAgentSystem(Map param) {
        try {
            agentProf = (AgentProfileGenerator) param.get(AgentSystemInitializer.paramID.AGENT_PROFILE);
            creator = (AgentCreator) param.get(AgentSystemInitializer.paramID.AGENT_CREATOR);
            updator = (AgentUpdator) param.get(AgentSystemInitializer.paramID.AGENT_UPDATOR);
            
            AgentMessageQueue.setParameter(param);
            agentMap = new HashMap();
            
            return "[Success AgentSystem Initialize !] - "+AgentMessageQueue.getParameter();
        } catch (Exception e) {
            return e.toString();
        }
    }

    private Map<String, AgentMessageQueue> agentMap;
    public String createAgent(String agID) {
        AgentMessageQueue agmq = new AgentMessageQueue(agID, updator);
        agentMap.put(agID, agmq);
        
        Map setter = agentProf.generate(agID);
        String msg = creator.create(setter);
        
        Thread thread = new Thread(agmq);
        thread.start();
        
        return msg;
    }
    
    public Boolean updateAgent(String agID, List data){
        AgentMessageQueue agmq = (AgentMessageQueue) agentMap.get(agID);
        Boolean result = agmq.put(data);
        
        //System.out.println(agmq.toString());
        
        return result;
    }
    
    public String stopAgentSystem(){
        AgentMessageQueue.runnable = false;
        for(AgentMessageQueue agmq : agentMap.values())
            agmq.put("quit");
        
        return "[Success AgentSystem Shutdown !]";
    }
}
