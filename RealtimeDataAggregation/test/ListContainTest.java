
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kaeru
 */
public class ListContainTest {
	public static void main(String[] args) {
		List test = new ArrayList();
		
		Long start = System.currentTimeMillis();
		for(int i=0; i < 1000000; i++)
			test.add(i);
		Long stop = System.currentTimeMillis();
		System.out.println("List Adding time="+(stop-start));
		
		start = System.currentTimeMillis();
		test.contains(test.size() / 2);
		stop = System.currentTimeMillis();
		System.out.println("List Search time="+(stop-start));
	}
}
