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
import java.util.List;
import java.util.Map;

/**
 *
 * @author kaeru
 */
public class BenchmarkTest {
	
	//agSetbenchを変更したため動かない
	public static void main(String[] args) {
		AgentBenchmark ag = AgentBenchmark.getInstance();
		ag.setDummyParameter();
		//ag.setBenchList(1000);
		
		Map datalog = new HashMap();
		Long totalData = 0L;
		Long start = System.currentTimeMillis();
		Long best = 0L;
		try {
			while (true) {
				Long bstart = System.currentTimeMillis();
				//UserData user = ag.bench();
				Map<Object, List> user = ag.benchSet();
				best += System.currentTimeMillis() - bstart;
				if (user == null) {
					continue;
				}
				
				/*if (datalog.get(user.id) == null) {
					datalog.put(user.id, 0);
				}
				datalog.put(user.id, (int) datalog.get(user.id) + (int) user.data);*/

				//totalData++;
				for(Object key : user.keySet())
					totalData += user.get(key).size();
			}
		} catch (TimeOverEvent ex) {
			//ex.printStackTrace();
		}
		Long stop = System.currentTimeMillis();

		Integer idTotal = 0;
		System.out.println("Results {");
		for (Object id : datalog.keySet()) {
			//System.out.println("\t " + id + ", " + datalog.get(id));
			idTotal++;
		}
		System.out.println("} Total=" + totalData + "-" + ag.getTotalGenerate() + " ID=" + idTotal + " Time[ms]:" + (stop - start));
		System.out.println("Bench = "+best+"ms");
	}
}
