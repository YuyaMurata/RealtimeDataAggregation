/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.test;

import bench.main.AgentBenchmark;
import bench.template.UserData;
import bench.time.TimeOverEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public class BenchmarkTest {

    public static void main(String[] args) {
        AgentBenchmark ag = new AgentBenchmark();
        ag.setDummyParameter();

        Map datalog = new HashMap();
        Long totalData = 0L;
        Long start = System.currentTimeMillis();
        try {
            while (true) {
                UserData user = ag.bench();
                if(user== null) continue;
                
                if (datalog.get(user.id) == null) {
                    datalog.put(user.id, 0);
                }
                datalog.put(user.id, (int) datalog.get(user.id) + (int) user.data);
                
                totalData++;
            }
        } catch (TimeOverEvent ex) {
            //ex.printStackTrace();
        }
        Long stop = System.currentTimeMillis();
        
        System.out.println("Results {");
        for(Object id : datalog.keySet())
            System.out.println("\t "+id+", "+datalog.get(id));
        System.out.println("} Total="+totalData+"-"+ag.getTotalGenerate()+" Time[s]:"+(stop-start));
    }
}
