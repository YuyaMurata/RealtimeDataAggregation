package rda.control.flow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WindowController extends Thread{
    public enum paramID{
        WINDOW_SIZE, WINDOW_TIME
    }
    
    private Map<Object, Window> windowMap = new HashMap<>();
    private ConcurrentLinkedQueue executableQueue;
    private Long aliveTime;
    
    public WindowController(Map param) {
        this.executableQueue = new ConcurrentLinkedQueue();
        this.aliveTime = (Long) param.get(paramID.WINDOW_TIME);
        
        Window.setParameter((Integer) param.get(paramID.WINDOW_SIZE), executableQueue);
    }
        
    public void pack(Object id, Object data){
        try{
            windowMap.get(id).pack(data);
        }catch(NullPointerException e){
            Window window = new Window(id);
            window.pack(data);
            windowMap.put(id, window);
        }  
    }
    
    public Collection getWindows(){
        return windowMap.values();
    }
    
    public void addExecutable(Window window){
        executableQueue.offer(window);
        windowMap.remove(window.id);
        //System.out.println("Window Controller Size = "+executableQueue.size());
    }
    
    public Window get(){
        return (Window)executableQueue.poll();
    }

    public boolean check(String id){
        return windowMap.get(id) != null;
    }
    
    private static Boolean runnable;
    public static void setRunnable(Boolean state){
        runnable = state;
    }
    
    @Override
    public void run() {
        while(runnable){
            for(Window window : windowMap.values())
                addExecutable(window);
            
            try {
                Thread.sleep(aliveTime);
            } catch (InterruptedException ex) {
            }
        }
    }   
}