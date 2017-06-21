
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
public class MapListTest {
	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("A", new ArrayList());
		System.out.println(map);
		
		List list = (List) map.get("A");
		list.add(1);
		System.out.println(map);
		
		map.put("A", list);
		System.out.println(map);
	}
}
