/**
 * 
 */
package es.us.isa.ppinot.model.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;

/**
 * @author BEstrada
 *
 */
public class ExpandedCompositeMeasure {
		
	private String name;
	private CompositeTypeDefinition typeDefinition;
	private MeasureDefinition rootMeasure;
	protected Map<String, MeasureDefinition> usedMeasureIdMap;
	
	public ExpandedCompositeMeasure() {
		this.name = "";
		this.setRootMeasure(null);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompositeTypeDefinition getTypeDefinition() {
		return typeDefinition;
	}

	public void setTypeDefinition(CompositeTypeDefinition typeDefinition) {
		this.typeDefinition = typeDefinition;
	}
	
	public MeasureDefinition getRootMeasure() {
		return rootMeasure;
	}

	public void setRootMeasure(MeasureDefinition rootDefinition) {
		this.rootMeasure = rootDefinition;
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
		
		//CAMBIAR LAS ÚLTIMAS VALIDACIONES POR/CON EL MÉTODO QUE DEVUELVE EL LISTADO DE TODAS LAS MEDIDAS QUE CONFORMAN LA EXPANDED_COMPOSITE
		
		int iVariableResultado = -1; //Si no se cambia el valor a 0 o 1 es un error en la ejecución y la medida no es válida.
				
		if(this.getTypeDefinition() == null || this.getClass() == null || this.getName() == null || this.getName().equals(""))
			return false;
		
		if(this.getUsedMeasureMap().size() <= 0) iVariableResultado = 1;
		
		if(this.getTypeDefinition().getClass().getName().substring(this.getTypeDefinition().getClass().getName().lastIndexOf(".")+1).equals("TimeType"))
			iVariableResultado = 0;
		
		return ( this != null
			&&	((this.getUsedMeasureMap().size() == (this.getTypeDefinition().getClass().getDeclaredFields().length + iVariableResultado)) || this.getUsedMeasureMap().size() == 0) //Comparo la cantidad de tipos de unos y otros. Se suma uno porque en el map se incluye el Root, en los campos declarados eso no está.
			&& (!this.getName().equals(""))
			&& (this.getTypeDefinition()!= null)
		);	
		
	}
	
	public ValidateConnection ValidateConnections(){
		
		ValidateConnection vcResult = new ValidateConnection();
		
		//Se debe tener en cuenta que la validación de conexiones se debe aplicar
		//SOLAMENTE a las medidas que NO son HOJA.
		//Ya que al ser una medida Expanded, no tiene por qué estar conectada con el exterior.
		
		if(!this.valid()) return vcResult;
		
		List<String> lsConexionesNoRealizadas = new ArrayList<String>();
		
		int iElements = this.getUsedMeasureMap().size(), //Conexiones externas
			iConexionesRequeridas = 0, //Algunas, como las de tiempo, requieren dos conexiones.
			iConexionesNoRealizadas = 0;
		
		Object[] clases = new Object[iElements];
		clases  = this.getUsedMeasureMap().values().toArray();
		
		
		return vcResult;
		
	}
	
	private boolean requiereConexiones(MeasureDefinition mdMeasure){
		
		//Si es lista requiere de conexiones pero no deben estar verificadas aquí, sino en la CCM ya que van al exterior.
		
		if(mdMeasure.getClass().getName().contains("DerivedMultiInstanceMeasure") || mdMeasure.getClass().getName().contains("DerivedSingleInstanceMeasure") || mdMeasure.getClass().getName().contains("AggregatedMeasure") ){
			return true;
		}else{
			return false;
		}
		
	}

	
	
}
