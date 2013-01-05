package es.us.isa.ppinot.owl.converter;

import java.util.Iterator;
import java.util.List;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import es.us.isa.bpmn.owl.converter.ToOWLConverter;
import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;
import es.us.isa.bpmn.xmlExtracter.XmlExtracter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.owl.notation.Vocabulary;
import es.us.isa.ppinot.xmlExtracter.PpiNotXmlExtracter;

/**
 * @author Ana Belen Sanchez Jerez
 * **/
public class PPINOT2OWLConverter extends ToOWLConverter {
	
	private String bpmnOntologyURI;
	private String bpmnGeneratedOntologyURI;
	private Bpmn20XmlExtracter bpmn20XmlExtracter;
	
	private GeneratePpinotAxioms generator;
	
	/**Constructor de la clase inObjectsOutOWL que inicializa todos los objetos
	 * necesarios de la api OWL para poder trabajar con ellos. Inicializa 
	 * OWLManager, OWLOntology,OWLDataFactory, las urls a ficheros owl. **/
	public PPINOT2OWLConverter(String baseIRI, OWLOntologyManager manager) {
		
		super( baseIRI, manager);
	}
	
    @Override
	protected void generateOntology(XmlExtracter xmlExtracter) throws OWLOntologyCreationException {
    	
    	String[] uris = { bpmnOntologyURI, Vocabulary.URI, bpmnGeneratedOntologyURI };
    	this.addOntologyImports(uris);
    	
		generator = new GeneratePpinotAxioms(
				this.getManager().getOWLDataFactory(), this.getManager(), this.getOntology(), 
				bpmnOntologyURI, bpmnGeneratedOntologyURI, this.getOntologyURI());
		
    	PpiNotXmlExtracter ppiNotXmlExtracter = (PpiNotXmlExtracter) xmlExtracter;

		try {
			
			this.getDeclarationIndividualsCountInstanceMeasure( ppiNotXmlExtracter.getCountInstanceMeasure(), bpmn20XmlExtracter);

			this.getDeclarationIndividualsTimeInstanceMeasure( ppiNotXmlExtracter.getTimeInstanceMeasure(), bpmn20XmlExtracter);
			this.getDeclarationIndividualsStateConditionInstanceMeasure( ppiNotXmlExtracter.getStateConditionInstanceMeasure(), bpmn20XmlExtracter);
			this.getDeclarationIndividualsDataPropertyConditionInstanceMeasure( ppiNotXmlExtracter.getDataPropertyConditionInstanceMeasure());
			this.getDeclarationIndividualsDataInstanceMeasure( ppiNotXmlExtracter.getDataInstanceMeasure() );
			
			this.getDeclarationIndividualsCountAggregatedMeasure( ppiNotXmlExtracter.getCountAggregatedMeasure(), bpmn20XmlExtracter);
			this.getDeclarationIndividualsTimeAggregatedMeasure( ppiNotXmlExtracter.getTimeAggregatedMeasure(), bpmn20XmlExtracter);
			this.getDeclarationIndividualsStateConditionAggregatedMeasure( ppiNotXmlExtracter.getStateConditionAggregatedMeasure(), bpmn20XmlExtracter);
			this.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure( ppiNotXmlExtracter.getDataPropertyConditionAggregatedMeasure());
			this.getDeclarationIndividualsDataAggregatedMeasure( ppiNotXmlExtracter.getDataAggregatedMeasure());
			this.getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure( ppiNotXmlExtracter.getDerivedSingleInstanceAggregatedMeasure(), bpmn20XmlExtracter);
	
			this.getDeclarationIndividualsDerivedMultiInstanceMeasure( ppiNotXmlExtracter.getDerivedMultiInstanceMeasure(), bpmn20XmlExtracter);
			this.getDeclarationIndividualsDerivedSingleInstanceMeasure( ppiNotXmlExtracter.getDerivedSingleInstanceMeasure(), bpmn20XmlExtracter);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
    public void setBpmnData(String bpmnGeneratedOntologyURI, Bpmn20XmlExtracter bpmn20XmlExtracter) {

    	this.bpmnOntologyURI = es.us.isa.bpmn.owl.notation.Vocabulary.URI;
    	this.bpmnGeneratedOntologyURI = bpmnGeneratedOntologyURI;
    	this.bpmn20XmlExtracter = bpmn20XmlExtracter;
    }
    
	/***Declaraciones en owl de la medida countInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsCountInstanceMeasure(List<CountInstanceMeasure> countInstanceMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {

		Iterator<?> itr = countInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			CountInstanceMeasure element = (CountInstanceMeasure) itr.next();
			
		    generator.converterCountInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}
	}
	
	/***Declaraciones en owl de la medida timeInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsTimeInstanceMeasure(List<TimeInstanceMeasure> timeMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = timeMeasure.iterator(); 
		while(itr.hasNext()) {
			
			TimeInstanceMeasure element = (TimeInstanceMeasure) itr.next();
			
			generator.converterTimeInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}
	
	/***Declaraciones en owl de la medida StateConditionInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsStateConditionInstanceMeasure(List<StateConditionInstanceMeasure> elementConditionMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = elementConditionMeasure.iterator(); 
		while(itr.hasNext()) {
			
			StateConditionInstanceMeasure element = (StateConditionInstanceMeasure) itr.next();
			
		    generator.converterStateConditionInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataPropertyConditionInstanceMeasure(List<DataPropertyConditionInstanceMeasure> dataConditionMeasure) throws Exception {
		
		Iterator<?> itr = dataConditionMeasure.iterator(); 
		while(itr.hasNext()) {
			
			DataPropertyConditionInstanceMeasure element = (DataPropertyConditionInstanceMeasure) itr.next();
	
			generator.converterDataPropertyConditionInstanceMeasureOWL(element);
		}	
	}
	
	/***Declaraciones en owl de la medida DataInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataInstanceMeasure(List<DataInstanceMeasure> dataInstanceMeasure) throws Exception {
		
		Iterator<?> itr = dataInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			
			DataInstanceMeasure element = (DataInstanceMeasure) itr.next();
			
		    generator.converterDataInstanceMeasureOWL(element);
		}	
	}

	/***Declaraciones en owl de medidas de tipo countAggregatedMeasure 
	 * @param jaxbElement 
	 * @param taskList 
	 * @throws Exception ***/
	
	/***Declaraciones en owl de la medida countAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsCountAggregatedMeasure(List<AggregatedMeasure> countAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = countAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			generator.converterCountAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}
		
	}

//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida timeAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsTimeAggregatedMeasure(List<AggregatedMeasure> timeAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = timeAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
	
		    generator.converterTimeAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}
		
	}
	
	
	/***Declaraciones en owl de la medida StateConditionAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsStateConditionAggregatedMeasure(List<AggregatedMeasure> elementConditionAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = elementConditionAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			generator.converterStateConditionAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(List<AggregatedMeasure> dataConditionAggregatedMeasure) throws Exception {
		
		Iterator<?> itr = dataConditionAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			generator.converterDataPropertyConditionAggregatedMeasureOWL(element);
		}	
	}
	
	/***Declaraciones en owl de la medida DataAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataAggregatedMeasure(List<AggregatedMeasure> dataAggregatedMeasure) throws Exception {
		
		Iterator<?> itr = dataAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			generator.converterDataAggregatedMeasureOWL(element);
		}	
	}
	
	private void getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(List<AggregatedMeasure> dataAggregatedMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = dataAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			generator.converterDerivedSingleInstanceAggregatedMeasureOWL(element, bpmn20XmlExtracter);
		}	
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida DerivedMultiInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsDerivedMultiInstanceMeasure(List<DerivedMultiInstanceMeasure> derivedProcessMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Iterator<?> itr = derivedProcessMeasure.iterator(); 
		while(itr.hasNext()) {
			
			DerivedMultiInstanceMeasure element = (DerivedMultiInstanceMeasure) itr.next();

		    generator.converterDerivedMultiInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	private void getDeclarationIndividualsDerivedSingleInstanceMeasure(List<DerivedSingleInstanceMeasure> derivedInstanceMeasure, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {

		//Para las derivadas voy a procesar las medidas aggregated que contiene por separado utilizando 
		//las funciones definidas anteriormente
		Iterator<?> itr = derivedInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			
			DerivedSingleInstanceMeasure element = (DerivedSingleInstanceMeasure) itr.next();

		    generator.converterDerivedSingleInstanceMeasureOWL(element, bpmn20XmlExtracter);
		}
	}
	
}
