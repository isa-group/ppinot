package es.us.isa.bpmn.owl.converter;

/**
 * @autor Ana Belen Sanchez Jerez
 * **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;
import es.us.isa.ppinot.bpmnppinot_xml_owl.xmlExtracter.ppinotBpmn20.Bpmn20XmlExtracter;

public class UtilsObjectOWLBpmn {
	
	/** Devuelve el tipo de la Actividad**/
	@SuppressWarnings("rawtypes")
	public static String[] getNameTypeActivity(String idActivity, JAXBElement jaxbElement) throws Exception{
		
		Bpmn20XmlExtracter.getBpmn20elements(jaxbElement);
		List <TTask> taskList = Bpmn20XmlExtracter.taskList;
		Iterator<TTask> it = taskList.iterator();
		List <TSubProcess> subprocessList = Bpmn20XmlExtracter.subProcessList;
		Iterator<TSubProcess> itsub = subprocessList.iterator();
		List <TDataObject> dataObjectList = Bpmn20XmlExtracter.dataObjectList;
		Iterator<TDataObject> itObj = dataObjectList.iterator();
		List <TStartEvent> startEventList = Bpmn20XmlExtracter.startEventList;
		Iterator<TStartEvent> itstart = startEventList.iterator();
		List <TEndEvent> endEventList = Bpmn20XmlExtracter.endEventList;
		Iterator<TEndEvent> itend = endEventList.iterator();
		
		Boolean enc = false;
		String nameString = null;
		String type = null;
		while (it.hasNext() && !enc) {
			TTask tTask = (TTask) it.next();
			//System.out.println("taskid "+tTask.getId()+ " idComparador"+idActivity);
			if(tTask.getId()== idActivity){
				nameString = tTask.getName();
				type = "Activity";
				enc = true;
			}
		}
		while (itsub.hasNext() && !enc) {
			TSubProcess tsubprocess = (TSubProcess) itsub.next();
			//System.out.println("taskid "+tTask.getId()+ " idComparador"+idActivity);
			if(tsubprocess.getId()== idActivity){
				nameString = tsubprocess.getName();
				type = "Activity";
				enc = true;
			}
		}
		while (itObj.hasNext() && !enc) {
			TDataObject tdataobject = (TDataObject) itObj.next();
			//System.out.println("taskid "+tTask.getId()+ " idComparador"+idActivity);
			if(tdataobject.getId()== idActivity){
				nameString = tdataobject.getName();
				type = "DataStateChange";
				enc = true;
			}
		}
		while (itstart.hasNext() && !enc) {
			TStartEvent tstart = (TStartEvent) itstart.next();
			//System.out.println("taskid "+tTask.getId()+ " idComparador"+idActivity);
			if(tstart.getId()== idActivity){
				nameString = tstart.getName();
				type = "EventTrigger";
				enc = true;
			}
		}
		while (itend.hasNext() && !enc) {
			TEndEvent tend = (TEndEvent) itend.next();
			//System.out.println("taskid "+tTask.getId()+ " idComparador"+idActivity);
			if(tend.getId()== idActivity){
				nameString = tend.getName();
				type = "EventTrigger";
				enc = true;
			}
		}
		nameString = nameString.replaceAll(" ", "");
		String[] timesInstant = new String[2];
		timesInstant[0] = nameString;
		timesInstant[1]= type;
		return timesInstant; 		
	}
	
	public static String getNameDataObject(String idDataObject){
		System.out.println("idDataObject"+idDataObject);
		List <TDataObject> dataObjectList = Bpmn20XmlExtracter.getDataObjectList();
		Iterator<TDataObject> itObj = dataObjectList.iterator();
		Boolean enc = false;
		String name = null;
		while (itObj.hasNext() && !enc) {
			TDataObject tdataobject = (TDataObject) itObj.next();
			System.out.println("original:"+tdataobject.getId().trim()+ " comparador:"+idDataObject.trim());
			if(tdataobject.getId().trim().equals(idDataObject.trim())){
				name = tdataobject.getName();
				name = name.replaceAll(" ", "");
				enc = true;
			}
		}
		return name;
	}
	
	/**Funcion que dado un nombre de medida extraído del args del main
	 * te obtiene el nombre completo por el que hay que buscar la medida
	 * para obtener las instancias con el razonador**/
	
	public static String getNameImpliedFlowElement(String nameMeasure, String type){
		String nameQuery = "ImpliedBPFlowElement";
		String completedName = nameMeasure;
		if (type.equals("countAggregatedMeasure")){
			completedName = completedName + "Intermediate1";
		
		}else if(type.equals("timeAggregatedMeasure")){
			completedName = completedName + "Intermediate1";
		
		}else if(type.equals("dataAggregatedMeasure")){
			completedName = completedName + "Intermediate1";
		
		}else if(type.equals("dataConditionAggregatedMeasure")){
			completedName = completedName + "Intermediate1";
		
		}else if(type.equals("elementConditionAggregatedMeasure")){
			completedName = completedName + "Intermediate1";
		
		}else if(type.equals("derivedProcessMeasure")){
			completedName = completedName + "Intermediate1";
		
		}
		//Para el resto de medidas de instancia es igual pero sin el Intermediate1
		completedName = completedName+nameQuery;
		return completedName;
	}
	
	/**Funcion para obtener los directly precedes de los elementos del modelo bpmn **/
	public static List<String> getDirectlyPrecedes(List<TSequenceFlow> sequenceFlowList, Object task) {
		Iterator<?> itr = sequenceFlowList.iterator(); 
		List<String> targetList = new ArrayList<String>();
		
		while(itr.hasNext()) {
			TSequenceFlow element = (TSequenceFlow) itr.next();
			String nameTarget = null;
			if(element.getSourceRef().equals(task)){
				Object obj =element.getTargetRef();
				System.out.println("SEQUENCEFLOW:"+obj);
				
				if(obj instanceof TSubProcess){
					TSubProcess SpecificElement = (TSubProcess) obj;
					nameTarget = SpecificElement.getName();
					nameTarget = nameTarget.replaceAll(" ", "");
				}else if(obj instanceof TExclusiveGateway){
					TExclusiveGateway SpecificElement = (TExclusiveGateway) obj;
					nameTarget = SpecificElement.getName();
					nameTarget = nameTarget.replaceAll(" ", "");
				}else if(obj instanceof TEndEvent){
					TEndEvent SpecificElement = (TEndEvent) obj;
					nameTarget = SpecificElement.getName();
					nameTarget = nameTarget.replaceAll(" ", "");
				}else if(obj instanceof TTask){
					TTask SpecificElement = (TTask) obj;
					nameTarget = SpecificElement.getName();
					nameTarget = nameTarget.replaceAll(" ", "");
				}	
				targetList.add(nameTarget);
			}
		}
		
		return targetList;
	}
	
	/**Funcion que dado un nombre de medida extraído del args del main
	 * te obtiene el nombre completo por el que hay que buscar la query
	 * de la medida para que te devuelva aquellas medidas de las que depende
	 * directa e indirectamente tras llamar al analizadors**/
	
	public static String getNameQueriesDirectlyDependOn(String nameMeasure){
		String nameQuery = "DirectlyDependOn";
		String completedName = nameMeasure + nameQuery;
		return completedName;
	}
	
	public static String getNameQueriesIndirectlyDependOn(String nameMeasure){
		String nameQuery = "IndirectlyDependOn";
		String completedName = nameMeasure + nameQuery;
		return completedName;
	}
	
	public static String getCleanName(String name) {
		
		return name.replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "");
	}
}

