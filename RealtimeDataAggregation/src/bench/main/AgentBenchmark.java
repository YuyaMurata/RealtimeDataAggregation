/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.main;

import bench.generator.DataGenerator;
import bench.template.UserData;
import bench.type.DataType;
import bench.type.FlatData;
import bench.type.ImpulseData;
import bench.type.MountData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public class AgentBenchmark {
    public static enum paramID {
        TIME_RUN, TIME_PERIOD, DATA_VOLUME, 
        AMOUNT_USER, AGENT_DEFAULT_VALUE, DATA_MODE, 
        DATA_SEED
    }
    
    private Map param = new HashMap();
    public void setParameter(Map param){
        this.param = param;
    }
    
    private int select = 0;
    public void setDataType(int select){
        this.select = select;
    }
    
    private DataType setTypeParameter(int select){
        switch(select){
            case 0: return new FlatData(
                        (Long)      param.get(paramID.TIME_RUN), 
                        (Long)      param.get(paramID.TIME_PERIOD), 
                        (Long)      param.get(paramID.DATA_VOLUME), 
                        (Integer)   param.get(paramID.AMOUNT_USER), 
                        (Integer)   param.get(paramID.AGENT_DEFAULT_VALUE),
                        (Integer)   param.get(paramID.DATA_MODE),
                        (Long)      param.get(paramID.DATA_SEED)
                );
            case 1: return new MountData(
                        (Long)      param.get(paramID.TIME_RUN), 
                        (Long)      param.get(paramID.TIME_PERIOD), 
                        (Long)      param.get(paramID.DATA_VOLUME), 
                        (Integer)   param.get(paramID.AMOUNT_USER), 
                        (Integer)   param.get(paramID.AGENT_DEFAULT_VALUE),
                        (Integer)   param.get(paramID.DATA_MODE),
                        (Long)      param.get(paramID.DATA_SEED)
                );
            case 2: return new ImpulseData(
                        (Long)      param.get(paramID.TIME_RUN), 
                        (Long)      param.get(paramID.TIME_PERIOD), 
                        (Long)      param.get(paramID.DATA_VOLUME), 
                        (Integer)   param.get(paramID.AMOUNT_USER), 
                        (Integer)   param.get(paramID.AGENT_DEFAULT_VALUE),
                        (Integer)   param.get(paramID.DATA_MODE),
                        (Long)      param.get(paramID.DATA_SEED)
                );
        }
        
        return null;
    }
    
    private DataGenerator setDataType(List userList){
        DataType type = setTypeParameter(select);
        DataGenerator datagen = new DataGenerator(type);
        
        type.setUserLists(userList);
        
        return datagen;
    }
    
    public void bench(List userList) {
        UserData user;
        DataGenerator dgen = setDataType(userList);
    }
    
    //Dummy Parameter
    public void dummyBench() {
        long numRun = 60;
        
        //DummyUser Settings
        List userList = new ArrayList();
        for(int i=0; i < 10; i++) userList.add("TEST#00"+i);
        
        //Dummy Parameter
        Map param = new HashMap();
        param.put(paramID.TIME_RUN, numRun);
        param.put(paramID.TIME_PERIOD, 1000L);
        param.put(paramID.DATA_VOLUME, 1000L);
        param.put(paramID.AMOUNT_USER, 10);
        param.put(paramID.AGENT_DEFAULT_VALUE, 1);
        param.put(paramID.DATA_MODE, 1);
        param.put(paramID.DATA_SEED, Long.MAX_VALUE);
        
        UserData user;
        setParameter(param);
        setDataType(2);
        DataGenerator dgen = setDataType(userList);
        
        Map datalog = new HashMap();
        Long test = 0L;
        for(long t=0; t < numRun; t++){
            while((user = dgen.generate(t)) != null){
                if(datalog.get(user.id) == null) datalog.put(user.id, 1);
                datalog.put(user.id, (int)datalog.get(user.id) + (int)user.data);
            }
            
            //Check DataLog
            System.out.print("t-"+t+" ");
            for(Object id : datalog.keySet())
                System.out.print(id +","+datalog.get(id)+" / ");
            test += Long.valueOf(dgen.toString(t));
            System.out.println(test);
        }
        
        //DataLog Results
        System.out.println("\n Results :");
        for(Object id : datalog.keySet())
            System.out.println(id +","+datalog.get(id));
        System.out.println(dgen.toString());
    }
}
