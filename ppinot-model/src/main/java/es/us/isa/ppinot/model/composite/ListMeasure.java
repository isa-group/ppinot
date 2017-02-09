package es.us.isa.ppinot.model.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;

import java.io.PrintStream; //Para imprimir de una forma diferente los valores de i+1

public class ListMeasure extends MeasureDefinition implements Cloneable{
	public String name;
	public String function;
	protected Map<String, MeasureDefinition> usedMeasureIdMap;
	
	
	/**
	 * Constructores de la clase
	 */
	public ListMeasure() {
		super();
		this.name = "";
		this.function = "";
	}
	public ListMeasure(String name, String function) {
		super();
		this.name = name;
		this.function = function;
		//this.usedMeasureIdMap = usedMeasureIdMap;
	}
	
	/**
	 * Getter y Setter de los atributos de la clase
	 */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFunction() {
		return this.function;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}
	
	/**
	 * Método para hacer el rastreo de las medidas a utilizar y enlazar
	 */
	public Map<String, MeasureDefinition> getUsedMeasureMap() {

    	if (this.usedMeasureIdMap==null)
    		this.usedMeasureIdMap = new HashMap<String, MeasureDefinition>();
    	return this.usedMeasureIdMap;
    }
	
	public void addUsedMeasure(String variable, MeasureDefinition measure) {
    	
    	this.getUsedMeasureMap().put((variable.contentEquals(""))?measure.getId():variable, measure);
    }
	
	/**
	 *Método para comprobar si se proporcionan todos los datos del método.
	 */
	
	public void printMeasureList(){
		
		int iElements = this.getUsedMeasureMap().size();
		Object[] variable = new Object[iElements];
		variable = this.getUsedMeasureMap().values().toArray();
		 
		System.out.println("Measure name: " + this.getName());
		System.out.println("Function list: " + this.getFunction());
		System.out.println("Measures conforming the list: " + iElements);
		
		if(iElements > 0){
			System.out.println("Type of measures expected: " + variable[0].toString().substring(variable[0].toString().lastIndexOf(".")+1, variable[0].toString().indexOf("@")));
			
			//Getting measure names
			Object[] clases = new Object[iElements];
			clases  = this.getUsedMeasureMap().values().toArray();
			
			for(int i=0; i < iElements; i++){
						
				if(clases[i].getClass().getName().contains("TimeMeasure")){
					TimeMeasure tmTiempo = new TimeMeasure();
					tmTiempo = (TimeMeasure)clases[i];
					System.out.println("   - TimeMeasure: "+tmTiempo.getName());
					tmTiempo = null;
				}else{
					if(clases[i].getClass().getName().contains("DataMeasure")){
						DataMeasure dmData = new DataMeasure();
						dmData = (DataMeasure)clases[i];
						System.out.println("   - DataMeasure: "+dmData.getName());
						dmData = null;
					}else{
						if(clases[i].getClass().getName().contains("StateConditionMeasure")){
							StateConditionMeasure smState = new StateConditionMeasure();
							smState = (StateConditionMeasure)clases[i];
							System.out.println("   - StateConditionMeasure: "+smState.getName());
							smState = null;
						}else{
							if(clases[i].getClass().getName().contains("CountMeasure")){
								CountMeasure cmCount = new CountMeasure();
								cmCount = (CountMeasure)clases[i];
								System.out.println("   - cmCount: "+cmCount.getName());
								cmCount = null;
							}else{
								System.out.println("   - There is no base measures associated with this list.");
							}
						}
					}
				}
			}
		}
		
		
		
	}
	
	public boolean validateAmount(){

		if(this.getUsedMeasureMap().size() <= 0){ 
			 return false;
		}
		return true;
	}
	
