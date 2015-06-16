package es.us.isa.ppinot.model.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.ProcessInstanceCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.TimeConnectionType;

public class CollapsedCompositeMeasure {

	private String name;
	//private List<MeasureDefinition> connectedMeasures;
	protected ExpandedCompositeMeasure ecm;
	protected Map<String, MeasureDefinition> usedMeasureIdMap;
		
	/*
	 * Constructores de la clase
	 * */
	public CollapsedCompositeMeasure(){
		this.name = "";
		//this.connectedMeasures = null;
	} 
	
	public CollapsedCompositeMeasure(String name,
			List<MeasureDefinition> connectedMeasures) {
		super();
		this.name = name;
		//this.connectedMeasures = connectedMeasures;
	}

	/*
	 * Getters y Setters de los atributos
	 */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/*public List<MeasureDefinition> getConnectedMeasures() {
		return connectedMeasures;
	}

	public void setConnectedMeasures(List<MeasureDefinition> connectedMeasures) {
		this.connectedMeasures = connectedMeasures;
	}*/
	
	public ExpandedCompositeMeasure getEcm() {
		return ecm;
	}

	public void setEcm(ExpandedCompositeMeasure ecm) {
		this.ecm = ecm;
	}

	public Map<String, MeasureDefinition> getUsedMeasureMap() {

    	if (this.usedMeasureIdMap==null)
    		this.usedMeasureIdMap = new HashMap<String, MeasureDefinition>();
    	return this.usedMeasureIdMap;
    }

	public void addUsedMeasure(String variable, MeasureDefinition measure) {
    	
    	this.getUsedMeasureMap().put((variable.contentEquals(""))?measure.getId():variable, measure);
    }
	
	public boolean valid(){
		
		if(this.name.equals("")) return false;
		
		if(this.usedMeasureIdMap == null) return false; //Debe tener, al menos, una medida para ser conectada.
		
		if(this.usedMeasureIdMap.size() < 0) return false;
		
		return true;
			
	}
	
