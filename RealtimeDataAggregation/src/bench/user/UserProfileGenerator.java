/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.user;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.random.RandomDataGenerator;

/**
 *
 * @author kaeru
 */
public class UserProfileGenerator {
    public enum profileID{
        ID, NAME, AGE, SEX, ADDRESS
    }
    
    private static UserProfileGenerator generator = new UserProfileGenerator();
    public UserProfileGenerator(){
    }
    
    public static UserProfileGenerator getInstance(){
        return generator;
    }
    
    public Map getProfile(String userID){
        return (Map) profile.get(userID);
    }
    
    private static Map profile;
    public static void setParameter(Integer num, Integer mode, Long seed){
        if(seed != -1L) rand.reSeed(seed);
        
        profile = generate(num, mode);
    }
    
    private static final RandomDataGenerator rand = new RandomDataGenerator();
    private static Map generate(Integer n, Integer mode){
        Map profMap = new HashMap();
        for(int i=0; i < n; i++){
            Map map = new HashMap();
            
            String userID = "U#00"+i;
            map.put(profileID.ID, userID);
            map.put(profileID.NAME, "NAME-"+userID);
            map.put(profileID.AGE, getAge(mode));
            map.put(profileID.SEX, getSex(mode));
            map.put(profileID.ADDRESS, "CITY-"+userID);
            
            profMap.put("U#00"+i, map);
        }
        
        return profMap;
    }
    
    private static Integer getAge(Integer mode){
        if(mode == 0)
            return rand.nextInt(0, 100);
        else{
            int age = (int)rand.nextGaussian(50, 10D);
            if((age > 100) || (age < 0)) 
                age = rand.nextInt(0, 100);
            return age;
        }
    }
    
    private static Integer getSex(Integer mode){
        return rand.nextInt(0, 1);
    }
}
