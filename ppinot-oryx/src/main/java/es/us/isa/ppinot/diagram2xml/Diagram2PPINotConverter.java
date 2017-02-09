package es.us.isa.ppinot.diagram2xml;

import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.model.extension.PropertyListItem;
import de.hpi.bpmn2_0.transformation.Constants;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import es.us.isa.ppinot.xml.*;

import org.json.JSONException;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.generic.GenericDiagram;
import org.oryxeditor.server.diagram.generic.GenericEdge;
import org.oryxeditor.server.diagram.generic.GenericShape;

import javax.xml.bind.JAXBElement;

import java.util.*;

/**
 * Diagram2PPINotConverter
 *
 * @author resinas
 */
public class Diagram2PPINotConverter {

    public static Constants getConstants() {
        return new Constants() {
            @Override
            public List<Class<? extends AbstractBpmnFactory>> getAdditionalFactoryClasses() {
                return new ArrayList<Class<? extends AbstractBpmnFactory>>();
            }

            @Override
            public List<Class<? extends PropertyListItem>> getAdditionalPropertyItemClasses() {
                return new ArrayList<Class<? extends PropertyListItem>>();
            }

            @Override
            public Map<String, String> getCustomNamespacePrefixMappings() {
                Map<String, String> n = new HashMap<String, String>();
                n.put("http://www.isa.us.es/ppinot", "ppinot");

                return n;
            }
        };
    }

    private final ProcessManager handler;
    private GenericDiagram<?, ?> diagram;

    private Map<String, JAXBElement<?>> elementsById;
    private Map<String, GenericShape<?, ?>> shapesById;

    private PPINotFactory factory;
    private Map<String, TPpiset> ppisets;
    private Map<String, TMeasureConnector> connectorBySource;
    private Map<String, String> processByMeasure;

    private Map<String, String> aggregatedIdMapper;

    public Diagram2PPINotConverter(BasicDiagram diagram, Diagram2BpmnConverter bpmnConverter, PPINotFactory factory) {
        
    	this.diagram = diagram;
        this.handler = new BpmnConverterProcess(bpmnConverter);
        this.factory = factory;

        this.elementsById = new HashMap<String, JAXBElement<?>>();
        this.shapesById = new HashMap<String, GenericShape<?, ?>>();
        this.ppisets = new HashMap<String, TPpiset>();
        this.connectorBySource = new HashMap<String, TMeasureConnector>();
        this.processByMeasure = new HashMap<String, String>();
        this.aggregatedIdMapper = new HashMap<String, String>();
    }

    public void transform() throws JSONException {
    	//AL QUITAR EL PRINT, QUITAR EL "throws JSONException" QUE SE AGREGÓ POR OBLIGACIÓN
    	System.out.println("DIAGRAM: IN:" + diagram.getNumIncomings() + " - OUT:" + diagram.getNumOutgoings() + " - ChildShapes:" + diagram.getNumChildShapes() + " - \n   JSON:" + diagram.getJSON().toString());
        createElementsRecursively(diagram);
        linkElements();
        createPPISets();
        addMeasuresToPPISet();
        addPPITOPPISet();

        handler.addPPISet(ppisets);
    }

    private void createElementsRecursively(GenericShape<?, ?> shape) {
    	
        JAXBElement<?> elem = factory.createElement(shape);
                
        if (elem != null) {
            elementsById.put(shape.getResourceId(), elem);
            shapesById.put(shape.getResourceId(), shape);
        }

        for (GenericShape<?, ?> s : shape.getChildShapesReadOnly()) {
            createElementsRecursively(s);
        }
    }

    private void linkElements() {
        for (JAXBElement<?> elem : getAllElements()) {
            if (isConnector(elem)) {
                linkConnector((TMeasureConnector) elem.getValue());
            } else if (isPPI(elem)) {
                linkPPI((TPpi) elem.getValue());
            }
        }
    }

