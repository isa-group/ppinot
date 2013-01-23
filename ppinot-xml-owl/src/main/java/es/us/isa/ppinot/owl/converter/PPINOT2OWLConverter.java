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
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.owl.notation.Vocabulary;

public class PPINOT2OWLConverter extends ToOWLConverter implements PPINOT2OWLConverterInterface {
	 
	private String bpmnOntologyURI;
	private String bpmnGeneratedOntologyURI;
	private Bpmn20ModelHandlerInterface bpmn20ModelHandler;
	
	private GeneratePpinotAxioms generator;
	
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

			this.getDeclarationIndividualsAggregatedMeasure( ppiNotModelHandler.getCountAggregatedModelMap());
			this.getDeclarationIndividualsAggregatedMeasure( ppiNotModelHandler.getTimeAggregatedModelMap());
			this.getDeclarationIndividualsAggregatedMeasure( ppiNotModelHandler.getStateConditionAggregatedModelMap());
			this.getDeclarationIndividualsAggregatedMeasure( ppiNotModelHandler.getDataPropertyConditionAggregatedModelMap());
			this.getDeclarationIndividualsAggregatedMeasure( ppiNotModelHandler.getDataAggregatedModelMap());
			this.getDeclarationIndividualsAggregatedMeasure( ppiNotModelHandler.getDerivedSingleInstanceAggregatedModelMap());
	
			this.getDeclarationIndividualsDerivedMeasure( ppiNotModelHandler.getDerivedMultiInstanceModelMap());
			this.getDeclarationIndividualsDerivedMeasure( ppiNotModelHandler.getDerivedSingleInstanceModelMap());
			
			this.getDeclarationIndividualsPpiMeasure( ppiNotModelHandler.getPpiModelMap());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
    public void setBpmnData(String bpmnGeneratedOntologyURI, Bpmn20ModelHandlerInterface bpmn20ModelHandler) {

    	this.bpmnOntologyURI = es.us.isa.bpmn.owl.notation.Vocabulary.URI;
    	this.bpmnGeneratedOntologyURI = bpmnGeneratedOntologyURI;
    	this.bpmn20ModelHandler = bpmn20ModelHandler;
    }
    
//***********************************************************************************/
    
	private void getDeclarationIndividualsCountInstanceMeasure(Map<String, CountInstanceMeasure> modelMap) throws Exception {

		Iterator<Entry<String, CountInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, CountInstanceMeasure> pairs = (Map.Entry<String, CountInstanceMeasure>)itInst.next();
	        CountInstanceMeasure element = pairs.getValue();
			
		    generator.converterCountInstanceMeasureOWL(element, bpmn20ModelHandler);
		}
	}
	
	private void getDeclarationIndividualsTimeInstanceMeasure(Map<String, TimeInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, TimeInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TimeInstanceMeasure> pairs = (Map.Entry<String, TimeInstanceMeasure>)itInst.next();
	        TimeInstanceMeasure element = pairs.getValue();
			
			generator.converterTimeInstanceMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	private void getDeclarationIndividualsStateConditionInstanceMeasure(Map<String, StateConditionInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, StateConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, StateConditionInstanceMeasure> pairs = (Map.Entry<String, StateConditionInstanceMeasure>)itInst.next();
	        StateConditionInstanceMeasure element = pairs.getValue();
			
		    generator.converterStateConditionInstanceMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	private void getDeclarationIndividualsDataPropertyConditionInstanceMeasure(Map<String, DataPropertyConditionInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DataPropertyConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst.next();
	        DataPropertyConditionInstanceMeasure element = pairs.getValue();
	
			generator.converterDataPropertyConditionInstanceMeasureOWL(element);
		}	
	}
	
	private void getDeclarationIndividualsDataInstanceMeasure(Map<String, DataInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DataInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
	        DataInstanceMeasure element = pairs.getValue();
			
		    generator.converterDataInstanceMeasureOWL(element);
		}	
	}

//***********************************************************************************/
//***********************************************************************************/
	
	private void getDeclarationIndividualsAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterAggregatedMeasureOWL(element, bpmn20ModelHandler);
		}
		
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	private void getDeclarationIndividualsDerivedMeasure(Map<String, DerivedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
	        DerivedMeasure element = pairs.getValue();

		    generator.converterDerivedMeasureOWL(element, bpmn20ModelHandler);
		}
	}

//**********************************************************************/
//**********************************************************************/
//**********************************************************************/
//**********************************************************************/
	
	private void getDeclarationIndividualsPpiMeasure(Map<String, PPI> modelMap) throws Exception {

		Iterator<Entry<String, PPI>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI element = pairs.getValue();

		    generator.converterPpiOWL(element, bpmn20ModelHandler);
		}
	}
}
