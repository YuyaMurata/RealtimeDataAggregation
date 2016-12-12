/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.extension.agent.comm;

import bench.template.UserData;
import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import rda.agent.table.DestinationAgentTable;
import rda.control.stream.WindowStream;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class AgentIntaractionComm implements AgentExecutor, Serializable {
    public enum paramID{
        AGENT_CONNECTION,WINDOW, AGENT_TABLE
    }
    
    private WindowStream window;
    private DestinationAgentTable table;
    public AgentIntaractionComm(WindowStream window, DestinationAgentTable table) {
        this.window = window;
        this.table = table;
        this.window.start();
    }
    
    public void connect(Object connectID, Object data){
        try{
            Object id = table.getDestAgentID(connectID);
        
            //Get List after that UserID Hash 
            System.out.println(id+" : "+((UserData)data).toString());
            
            this.window.in(id, data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Extension
    Map param;
    public AgentIntaractionComm(Map param) {
        this.param = param;
    }
    
    public void quit(){
        WindowStream.setRunnable(false);
    }

    @Override
    public Object complete(Collection<Object> results) {
        return results;
    }

    @Override
    public Object execute() {
        AgentSystemExtension extension = AgentSystemExtension.getInstance();
        String msg = extension.setAgentIntaraction(param);
        
        AgentManager am = AgentManager.getAgentManager();
        String regionName = am.getRegionName();
        
        return regionName + " : " + msg;
    }
    
    public static String setExtensionAgentIntaraction(AgentClient client, Map param){
        try {
            AgentIntaractionComm executor = new AgentIntaractionComm(param);

            Object reply = client.execute(executor);

            String msg = "Set Communication AgentSystemExtension : Reply is " + reply;
            
            return msg;
        } catch (AgentException ex) {
            return ex.toString();
        }
    }
    
    public String toString(){
        return window.toString()+" : "+table.toString();
    }
}
