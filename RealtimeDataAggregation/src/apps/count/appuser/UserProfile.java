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
public class UserProfile extends AgentProfile {
	public enum paramID {
		USER_SEED, USER_MODE
	}

	public enum profileID {
		ID, NAME, AGE, SEX, ADDRESS
	}

	public UserProfile(List userList) {
		super(userList);
	}

	private static final RandomDataGenerator rand = new RandomDataGenerator();
	private static Integer userMode;

	public static void setParameter(Map appProp) {
		long seed = (Long) appProp.get(paramID.USER_SEED);
		userMode = (Integer) appProp.get(paramID.USER_MODE);
		
		if (seed != -1L) {
			rand.reSeed(seed);
		}
	}

	@Override
	public Map initProfile(List userList) {
		Map profMap = new HashMap();
		for (String userID : (List<String>) userList) {
			Map map = new HashMap();

			map.put(profileID.ID, userID);
			map.put(profileID.NAME, "NAME-" + userID);
			int age = getAge();
			map.put(profileID.AGE, age);
			map.put(profileID.SEX, getSex());
			map.put(profileID.ADDRESS, "CITY-" + userID);
			
			ageMap.put(userID, age);
			
			profMap.put(userID, map);
		}

		return profMap;
	}

	private Integer getAge() {
		if (userMode == 0) {
			return rand.nextInt(0, 100);
		} else {
			int age = (int) rand.nextGaussian(50, 10D);
			if ((age > 100) || (age < 0)) {
				age = rand.nextInt(0, 100);
			}
			return age;
		}
	}

	private Integer getSex() {
		return rand.nextInt(0, 1);
	}
	
	private Map ageMap = new HashMap();
	@Override
	public Object getAttribute(Object agID) {
		return ageMap.get(agID);
	}
}