	public ValidateConnection ValidateConnections(){
		
		System.out.println("[[Validación_de_medidas_conectadas]]"); //---------------------------------------------
		
		ValidateConnection vcResult = new ValidateConnection();
		
		if(!this.valid()) return vcResult;
		
		List<String> lsConexionesNoRealizadas = new ArrayList<String>();
		 
		int iElements = this.getUsedMeasureMap().size(), //Conexiones externas
			iConexionesRequeridas = 0, //Algunas, como las de tiempo, requieren dos conexiones.
			iConexionesNoRealizadas = 0;
		Object[] clases = new Object[iElements];
		clases  = this.getUsedMeasureMap().values().toArray();
		
		for(int i=0; i < iElements; i++){
			if(clases[i].getClass().getName().contains("TimeMeasure")){
				iConexionesRequeridas += 2;
				TimeMeasure tmTiempo = new TimeMeasure();
				 tmTiempo = (TimeMeasure)clases[i];
				 
				 if(tmTiempo.getFrom() == null) {
					 iConexionesNoRealizadas++;
					 lsConexionesNoRealizadas.add("TimeMeasure("+tmTiempo.getName()+") - Conection: From");
				 }
				 if(tmTiempo.getTo() == null) {
					 iConexionesNoRealizadas++;
					 lsConexionesNoRealizadas.add("TimeMeasure("+tmTiempo.getName()+") - Conection: To");
				 }
				
			}else{
				if(clases[i].getClass().getName().contains("DataMeasure")){
					
					iConexionesRequeridas++;
					 
					 DataMeasure dmDatos = new DataMeasure();
					 dmDatos = (DataMeasure)clases[i];
					 
					 if(dmDatos.getDataContentSelection() == null){
						 iConexionesNoRealizadas++;
						 lsConexionesNoRealizadas.add("DataMeasure("+dmDatos.getName()+") - Conection: DataContentSelection");
					 }	
					 
				}else{
					if(clases[i].getClass().getName().contains("StateConditionMeasure")){
						
						iConexionesRequeridas++;
						 
						 StateConditionMeasure scEstado = new StateConditionMeasure();
						 scEstado = (StateConditionMeasure)clases[i];
						 
						 if(scEstado.getCondition() == null){
							 iConexionesNoRealizadas++;
							 lsConexionesNoRealizadas.add("StateConditionMeasure("+scEstado.getName()+") - Conection: Condition");
						 }
						 
						 
					}
					else{
						if(clases[i].getClass().getName().contains("CountMeasure")){
							iConexionesRequeridas++;
							 
							 CountMeasure cmConteo = new CountMeasure();
							 cmConteo = (CountMeasure)clases[i];
							 
							 if(cmConteo.getWhen() == null){
								 iConexionesNoRealizadas++;
								 lsConexionesNoRealizadas.add("CountMeasure("+cmConteo.getName()+") - Conection: When");
							 }
						}else{
							if(clases[i].getClass().getName().contains("ListMeasure")){
								
								ListMeasure lmMeasure = new ListMeasure();
								lmMeasure = (ListMeasure)clases[i];
								ValidateConnection vcLista = lmMeasure.ValidateConnections();
								
								if(vcLista != null){
									
									
									if(vcLista.getUnrealizedConnections() == null){
										iConexionesRequeridas++; //Al menos debe haber una conexión a la medida de lista y no está.
										iConexionesNoRealizadas++; //Al menos debe haber una conexión a la medida de lista y no está.
										lsConexionesNoRealizadas.add("Se necesita al menos una conexión hacia la lista.");
									}
									else{
										iConexionesRequeridas = iConexionesRequeridas + vcLista.getTotalRequiredConnections();
										iConexionesNoRealizadas = iConexionesNoRealizadas + vcLista.getTotalUnrealizedConnections();
										for(int jl = 0; jl < vcLista.getUnrealizedConnections().size(); jl++ ){
											lsConexionesNoRealizadas.add(vcLista.getUnrealizedConnections().get(jl));
										}
									}
									
								}else{
									System.out.println("ListMeasure is Null.");
									 return vcResult;
								}
							}else{
								System.out.println("Trying to use an unidentified measure.");
								 return vcResult;
							}
						}
					}
				}
			}
		}
		
		if(iConexionesNoRealizadas == iConexionesRequeridas){ vcResult.setAllMeasuresConnected(true); }
		 
		vcResult.setTotalRequiredConnections(iConexionesRequeridas);
		vcResult.setTotalUnrealizedConnections(iConexionesNoRealizadas);
		vcResult.setUnrealizedConnections(lsConexionesNoRealizadas);
		
		//Esto eventualmente se puede eliminar, ya que debería mostrarse directamente desde donde se llama, con el objeto devuelto
		System.out.println("* AllMeasuresConnected: " + vcResult.isAllMeasuresConnected());
		System.out.println("* TotalRequiredConnections: " + vcResult.getTotalRequiredConnections());
		System.out.println("* TotalUnrealizedConnections " + vcResult.getTotalUnrealizedConnections());
		System.out.println("* UnrealizedConnections: ");
		for(int ic = 0; ic < vcResult.getUnrealizedConnections().size(); ic++){
			System.out.println("\t["+ic+"]"+vcResult.getUnrealizedConnections().get(ic));
		}
				
		return vcResult;
	}
	
	//TimeMeasure - setFrom | setTo
	/**
	 * Utilizado para definir las conexiones de una TimeMeasure.
	 * Las opciones de conexión son: SetFrom y SetTo
	 * @param nameMeasure Nombre de la medida a enlazar
	 * @param tic TimeInstantCondition a definir.
	 * @param tct Opciones: FROM | TO
	 * @return boolean
	 */
	public boolean AddConnection(String nameMeasure, TimeInstantCondition tic, TimeConnectionType tct){ //String connector es para identificar si es From o To
		//TimeMeasure - setFrom / setTo
		
		int iElements = this.getUsedMeasureMap().size(), iResultados = 0;
		Object[] variable = new Object[iElements], types = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		types = this.getUsedMeasureMap().keySet().toArray();
		
		//Busco el nombre de la medida en las medidas utilizadas por la collapsed(ccm).
		for(int i = 0; i < this.getUsedMeasureMap().size(); i++){
			
			if(types[i].toString().equals(nameMeasure)){//Ha encontrado la medida como elemento de conexion interna
				iResultados ++;
				if(variable[i].getClass().getName().toString().contains("TimeMeasure")){
					
					TimeMeasure tmMeasure = new TimeMeasure();
					tmMeasure = (TimeMeasure)variable[i];					
					
					if(tct.toString().equals("FROM")){
						tmMeasure.setFrom(tic);
					}else{
						//Es para la conexión de "TO"
						tmMeasure.setTo(tic);
					}
					this.addUsedMeasure(types[i].toString(), tmMeasure); //Reasigno la variable
					return true;
					
				}else{
					System.out.println("La variable: "+ nameMeasure + "no se corresponde con el tipo de medida esperado.");
					return false;
				}
			}
		}
		
		return false; //No encontró medidas con el nombre indicado
	}
	
