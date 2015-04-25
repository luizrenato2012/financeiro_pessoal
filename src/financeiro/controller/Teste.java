package financeiro.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class Teste {
	
	static void timeZone() {
		TimeZone timeZone = TimeZone.getDefault();
		 System.out.println(timeZone.getID());
		 
		 for(String str : timeZone.getAvailableIDs()){
			 System.out.println(str);
		 }
	}
	
	static void testeMap() {
		Map<Integer,String> map = new HashMap<Integer, String>();
		map.put(1, "Um");
		map.put(2, "Dois");
		map.put(3, "Três");
		map.put(4, "Quatro");
		map.put(5, "Cinco");
		map.put(6, "Seis");
		
		for(Integer key : map.keySet() ) {
			System.out.println(key + " - " + map.get(key));
		}
	}

	public static void main(String[] args) {
		testeMap();
	}

}
