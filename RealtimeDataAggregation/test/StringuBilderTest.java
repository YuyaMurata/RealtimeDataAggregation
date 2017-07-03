/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kaeru
 */
public class StringuBilderTest {
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("a");
		sb.append("b");
		sb.append("c");
		System.out.println(sb);
		
		sb.deleteCharAt(sb.length()-1);
		System.out.println(sb);
		
	}
}