	//CountMeasure - SetWhen
	/**
	 * Utilizado para definir las conexiones de una CountMeasure.
	 * Opciones de conexión son: setWhen
	 * 
	 * @param nameMeasure Nombre de la medida a enlazar
	 * @param tic TimeInstanceCondition a definir
	 * @return boolean
	 */
	public boolean AddConnection(String nameMeasure, TimeInstantCondition tic){ 
		
		//CountMeasure - setWhen
		
		int iElements = this.getUsedMeasureMap().size(), iResultados = 0;
		Object[] variable = new Object[iElements], types = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		types = this.getUsedMeasureMap().keySet().toArray();
		
		//Busco el nombre de la medida en las medidas utilizadas por la collapsed(ccm).
		for(int i = 0; i < this.getUsedMeasureMap().size(); i++){
			
			if(types[i].toString().equals(nameMeasure)){//Ha encontrado la medida como elemento de conexion interna
				iResultados ++;
				if(variable[i].getClass().getName().toString().contains("CountMeasure")){
					
					CountMeasure cmMeasure = new CountMeasure();
					cmMeasure = (CountMeasure)variable[i];
					
					cmMeasure.setWhen(tic);
					
					this.addUsedMeasure(types[i].toString(), cmMeasure);
					return true;
					
				}else{
					System.out.println("La variable: "+ nameMeasure + "no se corresponde con el tipo de medida esperado.");
					return false;
				}
			}
		}
		
		return false; //No encontró medidas con el nombre indicado
	}
	
	//DataMeasure - setDataContentSelection
	/**
	 * Utilizado para definir las conexiones de una DataMeasure
	 * Las opciones de conexión son: setDataContentSelection
	 * @param nameMeasure Nombre de la medida a enlazar
	 * @param dcs DataContentSelection a definir
	 * @return boolean
	 */
	public boolean AddConnection(String nameMeasure, DataContentSelection dcs){ 
		
		//DataMeasure - setDataContentSelection
		
		int iElements = this.getUsedMeasureMap().size();
		Object[] variable = new Object[iElements], types = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		types = this.getUsedMeasureMap().keySet().toArray();
		
		//Busco el nombre de la medida en las medidas utilizadas por la collapsed(ccm).
		for(int i = 0; i < this.getUsedMeasureMap().size(); i++){
			
			if(types[i].toString().equals(nameMeasure)){//Ha encontrado la medida como elemento de conexion interna
				
				if(variable[i].getClass().getName().toString().contains("DataMeasure")){
					
					DataMeasure dmMeasure = new DataMeasure();
					dmMeasure = (DataMeasure)variable[i];					
					
					dmMeasure.setDataContentSelection(dcs);
					
					this.addUsedMeasure(types[i].toString(), dmMeasure); //Reasigno la variable
					return true;
					
				}else{
					System.out.println("La variable: "+ nameMeasure + "no se corresponde con el tipo de medida esperado.");
					return false;
				}
			}
		}
		return false;
	}
	
	//StateConditionMeasure - setCondition
	/**
	 * Utilizado para definir las conexiones de una StateConditionMeasure
	 * Las opciones de conexión son: setCondition(StateCondition)
	 * @param nameMeasure Nombre de la medida a enlazar
	 * @param sc StateCondition a definir
	 * @return boolean
	 */
	public boolean AddConnection(String nameMeasure, StateCondition sc){
		
		//StateCondition - setCondition
		
		int iElements = this.getUsedMeasureMap().size(), iResultados = 0;
		Object[] variable = new Object[iElements], types = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		types = this.getUsedMeasureMap().keySet().toArray();
		
		//Busco el nombre de la medida en las medidas utilizadas por la collapsed(ccm).
		for(int i = 0; i < this.getUsedMeasureMap().size(); i++){
			
			if(types[i].toString().equals(nameMeasure)){//Ha encontrado la medida como elemento de conexion interna
				iResultados ++;
				if(variable[i].getClass().getName().toString().contains("StateConditionMeasure")){
					
					StateConditionMeasure scmMeasure = new StateConditionMeasure();
					scmMeasure = (StateConditionMeasure)variable[i];					
					
					scmMeasure.setCondition(sc);
					
					this.addUsedMeasure(types[i].toString(), scmMeasure); //Reasigno la variable
					return true;
					
				}else{
					System.out.println("La variable: "+ nameMeasure + "no se corresponde con el tipo de medida esperado.");
					return false;
				}
			}
		}
		return false;
		
	}
		
