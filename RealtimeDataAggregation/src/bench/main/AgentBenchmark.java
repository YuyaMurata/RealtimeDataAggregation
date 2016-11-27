/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.main;

import bench.generator.DataGenerator;
import bench.template.UserData;
import bench.time.TimeOverEvent;
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
        DATA_SEED, TYPE_SELECT, ID_RULE
    }

    private Map param = new HashMap();

    public void setParameter(Map param) {
        this.param = param;
        
        this.term = (Long) param.get(paramID.TIME_RUN);
        this.period = (Long) param.get(paramID.TIME_PERIOD);
        
        preparedBenchMark((Integer)param.get(paramID.AMOUNT_USER), 
                            (String)param.get(paramID.ID_RULE), 
                            (Integer)param.get(paramID.TYPE_SELECT));
    }
    
    private List userLists;
    private DataGenerator datagen;
    private void preparedBenchMark(Integer n, String rule, Integer select){
        userLists = new ArrayList();
        for(int i=0; i < n; i++)
            userLists.add(rule+i);
        
        DataType type = setTypeParameter(select);
        type.setUserLists(userLists);
        
        timerFlg = true;
        time = 1L;
        datagen = new DataGenerator(type);
    }

    private DataType setTypeParameter(int select) {
        switch (select) {
            case 0:
                return new FlatData(
                        (Long) param.get(paramID.TIME_RUN),
                        (Long) param.get(paramID.TIME_PERIOD),
                        (Long) param.get(paramID.DATA_VOLUME),
                        (Integer) param.get(paramID.AMOUNT_USER),
                        (Integer) param.get(paramID.AGENT_DEFAULT_VALUE),
                        (Integer) param.get(paramID.DATA_MODE),
                        (Long) param.get(paramID.DATA_SEED)
                );
            case 1:
                return new MountData(
                        (Long) param.get(paramID.TIME_RUN),
                        (Long) param.get(paramID.TIME_PERIOD),
                        (Long) param.get(paramID.DATA_VOLUME),
                        (Integer) param.get(paramID.AMOUNT_USER),
                        (Integer) param.get(paramID.AGENT_DEFAULT_VALUE),
                        (Integer) param.get(paramID.DATA_MODE),
                        (Long) param.get(paramID.DATA_SEED)
                );
            case 2:
                return new ImpulseData(
                        (Long) param.get(paramID.TIME_RUN),
                        (Long) param.get(paramID.TIME_PERIOD),
                        (Long) param.get(paramID.DATA_VOLUME),
                        (Integer) param.get(paramID.AMOUNT_USER),
                        (Integer) param.get(paramID.AGENT_DEFAULT_VALUE),
                        (Integer) param.get(paramID.DATA_MODE),
                        (Long) param.get(paramID.DATA_SEED)
                );
        }

        return null;
    }

    //BenchMark Main
    private Long watch, term, period, time;
    private Boolean timerFlg;
    public UserData bench() throws TimeOverEvent {
        if(timerFlg) {
            watch = System.currentTimeMillis();
            timerFlg = false;
        }
        
        UserData user = datagen.generate(time);
        if(user == null){
            Long sleepTime = System.currentTimeMillis() - watch;
            if(period > sleepTime){
                try {
                    Thread.sleep(period - sleepTime);
                } catch (InterruptedException ex) {
                }
            }
            
            //Test
            sleepTime = System.currentTimeMillis() - watch;
            System.out.println("Term[s] = "+term+" TimePeriod[ms] : "+sleepTime);
            
            timerFlg = true;
            time++;
            
            if(term < time)
                throw new TimeOverEvent("AgentBenchmark", term);
        }
        
        return user;
    }

    //Dummy Parameter
    public void setDummyParameter() {
        //Dummy Parameter
        Map dparam = new HashMap();
        dparam.put(paramID.TIME_RUN, 60L);
        dparam.put(paramID.TIME_PERIOD, 1000L);
        dparam.put(paramID.DATA_VOLUME, 1000L);
        dparam.put(paramID.AMOUNT_USER, 10);
        dparam.put(paramID.AGENT_DEFAULT_VALUE, 1);
        dparam.put(paramID.DATA_MODE, 1);
        dparam.put(paramID.DATA_SEED, Long.MAX_VALUE);
        dparam.put(paramID.ID_RULE, "TEST#00");
        dparam.put(paramID.TYPE_SELECT, 0);

        setParameter(dparam);

        /*UserData user;
        Map datalog = new HashMap();
        Long test = 0L;
        for (long t = 0; t < numRun; t++) {
            while ((user = datagen.generate(t)) != null) {
                if (datalog.get(user.id) == null) {
                    datalog.put(user.id, 1);
                }
                datalog.put(user.id, (int) datalog.get(user.id) + (int) user.data);
            }

            //Check DataLog
            System.out.print("t-" + t + " ");
            for (Object id : datalog.keySet()) {
                System.out.print(id + "," + datalog.get(id) + " / ");
            }
            test += Long.valueOf(datagen.toString(t));
            System.out.println(test);
        }

        //DataLog Results
        System.out.println("\n Results :");
        for (Object id : datalog.keySet()) {
            System.out.println(id + "," + datalog.get(id));
        }
        System.out.println(datagen.toString());*/
    }
}
