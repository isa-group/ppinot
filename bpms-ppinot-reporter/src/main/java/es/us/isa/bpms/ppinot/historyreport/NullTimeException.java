package es.us.isa.bpms.ppinot.historyreport;

/**
 * Clase con la excepcion que se lanza al encontrar una fecha con valor null al calcular un PPI 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NullTimeException  extends Exception {
	
	/**
	 * Constructor de la clase
	 * 
	 * @param msg Mensaje de la excepcion
	 */
    public NullTimeException(String msg) {
        super(msg);
    }

}
