/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.flow;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author kaeru
 */
public class Window {

	public Object id;
	private List win;

	public Window(Object id) {
		this.id = id;
		this.win = new CopyOnWriteArrayList();
	}

	private static Integer size;
	private static WindowController winctrl;

	public static void setParameter(Integer wsize, WindowController ctrl) {
		size = wsize;
		winctrl = ctrl;
	}

	public void pack(Object obj) {
		win.add(obj);

		if (win.size() >= size) {
			winctrl.addExecutable(this);
		}
	}

	public List unpack() {
		return this.win;
	}

	public Integer getSize() {
		return this.win.size();
	}
}
