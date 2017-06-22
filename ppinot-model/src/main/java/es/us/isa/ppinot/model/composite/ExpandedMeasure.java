package es.us.isa.ppinot.model.composite;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.connector.NumberOfTypes;
import es.us.isa.ppinot.model.connector.aggregated.AggregatedConnector;
import es.us.isa.ppinot.model.connector.base.TimeConnector;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.derived.ListMeasure;


public class ExpandedMeasure extends CompositeMeasure implements Cloneable{
	
	private MeasureDefinition measure;
		
	public ExpandedMeasure(){
		super();
		this.setMeasure(null);
	}
	
	public ExpandedMeasure(String id, String name, String description, String scale, String unitOfMeasure, MeasureDefinition measure){
		super(id, name, description, scale, unitOfMeasure);
		this.setMeasure(measure);
	}
	
	public void setMeasure(MeasureDefinition measure) {
		this.measure = measure;
	}
	
	public MeasureDefinition getMeasure() {
		return measure;
	}

	public boolean valid(){
				
		if (this.getName() != null && !this.getName().contentEquals("") && this.measure != null && !hasExpandedMeasuresInside())
			return true;
		else
			return false;
		//Que tenga al menos una medida hoja (no conectada)
	}
	
	///////////////////////////////////////////////////////////////
	
