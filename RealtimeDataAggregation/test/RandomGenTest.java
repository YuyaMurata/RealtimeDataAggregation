
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.random.RandomDataGenerator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kaeru
 */
public class RandomGenTest {
	public static void main(String[] args) {
		RandomDataGenerator rand = new RandomDataGenerator();
		
		Map<Object, Integer> map = new HashMap();
		for(int i=0; i < 101; i++)
			map.put(i, 0);
		
		Map<Object, Integer> user = new HashMap();
		for(int i=0; i < 100; i++){
			int age = rand.nextInt(0, 100);
			/*int age = (int) rand.nextGaussian(50, 10D);
			if ((age > 100) || (age < 0)) {
				age = rand.nextInt(0, 100);
			}*/
			user.put(i, age);
		}
		
		for(int i=0; i < 100000; i++){
			//int age = rand.nextInt(0, 100);
			/*int age = (int) rand.nextGaussian(50, 10D);
			if ((age > 100) || (age < 0)) {
				age = rand.nextInt(0, 100);
			}*/
			
			//Seq
			//map.put(user.get(i%100), map.get(user.get(i%100))+1);
			
			//Rand
			int j = rand.nextInt(0,99);
			map.put(user.get(j), map.get(user.get(j))+1);
		}
		
		display(map);
	}
	
	//Display
	public static void display(Map<Object, Integer> map){
		for(Object age : map.keySet()){
			System.out.print(age+","+map.get(age)+" ");
			int l = map.get(age) / 50 ;
			for(int i=0; i < l; i++)
				System.out.print("*");
				
			System.out.println("");
		}
	}
}
