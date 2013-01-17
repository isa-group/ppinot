package es.us.isa.ppinot.owl.converter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.handler.ModelHandleInterface;
import es.us.isa.bpmn.owl.converter.ToOWLConverter;
import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.owl.notation.Vocabulary;

/**
 * @author Ana Belen Sanchez Jerez
 * **/
public class PPINOT2OWLConverter extends ToOWLConverter implements PPINOT2OWLConverterInterface {
	 
	private String bpmnOntologyURI;
	private String bpmnGeneratedOntologyURI;
	private Bpmn20ModelHandlerInterface bpmn20ModelHandler;
	
	private GeneratePpinotAxioms generator;
	
	/**Constructor de la clase inObjectsOutOWL que inicializa todos los objetos
	 * necesarios de la api OWL para poder trabajar con ellos. Inicializa 
	 * OWLManager, OWLOntology,OWLDataFactory, las urls a ficheros owl. **/
	public PPINOT2OWLConverter(String baseIRI, OWLOntologyManager manager) {
		
		super( baseIRI, manager);
	}
	
    @Override
	protected void generateOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException {
    	
    	String[] uris = { bpmnOntologyURI, Vocabulary.URI, bpmnGeneratedOntologyURI };
    	this.addOntologyImports(uris);
    	
		generator = new GeneratePpinotAxioms(
				this.getManager().getOWLDataFactory(), this.getManager(), this.getOntology(), 
				bpmnOntologyURI, bpmnGeneratedOntologyURI, this.getOntologyURI());
		
    	PpiNotModelHandler ppiNotModelHandler = (PpiNotModelHandler) modelHandler;

		try {
			
			this.getDeclarationIndividualsCountInstanceMeasure( ppiNotModelHandler.getCountInstanceModelMap());

			this.getDeclarationIndividualsTimeInstanceMeasure( ppiNotModelHandler.getTimeInstanceModelMap());
			this.getDeclarationIndividualsStateConditionInstanceMeasure( ppiNotModelHandler.getStateConditionInstanceModelMap());
			this.getDeclarationIndividualsDataPropertyConditionInstanceMeasure( ppiNotModelHandler.getDataPropertyConditionInstanceModelMap());
			this.getDeclarationIndividualsDataInstanceMeasure( ppiNotModelHandler.getDataInstanceModelMap() );
			
			this.getDeclarationIndividualsCountAggregatedMeasure( ppiNotModelHandler.getCountAggregatedModelMap());
			this.getDeclarationIndividualsTimeAggregatedMeasure( ppiNotModelHandler.getTimeAggregatedModelMap());
			this.getDeclarationIndividualsStateConditionAggregatedMeasure( ppiNotModelHandler.getStateConditionAggregatedModelMap());
			this.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure( ppiNotModelHandler.getDataPropertyConditionAggregatedModelMap());
			this.getDeclarationIndividualsDataAggregatedMeasure( ppiNotModelHandler.getDataAggregatedModelMap());
			this.getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure( ppiNotModelHandler.getDerivedSingleInstanceAggregatedModelMap());
	
			this.getDeclarationIndividualsDerivedMultiInstanceMeasure( ppiNotModelHandler.getDerivedMultiInstanceModelMap());
			this.getDeclarationIndividualsDerivedSingleInstanceMeasure( ppiNotModelHandler.getDerivedSingleInstanceModelMap());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
    public void setBpmnData(String bpmnGeneratedOntologyURI, Bpmn20ModelHandlerInterface bpmn20ModelHandler) {

    	this.bpmnOntologyURI = es.us.isa.bpmn.owl.notation.Vocabulary.URI;
    	this.bpmnGeneratedOntologyURI = bpmnGeneratedOntologyURI;
    	this.bpmn20ModelHandler = bpmn20ModelHandler;
    }
    
	/***Declaraciones en owl de la medida countInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsCountInstanceMeasure(Map<String, CountInstanceMeasure> modelMap) throws Exception {

		Iterator<Entry<String, CountInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, CountInstanceMeasure> pairs = (Map.Entry<String, CountInstanceMeasure>)itInst.next();
	        CountInstanceMeasure element = pairs.getValue();
			
		    generator.converterCountInstanceMeasureOWL(element, bpmn20ModelHandler);
		}
	}
	
	/***Declaraciones en owl de la medida timeInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsTimeInstanceMeasure(Map<String, TimeInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, TimeInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TimeInstanceMeasure> pairs = (Map.Entry<String, TimeInstanceMeasure>)itInst.next();
	        TimeInstanceMeasure element = pairs.getValue();
			
			generator.converterTimeInstanceMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	/***Declaraciones en owl de la medida StateConditionInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsStateConditionInstanceMeasure(Map<String, StateConditionInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, StateConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, StateConditionInstanceMeasure> pairs = (Map.Entry<String, StateConditionInstanceMeasure>)itInst.next();
	        StateConditionInstanceMeasure element = pairs.getValue();
			
		    generator.converterStateConditionInstanceMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataPropertyConditionInstanceMeasure(Map<String, DataPropertyConditionInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DataPropertyConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst.next();
	        DataPropertyConditionInstanceMeasure element = pairs.getValue();
	
			generator.converterDataPropertyConditionInstanceMeasureOWL(element);
		}	
	}
	
	/***Declaraciones en owl de la medida DataInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataInstanceMeasure(Map<String, DataInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DataInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
	        DataInstanceMeasure element = pairs.getValue();
			
		    generator.converterDataInstanceMeasureOWL(element);
		}	
	}

	/***Declaraciones en owl de medidas de tipo countAggregatedMeasure 
	 * @param jaxbElement 
	 * @param taskList 
	 * @throws Exception ***/
	
	/***Declaraciones en owl de la medida countAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsCountAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterCountAggregatedMeasureOWL(element, bpmn20ModelHandler);
		}
		
	}

//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida timeAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsTimeAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
	
		    generator.converterTimeAggregatedMeasureOWL(element, bpmn20ModelHandler);
		}
		
	}
	
	
	/***Declaraciones en owl de la medida StateConditionAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsStateConditionAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterStateConditionAggregatedMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterDataPropertyConditionAggregatedMeasureOWL(element);
		}	
	}
	
	/***Declaraciones en owl de la medida DataAggregatedMeasure de PPINOT ***/
	private void getDeclarationIndividualsDataAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterDataAggregatedMeasureOWL(element);
		}	
	}
	
	private void getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterDerivedSingleInstanceAggregatedMeasureOWL(element, bpmn20ModelHandler);
		}	
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida DerivedMultiInstanceMeasure de PPINOT ***/
	private void getDeclarationIndividualsDerivedMultiInstanceMeasure(Map<String, DerivedMultiInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DerivedMultiInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DerivedMultiInstanceMeasure> pairs = (Map.Entry<String, DerivedMultiInstanceMeasure>)itInst.next();
	        DerivedMultiInstanceMeasure element = pairs.getValue();

		    generator.converterDerivedMultiInstanceMeasureOWL(element, bpmn20ModelHandler);
		}
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	private void getDeclarationIndividualsDerivedSingleInstanceMeasure(Map<String, DerivedSingleInstanceMeasure> modelMap) throws Exception {

		Iterator<Entry<String, DerivedSingleInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DerivedSingleInstanceMeasure> pairs = (Map.Entry<String, DerivedSingleInstanceMeasure>)itInst.next();
	        DerivedSingleInstanceMeasure element = pairs.getValue();

		    generator.converterDerivedSingleInstanceMeasureOWL(element, bpmn20ModelHandler);
		}
	}
	
}
