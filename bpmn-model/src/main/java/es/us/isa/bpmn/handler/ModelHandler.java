package es.us.isa.bpmn.handler;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * Clase abstracta a partir de la cual heredan las clases que cargan un XML y obtienen clases, y viceversa, que a partir de las clases
 * del modelo, exportan a XML
 * 
 * @author Edelia
 *
 */
@SuppressWarnings("rawtypes")
public abstract class ModelHandler {
	
	/**
	 * Objeto con instancias de las clases en los xml, que es utilizado para exportar e importar xml
	 */
	private Object factory;
	
	private JAXBContext jc;
	
	/**
	 * Objeto JAXBElement que se obtiene del unmarshall de un xml
	 */
	private JAXBElement importElement;

	/**
	 * Objeto JAXBElement que se utiliza para hacer el marshall de un xml
	 */
	private JAXBElement exportElement;
	
	/**
	 * Constructor de la clase.
	 * Crea el objeto para convertir un xml en instancias de las clases en el paquete pack.
	 * 
	 * @throws JAXBException
	 */
	public ModelHandler() throws JAXBException {

		super();
		
		this.iniLoader();
	}
	
	/**
	 * Configuración para importar y exportar XML
	 * 
	 * @param classList Arreglo con las clases para leer y guardar como xml 
	 * @param factory Factory utilizada
	 */
	protected void xmlConfig(Class[] classList, Class factory) {
		
		try {
			
			// crea el JAXBContext para hacer marshall y unmarshall
			this.jc = JAXBContext.newInstance( classList );
	    	
			// crea un objeto de la clase factory idicada
			this.factory = factory.newInstance();
		} catch (JAXBException e1) {

			e1.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve el valor del atributo importElement
	 * Objeto JAXBElement que se obtiene del unmarshall de un xml
	 * 
	 * @return Valor del atributo
	 */
	protected JAXBElement getImportElement() {
		return this.importElement;
	}

	/**
	 * Da valor al atributo element
	 * Objeto JAXBElement que se utiliza para hacer el marshall de un xml
	 * 
	 * @param exportElement Valor del atributo
	 */
	protected void setExportElement( JAXBElement exportElement) {
		this.exportElement = exportElement;
	}

	/**
	 * Devuelve la factory utilizada 
	 * 
	 * @return
	 */
	protected Object getFactory() {
		return factory;
	}

	/**
	 * Realiza las inicializaciones propias de la clase que implementa a ModelHandler. En esta inicialización hay que incluir
	 * la llamada a xmlConfig para configurar las clases a generar y la factory
	 * 
	 */
	protected abstract void iniLoader() throws JAXBException;
	
	/**
	 * Genera el objeto JAXBElement que permite exportar a un xml
	 */
	protected abstract void generateExportElement(String procId);
	
	/**
	 * Genera las listas de definiciones de medidas a partir de un xml importado
	 */
	protected abstract void generateModelLists();
	
	/**
	 * Carga el fichero del cual se obtendrán los objetos
	 * 
	 * @param path Camino del archivo
	 * @param file Nombre del archivo
	 * @throws JAXBException
	 */
	public void load(String path, String file) throws JAXBException {
		
		// hace el unmarshall a las clases generadas por Jaxb
		this.importElement = (JAXBElement) this.jc.createUnmarshaller().unmarshal( new File(path,  file) );
		
        // obtiene los objetos del modelo, a partir de los objetos obtenidos del unmarshall
		this.generateModelLists();
	}

    /**
	 * Carga el stream del cual se obtendrán los objetos
     * 
     * @param stream 
     * @throws JAXBException
     */
	public void load(InputStream stream) throws JAXBException {
    	
		// hace el unmarshall a las clases generadas por Jaxb
		this.importElement =  (JAXBElement<?>) jc.createUnmarshaller().unmarshal(stream);

        // obtiene los objetos del modelo, a partir de los objetos obtenidos del unmarshall
        this.generateModelLists();
    }

	/**
	 * Salva el xml a un archivo 
	 * 
	 * @param path Camino
	 * @param file Nombre del fichero
	 * @param procId Id del proceso
	 * @throws JAXBException
	 */
	public void save(String path, String file, String procId) throws JAXBException {
		
		// genera un objeto del tipo JAXBElement con los objetos que pueden ser exportados a un fichero xml
    	this.generateExportElement(procId);
    	
    	// exporta el xml a partir del JAXBElement generado
        this.jc.createMarshaller().marshal( this.exportElement, new File(path, file) );
    }
	
	/**
	 * Salva el xml a stream
	 * 
	 * @param stream
	 * @param procId Id del proceso
	 * @throws JAXBException
	 */
	public void save(OutputStream stream, String procId) throws JAXBException {

		// genera un objeto del tipo JAXBElement con los objetos que pueden ser exportados a un fichero xml
    	this.generateExportElement(procId);
    	
    	// exporta el xml a partir del JAXBElement generado
        this.jc.createMarshaller().marshal( this.exportElement, stream );
	}
	
}
