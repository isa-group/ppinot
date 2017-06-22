package es.us.isa.ppinot.model.composite;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.connector.CompositeConnector;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * CompositeMeasure
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author Bedilia Estrada
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY, property="kind")
public class CompositeMeasure extends MeasureDefinition{

	public CompositeMeasure(){
		super();		
	}
	
	public CompositeMeasure(String id, String name, String description, String scale, String unitOfMeasure){
		super(id, name, description, scale, unitOfMeasure);
	}
	
	/**
     * Devuelve el atributo usedConnectorMap:
     * Connectores a partir de los cuales se asociarán medidas a la CompositeMeasure
     * 
     * @return El conector de medida que se agrega
     */
	
	
	
	//FALTA VALIDAR LA COMPOSITE CONNECTOR 
}
