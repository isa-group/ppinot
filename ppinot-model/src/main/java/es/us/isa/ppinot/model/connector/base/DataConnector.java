package es.us.isa.ppinot.model.connector.base;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.condition.Condition;


public class DataConnector extends BaseMeasureConnector implements Cloneable{

	// Dataobject y propiedad a la que se le aplica la medida
	private DataContentSelection ccDataContentSelection;
	// Condicion de la medida
	//private Condition ccPrecondition;
		
	public DataConnector(){
		super();
		this.setCcDataContentSelection(null);
		//this.setCcPrecondition(null);
	}
	
	public DataConnector(String id, String name, String description, DataContentSelection ccDataContentSelection, Condition ccPrecondition){
		super(id, name, description);
		this.setCcDataContentSelection(ccDataContentSelection);
		//this.setCcPrecondition(ccPrecondition);
	}
	
	public void setCcDataContentSelection(DataContentSelection ccDataContentSelection) {
		this.ccDataContentSelection = ccDataContentSelection;
	}
	
	/*public void setCcPrecondition(Condition ccPrecondition) {
		this.ccPrecondition = ccPrecondition;
	}*/
	
	public DataContentSelection getCcDataContentSelection() {
		return this.ccDataContentSelection;
	}
	
	/*public Condition getCcPrecondition() {
		return ccPrecondition;
	}*/
	
	public boolean valid() {
		
		try{
			return super.valid() &&
					this.getCcDataContentSelection().getDataobjectId()!=null && ! "".equals(this.getCcDataContentSelection().getDataobjectId()) &&
					this.getCcDataContentSelection().getSelection()!=null && ! this.getCcDataContentSelection().getSelection().isEmpty();	
		}catch(Exception e){
			return false;
		}
		
		
	}
	
	public DataConnector clone() {
        final DataConnector clone;
        try {
        	clone = (DataConnector) super.clone();
        }
        catch( Exception ex ) { 
            throw new RuntimeException( "\t!>>>> Excepción en DataConnector - clone() \n" + ex.getMessage());
        }
        return clone;
    }
	
}
