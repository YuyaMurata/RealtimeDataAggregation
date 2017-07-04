/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.flow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kaeru
 */
public class Window {
	public Object id;
	private List win;
	private WindowController winctrl;
	public Window(WindowController ctrl, Object id) {
		this.winctrl = ctrl;
		this.id = id;
		this.win = new ArrayList();
	}

	private static Integer size;
	public static void setParameter(Integer wsize) {
		size = wsize;
	}
	
	//1 Data Input
	public void pack(Object obj) {
		win.add(obj);

		if (win.size() >= size) {
			winctrl.addExecutable(this);
		}
	}
	
	//List Input
	public void pack(List data){
		System.out.println("Winodw="+data.size());
		win.addAll(data);
		winctrl.addExecutable(this);
	}

	public List unpack() {
		return this.win;
	}

	public Integer getSize() {
		return this.win.size();
	}
	
	public static Integer getMaxSize(){
		return size;
	}
}
