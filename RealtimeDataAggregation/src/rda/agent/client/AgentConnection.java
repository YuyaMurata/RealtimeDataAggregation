/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rda.agent.client;

import com.ibm.agent.exa.client.AgentClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 *
 * @author kaeru
 */
public class AgentConnection {
    public enum paramID{
        POOL_SIZE
    }
    
    private ObjectPool<AgentClient> _pool;
    private String[] aghost;
    
    public AgentConnection(Integer poolsize, String[] aghost){
        this.aghost = aghost;
        createObjectPool(poolsize, aghost);
    }
    
    private void createObjectPool(Integer poolsize, String[] aghost){
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxIdle(poolsize);
        conf.setMaxTotal(poolsize);
        
        this._pool = new GenericObjectPool<>(new AgentClientFactory(aghost[0], aghost[1], aghost[2]), conf);
        
        System.out.println("***********************************************************");
        System.out.println("total:"+((GenericObjectPool) _pool).getMaxTotal()
                            +" , minIdle:"+((GenericObjectPool) _pool).getMinIdle()
                            + " , maxIdle:"+((GenericObjectPool) _pool).getMaxIdle());
        System.out.println("***********************************************************");
        
        this.aghost = aghost;
    }
    
    public void setPoolSize(Integer poolsize, String[] aghost){
        createObjectPool(poolsize, aghost);
    }
    
    public AgentClient getClient(){
        AgentClient ag = null;
        
        try {
            ag = _pool.borrowObject();
            
            return ag;
        } catch (Exception ex) {
            System.out.println("Not Connect AgentClient!");
        }
        
        return ag;
    }
    
    public void returnConnection(AgentClient ag){
        if(ag != null)
            try {
                _pool.returnObject(ag);
            } catch (Exception ex) {}
    }
    
    public void close(){
        _pool.close();
    }

    public Integer getActiveObject(){
        return _pool.getNumActive();
    }
    
    public Integer getIdleObject(){
        return _pool.getNumIdle();
    }
    
    public String toString(){
        String str = aghost[0]+" - ";
        str += "ActibeObj : "+getActiveObject()+" ,IdleObj : "+getIdleObject();
        return str;
    }
}
