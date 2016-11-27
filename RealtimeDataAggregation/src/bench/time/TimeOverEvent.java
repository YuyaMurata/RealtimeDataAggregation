/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bench.time;

/**
 *
 * @author kaeru
 */
public class TimeOverEvent extends Exception{
    public TimeOverEvent(String name, Long time) {
        super(name+":"+time);
    }
}
