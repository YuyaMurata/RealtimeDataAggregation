/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.appuser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.random.RandomDataGenerator;
import rda.agent.profile.AgentProfile;

/**
 *
 * @author kaeru
 */
public class UserProfile extends AgentProfile{
    public enum paramID{
        USER_SEED, USER_MODE
    }
    public enum profileID{
        ID, NAME, AGE, SEX, ADDRESS
    }

    public UserProfile(List userList) {
        super(userList);
    }
    
    private static final RandomDataGenerator rand = new RandomDataGenerator();
    private static Integer userMode = 0;
    public static void setParameter(Integer mode, Long seed){
        if(seed != -1L) rand.reSeed(seed);
        userMode = mode;
    }
    
    @Override
    public Map initProfile(List userList) {
        Map profMap = new HashMap();
        for(String userID : (List<String>)userList){
            Map map = new HashMap();
            
            map.put(profileID.ID, userID);
            map.put(profileID.NAME, "NAME-"+userID);
            map.put(profileID.AGE, getAge(userMode));
            map.put(profileID.SEX, getSex(userMode));
            map.put(profileID.ADDRESS, "CITY-"+userID);
            
            profMap.put(userID, map);
        }
        
        return profMap;
    }
    
    private Integer getAge(Integer mode){
        if(mode == 0)
            return rand.nextInt(0, 100);
        else{
            int age = (int)rand.nextGaussian(50, 10D);
            if((age > 100) || (age < 0)) 
                age = rand.nextInt(0, 100);
            return age;
        }
    }
    
    private Integer getSex(Integer mode){
        return rand.nextInt(0, 1);
    }
}
