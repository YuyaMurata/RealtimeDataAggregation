
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kaeru
 */
public class MapFilterTest {
	public static void main(String[] args) {
		Map<Object, List> map = new HashMap();
		List a = new ArrayList();
		List b = new ArrayList();
		
		a.add("A1");a.add("A2");a.add("A3");
		b.add("B1");b.add("B2");b.add("B3");
		map.put("A0", a);
		map.put("B0", b);
		
		Object str = map.keySet().stream().filter(id -> map.get(id).contains("B2")).findFirst().get();
		
		System.out.println(str);
	}
}
