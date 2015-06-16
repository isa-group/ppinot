package es.us.isa.ppinot.model.composite.type;

import es.us.isa.ppinot.model.composite.CompositeType;
import es.us.isa.ppinot.model.composite.ListMeasure;

public class TimeType extends CompositeType {

	public ListMeasure listMeasure;
	
	/**
	 * Constructores de la clase
	 */

	public TimeType() {
		super();
		this.listMeasure = null;
	}
	
	public TimeType(ListMeasure listMeasure) {
		super();
		this.listMeasure = listMeasure;
	}

	
	
	public ListMeasure getListMeasure() {
		return listMeasure;
	}

	public void setListMeasure(ListMeasure listMeasure) {
		this.listMeasure = listMeasure;
	}
	
	
	
}
