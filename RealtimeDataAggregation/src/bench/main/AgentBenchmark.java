/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.main;

import bench.generator.DataGenerator;
import bench.template.UserData;
import bench.time.BenchTiming;
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

	private static AgentBenchmark agBench = new AgentBenchmark();

	private AgentBenchmark() {
	}

	public static AgentBenchmark getInstance() {
		return agBench;
	}

	public void setParameter(Map param) {
		this.param = param;

		this.term = (Long) param.get(paramID.TIME_RUN);
		this.period = (Long) param.get(paramID.TIME_PERIOD);

		preparedBenchmark((Integer) param.get(paramID.AMOUNT_USER),
				(String) param.get(paramID.ID_RULE),
				(Integer) param.get(paramID.TYPE_SELECT));
	}

	private List userLists;
	private DataGenerator datagen;

	private void preparedBenchmark(Integer n, String rule, Integer select) {
		userLists = new ArrayList();
		for (int i = 0; i < n; i++) {
			userLists.add(rule + i);
		}

		DataType type = setTypeParameter(select);
		type.setUserLists(userLists);

		BenchTiming.timerFlg = true;
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
	private Long term, period, time;

	public UserData bench() throws TimeOverEvent {
		BenchTiming.start(datagen);

		UserData user = datagen.generate(time);
		
		BenchTiming.stop(user, period, term, time);

		return user;
	}

	//Dummy Parameter
	public void setDummyParameter() {
		//Dummy Parameter
		Map dparam = new HashMap();
		dparam.put(paramID.TIME_RUN, 5L);
		dparam.put(paramID.TIME_PERIOD, 0L);
		dparam.put(paramID.DATA_VOLUME, 100000L);
		dparam.put(paramID.AMOUNT_USER, 1000);
		dparam.put(paramID.AGENT_DEFAULT_VALUE, 1);
		dparam.put(paramID.DATA_MODE, 0);
		dparam.put(paramID.DATA_SEED, Long.MAX_VALUE);
		dparam.put(paramID.ID_RULE, "TEST#00");
		dparam.put(paramID.TYPE_SELECT, 0);

		setParameter(dparam);
	}

	public List getUserList() {
		if (userLists.isEmpty()) {
			System.out.println("Do not set parameters!");
		}
		return userLists;
	}

	public Long getTotalGenerate() {
		return datagen.getAmountData();
	}
}
