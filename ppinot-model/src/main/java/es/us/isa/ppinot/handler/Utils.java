package es.us.isa.ppinot.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Utilidades
 * 
 * @author Edelia
 *
 */
public class Utils {

	public static String FORMATDATE = "yyyy/MM/dd";
	public static String FORMATDATEHOUR = "yyyy/MM/dd HH:mm:ss";
	public static Integer STARTYEAR = 2009;

	/**
	 * Genera un objeto Date a partir de una cadena con una fecha
	 * 
	 * @param dateString Cadena
	 * @return Objeto Date
	 */
	public static Date parseDate(String dateString) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( FORMATDATE );
		Date date;
		try {

			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			
			date = null;
		}
		return date;
	}
	
	/**
	 * Genera un objeto Date a partir de una cadena con fecha y hora
	 * 
	 * @param dateString Cadena
	 * @return Objeto Date
	 */
	public static Date parseDateHour(String dateString) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( FORMATDATEHOUR );
		Date date;
		try {

			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			
			date = null;
		}
		return date;
	}

	/**
	 * Genera una cadena a partir de un objeto Date con una fecha
	 * 
	 * @param date Objeto Date
	 * @return Cadena
	 */
	public static String formatString(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( FORMATDATE );
		String string = "";
		if (date!=null) 
			string = dateFormat.format(date);
		return string;
	}

	/**
	 * Genera una cadena a partir de un objeto Date con una fecha y hora
	 * 
	 * @param date Objeto Date
	 * @return Cadena
	 */
	public static String formatStringHour(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( FORMATDATEHOUR );
		String string = "";
		if (date!=null) 
			string = dateFormat.format(date);
		return string;
	}
	
	/**
	 * Convierte un doble a cadena
	 * 
	 * @param doub Double
	 * @return Cadena
	 */
	public static String doubleToString(Double doub) {
		
		if (doub==null)
			return "";
		else
			return String.valueOf(doub);
	}
	
	/**
	 * Convierte una cadena a doble
	 * 
	 * @param doub Cadena
	 * @return Double
	 */
	public static Double stringToDouble(String string) {
		
		if (string==null || string=="")
			return null;
		else
			return Double.valueOf(string);
	}
	
	/**
	 * Convierte un boolean a cadena
	 * 
	 * @param bool Boolean
	 * @return Cadena
	 */
	public static String booleanToStringInteger(Boolean bool) {
		
		String str = "0"; 
		if (bool) 
			str = "1";
		return str;
	}
	
	/**
	 * Foamatea una cadena con un decimal. Devuelve una cadena con el número con dos lugares decimales
	 * 
	 * @param str Cadena
	 * @return Cadena
	 */
	public static String numberFormat( String str) {

		try {
			
			Double doub = Double.valueOf(str);
			
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.US);
	
			formatter.format("%10.2f", doub);
			
			return sb.substring(0);
			
		} catch (Exception e) {
			
			return "";
		}
	}
	
	/**
	 * Obtiene el año actual
	 * 
	 * @return Entero con el año actual
	 */
	public static Integer currentYear() {
		
		GregorianCalendar gFechaActual = new GregorianCalendar(); 
		return gFechaActual.get(GregorianCalendar.YEAR);
	}
}
