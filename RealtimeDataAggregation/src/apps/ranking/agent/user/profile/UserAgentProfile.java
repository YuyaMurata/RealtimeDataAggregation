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
    
    public UserAgentProfile(List agIDLists, AgentProfileGenerator profGen) {
        super(agIDLists);
        setUserProfile(agIDLists, profGen);
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
            //msgdata.putAll(profGen.generate(agID)); <- setUserProfileで設定
            profParam.put(paramID.MESSAG_DATA, msgdata);
            
            map.put(agID, profParam);
        }
        
        return map;
    }
    
    private void setUserProfile(List userIDLists, AgentProfileGenerator profGen){
        for(Object agID : userIDLists){
            Map idProfMap = (Map) profile.get(agID).get(paramID.MESSAG_DATA);
            idProfMap.putAll(profGen.generate(agID));
        }
    }
}
