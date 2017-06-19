/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.template;

import java.io.Serializable;

/**
 *
 * @author kaeru
 */
public class UserData implements Serializable {

	public String id;
	public long data;
	public long time;

	public UserData() {
	}

	public UserData(String id, long data) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id = id;
		this.data = data;
		this.time = System.currentTimeMillis();
	}
	
	public String getID(){
		return id;
	}
	
	public Long getData(){
		return data;
	}

	public String toString() {
		return "UserID = " + id + " Data = " + data + " Time = " + time;
	}
}
