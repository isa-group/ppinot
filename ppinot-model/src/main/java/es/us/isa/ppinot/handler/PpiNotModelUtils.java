package es.us.isa.ppinot.handler;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidades
 * 
 * @author Edelia
 *
 */
@Deprecated
public class PpiNotModelUtils {

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
	 * @param string Cadena
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
	 * Foamatea una cadena con un decimal. Devuelve una cadena con el numero con dos lugares decimales
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

    /**
     * Obtiene los datos del periodo que se desea analizar, a partir de la propiedad analisysPeriod de una medida agregada
     *
     * @param period Cadena con el periodo de analisis
     * @return Mapa con los datos del periodo de analisis
     */
    @Deprecated
    public static Map<String, String> parseAnalysisPeriod(String period) {

        Map<String, String> map = new HashMap<String, String>();

        if (period.contentEquals("")) {

            map.put("year", "");
            map.put("period", "");
            map.put("startDate", "");
            map.put("endDate", "");
            map.put("inStart", "");
            map.put("inEnd", "");
        }
        else
            try {
                Pattern patron = Pattern.compile("(interval)\\((\\d{4}\\/\\d{2}\\/\\d{2}),(\\d{4}\\/\\d{2}\\/\\d{2})\\,(true|false)\\,(true|false)\\)|(period)\\((\\d{4}),(trimestre|semestre|mes)\\,(true|false)\\,(true|false)\\)");
                Matcher matcher = patron.matcher( period );
                matcher.find();

                if (matcher.group(6)!=null && matcher.group(6).contentEquals("period")) {

                    map.put("year", matcher.group(7));
                    map.put("period", matcher.group(8));
                    map.put("startDate", "");
                    map.put("endDate", "");
                    map.put("inStart", (matcher.group(9).compareTo("true")==0)?"yes":"");
                    map.put("inEnd", (matcher.group(10).compareTo("true")==0)?"yes":"");
                }
                else
                if (matcher.group(1).contentEquals("interval")) {

                    map.put("year", "");
                    map.put("period", "");
                    map.put("startDate", matcher.group(2));
                    map.put("endDate", matcher.group(3));
                    map.put("inStart", (matcher.group(4).compareTo("true")==0)?"yes":"");
                    map.put("inEnd", (matcher.group(5).compareTo("true")==0)?"yes":"");
                }

            }  catch (Exception e) {

                map.put("year", "");
                map.put("period", "");
                map.put("startDate", "");
                map.put("endDate", "");
                map.put("inStart", "");
                map.put("inEnd", "");
            }

        return map;
    }

    /**
     * Obtiene los valores de referencia para evaluar el grado de satisfaccion de un ppi, a partir de la propiedad target
     *
     * @param target Valor de la propiedad target de un ppi
     * @return Mapa con los valores de referencia. El valor minimo se devuelve con la llave refMin y el valor maximo con la llave refMax
     */
    @Deprecated
    public static Map<String, Double> parseTarget(String target) {

        Map<String, Double> map = new HashMap<String, Double>();

        try {
            Pattern patron = Pattern.compile("(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)\\-(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)|(>|<|=|@)(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)");
            Matcher matcher = patron.matcher( target );
            matcher.find();

            if (matcher.group(3)!=null && matcher.group(3).contentEquals("=")) {

                map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(4)));
                map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(4)));
            } else
            if (matcher.group(3)!=null && matcher.group(3).contentEquals(">")) {

                map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(4)));
            } else
            if (matcher.group(3)!=null && matcher.group(3).contentEquals("<")) {

                map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(4)));
            } else
            if (matcher.group(1)!=null && matcher.group(2)!=null) {
                map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(1)));
                map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(2)));
            }
        }  catch (Exception e) {

            map.put("refMin", null);
            map.put("refMax", null);
        }

        return map;
    }

}
