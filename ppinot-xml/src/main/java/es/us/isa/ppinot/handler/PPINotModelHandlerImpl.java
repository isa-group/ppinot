package es.us.isa.ppinot.handler;

import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExtensionElements;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.handler.transformation.DefaultXMLToMeasureDefinitionConverter;
import es.us.isa.ppinot.handler.transformation.model.XMLToMeasureDefinition;
import es.us.isa.ppinot.handler.transformation.model.XMLToPPISet;
import es.us.isa.ppinot.handler.transformation.xml.MeasureDefinitionToXML;
import es.us.isa.ppinot.handler.transformation.xml.PPISetToXML;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.xml.ObjectFactory;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TPpiset;

import javax.xml.bind.JAXBElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Clase que permite exportar e importar a XMLs de PPINOT. 
 *
 * @author resinas
 * @author Edelia
 *
 */
public class PPINotModelHandlerImpl extends Bpmn20ModelHandlerImpl implements PPINotModelHandler {

    private Map<String, PPISet> ppiSetMap;
    private XMLToMeasureDefinition<TMeasure> xmlConverter;
    private MeasureDefinitionToXML<MeasureDefinition> measureConverter;

    /**
	 * Class constructor
	 * 
	 */
    public PPINotModelHandlerImpl() {
        this(new DefaultXMLToMeasureDefinitionConverter(), new DefaultXMLToMeasureDefinitionConverter());
    }

	public PPINotModelHandlerImpl(XMLToMeasureDefinition<TMeasure> xmlConverter, MeasureDefinitionToXML<MeasureDefinition> measureConverter) {
		super();
        this.xmlConverter = xmlConverter;
        this.measureConverter = measureConverter;
        ppiSetMap = new HashMap<String, PPISet>();
    }

    public Collection<PPISet> getPPISets() {
        return ppiSetMap.values();
    }

    public void setPPISets(Collection<PPISet> ppiSets) {
        ppiSetMap = new HashMap<String, PPISet>();
        for (PPISet p : ppiSets) {
            ppiSetMap.put(p.getProcessId(), p);
        }
    }

    public PPISet getPPISet(String processId) {
        return ppiSetMap.get(processId);
    }

    public void setPPISet(PPISet ppiSet) {
        ppiSetMap.put(ppiSet.getProcessId(), ppiSet);
    }

    /**
	 * Returns factory classes that are used while loading XML files
	 * 
	 */
	@Override
	protected Class[] iniLoader() {

        return new Class[]{es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class,
                ObjectFactory.class,
                es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory.class};
    }

    @Override
    protected void beforeSave() {
        PPISetToXML converter = new PPISetToXML(measureConverter, getElementIds(), new ObjectFactory());

        for (String id : ppiSetMap.keySet()) {
            PPISet ppiSet = ppiSetMap.get(id);
            if (ppiSet != null) {
                TPpiset xml = converter.create(ppiSet);
                TProcess p = getProcess(id);
                writeTPPISet(p, xml);
            }
        }
    }

    private void writeTPPISet(TProcess process, TPpiset ppiset) {
        TExtensionElements elems = process.getExtensionElements();
        es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory bpmnFactory = new es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory();

        if (elems == null)
            elems = bpmnFactory.createTExtensionElements();

        removeTPPISet(elems);

        elems.getAny().add(ppiset);

    }

    private void removeTPPISet(TExtensionElements elems) {
        Object toRemove = null;
        for (Object extension : elems.getAny()) {
            if (extension instanceof JAXBElement &&
                    ((JAXBElement) extension).getValue() instanceof TPpiset) {
                toRemove = extension;
                break;
            }
        }
        if (toRemove != null)
            elems.getAny().remove(toRemove);
    }


    /**
     * Generates model instances from JAXB classes by means of XMLToXXX transformers
     * After invoking this method, model instances are available from the corresponding get methods
	 */
	@Override
	protected void afterLoad() {
        super.afterLoad();

        XMLToPPISet ppiSetConverter = new XMLToPPISet(xmlConverter);

        for (TProcess process : readProcesses()) {
            TPpiset tPpiset = readTPPISet(process);
            if (tPpiset != null) {
                PPISet ppiSet = ppiSetConverter.create(tPpiset, process.getId());
                ppiSetMap.put(process.getId(), ppiSet);
            }
        }
    }

    private TPpiset readTPPISet(TProcess process) {
        TPpiset ppiset = null;

        if (process != null &&
                process.getExtensionElements() != null &&
                process.getExtensionElements().getAny() != null) {
            for (Object extension : process.getExtensionElements().getAny()) {
                if (extension instanceof JAXBElement &&
                        ((JAXBElement) extension).getValue() instanceof TPpiset) {
                    ppiset = (TPpiset) ((JAXBElement) extension).getValue();
                    break;
                }
            }
        }

        return ppiset;
    }

}
