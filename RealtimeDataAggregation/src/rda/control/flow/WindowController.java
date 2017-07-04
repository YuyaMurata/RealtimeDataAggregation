package rda.control.flow;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WindowController extends Thread {

	public enum paramID {
		WINDOW_SIZE, WINDOW_TIME
	}

	private Map<Object, Window> windowMap = new ConcurrentHashMap<>();
	private BlockingQueue executableQueue;
	private Long aliveTime, packTime, createWinTime, addQTime;

	public WindowController(Map param) {
		this.executableQueue = new LinkedBlockingQueue();
		this.aliveTime = (Long) param.get(paramID.WINDOW_TIME);
		Window.setParameter((Integer) param.get(paramID.WINDOW_SIZE));
		
		this.packTime = 0L;
		this.createWinTime = 0L;
		this.addQTime = 0L;
	}

	public synchronized void pack(Object id, Object data) {
		if(windowMap.get(id) != null){
			Long st = System.currentTimeMillis();
			
			windowMap.get(id).pack(data);
			
			packTime += System.currentTimeMillis() - st;
		}else{
			Long st = System.currentTimeMillis();
			
			windowMap.put(id, new Window(this, id));
			windowMap.get(id).pack(data);
			
			createWinTime += System.currentTimeMillis() - st;
		}
	}

	public synchronized void addExecutable(Window window) {
		Long st = System.currentTimeMillis();
		
		executableQueue.offer(window);
		windowMap.remove(window.id);
		
		addQTime += System.currentTimeMillis() - st;
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
		return "WindowController Time = "+packTime+", "+createWinTime+" ,"+addQTime;
	}
}
