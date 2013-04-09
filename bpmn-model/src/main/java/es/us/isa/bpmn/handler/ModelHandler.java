package es.us.isa.bpmn.handler;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * Clase abstracta a partir de la cual heredan las clases que permiten exportar e importar a XML. 
 * 
 * Cuando se importa de xml se obtiene la informacion que contiene en instancias de clases generadas con Jaxb (clases Jaxb) y a partir de 
 * esas instancias se puede obtener instancias de clases que modelen esa informacion (clases del modelo), es decir:
 * 
 * codigo xml -> instancias de clases Jabx -> clases del modelo
 * 
 * Esto permite que si cambia la sintaxis del xml (con lo cual cambian las clases Jaxb), los proyectos que utilicen las clases del modelo 
 * no sufren cambios. 
 * 
 * Esta clase tiene las siguientes funciones:
 * - Carga un xml y obtiene las clases del modelo.
 * - A partir de las clases del modelo genera un xml.
 * 
 * @author Edelia
 *
 */
@SuppressWarnings("rawtypes")
public abstract class ModelHandler {
	
	// Objeto factory utilizado para generar instancias de las clases Jaxb
	private Object factory;
	
	// Objeto JAXBElement utilizado para manejar el xml
	private JAXBContext jc;
	
	// Objeto JAXBElement que se obtiene del unmarshall de un xml (a partir de un archivo xml obtener clases Jaxb)
	private JAXBElement importElement;

	// Objeto JAXBElement que se utiliza para hacer el marshall de un xml (a partir de las clases Jaxb salvar el archivo xml)
	private JAXBElement exportElement;
	
	/**
	 * Constructor de la clase.
	 * Realiza las inicializaciones implementadas en las subclases mediante iniLoader.
	 * 
	 * @throws JAXBException
	 */
	public ModelHandler() throws JAXBException {

		super();
		
		this.iniLoader();
	}
	
	/**
	 * Configuracion para importar y exportar XML.
	 * Este metodo debe ser invocado en el iniLoader de la subclase, para especificar las clases Jaxb y la clase factory utilizadas. 
	 * 
	 * @param classList Arreglo con las clases para leer y guardar como xml 
	 * @param factory Factory utilizada
	 */
	protected void xmlConfig(Class[] classList, Class factory) {
		
		try {
			
			// crea el JAXBContext para manejar los XML
			this.jc = JAXBContext.newInstance( classList );
	    	
			// crea un objeto de la clase factory indicada
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
	 * @return Objeto factory
	 */
	protected Object getFactory() {
		return factory;
	}

	/**
	 * Realiza las inicializaciones propias de la clase que implementa a ModelHandler. 
	 * En esta inicializacion hay que incluir la llamada a xmlConfig para configurar las clases Jaxb y la clase factory.
	 * 
	 * @throws JAXBException
	 */
	protected abstract void iniLoader() throws JAXBException;
	
	/**
	 * Genera las instancias de clases Jaxb a partir de instancias de clases del modelo. 
	 * Tiene que ser implementado en las subclases.
	 * Genera el JAXBElement para exportar, por lo que debe finalizar invocando a this.setExportElement
	 * 
	 * @param procId Id del proceso en el xml. Es utilizado para formar el nombre del archivo xml generado
	 */
	protected abstract void generateExportElement(String procId);
	
	/**
	 * Genera las instancias de clases del modelo a partir de instancias de clases Jabx. 
	 * Tiene que ser implementado en las subclases.
	 * Utiliza el elemento JAXBElement para importar, invocando a this.getExportElement
	 */
	protected abstract void generateModelLists();
	
	/**
	 * Genera las instancias de clases del modelo a partir del xml en un archivo
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
	 * Genera las instancias de clases del modelo a partir del xml en un stream
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
	 * Salva el xml a un archivo, a partir de las instancias de clases del modelo
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
	 * Salva el xml a un stream, a partir de las instancias de clases del modelo
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
