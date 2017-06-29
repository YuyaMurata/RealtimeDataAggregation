
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kaeru
 */
public class MapListTest {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList();
		for(int i=0; i < 100; i++){
			list.add(i % 10);
		}
		
		long start = System.currentTimeMillis();
		Map<Object, Integer> group = new TreeMap();
		for(Integer n : list){
			if(group.get(n) == null)
				group.put(n, 0);
			
			group.put(n, group.get(n)+1);
		}
		for(Object key :group.keySet())
			System.out.println("key:"+key+" "+group.get(key));
		long stop = System.currentTimeMillis();
		System.out.println(" time="+(stop-start));
		
		System.out.println("Stream!");
		
		long start2 = System.currentTimeMillis();
		Map<Object, List<Integer>> group2 = list.stream().collect(Collectors.groupingBy(key -> key));
		Map<Object, Long> group3 = group2.keySet().stream()
									.collect(Collectors.toMap(
										u -> u, 
										u -> group2.get(u).stream().count()
									));
		
		group3.entrySet().stream().forEach(System.out::println);
		
		long stop2 = System.currentTimeMillis();
		System.out.println(" time="+(stop2-start2));
		
		System.out.println(list);
		list.stream().distinct().forEach(System.out::println);
	}
}
