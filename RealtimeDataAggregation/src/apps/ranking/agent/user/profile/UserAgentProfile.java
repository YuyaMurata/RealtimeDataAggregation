/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.user.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.profile.AgentProfile;
import rda.agent.profile.AgentProfileGenerator;

/**
 *
 * @author kaeru
 */
public class UserAgentProfile extends AgentProfile{
    public static enum paramID{
        ID, MESSAG_DATA
    }
    private AgentProfileGenerator profGen;
    
    public UserAgentProfile(List agIDLists, AgentProfileGenerator profGen) {
        super(agIDLists);
        this.profGen = profGen;
    }
    
    @Override
    public Map initProfile(List agIDLists) {
        Map map = new HashMap();
        
        for(Object agID : agIDLists){
            Map profParam = new HashMap();
            
            //ID
            profParam.put(paramID.ID, agID);
            
            //MESSAG_DATA
            Map msgdata = new HashMap();
            System.out.println(profGen.generate(agID));
            msgdata.putAll(profGen.generate(agID));
            profParam.put(paramID.MESSAG_DATA, msgdata);
            
            map.put(agID, profParam);
        }
        
        return map;
    }
    
}
