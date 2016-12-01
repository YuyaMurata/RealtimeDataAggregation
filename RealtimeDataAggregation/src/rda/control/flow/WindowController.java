package rda.control.flow;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WindowController extends Thread{
    public enum paramID{
        WINDOW_SIZE, WINDOW_TIME
    }
    
    private Map<Object, Window> windowMap = new ConcurrentHashMap<>();
    private BlockingQueue executableQueue;
    private Long aliveTime;
    
    public WindowController(Map param) {
        this.executableQueue = new LinkedBlockingQueue();
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
        executableQueue.add(window);
        windowMap.remove(window.id);
        //System.out.println("Window Controller Size = "+executableQueue.size());
    }
    
    public void returnExecutable(Window window){
        if(executableQueue.contains(window))
            remove();
        else
            executableQueue.add(window);
        //System.out.println("Window Controller Size = "+executableQueue.size());
    }
    
    public Window get(){
        return (Window)executableQueue.poll();
    }
    
    public void remove(){
        executableQueue.poll();
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
            
            System.out.println("WINDOW_QS = "+executableQueue.size());
            try {
                Thread.sleep(aliveTime);
            } catch (InterruptedException ex) {
            }
        }
    }   
}