/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public abstract class AgentProfile implements Serializable{
    private Map<Object, Map> profile = new HashMap();
    private List idList;
    public AgentProfile(List agIDLists){
        this.profile = initProfile(agIDLists);
        this.idList = agIDLists;
    }
    
    public abstract Map initProfile(List agIDLists);
    
    public Map getProfile(Object agID){
        return profile.get(agID);
    }
    
    public List getIDList(){
        return idList;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Object agID : profile.keySet()){
            sb.append(agID);
            sb.append(" : ");
            sb.append(profile.get(agID));
            sb.append("\n");
        }
        return sb.toString();
    }
}