    private void createPPISets() {
        for (String id : handler.getProcessIds()) {
            ppisets.put(id, new TPpiset());
        }
    }



    private void addMeasuresToPPISet() {
    	
    	System.out.println("->> addMeasuresToPPISet - FOR::");
    	
        for (JAXBElement<?> elem : getAllElements()) {
        	
            if (isConnector(elem)) {
            	
            	System.out.println("ENCONTRADO OBJETO CONECTOR: " + elem.getName() + "--" + elem.getClass().getName());
            	
                TMeasureConnector conn = (TMeasureConnector) elem.getValue();
                System.out.println("   ELEM(Value, name) = " +  elem.getValue() + ", " + elem.getName());
                System.out.println("   CONN(ClassName, source, target) = " +  conn.getClass().getName() + ", " + conn.getSourceRef().toString() + ", " + conn.getTargetRef().toString());
                
                String processId = getProcessOf(getTargetRef(conn));
                TPpiset ppiset = ppisets.get(processId);

                if (ppiset != null) {

               		ppiset.getMeasureConnector().add((JAXBElement<? extends TMeasureConnector>) elem);
                	
                	System.out.println("-> Antes de convertir la medida: ");
                	TMeasure tmPrueba = (TMeasure) getSourceRef(conn);
                	System.out.println("   Valor de la medida convertida: " + tmPrueba.getClass().getName());
                	
                    String measureId = ((TMeasure) getSourceRef(conn)).getId();
                    
                    System.out.println("   measureId(String): " +  measureId);
                    if (!processByMeasure.containsKey(measureId)) {
                        JAXBElement<? extends TMeasure> jaxbElement = (JAXBElement<? extends TMeasure>) getJaxbElement(measureId);
                        ppiset.getMeasure().add(jaxbElement);
                        processByMeasure.put(measureId, processId);
                        processByMeasure.put(jaxbElement.getValue().getId(), processId);
                    }
                }
            }else{
            	System.out.println("ENCONTRADO OBJETO QUE NO ES CONECTOR: " + elem.getName() + "--" + elem.getClass().getName());
            }
        }
    }

    private void addPPITOPPISet() {
        for (JAXBElement<?> elem : getAllElements()) {
            if (isPPI(elem)) {
                TPpi ppi = (TPpi) elem.getValue();
                TMeasure measure = (TMeasure) ppi.getMeasuredBy();
                String processId = processByMeasure.get(measure.getId());
                TPpiset ppiset = ppisets.get(processId);
                if (ppiset != null) {
                    ppiset.getPpi().add(ppi);
                }
            }
        }
    }

    private String getProcessOf(Object targetRef) {
        String id;
        if (targetRef instanceof TMeasure) {
            String targetId = ((TMeasure) targetRef).getId();
            TMeasureConnector connector = connectorBySource.get(targetId);
            id = getProcessOf(getTargetRef(connector));
        } else {
            id = handler.getProcessIdOfElement(targetRef);
        }

        return id;
    }

    private Collection<JAXBElement<?>> getAllElements() {   	
        return elementsById.values();
    }

    private Object getElement(String id) {
        Object elem;
        JAXBElement<?> jaxbElement = getJaxbElement(id);

        if (jaxbElement != null)
            elem = jaxbElement.getValue();
        else
            elem = handler.getProcessElement(id);

        return elem;
    }

    private JAXBElement<?> getJaxbElement(String id) {
        return aggregatedIdMapper.containsKey(id) ?
                    elementsById.get(aggregatedIdMapper.get(id)) : elementsById.get(id);
    }

