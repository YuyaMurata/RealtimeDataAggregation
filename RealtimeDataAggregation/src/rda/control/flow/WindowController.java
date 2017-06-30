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
	private Long aliveTime;

	public WindowController(Map param) {
		this.executableQueue = new LinkedBlockingQueue();
		this.aliveTime = (Long) param.get(paramID.WINDOW_TIME);
		Window.setParameter((Integer) param.get(paramID.WINDOW_SIZE));
	}

	public synchronized void pack(Object id, Object data) {
		try {
			windowMap.get(id).pack(data);
		} catch (NullPointerException e) {
			Window window = new Window(this, id);
			window.pack(data);
			windowMap.put(id, window);
		}
	}

	public synchronized void addExecutable(Window window) {
		executableQueue.offer(window);
		windowMap.remove(window.id);
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
		long total = 0L;
		while (runnable) {
			for (Object id : windowMap.keySet()) {
				addExecutable(windowMap.get(id));
			}

			try {
				Thread.sleep(aliveTime);
			} catch (InterruptedException ex) {
			}
		}
		System.out.println("Flow-"+total);
	}
}
