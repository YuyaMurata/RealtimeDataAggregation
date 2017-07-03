/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kaeru
 */
public class TimeTest {
	public static void main(String[] args) {
		Long start = System.currentTimeMillis();
		Long tt = 0L;
		
		for(int i=0; i < 10000000; i++){
			Long s = System.currentTimeMillis();
			tt += System.currentTimeMillis() - s;
		}
		
		Long stop = System.currentTimeMillis();
		System.out.println("Time = "+(stop-start)+" Loop ="+tt);
	}
}
