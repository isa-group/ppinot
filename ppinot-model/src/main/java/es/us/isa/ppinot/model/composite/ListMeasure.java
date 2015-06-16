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

public class ListMeasure extends MeasureDefinition{
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
	 public boolean valid(){
		 
		 		 
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
	
	 public ValidateConnection ValidateConnections(){
		 
		 ValidateConnection vcResult = new ValidateConnection();
		 
		 if(!this.valid()) return vcResult;
		 
		 List<String> lsConexionesNoRealizadas = new ArrayList<String>();
		 
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
					 lsConexionesNoRealizadas.add("TimeMeasure("+tmTiempo.getName()+") - Conection: From");
				 }
				 if(tmTiempo.getTo() == null) {
					 iConexionesNoRealizadas++;
					 //System.out.println("tmTiempo.getTo() = NULL");
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
							 lsConexionesNoRealizadas.add("StateConditionMeasure("+scEstado.getName()+") - Conection: getCondition");
						 }
						 
					 }else{
						 if(clases[i].getClass().getName().contains("CountMeasure")){
							 
							 iConexionesRequeridas++;
							 
							 CountMeasure cmConteo = new CountMeasure();
							 cmConteo = (CountMeasure)clases[i];
							 
							 if(cmConteo.getWhen() == null){
								 iConexionesNoRealizadas++;
								 lsConexionesNoRealizadas.add("CountMeasure("+cmConteo.getName()+") - Conection: getWhen");
							 }
						 }else{
							 System.out.println("Se ha utilizado un tipo de medida no identificada.");
							 return vcResult; 
						 }
					 }
				 }
			 } 
		 }
		 
		 
		 if(iConexionesNoRealizadas == iConexionesRequeridas){ vcResult.setAllMeasuresConnected(true); }
		 
		 vcResult.setTotalRequiredConnections(iConexionesRequeridas);
		 vcResult.setTotalUnrealizedConnections(iConexionesNoRealizadas);
		 vcResult.setUnrealizedConnections(lsConexionesNoRealizadas);
		
		 return vcResult;
	 }
	 
}
