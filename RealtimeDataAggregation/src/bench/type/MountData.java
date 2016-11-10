package bench.type;

public class MountData extends DataType{
    
    public MountData(Long time, Long period, Long volume, int numberOfUser, int valueOfUser, int datamode, long seed) {
        super(  "MountType",
                time, 
                period, 
                volume, 
                numberOfUser, 
                valueOfUser, 
                datamode, 
                seed);     
    }
    
    @Override
    public String toString(){
        return name + " DataN_" + total;
    }

    @Override
    public String toString(Long time) {
        return String.valueOf((time + 1) * volume);
    }

    @Override
    public Long dataVolume(Long time, Long volume) {
        return volume;
    }

    @Override
    public Long timeToVolume(Long time, Long volume) {
        return (time + 1) * volume;
    }
}