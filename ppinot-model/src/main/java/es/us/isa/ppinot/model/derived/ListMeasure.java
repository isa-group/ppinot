/************************************************************************/
/*** MODIFIED VERSION *************************************************/
/************************************************************************/

package es.us.isa.ppinot.model.derived;

import org.codehaus.jackson.annotate.JsonTypeInfo;



//import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
//import es.us.isa.Response;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
		include=JsonTypeInfo.As.PROPERTY, property="kind")
public class ListMeasure extends DerivedMeasure implements Cloneable{
	
	public ListMeasure() {
        super();
    }
	
	/**
     * Constructor de la clase
     * 
     * @param id Id of the measure
     * @param name Name of the measure
     * @param description Description of the measure
     * @param scale Scale of the measure
     * @param unitOfMeasure Unit of measure
     * @param function Function to be applied over all elements of the measure
     */
	
	public ListMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String function) {
    	super(id, name, description, scale, unitOfMeasure, function);
	}
	
	public ListMeasure clone(){
    	
    	final ListMeasure clone;
    	
    	try{
    		clone = (ListMeasure) super.clone();
    	}catch(Exception ex){ //CloneNotSupported
    		throw new RuntimeException( "\t!>>>> Exception in ListMeasure - clone()" );
    	}
    	
    	return clone;
    }
	
	//My classes to include the management of a List
	
	//Incluir type en la validación???
	
	public void printAttributes(){
		System.out.println("ListMeasure:");
		System.out.println(" - id: " + this.getId());
		System.out.println(" - name: " + this.getName());
		System.out.println(" - function: " + this.getFunction());
					
		int iMeasureElements = this.getUsedMeasureMap().size();
		int iExternalElements = this.getUsedExternalValueMap().size();
		
		//>USADO: Object[] variable = new Object[iMeasureElements];
		//>USADO: variable = this.getUsedMeasureMap().values().toArray();
		System.out.println(" - measures connected: " + iMeasureElements);
				
		if(iMeasureElements > 0){
			//System.out.println("   · Type of measures expected for the list members: " + variable[0].toString().substring(variable[0].toString().lastIndexOf(".")+1, variable[0].toString().indexOf("@")));
			
			Object[] clases = new Object[iMeasureElements];
			clases  = this.getUsedMeasureMap().values().toArray();
			
			for(int i=0; i < iMeasureElements; i++){
						
				if(clases[i].getClass().getName().contains("TimeMeasure")){
					TimeMeasure tmTiempo = new TimeMeasure();
					tmTiempo = (TimeMeasure)clases[i];
					System.out.println("   · TimeMeasure: "+tmTiempo.getName());
					tmTiempo = null;
				}else{
					if(clases[i].getClass().getName().contains("DataMeasure")){
						DataMeasure dmData = new DataMeasure();
						dmData = (DataMeasure)clases[i];
						System.out.println("   · DataMeasure: "+dmData.getName());
						dmData = null;
					}else{
						if(clases[i].getClass().getName().contains("StateConditionMeasure")){
							StateConditionMeasure smState = new StateConditionMeasure();
							smState = (StateConditionMeasure)clases[i];
							System.out.println("   · StateConditionMeasure: "+smState.getName());
							smState = null;
						}else{
							if(clases[i].getClass().getName().contains("CountMeasure")){
								CountMeasure cmCount = new CountMeasure();
								cmCount = (CountMeasure)clases[i];
								System.out.println("   · cmCount: "+cmCount.getName());
								cmCount = null;
							}else{
								System.out.println("   · There is no base measures associated with this list.");
							}
						}
					}
				}
			}
		}//else{			//If <= 0 
		 //	System.out.println("   · ERROR. The list should have at least one measure.");
		 //}
		System.out.println(" - external values connected: " + iExternalElements);
		
	}
	
	public boolean valid(){
		
		boolean response = true;
		//Response rs = new Response();
		//rs.setResponse(true);
		
		//Id must have a value
		if(this.getId()==null || this.getId().contentEquals("")){
			response =  false;
			//rs.setResponse(false);
			//rs.setMessageError(rs.getMessageError() + "   · ERROR. ID is not assiged.");
		}
		
		//Function must have a value
		if(this.getFunction()==null || this.getFunction().contentEquals("") ){
			response =  false;
			//rs.setResponse(false);
			//rs.setMessageError(rs.getMessageError() + "   · ERROR. Function is not assigned.");
		}
		
		int iMeasureElements = this.getUsedMeasureMap().size();
		int iExternalElements = this.getUsedExternalValueMap().size();
		
		if(iMeasureElements <= 0 && iExternalElements <= 0){
			response = false;
			//rs.setResponse(false);
			//rs.setMessageError(rs.getMessageError() + "   · ERROR. No measures or external values are connected with the list.");
		}
			
		if(iMeasureElements >0 && iExternalElements >0){
			response = false;
			//rs.setResponse(false);
			//rs.setMessageError(rs.getMessageError() + "   · ERROR. Measures 'AND' external values are connected with the list. Only one of them is allowed.");
		}
		
		//At least one measure connected
		if(iMeasureElements > 0){
						
			//All measures must be of the same type
			
			Object[] variable = new Object[iMeasureElements];
			variable = this.getUsedMeasureMap().values().toArray();
			//>USADO: String differences = "\t\t" + variable[0].toString().substring(variable[0].toString().lastIndexOf(".")+1, variable[0].toString().indexOf("@")) + "\n";
			boolean sameType = true;
			
			for(int i=0; i < iMeasureElements-1; i++){
				 if(!variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")).equals(variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")))){
					 
					 //>USADO: differences += "\t\t" + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n";
					 sameType = false;
					 
					 //differences += "["+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - ["+(i+1)+"]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n";
					 //System.out.printf("lMeasure (Diferentes) - [i:"+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [i+1:%d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
				 }
				 //System.out.printf("lMeasure (Iguales) - [i:"+i+"]: " + variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")) + " - [i+1: %d]: " + variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")) + "\n", (i+1));
			}
			
			if(sameType == false){
				response = false;
			   //rs.setResponse(false);
			   //rs.setMessageError(rs.getMessageError() + "   · ERROR. More than one type of measure were found\n\t    Types of measures found: \n"+ differences.substring(0, differences.length()-1));
			}
		}
		
		if(iExternalElements > 0){
				
			Object[] variable, names = new Object[iExternalElements];
			variable = this.getUsedExternalValueMap().values().toArray();
			names = this.getUsedExternalValueMap().keySet().toArray();
			String emptyString = "";
			
			//Por el momento solo reviso que tengo "ALGO". Luego a ver si valido que todos sean del mismo tipo.
			//Pero todos podrían ser Strings... =/
			for(int i=0; i < iExternalElements; i++){
				if(variable[i].toString().contentEquals("")){
					emptyString =  names[i] + " - " + emptyString;
				}
			}
			
			if(!emptyString.contentEquals("")){
				response = false;
				//rs.setResponse(false);
				//rs.setMessageError(rs.getMessageError() + "   · ERROR. Empty external values found [ "+emptyString.substring(0, emptyString.length()-3)+" ].");
			}
				
		}
						
		//ME FALTARÍA VALIDAR LAS CONEXIONES DE CADA MEDIDA CONECTADA CON UNA LISTA.
		//HACERLO MÁS ADELANTE.
			
		//if(rs.isResponse())
		//	rs.setMessageError(" - valid measure: " + rs.isResponse());
		//else
		//	rs.setMessageError(" - valid measure: " + rs.isResponse() + "\n" + rs.getMessageError());
		
		//return rs;
				
		return response;
	}

	
	
}
