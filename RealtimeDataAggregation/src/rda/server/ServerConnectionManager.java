/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.server;

import java.util.ArrayList;
import java.util.List;
import rda.agent.client.AgentConnection;
import rda.property.RDAProperty;

/**
 *
 * @author kaeru
 */
public class ServerConnectionManager {
    public enum paramID{
        AMOUNT_SERVERS, AMOUNT_REGIONS, HOSTNAME_RULE, SERVER_PORT, APP_CLASS
    }
    
    private static ServerConnectionManager manager = new ServerConnectionManager();

    private ServerConnectionManager() {
        createServerConnection();
    }
    
    public static ServerConnectionManager getInstance(){
        return manager;
    }
    
    private List server = new ArrayList();
    private void createServerConnection(){
        RDAProperty rdaprop = RDAProperty.getInstance();
        
        for(int i=0; i < (Integer)rdaprop.getParameter(paramID.AMOUNT_SERVERS); i++){
            String host = (String)rdaprop.getParameter(paramID.HOSTNAME_RULE)+i;
            server.add(new AgentConnection(
                        (Integer)rdaprop.getParameter(AgentConnection.paramID.POOL_SIZE), 
                        new String[]{host+":"+rdaprop.getParameter(paramID.SERVER_PORT),
                            (String)rdaprop.getParameter(paramID.APP_CLASS),
                            "agent"}
                            )
                        );    
        }
        
        //Localhost
        server.add(new AgentConnection(
                        (Integer)rdaprop.getParameter(AgentConnection.paramID.POOL_SIZE), 
                        new String[]{"localhost:2809",
                            (String)rdaprop.getParameter(paramID.APP_CLASS),
                            "agent"}
                        )
                    );
    }
    
    public AgentConnection getLocalServer(){
        return (AgentConnection) server.get(server.size()-1);
    }
    
    public AgentConnection getDistributedServer(){
        //size-1 = localserver
        return null;
    }
}
