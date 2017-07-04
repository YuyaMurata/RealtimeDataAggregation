/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.time;

import bench.generator.DataGenerator;
import bench.template.UserData;

/**
 *
 * @author kaeru
 */
public class BenchTiming {
	public static Boolean timerFlg;
	public static Long time, period, term;
	private static Long timer;
	public static void start(DataGenerator datagen){
		if (timerFlg) {
			timer = System.currentTimeMillis();
			System.out.println("DataGen="+datagen.getTimeVolme());
			timerFlg = false;
		}
	}
	
	public static void stop(Object user) throws TimeOverEvent{
		if (user == null) {
			Long sleepTime = System.currentTimeMillis() - timer;
			if (period > sleepTime) {
				try {
					Thread.sleep(period - sleepTime);
				} catch (InterruptedException ex) {
				}
			}

			//Test
			sleepTime = System.currentTimeMillis() - timer;
			System.out.println("Time[s] = " + time + " TimePeriod[ms] : " + sleepTime);

			timerFlg = true;
			time++;
			

			if (term < time) 
				throw new TimeOverEvent("AgentBenchmark", time);
		}
	}
	
	//Only List
	public static void start(DataGenerator datagen, Long datasize){
		if (timerFlg) {
			timer = System.currentTimeMillis();
			System.out.println("DataGen="+datagen.getTimeVolme());
			datagen.subTimeVolme(datasize);
			timerFlg = false;
		}
	}
}
