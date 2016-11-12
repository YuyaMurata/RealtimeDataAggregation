/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.profile;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public class AgentProfileGenerator implements Serializable{
    private AgentProfile prof;
    
    public AgentProfileGenerator(AgentProfile prof) {
        this.prof = prof;
    }
    
    public Map generate(String agID){
        return prof.getProfile(agID);
    }
    
    public String toString(){
        return prof.toString();
    }
}
