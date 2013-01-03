package es.us.isa.ppinot.owl.converter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;

/**
 * @author Ana Belen Sanchez Jerez
 * **/
public class InObjectsOutOWLPpinot {

	private OWLOntologyManager ppinotMan;
	
	private File ppinotFile;
	
	private OWLOntology ppinotOnt;
	
	private String orgppinot;
	private String orgbpmn;
	
	private GenerateOWLPpinot converter;
	
	/**Constructor de la clase inObjectsOutOWL que inicializa todos los objetos
	 * necesarios de la api OWL para poder trabajar con ellos. Inicializa 
	 * OWLManager, OWLOntology,OWLDataFactory, las urls a ficheros owl. **/
	public InObjectsOutOWLPpinot(String caminoDestino, String ppinotFilename, String orgppinotIn, String orgbpmnIn, String orgbpmnExpr, OWLOntologyManager ppinotManIn) {
		
		ppinotFile = new File(caminoDestino + ppinotFilename);
		try {
			ppinotFile.createNewFile();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		
		ppinotMan = ppinotManIn;
		orgbpmn = orgbpmnIn;
		orgppinot = orgppinotIn;
		
		try {

	            ppinotOnt = ppinotMan.createOntology();

				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgbpmn) )));
				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgppinot) )));
				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgbpmnExpr) )));
	            
				converter = new GenerateOWLPpinot(
								ppinotMan.getOWLDataFactory(), ppinotMan, ppinotOnt, orgppinot, 
								orgbpmn,
								orgbpmnExpr, ppinotFile.toURI().toString()); 

		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}
		

	}
	
	/***Declaraciones en owl de la medida countInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsCountInstanceMeasure(List<CountInstanceMeasure> countInstanceMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		Iterator<?> itr = countInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			CountInstanceMeasure element = (CountInstanceMeasure) itr.next();
			
		    converter.converterCountInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}
		
	}
	
	/***Declaraciones en owl de la medida timeInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsTimeInstanceMeasure(List<TimeInstanceMeasure> timeMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		Iterator<?> itr = timeMeasure.iterator(); 
		while(itr.hasNext()) {
			TimeInstanceMeasure element = (TimeInstanceMeasure) itr.next();
			
			converter.converterTimeInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}
	
	/***Declaraciones en owl de la medida StateConditionInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsStateConditionInstanceMeasure(List<StateConditionInstanceMeasure> elementConditionMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		Iterator<?> itr = elementConditionMeasure.iterator(); 
		while(itr.hasNext()) {
			StateConditionInstanceMeasure element = (StateConditionInstanceMeasure) itr.next();
			
		    converter.converterStateConditionInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataPropertyConditionInstanceMeasure(List<DataPropertyConditionInstanceMeasure> dataConditionMeasure) throws Exception {
		Iterator<?> itr = dataConditionMeasure.iterator(); 
		while(itr.hasNext()) {
			DataPropertyConditionInstanceMeasure element = (DataPropertyConditionInstanceMeasure) itr.next();
	
			converter.converterDataPropertyConditionInstanceMeasureOWL(element);
		}	
	}
	
	/***Declaraciones en owl de la medida DataInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataInstanceMeasure(List<DataInstanceMeasure> dataInstanceMeasure) throws Exception {
		Iterator<?> itr = dataInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			DataInstanceMeasure element = (DataInstanceMeasure) itr.next();
			
		    converter.converterDataInstanceMeasureOWL(element);
		}	
	}

	/***Declaraciones en owl de medidas de tipo countAggregatedMeasure 
	 * @param jaxbElement 
	 * @param taskList 
	 * @throws Exception ***/
	
	/***Declaraciones en owl de la medida countAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsCountAggregatedMeasure(List<AggregatedMeasure> countAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		Iterator<?> itr = countAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			converter.converterCountAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}
		
	}

//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida timeAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsTimeAggregatedMeasure(List<AggregatedMeasure> timeAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		Iterator<?> itr = timeAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
	
		    converter.converterTimeAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}
		
	}
	
	
	/***Declaraciones en owl de la medida StateConditionAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsStateConditionAggregatedMeasure(List<AggregatedMeasure> elementConditionAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		Iterator<?> itr = elementConditionAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			converter.converterStateConditionAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(List<AggregatedMeasure> dataConditionAggregatedMeasure) throws Exception {
		Iterator<?> itr = dataConditionAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			converter.converterDataPropertyConditionAggregatedMeasureOWL(element);
		}	
	}
	
	/***Declaraciones en owl de la medida DataAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataAggregatedMeasure(List<AggregatedMeasure> dataAggregatedMeasure) throws Exception {
		Iterator<?> itr = dataAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			converter.converterDataAggregatedMeasureOWL(element);
		}	
	}
	
	public void getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(List<AggregatedMeasure> dataAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = dataAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			converter.converterDerivedSingleInstanceAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida DerivedMultiInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDerivedMultiInstanceMeasure(List<DerivedMultiInstanceMeasure> derivedProcessMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = derivedProcessMeasure.iterator(); 
		while(itr.hasNext()) {
			DerivedMultiInstanceMeasure element = (DerivedMultiInstanceMeasure) itr.next();

		    converter.converterDerivedMultiInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	public void getDeclarationIndividualsDerivedSingleInstanceMeasure(List<DerivedSingleInstanceMeasure> derivedInstanceMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		//Para las derivadas voy a procesar las medidas aggregated que contiene por separado utilizando 
		//las funciones definidas anteriormente
		
		Iterator<?> itr = derivedInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			DerivedSingleInstanceMeasure element = (DerivedSingleInstanceMeasure) itr.next();

		    converter.converterDerivedSingleInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}
	}
	
	
	public void getSaveOWL() {
		
		try {

			ppinotMan.saveOntology(ppinotOnt,IRI.create(ppinotFile.toURI()));
		} catch (OWLOntologyStorageException e) {

			e.printStackTrace();
		}
		
	}

	
	
	
}
