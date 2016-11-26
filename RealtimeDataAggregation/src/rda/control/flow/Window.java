/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.control.flow;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author kaeru
 */
public class Window {
    public Object id;
    private List win = new CopyOnWriteArrayList();

    public Window(Object id) {
        this.id = id;
    }
    
    private static Integer size;
    private static Queue queue;
    public static void setParameter(Integer wsize, Queue wqueue){
        size = wsize;
        queue = wqueue;
    }
  
    public void pack(Object obj){
        win.add(obj);
        
        if(win.size() >= size) queue.add(this);
    }
    
    public List unpack(){
        return this.win;
    }
    
    public Integer getSize(){
        return this.win.size();
    }
}