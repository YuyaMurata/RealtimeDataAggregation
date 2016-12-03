/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.rank.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rda.agent.profile.AgentProfile;

/**
 *
 * @author kaeru
 */
public class RankAgentProfile extends AgentProfile{
    public static enum paramID{
        ID, MESSAG_DATA
    }
    
    public RankAgentProfile(List agIDLists) {
        super(agIDLists);
    }
    
    @Override
    public Map initProfile(List agIDLists) {
        Map map = new HashMap();
        
        for(String agID : (List<String>)agIDLists){
            Map profParam = new HashMap();
            
            //ID
            profParam.put(paramID.ID, agID);
            
            //MESSAG_DATA
            List msgdata = new ArrayList();
            msgdata.add("Aggregate Conditios :" + agID);
            profParam.put(paramID.MESSAG_DATA, msgdata);
            
            map.put(agID, profParam);
        }
        
        return map;
    }
    
}
