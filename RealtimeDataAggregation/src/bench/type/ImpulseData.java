package bench.type;

public class ImpulseData extends DataType{
    private static final Long impulse = 1000000L;
    private static final Long burst = 10L;

    public ImpulseData(Long time, Long period, Long volume, int numberOfUser, int valueOfUser, int datamode, long seed) {
        super(  "ImpulseType",
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
        if(time == -1L) return toString();
        if((time % burst) == 0) return String.valueOf(impulse);
        else return String.valueOf(0);
    }

    @Override
    public Long dataVolume(Long time, Long volume) {
        return (time + 1) * time * volume / 2 / (time / burst + 1) + 1;
    }

    @Override
    public Long timeToVolume(Long time, Long volume) {
        if(time % burst == 0) return impulse;
        else return 0L;
    }
}