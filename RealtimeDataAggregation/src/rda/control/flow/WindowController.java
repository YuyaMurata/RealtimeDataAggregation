package rda.control.flow;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WindowController implements Runnable{
    private Map<String, Window> windowMap = new ConcurrentHashMap<>();
    private Queue executableQueue;
    private Long aliveTime;
    
    public WindowController(int limit, Long aliveTime) {
        this.executableQueue = new ConcurrentLinkedQueue<>();
        this.aliveTime = aliveTime;
        
        Window.setParameter(limit, executableQueue);
    }
        
    public void pack(String id, Object data){
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
        return (Window)executableQueue.peek();
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
            
            try {
                Thread.sleep(aliveTime);
            } catch (InterruptedException ex) {
            }
        }
    }   
}