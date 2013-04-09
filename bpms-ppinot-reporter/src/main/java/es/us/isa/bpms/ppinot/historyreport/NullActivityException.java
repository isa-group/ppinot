package es.us.isa.bpms.ppinot.historyreport;

/**
 * Clase con la excepcion que se lanza cuando se solicita una actividad no incluida en una instancia de proceso al calcular un PPI 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NullActivityException  extends Exception {
	
	/**
	 * Constructor de la clase
	 * 
	 * @param msg Mensaje de la excepcion
	 */
    public NullActivityException(String msg) {
        super(msg);
    }

}
