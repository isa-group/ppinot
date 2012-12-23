package es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.HermiT.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;


public class Analyser {

	static String fileOWLPpinot;
	static String fileOWLBpmn;
	static String orgppinot_extra;
	
	static OWLOntologyManager manager;
	static OWLReasoner reasoner;
	static OWLDataFactory fac;
	static OWLObjectRenderer renderer;
	
	
	/**
	 * Analyser
	 * 
	 * This contructor creates a Hermit reasoner.
	 * @author Ana Belen Sanchez Jerez
	 * @param f : File that contains the owl code.
	 * 
	 * @throws OWLOntologyCreationException
	 * */
	
	public Analyser(String orgbpmn, String orgppinot, String orgppinot_extraIn, String bpmnFileName, String ppinotFileName){
		
		manager = OWLManager.createOWLOntologyManager();
		orgppinot_extra = orgppinot_extraIn;
		
         // We load the ontology from a document - our IRI points to it directly
		try {
			
			File bpmnfile = new File(bpmnFileName);
			bpmnfile.setExecutable(true);
			bpmnfile.setReadable(true);
			bpmnfile.setWritable(true);
			
			File ppinotfile = new File(ppinotFileName);
			ppinotfile.setExecutable(true);
			ppinotfile.setReadable(true);
			ppinotfile.setWritable(true);

			fileOWLBpmn = bpmnfile.toURI().toString();
			fileOWLPpinot = ppinotfile.toURI().toString();
			
			OWLOntology ppinotExprOnt = manager.loadOntologyFromOntologyDocument( IRI.create(ppinotfile.toURI()) );
/*
			manager.addAxioms(ppinotExprOnt, manager.loadOntologyFromOntologyDocument( IRI.create(orgbpmn) ).getAxioms());
			manager.addAxioms(ppinotExprOnt, manager.loadOntologyFromOntologyDocument( IRI.create(orgppinot) ).getAxioms());
			manager.addAxioms(ppinotExprOnt, manager.loadOntologyFromOntologyDocument( IRI.create(orgppinot_extra) ).getAxioms());
			manager.addAxioms(ppinotExprOnt, manager.loadOntologyFromOntologyDocument( IRI.create(bpmnfile.toURI()) ).getAxioms());
*/
			
			OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
	        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
System.out.println("Clase Analyser 1 manager"+manager);

	        reasoner = reasonerFactory.createReasoner(ppinotExprOnt, config);
	        reasoner.precomputeInferences();
	        fac = manager.getOWLDataFactory();
System.out.println("Clase Analyser 1 fac"+fac);

	        renderer = new DLSyntaxObjectRenderer();
	        
		} catch (OWLOntologyCreationException e) {
System.out.println("Clase Analyser 2 e"+e);
			e.printStackTrace();
		}

        
	}
	
	
	/**
	 * analyze
	 * This method applies the reasoner to the previously generated owlcode to reason about it.
	 * 
	 * @param orgisa : String with the Ontology base url
	 * @param taskName : String with de name of the task Assignment
	 * 
	 * */
	
	public ArrayList<String> analyze(String measureName) {
		
		ArrayList<String> aindividuals = new ArrayList<String>();
		OWLClass measure = fac.getOWLClass(IRI.create(fileOWLPpinot+"#"+measureName));
        // Ask the reasoner for the instances of taskName
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(measure, false);
        // The reasoner returns a NodeSet containing individuals.
       
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        
        for(OWLNamedIndividual ind : individuals) {
        	aindividuals.add(renderer.render(ind)) ;
        }
       
        return aindividuals;
	}
	
	//devuelve las medidas relacionada con una actividad

	public ArrayList<String> analyzeActivityMeasures(String ActivityName) {
		ActivityName = ActivityName.replaceAll(" ", "");
		
		String [] classesIntermedia;
		OWLNamedIndividual activityIndividual = fac.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+ActivityName));
System.out.println("PluginNuevo-claseOWL "+activityIndividual);

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(activityIndividual, false);

/* prueba 
//---------------------------------------------------------------------        
        
        OWLObjectPropertyExpression dependsDirectlyOn = fac.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLNamedIndividual Activity = fac.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+ActivityName));
        OWLClassExpression Hasvalue= fac.getOWLObjectHasValue(dependsDirectlyOn, Activity);

        NodeSet<OWLNamedIndividual> x = reasoner.getInstances(Hasvalue, true);
//---------------------------------------------------------------------
*/
        // The reasoner returns a NodeSet containing individuals.
        Set<OWLClass> classesIndividuals = classesNodeSet.getFlattened();
        int sizeaind = classesIndividuals.size();
        
        classesIntermedia = new String [sizeaind];
        int i = 0;
        for(OWLClass ind : classesIndividuals) {
        	System.out.println("PluginNuevo-clase medida"+ind);
        	classesIntermedia[i] = renderer.render(ind);
            i++;
        }
       
        //Now, we choose only measures whose name is "...ImpliedBPFlowElement" from all individuals.
        ArrayList<String> classesList = new ArrayList<String>();

        for(String indiv : classesIntermedia) {
			System.out.println(" PluginNuevo-BucleFinal-clase medida"+indiv);
        	
        	if(indiv.endsWith("ImpliedBPFlowElement")){
        		
        		String[] parts = indiv.split("ImpliedBPFlowElement",2);
        		System.out.println(" PluginNuevo-BucleFinal-split0"+parts[0]);
        		String nombreMed =parts[0];
        		if(nombreMed.endsWith("Intermediate1")){ //Porque aparece tambien la medida que contiene
        			String[] auxiliar = nombreMed.split("Intermediate1",2);
        			nombreMed = auxiliar[0];
        		}
        		if(!classesList.contains(nombreMed)){
    				classesList.add(nombreMed);
    			}
        	}
        }
		
		//Pasamos al formato de return
       
    return classesList;
	}


	public ArrayList<String> analyzeRelationshipMeasures(String nameElement) {
		
		ArrayList<String> aindividuals = new ArrayList<String>();
		String completedNameMeasure = UtilsObjectOWL.getNameQueriesDirectlyDependOn(nameElement);
		OWLClass measureDirectly = fac.getOWLClass(IRI.create(fileOWLPpinot+"#"+completedNameMeasure));
System.out.println("Mostrando la query a ejecutar: "+measureDirectly);

		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(measureDirectly, false);
        // The reasoner returns a NodeSet containing individuals.
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        
        String completedNameMeasure2 = UtilsObjectOWL.getNameQueriesIndirectlyDependOn(nameElement);
		OWLClass measureIndirectly = fac.getOWLClass(IRI.create(fileOWLPpinot+"#"+completedNameMeasure2));
System.out.println("Mostrando la query a ejecutar: "+measureIndirectly);

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLNamedIndividual> individuals2NodeSet = reasoner.getInstances(measureIndirectly, false);
        // The reasoner returns a NodeSet containing individuals.
       
        Set<OWLNamedIndividual> individuals2 = individuals2NodeSet.getFlattened();
        
        individuals.addAll(individuals2);
        
        for(OWLNamedIndividual ind : individuals) {
        	String[] sinInter = renderer.render(ind).split("Intermediate1", 2);
        	aindividuals.add(sinInter[0]);
        }
 		
        return aindividuals;
	}

}