    private void linkConnector(TMeasureConnector connector) {
        GenericEdge<?, ?> edge = (GenericEdge<?, ?>) shapesById.get(connector.getId());
        String sourceId = edge.getSource().getResourceId();
        String targetId = edge.getTarget().getResourceId();

        Object source = null;
        Object target = null;

        if (connector instanceof TUses ||
                connector instanceof TIsGroupedBy ||
                connector instanceof TAggregates) {
            source = getElement(sourceId);
            target = getElement(targetId);
            
            //CODIGO AGREGADO POR BESTRADA - PRUEBA
            if(connector instanceof TUses){
            	System.out.println("CONNECTOR: "+connector.getClass().getName()+"SOURCE: " + source.getClass().getName() + " TARGET: " + target.getClass().getName() );
            }
            
        } else {
            source = getRef(getElement(sourceId));
            target = getRef(getElement(targetId));
        }

        connector.setSourceRef(source);
        connector.setTargetRef(target);

        sourceId = updateSourceIdForAggregatedMeasures(sourceId, source);

        connectorBySource.put(sourceId, connector);
    }

    private String updateSourceIdForAggregatedMeasures(String sourceId, Object source) {
        if (source instanceof TMeasure && ! ((TMeasure) source).getId().equals(sourceId)) {
            String id = ((TMeasure) source).getId();
            aggregatedIdMapper.put(id, sourceId);
            sourceId = id;
        }
        return sourceId;
    }

    private void linkPPI(TPpi ppi) {
        GenericShape<?, ?> shape = shapesById.get(ppi.getId());
        String measuredBy = getMeasuredBy(shape);
        ppi.setMeasuredBy(getElement(measuredBy));
    }

    private String getMeasuredBy(GenericShape<?, ?> shape) {
        String measuredBy = "";
        List<? extends GenericShape<?, ?>> childs = shape.getChildShapesReadOnly();
        if (childs.size() == 1) {
            measuredBy = childs.get(0).getResourceId();
        } else if (childs.size() > 1) {
            Iterator<? extends GenericShape<?, ?>> it = childs.iterator();
            GenericShape<?, ?> current = it.next();
            int incoming = current.getNumIncomings();
            measuredBy = current.getResourceId();

            while (it.hasNext()) {
                current = it.next();
                if (current.getNumIncomings() < incoming) {
                    measuredBy = current.getResourceId();
                    incoming = current.getNumIncomings();
                }
            }
        }
        return measuredBy;
    }


    private boolean isPPI(JAXBElement<?> elem) {
        return TPpi.class.isAssignableFrom(elem.getDeclaredType());
    }

    private boolean isConnector(JAXBElement<?> elem) {
        return TMeasureConnector.class.isAssignableFrom(elem.getDeclaredType());
    }

    private Object getSourceRef(TMeasureConnector connector) {
        Object sourceRef = connector.getSourceRef();
        return getRef(sourceRef);
    }

    private Object getTargetRef(TMeasureConnector connector) {
        return getRef(connector.getTargetRef());
    }

    private Object getRef(Object ref) {
    	
    	System.out.println("   Objeto conexión: ClassName:"+ (ref.getClass().getName()) + " && String:"+ ref.toString()+"&& HasCode:"+ref.hashCode());
    	
        if (ref != null && ref instanceof TAggregatedMeasure) {
            ref = getBaseMeasure((TAggregatedMeasure) ref);
        }
        return ref;
    }

    private TMeasure getBaseMeasure(TAggregatedMeasure ref) {
        TMeasure baseMeasure = ref.getCountMeasure();
        baseMeasure = ref.getDataMeasure() != null ? ref.getDataMeasure() : baseMeasure;
        baseMeasure = ref.getTimeMeasure() != null ? ref.getTimeMeasure() : baseMeasure;
        baseMeasure = ref.getStateConditionMeasure() != null ? ref.getStateConditionMeasure() : baseMeasure;
        baseMeasure = ref.getDataPropertyConditionMeasure() != null ? ref.getDataPropertyConditionMeasure() : baseMeasure;
        baseMeasure = ref.getDerivedSingleInstanceMeasure() != null ? ref.getDerivedSingleInstanceMeasure() : baseMeasure;
        //baseMeasure = ref.getMetric() != null ? ref.getMetric() : baseMeasure; 
        
        return baseMeasure;
    }

}
