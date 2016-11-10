/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.type;

import bench.data.Data;
import bench.template.UserData;
import java.util.List;

/**
 *
 * @author kaeru
 */
public abstract class DataType{
    public String name;
    private Long count;
    public Long total;
    private Data data;
    public Long term, period, volume;
    
    public DataType(String type, Long time, Long period, Long volume, int numberOfUser, int value, int datamode, long seed) {
        this.name = type;
        this.data = new Data();
        this.term = time;
        this.period = period;
        
        this.volume = dataVolume(time, volume);
        
        //initialise
        data.init(numberOfUser, value, datamode, seed);
        count = -1L;
        total = 0L;
    }
    
    public void setUserLists(List userList){
        data.setUserList(userList);
    }
    
    public abstract Long dataVolume(Long time, Long volume);
    
    public abstract Long timeToVolume(Long time, Long volume);
    
    public UserData nextData(Long time) {
        count++;
        UserData d = (UserData) data.getData();
        
        if(count >= timeToVolume(time, volume)) {
            d = null;
            total += count;
            count = -1L;
        }
        
        return d;
    }
    
    public String getName(){
        return name;
    }
    
    @Override
    public abstract String toString();
    
    public abstract String toString(Long time);
}