	public boolean validateTypesOfMeasures(){
		
		int iElements = this.getUsedMeasureMap().size();
		
		Object[] variable = new Object[iElements];
		 variable = this.getUsedMeasureMap().values().toArray();
		 
		 for(int i=0; i < iElements-1; i++){
			 if(!variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")).equals(variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")))){
				 //System.out.printf("lMeasure (Diferentes) - [i:"+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [i+1:%d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
				 System.out.printf("The list has at least two different types: ["+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [%d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
				return false;
			 }
			 //System.out.printf("lMeasure (Iguales) - [i:"+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [i+1: %d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
		 }
		 
		 return true;
	}
	
	 public boolean valid(){
		 
		 //TENER UN NOMBRE Y UNA FUNCIÓN DEFINIDA
		 		 
		 int iElements = this.getUsedMeasureMap().size();
		 
		 if(iElements <= 0){ 
			 System.out.println("lMeasure - Cantidad: " + iElements);
			 return false;
		}
		 
		//System.out.println("lMeasure - iElementos: " + iElements);
		 
		 Object[] variable = new Object[iElements];
		 variable = this.getUsedMeasureMap().values().toArray();
		 
		 for(int i=0; i < iElements-1; i++){
			 if(!variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")).equals(variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")))){
				 System.out.printf("lMeasure (Diferentes) - [i:"+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [i+1:%d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
				return false;
			 }
			 //System.out.printf("lMeasure (Iguales) - [i:"+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [i+1: %d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
		 }
		 
		 return true;
	 }
	
	 public ValidateConnection validateConnections(){
		 
		 //Estas dos podrían ser omitidas
		 ValidateConnection vcResult = new ValidateConnection();
		 if(!this.valid()) return vcResult;
		 
		 List<String> lsIncorrectConnections = new ArrayList<String>();
		 
		 int iElements = this.getUsedMeasureMap().size(),
			 iConexionesRequeridas = 0, //Algunas, como las de tiempo, requieren dos conexiones.
		     iConexionesNoRealizadas = 0;
		 
		 Object[] clases = new Object[iElements];
		 clases  = this.getUsedMeasureMap().values().toArray();//.keySet().toArray();
		
		 for(int i=0; i < iElements; i++){
			
			 //Identificando el tipo de medida para crear el objeto y verificar la conexión.
			 
			 if(clases[i].getClass().getName().contains("TimeMeasure")){
				 
				 iConexionesRequeridas += 2;
				 
				 TimeMeasure tmTiempo = new TimeMeasure();
				 tmTiempo = (TimeMeasure)clases[i];
				 
				 if(tmTiempo.getFrom() == null) {
					 iConexionesNoRealizadas++;
					 //System.out.println("tmTiempo.getFrom() = NULL");
					 lsIncorrectConnections.add("TimeMeasure("+tmTiempo.getName()+") - Connection: From");
				 }
				 if(tmTiempo.getTo() == null) {
					 iConexionesNoRealizadas++;
					 //System.out.println("tmTiempo.getTo() = NULL");
					 lsIncorrectConnections.add("TimeMeasure("+tmTiempo.getName()+") - Connection: To");
				 }		 
				 
			 }else{
				 if(clases[i].getClass().getName().contains("DataMeasure")){
					
					 iConexionesRequeridas++;
					 
					 DataMeasure dmDatos = new DataMeasure();
					 dmDatos = (DataMeasure)clases[i];
					 
					 if(dmDatos.getDataContentSelection() == null){
						 iConexionesNoRealizadas++;
						 lsIncorrectConnections.add("DataMeasure("+dmDatos.getName()+") - Connection: DataContentSelection");
					 }					 
					 
				 }else{
					 if(clases[i].getClass().getName().contains("StateConditionMeasure")){
						 
						 iConexionesRequeridas++;
						 
						 StateConditionMeasure scEstado = new StateConditionMeasure();
						 scEstado = (StateConditionMeasure)clases[i];
						 
						 if(scEstado.getCondition() == null){
							 iConexionesNoRealizadas++;
							 lsIncorrectConnections.add("StateConditionMeasure("+scEstado.getName()+") - Connection: getCondition");
						 }
						 
					 }else{
						 if(clases[i].getClass().getName().contains("CountMeasure")){
							 
							 iConexionesRequeridas++;
							 
							 CountMeasure cmConteo = new CountMeasure();
							 cmConteo = (CountMeasure)clases[i];
							 
							 if(cmConteo.getWhen() == null){
								 iConexionesNoRealizadas++;
								 lsIncorrectConnections.add("CountMeasure("+cmConteo.getName()+") - Connection: getWhen");
							 }
						 }else{
							 System.out.println("Se ha utilizado un tipo de medida no identificada.");
							 return vcResult; 
						 }
					 }
				 }
			 } 
		 }
		 
		 if(iConexionesRequeridas == (iConexionesRequeridas - iConexionesNoRealizadas)){ vcResult.setAllMeasuresConnected(true); }
		 
		 vcResult.setTotalRequiredConnections(iConexionesRequeridas);
		 vcResult.setTotalUnrealizedConnections(iConexionesNoRealizadas);
		 vcResult.setUnrealizedConnections(lsIncorrectConnections);
		
		 return vcResult;
	 }
	 
	 public ListMeasure clone(){
		 
		 final ListMeasure clone;
		 
		 try{
			 
			 clone = (ListMeasure) super.clone();
			 
		 }catch(Exception e){ //CloneNotSupported
			 throw new RuntimeException( "\t!>>>> Excepción en ListMeasure - clone()" );
		 }
		 return clone;
	 }
	 
}
