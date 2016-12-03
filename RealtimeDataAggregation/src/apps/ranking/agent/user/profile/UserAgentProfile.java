/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.user.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.profile.AgentProfile;

/**
 *
 * @author kaeru
 */
public class UserAgentProfile extends AgentProfile{
    public static enum paramID{
        ID, MESSAG_DATA
    }
    private Map profMap;
    
    public UserAgentProfile(List agIDLists, Map profMap) {
        super(agIDLists);
        this.profMap = profMap;
    }
    
    @Override
    public Map initProfile(List agIDLists) {
        Map map = new HashMap();
        
        for(String agID : (List<String>)agIDLists){
            Map profParam = new HashMap();
            
            //ID
            profParam.put(paramID.ID, agID);
            
            //MESSAG_DATA
            Map msgdata = new HashMap();
            msgdata.putAll(profMap);
            profParam.put(paramID.MESSAG_DATA, msgdata);
            
            map.put(agID, profParam);
        }
        
        return map;
    }
    
}
