/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.manager;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.soliddb.extension.Extension;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import rda.agent.creator.AgentCreator;
import rda.agent.profile.AgentProfileGenerator;
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
    
    private Boolean runnable;
    @Override
    public void shutdown() {
        this.runnable = false;
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
        System.out.println("Start AgentSystem Extension");

        System.out.println("************ ************  **********  ************");
        System.out.println("************ ************ ************ ************");
        System.out.println("    ***      ***          ***       **      ***    ");
        System.out.println("    ***      ************  ********         ***    ");
        System.out.println("    ***      ************    ********       ***    ");
        System.out.println("    ***      ***          **       ***      ***    ");
        System.out.println("    ***      ************ ************      ***    ");
        System.out.println("    ***      ************  **********       ***    ");
    }
    
    public String initAgentSystem(Map param){
        try{
            agentProf = (AgentProfileGenerator)param.get(AgentSystemInitializer.paramID.AGENT_PROFILE);
            creator = (AgentCreator) param.get(AgentSystemInitializer.paramID.AGENT_CREATOR);
            
            return regionName+"[Success AgentSystem Initialize !]";
        }catch(Exception e){
            return e.toString();
        }
    }
    
    private AgentProfileGenerator agentProf;
    private AgentCreator creator;
    public String createAgents(List agIDLists){
        StringBuilder sb = new StringBuilder();
        for(String agID : (List<String>)agIDLists){
            Map setter = agentProf.generate(agID);
            String msg = creator.create(setter);
            
            sb.append(msg);
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
