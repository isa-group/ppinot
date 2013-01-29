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

/**
 * Clases que convierten a owl, a partir de los objetos del modelo en un ModelHandleInterface para PPINOT
 * 
 * @author Edelia
 * 
 */
public class PPINOT2OWLConverter extends ToOWLConverter implements PPINOT2OWLConverterInterface {
	 
	// URI de la ontología base BPMN 2.0
	private String bpmnOntologyURI;
	// URI de la ontología BPMN relacionada con el proceso
	private String bpmnGeneratedOntologyURI;
	// Objeto que maneja el modelo con la información del BPMN relacionado con el proceso
	private Bpmn20ModelHandlerInterface bpmn20ModelHandler;
	
	// objeto mediante el cual se generan los axiomas que se adicionan a la ontología creada
	private GeneratePpinotAxioms generator;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param baseIRI IRI de la ontologia creada
	 * @param manager OWLOntologyManager utilizado
	 */
	public PPINOT2OWLConverter(String baseIRI, OWLOntologyManager manager) {
		
		super( baseIRI, manager);
	}
	
	/**
	 * Ejecuta las operaciones propias de cada subclase para generar la ontología a partir de un ModelHandleInterface
	 * 
	 * @param modelHandler Objeto ModelHandleInterface a partir del cual se genera la ontología
	 * @throws OWLOntologyCreationException
	 */
    @Override
	protected void generateOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException {
    	
    	// adiciona las declaraciones que indican las ontologías importadas en la ontología generada
    	String[] uris = { bpmnOntologyURI, Vocabulary.URI, bpmnGeneratedOntologyURI };
    	this.addOntologyImports(uris);
    	
    	// crea el objeto mediante el cual se generan los axiomas que se adicionan a la ontología creada
		generator = new GeneratePpinotAxioms(
				this.getManager().getOWLDataFactory(), this.getManager(), this.getOntology(), 
				bpmnOntologyURI, bpmnGeneratedOntologyURI, this.getOntologyURI());
		
    	// inicializaciones
    	PpiNotModelHandler ppiNotModelHandler = (PpiNotModelHandler) modelHandler;

		try {
			
			// adiciona a la ontología cada uno de los tipos de elementos PPINOT
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
	
	/**
	 * Da valor a propiedades de la clase que tienen información sobre el BPMN relacionado con el mismo proceso
	 * 
	 * @param bpmnGeneratedOntologyURI URI de la ontología BPMN relacionada con el proceso
	 * @param bpmn20ModelHandler Objeto que maneja el modelo con la información del BPMN relacionado con el proceso
	 */
    public void setBpmnData(String bpmnGeneratedOntologyURI, Bpmn20ModelHandlerInterface bpmn20ModelHandler) {

    	this.bpmnOntologyURI = es.us.isa.bpmn.owl.notation.Vocabulary.URI;
    	this.bpmnGeneratedOntologyURI = bpmnGeneratedOntologyURI;
    	this.bpmn20ModelHandler = bpmn20ModelHandler;
    }
    
	/**
	 * Adiciona a la ontología las medidas CountInstanceMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
    private void getDeclarationIndividualsCountInstanceMeasure(Map<String, CountInstanceMeasure> modelMap) throws Exception {

		Iterator<Entry<String, CountInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, CountInstanceMeasure> pairs = (Map.Entry<String, CountInstanceMeasure>)itInst.next();
	        CountInstanceMeasure element = pairs.getValue();
			
		    generator.converterCountInstanceMeasureOWL(element, bpmn20ModelHandler);
		}
	}
	
	/**
	 * Adiciona a la ontología las medidas TimeInstanceMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsTimeInstanceMeasure(Map<String, TimeInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, TimeInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TimeInstanceMeasure> pairs = (Map.Entry<String, TimeInstanceMeasure>)itInst.next();
	        TimeInstanceMeasure element = pairs.getValue();
			
			generator.converterTimeInstanceMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	/**
	 * Adiciona a la ontología las medidas StateConditionInstanceMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsStateConditionInstanceMeasure(Map<String, StateConditionInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, StateConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, StateConditionInstanceMeasure> pairs = (Map.Entry<String, StateConditionInstanceMeasure>)itInst.next();
	        StateConditionInstanceMeasure element = pairs.getValue();
			
		    generator.converterStateConditionInstanceMeasureOWL(element, bpmn20ModelHandler);
		}	
	}
	
	/**
	 * Adiciona a la ontología las medidas DataPropertyConditionInstanceMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsDataPropertyConditionInstanceMeasure(Map<String, DataPropertyConditionInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DataPropertyConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst.next();
	        DataPropertyConditionInstanceMeasure element = pairs.getValue();
	
			generator.converterDataPropertyConditionInstanceMeasureOWL(element);
		}	
	}
	
	/**
	 * Adiciona a la ontología las medidas DataInstanceMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsDataInstanceMeasure(Map<String, DataInstanceMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DataInstanceMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
	        DataInstanceMeasure element = pairs.getValue();
			
		    generator.converterDataInstanceMeasureOWL(element);
		}	
	}
	
	/**
	 * Adiciona a la ontología las medidas AggregatedMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsAggregatedMeasure(Map<String, AggregatedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
	        AggregatedMeasure element = pairs.getValue();
			
			generator.converterAggregatedMeasureOWL(element, bpmn20ModelHandler);
		}
		
	}
	
	/**
	 * Adiciona a la ontología las medidas DerivedMeasure
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsDerivedMeasure(Map<String, DerivedMeasure> modelMap) throws Exception {
		
		Iterator<Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
	        DerivedMeasure element = pairs.getValue();

		    generator.converterDerivedMeasureOWL(element, bpmn20ModelHandler);
		}
	}
	
	/**
	 * Adiciona a la ontología los Ppi
	 * 
	 * @param modelMap Map de las medidas
	 * @throws Exception
	 */
	private void getDeclarationIndividualsPpiMeasure(Map<String, PPI> modelMap) throws Exception {

		Iterator<Entry<String, PPI>> itInst = modelMap.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI element = pairs.getValue();

		    generator.converterPpiOWL(element, bpmn20ModelHandler);
		}
	}
}
