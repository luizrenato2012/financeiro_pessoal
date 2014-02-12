package financeiro.util;

import java.util.Date;

/**
 * 
 * @author Luiz Renato
 *
 */
public class DataUtils {
	
	public static Boolean isAnterior(Date d1,Date d2) {
		return d1.compareTo(d2) < 0;
	}
	
//	public static void main(String[] args) {
//		GregorianCalendar g1 = new GregorianCalendar(2013, 12, 29);
	//	GregorianCalendar g2 = new GregorianCalendar(2013, 12, 30);
		
	//	System.out.println(isPosterior(g1.getTime(), g2.getTime()));
	//	System.out.println(isPosterior(g2.getTime(), g1.getTime()));
		
//	}
	

}
