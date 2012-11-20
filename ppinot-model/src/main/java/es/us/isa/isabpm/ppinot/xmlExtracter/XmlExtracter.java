package es.us.isa.isabpm.ppinot.xmlExtracter;

import es.us.isa.isabpm.ppinot.model.PPI;
import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedSingleInstanceMeasure;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * Clase que a partir de un xml con información de PPIs permite obtener instancias de las clases en un paquete. Estas clases
 * debieron generarse con jaxb (por ejemplo, ver <a href="../ppinotXML/package-summary.html">el paquete ppinotXML</a>). A partir de esos objetos permite obtener listas de instancias 
 * de las clases en el paquete <a href="../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a> que son utilizadas en un objeto subclase de <a href="../historyreport/HistoryReport.html">HistoryReport</a> para obtener 
 * el reporte solicitado en el xml.
 * <p>
 * Publica métodos abstractos que deben ser implementados mediante una clase como <a href="prueba/PpiNotXmlExtracter.html">PpiNotXmlExtracter</a> en el paquete xmlExtracter.prueba,
 * de manera que la aplicación pueda utilizarse con diferentes xsd simplemente modificando el paquete del cual se hace uso. 
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
@SuppressWarnings("rawtypes")
public abstract class XmlExtracter {
	
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
	 * @param pack Nombre del paquete desde el cual se obtienen las clases generadas con jaxb
	 * @throws JAXBException
	 */
	public XmlExtracter() throws JAXBException {

		super();
		
		this.iniExtracter();
	}

	/**
	 * Inicializa el JAXBContext y los ObjectFactory en dependencia de la ubicación de los paquetes con las clases que permiten
	 * exportar y exportar a xml
	 * 
	 */
	protected abstract void iniExtracter() throws JAXBException;

	/**
	 * Da valor al atributo jc
	 * Objeto con instancias de las clases en el paquete de BPMN, que es utilizado para exportar e importar xml
	 * 
	 * @param of Valor del atributo
	 */
    protected void setJc(JAXBContext jc) {
		this.jc = jc;
	}

	/**
	 * Devuelve el valor del atributo importElement
	 * Objeto JAXBElement que se obtiene del unmarshall de un xml
	 * 
	 * @return Valor del atributo
	 */
	public JAXBElement getImportElement() {
		return this.importElement;
	}

	/**
	 * Da valor al atributo importElement
	 * Objeto JAXBElement que se obtiene del unmarshall de un xml
	 * 
	 * @param importElement Valor del atributo
	 */
	protected void setImportElement( JAXBElement importElement) {
		this.importElement = importElement;
	}

	/**
	 * Devuelve el valor del atributo element
	 * Objeto JAXBElement que se utiliza para hacer el marshall de un xml
	 * 
	 * @return Valor del atributo
	 */
	protected JAXBElement getExportElement() {
		return this.exportElement;
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
	 * Carga el fichero file, del cual se obtendrán los objetos
	 * 
	 * @param file
	 * @throws JAXBException
	 */
	public void unmarshall(String path, String file) throws JAXBException {
		
		// crea un objeto del tipo JAXBElement que permite acceder a los objetos obtenidos del fichero xml
		this.setImportElement( (JAXBElement) this.jc.createUnmarshaller().unmarshal( new File(path,  file) ) );
		
		this.generateModelLists();
	}

	/**
	 * Crea un fichero xml a partir de los objetos instancias de las clases en el paquete pack, que se encuentran en el atributo proccessType
	 * 
	 * @param path Camino del fichero xml
	 * @param nomFich Nombre del fichero xml
	 */
	public void marshall(String path, String file, String procId) {
        try {

    		// genera un objeto del tipo JAXBElement con los objetos que pueden ser exportados a un fichero xml
        	this.generateExportElement(procId);
        	
        	// exporta el xml a partir del JAXBElement generado
            this.jc.createMarshaller().marshal( this.getExportElement(), new File(path, file) );
        } catch( JAXBException jbe ){
            // ...
        }
    }
	
	/**
	 * Métodos abstractos que devuelven listas con instancias de las clases definidas en el paquete <a href="../../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a>. 
	 * Para realizar esto se utilizan instancias de las clases en <a href="../../ppinotXML/package-summary.html">el paquete ppinotXML<a> 
	 * que fueron generadas con jaxb y que se encuentran en element
	 */
	public abstract List<CountInstanceMeasure> getCountInstanceMeasure();
	public abstract List<TimeInstanceMeasure> getTimeInstanceMeasure();
	public abstract List<StateConditionInstanceMeasure> getStateConditionInstanceMeasure();
	public abstract List<DataInstanceMeasure> getDataInstanceMeasure();
	public abstract List<DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceMeasure();
	
	public abstract List<AggregatedMeasure> getTimeAggregatedMeasure();
	public abstract List<AggregatedMeasure> getCountAggregatedMeasure();
	public abstract List<AggregatedMeasure> getStateConditionAggregatedMeasure();
	public abstract List<AggregatedMeasure> getDataAggregatedMeasure();
	public abstract List<AggregatedMeasure> getDataPropertyConditionAggregatedMeasure();

	public abstract List<DerivedSingleInstanceMeasure> getDerivedSingleInstanceMeasure();
	public abstract List<DerivedMultiInstanceMeasure> getDerivedMultiInstanceMeasure();

	public abstract List<PPI> getPpiModel();
	
	/**
	 * Métodos abstractos que devuelven listas con instancias de las clases definidas en <a href="../../ppinotXML/package-summary.html">el paquete ppinotXML<a>, para ser exportadas a xml. 
	 * Para realizar esto se utilizan instancias de las clases en el paquete <a href="../../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a>, que es el que uriliza la appweb.
	 */
	public abstract void setTimeMeasure(List<TimeInstanceMeasure> modelList);
	public abstract void setCountMeasure(List<CountInstanceMeasure> modelList);
	public abstract void setStateConditionMeasure(List<StateConditionInstanceMeasure> modelList);
	public abstract void setDataMeasure(List<DataInstanceMeasure> modelList);
	public abstract void setDataPropertyConditionMeasure(List<DataPropertyConditionInstanceMeasure> modelList);

	public abstract void setTimeAggregatedMeasure(List<AggregatedMeasure> modelList);
	public abstract void setCountAggregatedMeasure(List<AggregatedMeasure> modelList);
	public abstract void setStateConditionAggregatedMeasure(List<AggregatedMeasure> modelList);
	public abstract void setDataAggregatedMeasure(List<AggregatedMeasure> modelList);
	public abstract void setDataPropertyConditionAggregatedMeasure(List<AggregatedMeasure> modelList);

	public abstract void setDerivedSingleInstanceMeasure(List<DerivedSingleInstanceMeasure> modelList);
	public abstract void setDerivedMultiInstanceMeasure(List<DerivedMultiInstanceMeasure> modelList);

	public abstract void setPpiModel(List<PPI> modelList);
	
	/**
	 * Genera el objeto JAXBElement que permite exportar a un xml, una vez que se han ejecutado los métodos set anteriores
	 */
	protected abstract void generateExportElement(String procId);
	
	/**
	 * Genera las listas de definiciones de medidas a partir de un xml importado
	 */
	protected abstract void generateModelLists();
	
}
