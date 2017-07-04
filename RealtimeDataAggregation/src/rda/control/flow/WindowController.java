package rda.control.flow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WindowController extends Thread {

	public enum paramID {
		WINDOW_SIZE, WINDOW_TIME
	}

	private Map<Object, Window> windowMap = new HashMap<>();
	private BlockingQueue executableQueue;
	private Long aliveTime;

	public WindowController(Map param) {
		this.executableQueue = new LinkedBlockingQueue();
		this.aliveTime = (Long) param.get(paramID.WINDOW_TIME);
		Window.setParameter((Integer) param.get(paramID.WINDOW_SIZE));
	}

	public synchronized void pack(Object id, Object data) {
		if(windowMap.get(id) != null){
			windowMap.get(id).pack(data);
		}else{
			windowMap.put(id, new Window(this, id));
			windowMap.get(id).pack(data);
		}
	}

	public synchronized void addExecutable(Window window) {
		executableQueue.offer(window);
		windowMap.remove(window.id);
		System.out.println(toString());
		//System.out.println("Window Controller Size = "+executableQueue.size());
	}

	public synchronized Window get() {
		return (Window) executableQueue.poll();
	}

	private static Boolean runnable;

	public static void setRunnable(Boolean state) {
		runnable = state;
	}

	@Override
	public void run() {
		while (runnable) {
			for (Window window : windowMap.values()) {
				addExecutable(window);
			}

			try {
				Thread.sleep(aliveTime);
			} catch (InterruptedException ex) {
			}
		}
	}
	
	public String toString(){
		return "WindowContoroller = "+executableQueue.size();
	}
}