	private boolean hasExpandedMeasuresInside(){		
		
		boolean temporalResponse = false;
		
		if( cleanString(this.measure.getClass().getName()).contentEquals("ExpandedMeasure") ){
			temporalResponse = true;
		}else{
			if( cleanString(this.measure.getClass().getName()).contentEquals("DerivedMeasure") ||
					cleanString(this.measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
					cleanString(this.measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
					cleanString(this.measure.getClass().getName()).contentEquals("ListMeasure")){
						temporalResponse = hasExpandedMeasuresInside((DerivedMeasure)this.getMeasure());
			}else{
				if( cleanString(this.measure.getClass().getName()).contentEquals("AggregatedMeasure")){
					temporalResponse = hasExpandedMeasuresInside((AggregatedMeasure)this.getMeasure());	
				}//else{//Es una base measure (o una CompositeMeasure)
			}
		}
		return temporalResponse;
	}
	
	private boolean hasExpandedMeasuresInside(DerivedMeasure dm){
		
		boolean temporalResponse = false;
		
		if(dm.getUsedMeasureMap().size() > 0){
					
			Object[] classes = new Object[dm.getUsedMeasureMap().size()];
			classes  = dm.getUsedMeasureMap().values().toArray();
			for(int i=0; i<classes.length; i++){
				
				//System.out.println("temporalResponse = " + temporalResponse + " - Class: " + classes[i].getClass().getName());
				if(temporalResponse == true) 
					return temporalResponse;
				
				if(cleanString(classes[i].getClass().getName()).equals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("ListMeasure") ){
					
					temporalResponse =  hasExpandedMeasuresInside((DerivedMeasure)classes[i]);
					
				}else{
					if(cleanString(classes[i].getClass().getName()).equals("AggregatedMeasure")){
						
						temporalResponse =  hasExpandedMeasuresInside((AggregatedMeasure)classes[i]);
						
					}else{
						if(cleanString(classes[i].getClass().getName()).equals("ExpandedMeasure")){
							
							temporalResponse = true;
							break;
							
						}
						//ELSE -> Es una base measure, por lo tanto también es FALSE
					}
				}
			}
		} //La derived no tiene medidas -> No tiene ExpandedMeasures
		
		return temporalResponse;
	}
	
	private boolean hasExpandedMeasuresInside(AggregatedMeasure aggm){
		
		boolean temporalResponse = false;
				
		if( cleanString( aggm.getBaseMeasure().getClass().getName() ).contentEquals("DerivedMeasure") ||
			cleanString( aggm.getBaseMeasure().getClass().getName() ).contentEquals("DerivedSingleInstanceMeasure") ||
			cleanString( aggm.getBaseMeasure().getClass().getName() ).contentEquals("DerivedMultiInstanceMeasure") ||
			cleanString( aggm.getBaseMeasure().getClass().getName() ).contentEquals("ListMeasure") ){
		
			temporalResponse = hasExpandedMeasuresInside((DerivedMeasure)aggm.getBaseMeasure());
			
		}else{
			
			if( cleanString( aggm.getBaseMeasure().getClass().getName() ).contentEquals("AggregatedMeasure")){
				
				temporalResponse = hasExpandedMeasuresInside((AggregatedMeasure)aggm.getBaseMeasure());
				
			}else{
				
				if( cleanString( aggm.getBaseMeasure().getClass().getName() ).contentEquals("ExpandedMeasure") ){
					
					temporalResponse = true;
					
				}//Es una base measure -> No tiene ExpandedMeasures
						
			}
			
		}
		
		return temporalResponse;
	}
	
	///////////////////////////////////////////////////////////////
	
	public List<MeasureDefinition> getLeafNodeMeasures() {

		List<MeasureDefinition> leafNodes= new ArrayList<MeasureDefinition>();
		
		/*if(this.measure !=null){				}*/
				
		if(cleanString(this.measure.getClass().getName()).equals("DerivedMeasure") ||
		   cleanString(this.measure.getClass().getName()).equals("DerivedSingleInstanceMeasure") || 
		   cleanString(this.measure.getClass().getName()).equals("DerivedMultiInstanceMeasure") || 
		   cleanString(this.measure.getClass().getName()).equals("ListMeasure")){
						
			leafNodes = getLeafNodeMeasures((DerivedMeasure)this.getMeasure(),leafNodes);
			
			
		}else{
			if(cleanString(this.measure.getClass().getName()).equals("AggregatedMeasure")){
				
				AggregatedMeasure aggm = (AggregatedMeasure)this.getMeasure();
				
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("ListMeasure") ){

						leafNodes = getLeafNodeMeasures((DerivedMeasure)aggm.getBaseMeasure(),leafNodes);
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).equals("AggregatedMeasure")){						
						leafNodes = getLeafNodeMeasures((AggregatedMeasure)aggm.getBaseMeasure(), leafNodes);
					}else{
						
						//Esto lo agrego para considerarlo nodo hoja 
						//Probar lo anterior para verificar que no afecta al resto de cálculos de hoja 
						//Desde aquí
						
						//System.out.println("Que tienen la medida" + aggm.getClass().getName() + "\n >> "+ aggm.getBaseMeasure().getClass().getName());
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("BaseMeasure") || aggm.getBaseMeasure() == null){
							leafNodes.add(aggm);
						}else //Hasta aquí
							leafNodes.add(aggm.getBaseMeasure());
					}
				}
				
			}else{
				//Es una time, count, stateCondition, data measure - Agregar a la lista de nodos hoja
				leafNodes.add(measure);
			}
		}
		
		//System.out.println("Size of the measure: " + leafNodes.size());
		return leafNodes;
	}
	
	public List<MeasureDefinition> getLeafNodeMeasures(DerivedMeasure dm, List<MeasureDefinition> list) {
		
		//No estoy incluyendo la posibilidad de ExternalValues
		
		if(dm.getUsedMeasureMap().size() > 0){
			//No agrego la Derived porque tiene conexiones
			
			Object[] classes = new Object[dm.getUsedMeasureMap().size()];
			classes  = dm.getUsedMeasureMap().values().toArray();
			for(int i=0; i<classes.length; i++){
				if(cleanString(classes[i].getClass().getName()).equals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("ListMeasure") ){
						
						list = getLeafNodeMeasures((DerivedMeasure)classes[i],list);
	
				}else{
					if(cleanString(classes[i].getClass().getName()).equals("AggregatedMeasure")){
						//Esto lo agrego para considerarlo nodo hoja 
						//Probar lo anterior para verificar que no afecta al resto de cálculos de hoja 
						//Desde aquí
						
						//System.out.println("Que tienen la medida" + aggm.getClass().getName() + "\n >> "+ aggm.getBaseMeasure().getClass().getName());
						AggregatedMeasure aggmaux = (AggregatedMeasure)classes[i];
						if(cleanString(aggmaux.getBaseMeasure().getClass().getName()).contentEquals("BaseMeasure") || aggmaux.getBaseMeasure() == null){
						 	list.add(aggmaux); //leafNodes.add(aggm);
						}else //Hasta aquí
							list = getLeafNodeMeasures((AggregatedMeasure)classes[i],list);
						
					}else{

						if(cleanString(classes[i].getClass().getName()).equals("TimeMeasure"))
							list.add((TimeMeasure)classes[i]);
						else
							if(cleanString(classes[i].getClass().getName()).equals("CountMeasure"))
								list.add((CountMeasure)classes[i]);
							else
								if(cleanString(classes[i].getClass().getName()).equals("StateConditionMeasure"))
									list.add((StateConditionMeasure)classes[i]);
								else
									if(cleanString(classes[i].getClass().getName()).equals("DataMeasure"))
										list.add((DataMeasure)classes[i]);
						
					}
				}
			}
			
		}else{
			//La derivedMeasure es un NodoHoja
			list.add(dm);
		}
		
		//Si la derived tiene nodos, NO se agrega, porque tienen conexiones y se hace recursivo
		//Si la derived no tiene nodos, SI se agrega
		return list;
	}
	
	public List<MeasureDefinition> getLeafNodeMeasures(AggregatedMeasure aggm, List<MeasureDefinition> list) {
		
		if(cleanString(aggm.getBaseMeasure().getClass().getName()).equals("AggregatedMeasure")){
				list = getLeafNodeMeasures((AggregatedMeasure)aggm.getBaseMeasure(), list);
		}else{
			if(cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMeasure") ||
			   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
		       cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
			   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("ListMeasure")){
				list = getLeafNodeMeasures((DerivedMeasure)aggm.getBaseMeasure(), list);
			}else{
				
				//Esto lo agrego para considerarlo nodo hoja 
				//Probar lo anterior para verificar que no afecta al resto de cálculos de hoja 
				//Desde aquí
				
				//System.out.println("Que tienen la medida" + aggm.getClass().getName() + "\n >> "+ aggm.getBaseMeasure().getClass().getName());
				
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("BaseMeasure") || aggm.getBaseMeasure() == null){
				 	list.add(aggm); //leafNodes.add(aggm);
				}else //Hasta aquí
					list.add(aggm.getBaseMeasure());
			}
		}
		
		//Si la aggregated tiene una base measure, NO se agrega, pero SI se agrega la baseM
		//Si la aggregated tiene OTRA aggregated, No se agrega y se hace recursivo
		//Si la aggregated tiene una DERIVED, No se agrega y se hace recursivo
		
		return list;
	}
	
	///////////////////////////////////////////////////////////////
	
	public void printMeasureStructure(){
		
		System.out.println("Measure Structure::");
		System.out.println("- " + cleanString(this.measure.getClass().getName()) + "["+this.measure.getName()+"]");
		
		if(this.measure.getClass().equals(DerivedSingleInstanceMeasure.class) || 
		   this.measure.getClass().equals(DerivedMultiInstanceMeasure.class) || 
		   this.measure.getClass().equals(ListMeasure.class)){
			
			DerivedMeasure dsim = (DerivedMeasure)this.measure;
			Object[] classes = new Object[dsim.getUsedMeasureMap().size()];
			classes  = dsim.getUsedMeasureMap().values().toArray();
			
			for(int i=0; i<classes.length; i++){
				
				if(cleanString(classes[i].getClass().getName()).equals("DerivedSingleInstanceMeasure") || 
				   cleanString(classes[i].getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).equals("ListMeasure") ){
						System.out.println("   · " + cleanString(classes[i].getClass().getName()));
						printMeasureStructure((DerivedMeasure) classes[i], "      · ");
				}else{
					
					if(cleanString(classes[i].getClass().getName()).equals("AggregatedMeasure")){
						System.out.println("   · " + cleanString(classes[i].getClass().getName()));
						printMeasureStructure((AggregatedMeasure) classes[i], "      · ");
					}else{
						System.out.println("   · " + cleanString(classes[i].getClass().getName()));
						/*cleanString(classes[i].getClass().getName()).equals("TimeMeasure") ||
						   cleanString(classes[i].getClass().getName()).equals("CountMeasure") ||
						   cleanString(classes[i].getClass().getName()).equals("StateConditionMeasure") ||
						   cleanString(classes[i].getClass().getName()).equals("DataMeasure") 
						*/
					}
				}	
			}				
		}else{
			if(this.measure.getClass().equals(AggregatedMeasure.class)){
				//Imprimir la baseMeasure, pero si es aggregated o derivada hacer el recursivo
				AggregatedMeasure aggm = (AggregatedMeasure)this.measure;
				System.out.println("   · "+cleanString(aggm.getBaseMeasure().getClass().getName()));
				if( cleanString(aggm.getBaseMeasure().getClass().getName()).equals("AggregatedMeasure") ){
					printMeasureStructure((AggregatedMeasure)aggm.getBaseMeasure(), "      · ");
				}else{
					if( cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMeasure") ||
						cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
						cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
						cleanString(aggm.getBaseMeasure().getClass().getName()).equals("ListMeasure")){
						printMeasureStructure((DerivedMeasure)aggm.getBaseMeasure(), "      · ");
					}
				}	
			} //else --> System.out.println(" >> Nodo hoja"); Ya ha sido impreso al inicio
		}
	}
	
	
	public void printMeasureStructure(DerivedMeasure dm, String tab){
		Object[] classes = new Object[dm.getUsedMeasureMap().size()];
		classes  = dm.getUsedMeasureMap().values().toArray();
		for(int i=0; i<classes.length; i++){
			System.out.println(tab + cleanString(classes[i].getClass().getName()));
			
			if(cleanString(classes[i].getClass().getName()).equals("DerivedMeasure") ||
			   cleanString(classes[i].getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
			   cleanString(classes[i].getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
			   cleanString(classes[i].getClass().getName()).equals("ListMeasure") ){
				printMeasureStructure((DerivedMeasure) classes[i],"      "+tab);
			}else{
				if(cleanString(classes[i].getClass().getName()).equals("AggregatedMeasure")){
					printMeasureStructure((AggregatedMeasure) classes[i], "   " + tab);
				}
			}
		}
	}
	
	
	public void printMeasureStructure(AggregatedMeasure aggm, String tab){
		//System.out.println(tab + cleanString(aggm.getClass().getName()));
		if(cleanString(aggm.getBaseMeasure().getClass().getName()).equals(AggregatedMeasure.class)){
			System.out.println(tab + cleanString(aggm.getClass().getName()));
			//System.out.println(tab + aggm.getBaseMeasure().getClass().getName());
			printMeasureStructure((AggregatedMeasure) aggm.getBaseMeasure(), "      "+tab);
		}else{
			if(cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMeasure") ||
			   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedSingleInstanceMeasure") ||
			   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("DerivedMultiInstanceMeasure") ||
			   cleanString(aggm.getBaseMeasure().getClass().getName()).equals("ListMeasure") ){
					//System.out.println(tab + cleanString(aggm.getClass().getName()));
					System.out.println("   "+tab + cleanString(aggm.getBaseMeasure().getClass().getName()));	
					printMeasureStructure((DerivedMeasure) aggm.getBaseMeasure(),"      "+tab);
			}else{
				System.out.println("   "+tab + cleanString(aggm.getBaseMeasure().getClass().getName()));
			}
		}	
	}
	
	///////////////////////////////////////////////////////////////
	
	public NumberOfTypes  getNumberOfLeafMeasuresByTypes(){
		
		int numAggregated = 0;
		int numCount = 0;
		int numData = 0;
		int numStateCondition = 0;
		int numTime = 0;
		int numDerivedSingle = 0;
		int numDerivedMulti = 0;
		int numList = 0;
		int numExternalValue = 0;
		int numOtros = 0;
			
		List<MeasureDefinition> list = this.getLeafNodeMeasures();
		
		if(list.size() > 0){
			                              
			for(int i=0; i<list.size(); i++){
				
				if(cleanString(list.get(i).getClass().getName()).equals("AggregatedMeasure")){
					numAggregated++;
				}else{
					if(cleanString(list.get(i).getClass().getName()).equals("CountMeasure")){
						numCount++;
					}else{
						if(cleanString(list.get(i).getClass().getName()).equals("DataMeasure")){
							numData++;
						}else{
							if(cleanString(list.get(i).getClass().getName()).equals("StateConditionMeasure")){
								numStateCondition++;
							}else{
								if(cleanString(list.get(i).getClass().getName()).equals("TimeMeasure")){
									numTime++;
								}else{
									if(cleanString(list.get(i).getClass().getName()).equals("DerivedSingleInstanceMeasure")){
										numDerivedSingle++;
									}else{
										if(cleanString(list.get(i).getClass().getName()).equals("DerivedMultiInstanceMeasure")){
											numDerivedMulti++;
										}else{
											if(cleanString(list.get(i).getClass().getName()).equals("ListMeasure")){
												numList++;
											}else{
												numOtros++;
											}
										}
									}
								}	
							}	
						}	
					}
				}
				
			}
			
		}
		
		return new NumberOfTypes(numAggregated, numCount, numData, numStateCondition, numTime, numDerivedSingle, numDerivedMulti, numList, numExternalValue, numOtros);
		
	}

	///////////////////////////////////////////////////////////////
	
	public String cleanString(String stringToClean){
		return stringToClean.substring(stringToClean.lastIndexOf(".")+1, stringToClean.length());
	}
	
	public ExpandedMeasure clone() {
        final ExpandedMeasure clone;
        try {
        	clone = (ExpandedMeasure) super.clone();
        }
        catch( Exception ex ) { //CloneNotSupportedException
            throw new RuntimeException( "\t!>>>> Excepción en ExpandedMeasure - clone() \n" + ex.getMessage());
        }
        
        return clone;
    }
	
	///////////////////////////////////////////////////////////////
	
	public void printAttributes(){
		
		try{
			
			System.out.println("Expanded Measure :: " + this.getName());
			System.out.println("- Id: " + this.getId());
			System.out.println("- Name: " + this.getName());
			System.out.println("- Description: " + this.getDescription());
			System.out.println("- Scale: " + this.getScale());
			System.out.println("- Unit of measure: " + this.getUnitOfMeasure() );
			
			//System.out.println("- Measure assigned: [" + cleanString(this.getMeasure().getClass().getName())+", "+this.getMeasure().getName()+"]");
			this.printMeasuresConnections(this.getMeasure());
		}catch(Exception e){
			System.out.println("- Measure assigned: Not identified");
		}
	}
	
	public void printMeasuresConnections(MeasureDefinition measure){
			
		System.out.println("- Measures and connections:");
		
		if( cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure") ||
			cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
			cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
			cleanString(measure.getClass().getName()).contentEquals("ListMeasure") ){
			
			printMeasuresConnections((DerivedMeasure)measure, "   ");
			
		}else{
			
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){

				printMeasuresConnections((AggregatedMeasure) measure, "   ");
				
			}else{
				
				//System.out.println("IMPRIMIR LA BASE" + cleanString(measure.getClass().getName()) + " - " + measure.getName());
				if(cleanString(measure.getClass().getName()).contentEquals("TimeMeasure")){
					TimeMeasure tm = (TimeMeasure)measure;
					System.out.println("   "+ cleanString(tm.getClass().getName()) + "["+tm.getName()+"]");
					
					try{
						System.out.println("   " + "> From:: " + tm.getFrom().getAppliesTo());
						System.out.println("   " + "> To:: " + tm.getTo().getAppliesTo());
						
					}catch(Exception e){
						System.out.println("   " + "> From:: [[Unidentified]]");
						System.out.println("   " + "> To:: [[Unidentified]]");
					}
					
				}else{
					if(cleanString(measure.getClass().getName()).contentEquals("CountMeasure")){
						
						CountMeasure cm = (CountMeasure)measure;
						System.out.println("   "+ cleanString(cm.getClass().getName()) + "["+cm.getName()+"]");
						
						try{
							System.out.println("   " + "> When:: " + cm.getWhen().getAppliesTo());
														
						}catch(Exception e){
							System.out.println("   " + "> When:: [[Unidentified]]");
						}
						
					}else{
						if(cleanString(measure.getClass().getName()).contentEquals("DataMeasure")){
							DataMeasure dm = (DataMeasure)measure;
							System.out.println("   " + cleanString(dm.getClass().getName()) + "["+dm.getName()+"]");
							
							try{
								System.out.println("   "  + "> DataContentSelection:: " + dm.getDataContentSelection().getDataobjectId());
															
							}catch(Exception e){
								System.out.println("   "  + "> DataContentSelection:: [[Unidentified]]");
							}
						}else{
							if(cleanString(measure.getClass().getName()).contentEquals("StateConditionMeasure")){
								StateConditionMeasure scm = (StateConditionMeasure)measure;
								System.out.println("   "+  cleanString(scm.getClass().getName()) + "["+scm.getName()+"]");
								
								try{
									System.out.println("   " + "> Condition:: " + scm.getCondition().getAppliesTo());
																
								}catch(Exception e){
									System.out.println("   " + "> Condition:: [[Unidentified]]");
								}
							}else{
								System.out.println("Measure unidentified ::> "+ cleanString(measure.getClass().getName()));
							}
						}
					}
				}
				
			}
		}
	}
	
	public void printMeasuresConnections(DerivedMeasure derived, String tab){
	
		System.out.println(tab + cleanString(derived.getClass().getName()) + "["+derived.getName() + "] connected with: ");
		
		Object[] classes = new Object[derived.getUsedMeasureMap().size()];
		classes  = derived.getUsedMeasureMap().values().toArray();
		for(int i=0; i<classes.length; i++){
			
			if( cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiMeasure") ||
				cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
				
				printMeasuresConnections((DerivedMeasure)classes[i], "   " + tab);
				
			}else{
				
				if( cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure") ){
					
					printMeasuresConnections((AggregatedMeasure)classes[i], "   " + tab);
					
				}else{
					
					//System.out.println("IMPRIMIR LA BASE" + cleanString(classes[i].getClass().getName()));
					
					if(cleanString(classes[i].getClass().getName()).contentEquals("TimeMeasure")){
						TimeMeasure tm = (TimeMeasure)classes[i];
						System.out.println("   "+ tab + cleanString(tm.getClass().getName()) + "["+tm.getName()+"]");
						
						try{
							System.out.println("   " + tab + "> From:: " + tm.getFrom().getAppliesTo());
							System.out.println("   " + tab + "> To:: " + tm.getTo().getAppliesTo());
							
						}catch(Exception e){
							System.out.println("   " + tab + "> From:: [[Unidentified]]");
							System.out.println("   " + tab + "> To:: [[Unidentified]]");
						}
						
					}else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("CountMeasure")){
							
							CountMeasure cm = (CountMeasure)classes[i];
							System.out.println("   "+ tab + cleanString(cm.getClass().getName()) + "["+cm.getName()+"]");
							
							try{
								System.out.println("   " + tab + "> When:: " + cm.getWhen().getAppliesTo());
															
							}catch(Exception e){
								System.out.println("   " + tab + "> When:: [[Unidentified]]");
							}
							
						}else{
							if(cleanString(classes[i].getClass().getName()).contentEquals("DataMeasure")){
								DataMeasure dm = (DataMeasure)classes[i];
								System.out.println("   "+ tab + cleanString(dm.getClass().getName()) + "["+dm.getName()+"]");
								
								try{
									System.out.println("   " + tab + "> DataContentSelection:: " + dm.getDataContentSelection().getDataobjectId());
																
								}catch(Exception e){
									System.out.println("   " + tab + "> DataContentSelection:: [[Unidentified]]");
								}
							}else{
								if(cleanString(classes[i].getClass().getName()).contentEquals("StateConditionMeasure")){
									StateConditionMeasure scm = (StateConditionMeasure)classes[i];
									System.out.println("   "+ tab + cleanString(scm.getClass().getName()) + "["+scm.getName()+"]");
									
									try{
										System.out.println("   " + tab + "> Condition:: " + scm.getCondition().getAppliesTo());
																	
									}catch(Exception e){
										System.out.println("   " + tab + "> Condition:: [[Unidentified]]");
									}
								}else{
									System.out.println("Measure unidentified ::> "+ cleanString(classes[i].getClass().getName()));
								}
							}
						}
					}
					
				}
				
			}
			
		}

		
	}
	
	public void printMeasuresConnections(AggregatedMeasure aggregated, String tab){
		
		System.out.println(tab + cleanString(aggregated.getClass().getName()) +"["+ aggregated.getName() + "] aggregates: ");
		
		if( cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
			cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
			cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiMeasure") ||
			cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure") ){
			
			printMeasuresConnections((DerivedMeasure) aggregated.getBaseMeasure(), "   " + tab);
			
		}else{
			
			if( cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure") ){
				
				printMeasuresConnections((AggregatedMeasure) aggregated.getBaseMeasure(), "   " + tab);
				
			}else{
				
				//PRINT BASE MEASURES Y SUS CONEXIONES
				System.out.println("   "+ tab + cleanString(aggregated.getBaseMeasure().getClass().getName()) + "["+aggregated.getBaseMeasure().getName()+"]");
				
				if(cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("TimeMeasure")){
					TimeMeasure tm = (TimeMeasure)aggregated.getBaseMeasure();
					try{
						System.out.println("   " + tab + "> From:: " + tm.getFrom().getAppliesTo());
						System.out.println("   " + tab + "> To:: " + tm.getTo().getAppliesTo());
						
					}catch(Exception e){
						System.out.println("   " + tab + "> From:: [[Unidentified]]");
						System.out.println("   " + tab + "> To:: [[Unidentified]]");
					}
					
				}else{
					if(cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("CountMeasure")){
						
						CountMeasure cm = (CountMeasure)aggregated.getBaseMeasure();
						try{
							System.out.println("   " + tab + "> When:: " + cm.getWhen().getAppliesTo());
														
						}catch(Exception e){
							System.out.println("   " + tab + "> When:: [[Unidentified]]");
						}
						
					}else{
						if(cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("DataMeasure")){
							DataMeasure dm = (DataMeasure)aggregated.getBaseMeasure();
							try{
								System.out.println("   " + tab + "> DataContentSelection:: " + dm.getDataContentSelection().getDataobjectId());
															
							}catch(Exception e){
								System.out.println("   " + tab + "> DataContentSelection:: [[Unidentified]]");
							}
						}else{
							if(cleanString(aggregated.getBaseMeasure().getClass().getName()).contentEquals("StateConditionMeasure")){
								StateConditionMeasure scm = (StateConditionMeasure)aggregated.getBaseMeasure();
								try{
									System.out.println("   " + tab + "> Condition:: " + scm.getCondition().getAppliesTo());
																
								}catch(Exception e){
									System.out.println("   " + tab + "> Condition:: [[Unidentified]]");
								}
							}else{
								System.out.println("Measure unidentified ::> "+ cleanString(aggregated.getBaseMeasure().getClass().getName()));
							}
						}
					}
				}
				
			}
			
		}
		
	}

	//
	
	public void printLeafMeasures(List<MeasureDefinition> list){
		
		if(list.isEmpty()){
			System.out.println("Expanded measure is not defined.");
			return;
		}
		
		if(list.size() > 0){
			for(int i = 0; i<list.size(); i++){
				System.out.println("- MeasureType: " + cleanString(list.get(i).getClass().getName()));
			}
		}
		
	}

	public TimeMeasure connectTime(TimeConnector cctime){
						
		TimeMeasure tm = null;
		
		if(this.measure.getName().contentEquals(cctime.getName())){
			
			//TimeMeasure 
			tm = (TimeMeasure) this.measure;
			tm.setTo(cctime.getCcTo());
			tm.setFrom(cctime.getCcFrom());
			this.setMeasure(tm);

		}else{
			System.out.println("(connectTime) FALTAN MEDIDAS POR INTENTAR CONECTAR");
			//tm = null;
		}	
		return tm;
	}
	
	public AggregatedMeasure connectAggregated(AggregatedConnector ccagg){
		
		AggregatedMeasure aggm = (AggregatedMeasure)this.measure;
		
		if(this.measure.getName().contentEquals(ccagg.getName())){
			
			if(cleanString(ccagg.getCcBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
			   cleanString(ccagg.getCcBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
			   cleanString(ccagg.getCcBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
			   cleanString(ccagg.getCcBaseMeasure().getClass().getName()).contentEquals("ListMeasure") ){
						
					System.out.println("Una Derived dentro de un Aggregated");
						
			}else{
				
				if(cleanString(ccagg.getCcBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
					
					System.out.println("Una Aggregated dentro de una Aggregated");
					
				}else{
					
					aggm.setBaseMeasure(ccagg.getCcBaseMeasure());
					this.setMeasure(aggm);	
				}
				
			}
			
		}else{
			System.out.println("(connectAggregated) Las medidas no coinciden, esperar a buscar otra");
			aggm = null;
		}	
		return aggm;
	}
	
}