	//StateConditionMeasure - ProcessInstanceCondition
	//Se elimina porque no hay getCondition que devuelva un ProcessInstanceCondition que me permita validar la conexión.
	/**
	 * Utilizado para definir las conexiones de una StateConditionMeasure
	 * Las opciones de conexión son: setCondition(ProcessInstanceCondition)
	 * @param nameMeasure Nombre de la medida a enlazar
	 * @param sc ProcessInstanceCondition a definir
	 * @return boolean
	 */
	/*public boolean AddConnection(String nameMeasure, ProcessInstanceCondition pic){
		
		//StateCondition - setCondition
		
		int iElements = this.getUsedMeasureMap().size(), iResultados = 0;
		Object[] variable = new Object[iElements], types = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		types = this.getUsedMeasureMap().keySet().toArray();
		
		//Busco el nombre de la medida en las medidas utilizadas por la collapsed(ccm).
		for(int i = 0; i < this.getUsedMeasureMap().size(); i++){
			
			if(types[i].toString().equals(nameMeasure)){//Ha encontrado la medida como elemento de conexion interna
				iResultados ++;
				if(variable[i].getClass().getName().toString().contains("StateConditionMeasure")){
					
					StateConditionMeasure scmMeasure = new StateConditionMeasure();
					scmMeasure = (StateConditionMeasure)variable[i];					
					
					scmMeasure.setCondition(pic);
					
					this.addUsedMeasure(types[i].toString(), scmMeasure); //Reasigno la variable
					return true;
					
				}else{
					System.out.println("La variable: "+ nameMeasure + "no se corresponde con el tipo de medida esperado.");
					return false;
				}
			}
		}
		return false;
		
	}*/
	
	/**
	 * Utilizado para definir la conexión de una TimeMeasure a una ListMeasure.
	 * Las opciones de conexión son: SetFrom o SetTo
	 *  
	 * @param nameList Nombre de la lista a la que se quiere enlazar una TimeMeasure
	 * @param nameMeasure Nombre de la medida que se quiere enlazar
	 * @param tic TimeInstantCondition a definir
	 * @param tct Opciones: FROM | TO
	 * @return boolean
	 */
	public boolean AddConnection(String nameList, MeasureDefinition measure){ 
				
		int iElements = this.getUsedMeasureMap().size();
		Object[] variable = new Object[iElements], types = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		types = this.getUsedMeasureMap().keySet().toArray();
		
		//Busco el nombre de la medida en las medidas utilizadas por la collapsed(ccm).
		for(int i = 0; i < this.getUsedMeasureMap().size(); i++){
			
			if(types[i].toString().equals(nameList)){//Ha encontrado la medida como elemento de conexion interna
				if(variable[i].getClass().getName().toString().contains("ListMeasure")){
					
					ListMeasure lmLista = new ListMeasure();
					lmLista = (ListMeasure)variable[i];
					
					
					if(lmLista.getUsedMeasureMap()==null || lmLista.getUsedMeasureMap().size() == 0){
						//La lista está vacía, agrego
						lmLista.addUsedMeasure(measure.getName(), measure);//Conexión con la lista
					}else{
						//Verifico que la medida a agregar sea del mismo tipo que las medidas existentes
						
						if(lmLista.getUsedMeasureMap().toString().contains( measure.getClass().getName() )){
							lmLista.addUsedMeasure(measure.getName(), measure);//Conexión con la lista
						}
						else{
							System.out.println("\n\t"+measure.getName()+"<"+ LimpiarCadena(measure.getClass().getName(), ".", "") +"> No se agregó a la lista porque no coincide con los tipos de medida ya utilizados.");
							System.out.println("\tLa lista actual contiene medidas de tipo: " + LimpiarCadena(lmLista.getUsedMeasureMap().toString(), ".", "").substring(0, LimpiarCadena(lmLista.getUsedMeasureMap().toString(), ".", "").lastIndexOf("@")));
						}
					}
					
					
					//lmLista.addUsedMeasure(measure.getName(), measure);//Conexión con la lista
					
					this.addUsedMeasure(nameList, lmLista);		
					
					return true;
					
				}else{
					System.out.println("La variable: <"+ nameList + "> no se corresponde con el tipo de medida esperado.");
					return false;
				}
			}
		}
		
		return false; //No encontró medidas con el nombre indicado
	}
	

	
	private String LimpiarCadena(String sCadena, String sInicio, String sFin){
		if(!sCadena.equals("") && !sInicio.equals("")){
			
			if(!sFin.equals("")){
				//Validar que el primero sea menor que el segundo.
				sCadena = sCadena.substring(sCadena.lastIndexOf(sInicio)+1, sCadena.indexOf(sFin));
			}else{
				sCadena = sCadena.substring(sCadena.lastIndexOf(sInicio)+1);
			}
		}
		
		return sCadena;
	}

}
