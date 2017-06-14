/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.data;

import bench.template.UserData;
import java.util.List;
import org.apache.commons.math3.random.RandomDataGenerator;

/**
 *
 * @author kaeru
 */
public class Data {

	private static int count = -1;
	private static final RandomDataGenerator rand = new RandomDataGenerator();
	private int numOfUser, value, mode;

	public Data() {
	}

	//Set All UserID
	public void init(int n, int value, int mode, long seed) {
		this.numOfUser = n;
		this.value = value;
		this.mode = mode;

		if (seed != -1L) {
			rand.reSeed(seed);
		}
	}

	private List userList;

	public void setUserList(List userList) {
		this.userList = userList;
	}

	private Integer idNo() {
		switch (mode) {
			case 0:
				return idSequentialNo();
			case 1:
				return idRandomNo();
		}
		return null;
	}

	private Integer idSequentialNo() {
		count++;
		if (count == numOfUser) {
			count = 0;
		}
		return count;
	}

	private Integer idRandomNo() {
		return rand.nextInt(0, numOfUser - 1);
	}

	//Get Data userID = Call % NUMBER_USER_AGENTS
	public Object getData() {
		String uid = (String) userList.get(idNo());
		return new UserData(uid, value);
	}
}
