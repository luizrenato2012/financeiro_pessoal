package financeiro.controller;

import java.util.TimeZone;

public class Teste {

	public static void main(String[] args) {
		TimeZone timeZone = TimeZone.getDefault();
		 System.out.println(timeZone.getID());
		 
		 for(String str : timeZone.getAvailableIDs()){
			 System.out.println(str);
		 }

	}

}
