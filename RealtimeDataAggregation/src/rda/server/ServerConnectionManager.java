/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.client.AgentConnection;

/**
 *
 * @author kaeru
 */
public class ServerConnectionManager {
    public enum paramID{
        AMOUNT_SERVERS, AMOUNT_REGIONS, HOSTNAME_RULE, SERVER_PORT, APP_CLASS,
        DEPLOY_PATTERN, AGENTTYPE_LIST, DEPLOY_BALANCE
    }
    
    private static ServerConnectionManager manager = new ServerConnectionManager();

    private ServerConnectionManager() {
    }
    
    public static ServerConnectionManager getInstance(){
        return manager;
    }
    
    private List server;
    public void createServerConnection(Map propMap){
        server = new ArrayList();
        for(int i=0; i < (Integer)propMap.get(paramID.AMOUNT_SERVERS); i++){
            String host = (String)propMap.get(paramID.HOSTNAME_RULE)+i;
            server.add(new AgentConnection(
                        (Integer)propMap.get(AgentConnection.paramID.POOL_SIZE), 
                        new String[]{host+":"+propMap.get(paramID.SERVER_PORT),
                            (String)propMap.get(paramID.APP_CLASS),
                            "agent"}
                            )
                        );    
        }
        
        //Localhost
        server.add(new AgentConnection(
                        (Integer)propMap.get(AgentConnection.paramID.POOL_SIZE), 
                        new String[]{"localhost:2809",
                            (String)propMap.get(paramID.APP_CLASS),
                            "agent"}
                        )
                    );
    }
    
    private Map deployMap;
    public void agentDeployServer(Map deployRule){
        deployMap = new HashMap();
        List cluster = server.subList(0, server.size()-1);
        if((int)deployRule.get(paramID.DEPLOY_PATTERN) == 0){
            for(Object idrule : (List)deployRule.get(paramID.AGENTTYPE_LIST))
                deployMap.put(idrule, cluster);
        } else {
            String[] numAgServer = ((String)deployRule.get(paramID.DEPLOY_BALANCE)).split(":");
            Integer i = 0;
            for(Object idrule : (List)deployRule.get(paramID.AGENTTYPE_LIST)){
                List agServerList = new ArrayList();
                int n = Integer.valueOf(numAgServer[i]);
                for(int j=0 ; j < n; j++){
                    agServerList.add(cluster.get(0));
                    cluster.remove(0);
                }
                deployMap.put(idrule, agServerList);
                i++;
            }
        }   
    }
    
    public AgentConnection getLocalServer(){
        return (AgentConnection) server.get(server.size()-1);
    }
    
    public AgentConnection getDistributedServer(){
        //size-1 = localserver
        return null;
    }
    
    public Map getDeployAllServer(){
        return deployMap;
    }
}
