/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.mq;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import rda.agent.updator.AgentUpdator;
import rda.extension.agent.exec.AgentSystemInitializer;

/**
 *
 * @author kaeru
 */
public class AgentMessageQueue implements Runnable{
    public static enum paramID{
        QUEUE_LENGTH, QUEUE_WAIT, AGENT_WAIT
    }
    
    private String agID;
    private BlockingQueue<Object> queue;
    private AgentUpdator agent;
    public AgentMessageQueue(String agID, AgentUpdator updator) {
        this.agID = agID;
        this.queue = new LinkedBlockingDeque<>(size+1);
        this.agent = updator;
    }
    
    private static Integer size;
    private static Long putwait, getwait;
    public static void setParameter(Map param){
        size = (Integer) param.get(paramID.QUEUE_LENGTH);
        putwait = (Long) param.get(paramID.QUEUE_WAIT);
        getwait = (Long) param.get(paramID.AGENT_WAIT);
    }
    
    public static String getParameter(){
        return "MQParameter wait = "+putwait+","+getwait+" length = "+size;
    }
    
    public static Boolean runnable = true;
    
    public Boolean put(Object obj){
        boolean success = false;
        try {
            success = queue.offer(obj, putwait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
        }
        
        return success;
    }
    
    public Object get(){
        Object obj = null;
        try {
            obj = queue.poll(getwait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
        }
        
        return obj;
    }
    
    @Override
    public void run() {
        System.out.println("AgentID-"+agID+" Start Running!");
        
        while(runnable){
            Object obj = get();
            
            if(obj != null) agent.update(agID, (List) obj);
        }
        
        System.out.println("AgentID-"+agID+" Stop Running!");
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(agID);
        sb.append(" : ");
        sb.append("MQLength = ");
        sb.append(queue.size());
        return sb.toString();
    }
}
