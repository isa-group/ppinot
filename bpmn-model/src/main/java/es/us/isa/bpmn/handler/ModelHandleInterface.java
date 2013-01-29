package es.us.isa.bpmn.handler;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interfaz de las clases que permiten exportar e importar a XML
 * 
 * @author Edelia
 *
 */
public interface ModelHandleInterface {

	/**
	 * Devuelve el id del proceso involucrado en el xml
	 * 
	 * @return Id del proceso
	 */
	public String getProcId();
	
	/**
	 * Genera las instancias de clases del modelo a partir del xml en un archivo
	 *
	 * @param path Camino del archivo
	 * @param file Nombre del archivo
	 * @throws Exception
	 */
	public void load(String path, String file) throws Exception;
	
    /**
	 * Genera las instancias de clases del modelo a partir del xml en un stream
     * 
     * @param stream 
     * @throws Exception
     */
	public void load(InputStream stream) throws Exception;
	
	/**
	 * Salva el xml a un archivo, a partir de las instancias de clases del modelo
	 * 
	 * @param path Camino
	 * @param file Nombre del fichero
	 * @param procId Id del proceso
	 * @throws Exception
	 */
	public void save(String path, String file, String procId) throws Exception;
	
	/**
	 * Salva el xml a un stream, a partir de las instancias de clases del modelo
	 * 
	 * @param stream
	 * @param procId Id del proceso
	 * @throws Exception
	 */
	public void save(OutputStream stream, String procId) throws Exception;
}
