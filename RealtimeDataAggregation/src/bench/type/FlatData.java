/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.type;

/**
 *
 * @author kaeru
 */
public class FlatData extends DataType{
    public FlatData(Long time, Long period, Long volume, int numberOfUser, int valueOfUser, int datamode, long seed) {
        super(  "FlatType",
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
        return String.valueOf(volume);
    }

    @Override
    public Long dataVolume(Long time, Long volume) {
        return time * volume / 2;
    }

    @Override
    public Long timeToVolume(Long time, Long volume) {
        return volume;
    }
}
