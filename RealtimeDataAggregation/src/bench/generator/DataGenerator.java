package bench.generator;

import bench.template.UserData;
import bench.type.DataType;

public class DataGenerator {

	private DataType type;
	
	public DataGenerator(DataType type) {
		this.type = type;
	}

	public UserData generate(Long time) {
		return type.nextData(time);
	}

	public String getName() {
		return type.getName();
	}

	@Override
	public String toString() {
		return type.toString();
	}

	public String toString(Long time) {
		return type.toString(time);
	}

	public Long getAmountData() {
		return type.total;
	}
	
	public Long getTimeVolme(){
		return type.getVolume();
	}
}
