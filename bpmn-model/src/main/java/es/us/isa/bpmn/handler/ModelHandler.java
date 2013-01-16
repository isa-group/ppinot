package es.us.isa.bpmn.handler;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * Clase que a partir de un xml con informaci�n de PPIs permite obtener instancias de las clases en un paquete. Estas clases
 * debieron generarse con jaxb (por ejemplo, ver <a href="../ppinotXML/package-summary.html">el paquete ppinotXML</a>). A partir de esos objetos permite obtener listas de instancias 
 * de las clases en el paquete <a href="../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a> que son utilizadas en un objeto subclase de <a href="../historyreport/HistoryReport.html">HistoryReport</a> para obtener 
 * el reporte solicitado en el xml.
 * <p>
 * Publica m�todos abstractos que deben ser implementados mediante una clase como <a href="prueba/PpiNotModelHandler.html">PpiNotModelHandler</a> en el paquete modelHandler.prueba,
 * de manera que la aplicaci�n pueda utilizarse con diferentes xsd simplemente modificando el paquete del cual se hace uso. 
 * 
 * @author Edelia Garc�a Gonz�lez
 * @version 1.0
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

	protected Object getFactory() {
		return factory;
	}

	/**
	 * Inicializa el JAXBContext y los ObjectFactory en dependencia de la ubicaci�n de los paquetes con las clases que permiten
	 * exportar y exportar a xml
	 * 
	 */
	protected abstract void iniLoader() throws JAXBException;
	
	/**
	 * Genera el objeto JAXBElement que permite exportar a un xml, una vez que se han ejecutado los m�todos set anteriores
	 */
	protected abstract void generateExportElement(String procId);
	
	/**
	 * Genera las listas de definiciones de medidas a partir de un xml importado
	 */
	protected abstract void generateModelLists();
	
	/**
	 * Carga el fichero file, del cual se obtendr�n los objetos
	 * 
	 * @param file
	 * @throws JAXBException
	 */
	public void load(String path, String file) throws JAXBException {
		
		// crea un objeto del tipo JAXBElement que permite acceder a los objetos obtenidos del fichero xml
		this.importElement = (JAXBElement) this.jc.createUnmarshaller().unmarshal( new File(path,  file) );
		
		this.generateModelLists();
	}

    public void load(InputStream stream) throws JAXBException {
    	
        JAXBElement<?> element = (JAXBElement<?>) jc.createUnmarshaller().unmarshal(stream);

        this.importElement = element;
        this.generateModelLists();
    }

	/**
	 * Crea un fichero xml a partir de los objetos instancias de las clases en el paquete pack, que se encuentran en el atributo proccessType
	 * 
	 * @param path Camino del fichero xml
	 * @param file Nombre del fichero xml
	 * @throws JAXBException 
	 */
	public void save(String path, String file, String procId) throws JAXBException {
		
		// genera un objeto del tipo JAXBElement con los objetos que pueden ser exportados a un fichero xml
    	this.generateExportElement(procId);
    	
    	// exporta el xml a partir del JAXBElement generado
        this.jc.createMarshaller().marshal( this.exportElement, new File(path, file) );
    }
	
	public void save(OutputStream stream, String procId) throws JAXBException {

		// genera un objeto del tipo JAXBElement con los objetos que pueden ser exportados a un fichero xml
    	this.generateExportElement(procId);
    	
    	// exporta el xml a partir del JAXBElement generado
        this.jc.createMarshaller().marshal( this.exportElement, stream );
	}
	
}
