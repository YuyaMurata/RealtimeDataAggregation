/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.profile;

import java.util.Map;
import sun.java2d.cmm.Profile;

/**
 *
 * @author kaeru
 */
public class AgentProfileGenerator {
    private AgentProfile prof;
    
    public AgentProfileGenerator(AgentProfile prof) {
        this.prof = prof;
    }
    
    public Map generate(String agID){
        return prof.getProfile(agID);
    }
}
