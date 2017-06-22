package es.us.isa.ppinot.model.composite;

import java.util.HashMap;
import java.util.Map;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.connector.CompositeConnector;
import es.us.isa.ppinot.model.connector.NumberOfTypes;
import es.us.isa.ppinot.model.connector.aggregated.AggregatedConnector;
import es.us.isa.ppinot.model.connector.base.CountConnector;
import es.us.isa.ppinot.model.connector.base.DataConnector;
import es.us.isa.ppinot.model.connector.base.StateConditionConnector;
import es.us.isa.ppinot.model.connector.base.TimeConnector;
import es.us.isa.ppinot.model.connector.derived.DerivedConnector;
import es.us.isa.ppinot.model.connector.derived.DerivedMultiInstanceConnector;
import es.us.isa.ppinot.model.connector.derived.DerivedSingleInstanceConnector;
import es.us.isa.ppinot.model.connector.derived.ListConnector;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.ListMeasure;

public class CollapsedMeasure extends CompositeMeasure{

	private String expandedCompositeName;
	private Map<String, CompositeConnector> usedConnectorMap;
	//private ExpandedMeasure expanded; - Idea original, lo elimino
	
	public CollapsedMeasure(){
		super();
		this.setExpandedCompositeName("");
	}
	
	public CollapsedMeasure(String id, String name, String description, String scale, String unitOfMeasure, String expandedCompositeName){
		super(id, name, description, scale, unitOfMeasure);
		this.setExpandedCompositeName(expandedCompositeName);
	}
	
	//Idea original, lo elimino
	/*public ExpandedMeasure getExpanded() {
		return expanded;
	}
	
	public void setExpanded(ExpandedMeasure expanded) {
		this.expanded = expanded;
	}*/

	public void setExpandedCompositeName(String expandedCompositeName){
		this.expandedCompositeName = expandedCompositeName;
	}
	
	public String getExpandedCompositeName(){
		return this.expandedCompositeName;
	}
	
	public Map<String, CompositeConnector> getUsedConnectorMap(){
		if(this.usedConnectorMap == null)
			this.usedConnectorMap = new HashMap<String, CompositeConnector>();
		return this.usedConnectorMap;
	}
	
	public void addUsedConnector(String variable, CompositeConnector connector){
		this.getUsedConnectorMap().put((variable.contentEquals(""))?connector.getId():variable, connector);
	}
	
	public CompositeConnector getUsedCompositeConnectorId(String id){
		return this.getUsedConnectorMap().get(id);
	}
	
	///////////////////////////////////////////////////////////////
	
	public void printConnectors(){
		
		System.out.println("Measure: " + cleanString(this.getClass().getName()) + "["+this.getName()+"]");
		
		if(this.getUsedConnectorMap() != null && this.getUsedConnectorMap().size() > 0){
			
			Object[] classes = new Object[this.getUsedConnectorMap().size()];
			classes = this.getUsedConnectorMap().values().toArray();
			                              
			for(int i=0; i<classes.length; i++){
				System.out.println(" - " + cleanString(classes[i].getClass().getName()));
			}
			
		}else{
			System.out.println("- ExpandedMeasure doesn't have Connectors");
		}
		
	}
	
	///////////////////////////////////////////////////////////////
	
	public boolean valid(){
	
		//Que todos los conectores tengan un nombre
		
		return this.getName() != null && !this.getName().contentEquals("") && this.getExpandedCompositeName() != null && !this.getExpandedCompositeName().contentEquals("") && this.usedConnectorMap != null && this.usedConnectorMap.size() > 0 && wellConnectedAndNamed();
		
	}
	
	public boolean wellConnectedAndNamed(){
		
		//Id and Name : Not empty, not null
		//Connections : AppliesTo not null
		
		boolean temporalResponse = true;
		
		if(this.getUsedConnectorMap() != null && this.getUsedConnectorMap().size() > 0){
			
			Object[] connectors = new Object[this.getUsedConnectorMap().size()];
			connectors  = this.getUsedConnectorMap().values().toArray();
			
			for(int i = 0; i < connectors.length; i++){
				
				if(temporalResponse == false)
					break;
					
				if( cleanString(connectors[i].getClass().getName()).contentEquals("TimeConnector") ){
					
					TimeConnector cctime = (TimeConnector)connectors[i];
					if( !cctime.valid())
						temporalResponse = false;

				}else{
					if( cleanString(connectors[i].getClass().getName()).contentEquals("CountConnector") ){
						
						CountConnector cccount = (CountConnector)connectors[i];
						if(!cccount.valid())
							temporalResponse = false;
						
					}else{
						if( cleanString(connectors[i].getClass().getName()).contentEquals("StateConditionConnector") ){
							
							StateConditionConnector ccstatecond = (StateConditionConnector)connectors[i];
							if(!ccstatecond.valid())
								temporalResponse = false;
							
						}else{
							if( cleanString(connectors[i].getClass().getName()).contentEquals("DataConnector") ){
								
								DataConnector ccdata = (DataConnector)connectors[i];
								if(!ccdata.valid())
									temporalResponse = false;
								
							}else{
								if( cleanString(connectors[i].getClass().getName()).contentEquals("DerivedSingleInstanceConnector") ){
									
									DerivedSingleInstanceConnector ccdersim = (DerivedSingleInstanceConnector)connectors[i];
									if(!ccdersim.valid())
										temporalResponse = false;
									
								}else{
									if( cleanString(connectors[i].getClass().getName()).contentEquals("DerivedMultiInstanceConnector") ){
																			
										DerivedMultiInstanceConnector ccdermim = (DerivedMultiInstanceConnector)connectors[i];
										if(!ccdermim.valid())
											temporalResponse = false;
										
									}else{
										if( cleanString(connectors[i].getClass().getName()).contentEquals("ListConnector") ){
																						
											ListConnector cclist = (ListConnector)connectors[i];
											if(!cclist.valid())
												temporalResponse = false;
											
										}else{
											if( cleanString(connectors[i].getClass().getName()).contentEquals("AggregatedConnector") ){
												
												AggregatedConnector ccagg = (AggregatedConnector)connectors[i];
												if(!ccagg.valid())
													temporalResponse = false;
												
											}else{
												System.out.println("No se ha encontrado el tipo correcto de connector:: " + connectors[i].getClass().getName());
											}
										}
									}
								}
							}
						}
					}
				}
				
				
				
			}
			
		}else{
			return false;
		}
				
		return temporalResponse;
		
	}

	///////////////////////////////////////////////////////////////
	
	public ExpandedMeasure connectCollapsedToExpanded(ExpandedMeasure expm){
		
		ExpandedMeasure response = new ExpandedMeasure();
		
		if(this.getExpandedCompositeName().equals(expm.getName())){
			
			if(expm.valid() && this.valid()){
				
				//System.out.println("-> " + this.getUsedConnectorMap().size());
				//System.out.println("-> " + expm.getLeafNodeMeasures().size());
				
				if(this.getUsedConnectorMap().size() == expm.getLeafNodeMeasures().size()){
					
					//NumberOfTypes numMeasures = expm.getNumberOfLeafMeasuresByTypes();
					//NumberOfTypes numConnectors = this.getNumberOfConnectorsByTypes();
								
					//expm.printLeafMeasures(expm.getLeafNodeMeasures());
					//this.printNumberOfConnectorsByTypes(numConnectors);
					
					/*if( numMeasures.numAggregated == numConnectors.numAggregated &&
						numMeasures.numCount == numConnectors.numCount &&
						numMeasures.numData == numConnectors.numData &&
						numMeasures.numStateCondition == numConnectors.numStateCondition &&
						numMeasures.numTime == numConnectors.numTime &&
						numMeasures.numDerivedSingle == numConnectors.numDerivedSingle &&
						numMeasures.numDerivedMulti == numConnectors.numDerivedMulti &&
						numMeasures.numList == numConnectors.numList &&
						numMeasures.numExternalValue == numConnectors.numExternalValue &&
						numMeasures.numOther == numConnectors.numOther ){
					*/
					if(this.hasEqualTypeOfElements(expm.getNumberOfLeafMeasuresByTypes(), this.getNumberOfConnectorsByTypes())){
						//PUEDO INTENTAR CONECTAR LAS MEDIDAS
						
						//Clono el patrón
						response = expm.clone();
						
						/*
						 * TEST
						 *
						 * */
						//Must be true and objects must have different memory addresses
						//System.out.println(expm != response);
						 
					    //As we are returning same class; so it should be true
					    //System.out.println(expm.getClass() == response.getClass());
					 
					    //Default equals method checks for references so it should be false. If we want to make it true,
					    //we need to override equals method in Employee class.
					    //System.out.println(expm.equals(response));
						
						
						
						//Copio las conexiones de la collapsed a la expanded clonada
						
						//System.out.println("->" + this.getUsedConnectorMap().size());
						
						Object[] connectors = new Object[this.getUsedConnectorMap().size()];
						connectors  = this.getUsedConnectorMap().values().toArray();
											
						for(int i = 0; i < connectors.length; i++){
							
							//System.out.println("[[CONNECTOR::"+i+"]] " + connectors[i].getClass().getName());
							if(cleanString(connectors[i].getClass().getName()).contentEquals("TimeConnector")){
								response.setMeasure(connectMeasure((TimeConnector)connectors[i], response.getMeasure()));
							}
							else{
								if(cleanString(connectors[i].getClass().getName()).contentEquals("CountConnector")){
									response.setMeasure(connectMeasure((CountConnector)connectors[i], response.getMeasure()));
								}
								else{
									if(cleanString(connectors[i].getClass().getName()).contentEquals("StateConditionConnector")){
										response.setMeasure(connectMeasure((StateConditionConnector)connectors[i], response.getMeasure()));
									}
									else{
										if(cleanString(connectors[i].getClass().getName()).contentEquals("DataConnector")){
											response.setMeasure(connectMeasure((DataConnector)connectors[i], response.getMeasure()));
										}
										else{
											if(cleanString(connectors[i].getClass().getName()).contentEquals("DerivedConnector")){
												System.out.println("Antes de llamar a DerivedConnector ");
												response.setMeasure(connectMeasure((DerivedConnector)connectors[i], response.getMeasure()));
											}else{
												if(cleanString(connectors[i].getClass().getName()).contentEquals("DerivedSingleInstanceConnector")){
													System.out.println("Antes de llamar a DerivedSingleInstanceConnector ");
													response.setMeasure(connectMeasure((DerivedSingleInstanceConnector)connectors[i], response.getMeasure()));
												}else{
													if(cleanString(connectors[i].getClass().getName()).contentEquals("DerivedMultiInstanceConnector")){
														System.out.println("Antes de llamar a DerivedMultiInstanceConnector ");
														response.setMeasure(connectMeasure((DerivedMultiInstanceConnector)connectors[i], response.getMeasure()));
													}else{
														if(cleanString(connectors[i].getClass().getName()).contentEquals("ListConnector")){
															response.setMeasure(connectMeasure((ListConnector)connectors[i], response.getMeasure()));
														}else{
															if(cleanString(connectors[i].getClass().getName()).contentEquals("AggregatedConnector")){
																response.setMeasure(connectMeasure((AggregatedConnector)connectors[i], response.getMeasure()));
															}
															else{
																System.out.println("[ERROR]:: Connector unidentified --> "+ cleanString(connectors[i].getClass().getName()));
															}
														}	
													}	
												}
											}
										}
									}
								}
							}
							
							//
							
							/*if( cleanString(connectors[i].getClass().getName()).contentEquals("TimeConnector") ){
								
								TimeMeasure tm = expm.connectTime((TimeConnector)connectors[i]);
								
								//TimeConnector cctime = (TimeConnector)connectors[i];
								//TimeMeasure timem = (TimeMeasure) response.g
							}
							
							if(cleanString(connectors[i].getClass().getName()).contentEquals("AggregatedConnector")){
								expm.connectAggregated((AggregatedConnector)connectors[i]);
							}*/
							
						}
						
						response.setName(this.getName());
						
						//for(int i = 0; i < this.getUsedConnectorMap().size(); i++){
							
							//System.out.println("> No tengo ni idea de cómo copiar las conexiones.");
							//System.out.println("> Tiene que ser por nombre, no puede ser aleatorio.");
							//System.out.println("");
							
							//System.out.println("->" + );
						//}
						

					}else{
						System.out.println("- [ERROR] Types of connectors in CollapsedMeasure do not coincide with the types of leaf measures in an ExpandedMeasure.");
						response = null;
					}
					
				}else{
					System.out.println("- [ERROR] The number of connectors in CollapsedMeasure does not coincide with the number of leaf measures in ExpandedMeasure. ");
					response = null;
				}
				
			}else{
				System.out.println("- [ERROR] At least one measure is not valid: \n  > ExpandedMeasure: " + expm.valid() +"\n  > CollapsedMeasure: "+ this.valid()); 
				response = null;
			}
		}else{
			System.out.println("- [ERROR] The name of the ExpandedMeasure in CollapsedMeasure does not coincide with the name of the ExpandedMeasure provided.");
			response = null;
		}
		
		return response;
	}

	//Método utilizado para copiar las conexiones de un TimeConnector a una ExpandedMeasure
	//2017-03-21
	public MeasureDefinition connectMeasure(TimeConnector cctime, MeasureDefinition measure){
		
		if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure")||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
	
			DerivedMeasure derm = (DerivedMeasure)measure;
			
			//System.out.println("***>> " + derm.getName());
					
			Object[] classes = new Object[derm.getUsedMeasureMap().size()];
			classes = derm.getUsedMeasureMap().values().toArray();
			                              
			for(int i=0; i<classes.length; i++){

				if(cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
					
					DerivedMeasure aux = (DerivedMeasure) connectMeasure(cctime, (DerivedMeasure) classes[i]);
					derm.addUsedMeasure(aux.getName(), aux);
					
				}else{
					if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
						
						AggregatedMeasure aux = (AggregatedMeasure) connectMeasure (cctime,(AggregatedMeasure) classes[i]);
						derm.addUsedMeasure(aux.getName(), aux);
						
					}else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("TimeMeasure")){
						
							TimeMeasure tm = (TimeMeasure)classes[i];
							if(tm.getName().contentEquals(cctime.getName())){
								tm.setFrom(cctime.getCcFrom());
								tm.setTo(cctime.getCcTo());
							}
						}
					}
				}
				measure = derm;	
			}
			
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
								
				AggregatedMeasure aggm = (AggregatedMeasure)measure;
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){
					
					measure = connectMeasure(cctime, aggm.getBaseMeasure());
					
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
						
						measure = connectMeasure(cctime, aggm.getBaseMeasure());
						
					}else{
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("TimeMeasure")){
							TimeMeasure timem = (TimeMeasure) aggm.getBaseMeasure();
							
							if(cleanString(aggm.getBaseMeasure().getName()).contentEquals(cctime.getName())){
								timem.setTo(cctime.getCcTo());
								timem.setFrom(cctime.getCcFrom());
								aggm.setBaseMeasure(timem);
								measure = aggm;
							}
						}
					}
				}
				
			}else{
				if(cleanString(measure.getClass().getName()).contentEquals("TimeMeasure")){
					
					TimeMeasure timem = (TimeMeasure) measure;
					if(measure.getName().contentEquals(timem.getName())){
						
						timem.setTo(cctime.getCcTo());
						timem.setFrom(cctime.getCcFrom());				
					}
					
					measure = timem;
				}
			}
		}
		return measure;
	}
	
	//Método utilizado para copiar las conexiones de un CountConnector a una ExpandedMeasure
	//2017-03-21
	public MeasureDefinition connectMeasure(CountConnector cccount, MeasureDefinition measure){
		
		if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure")||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
	
			DerivedMeasure derm = (DerivedMeasure)measure;
			
			//System.out.println("***>> " + derm.getName());
					
			Object[] classes = new Object[derm.getUsedMeasureMap().size()];
			classes = derm.getUsedMeasureMap().values().toArray();
			                              
			for(int i=0; i<classes.length; i++){

				if(cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
					
					DerivedMeasure aux = (DerivedMeasure) connectMeasure(cccount, (DerivedMeasure) classes[i]);
					derm.addUsedMeasure(aux.getName(), aux);
					
				}else{
					if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
						
						AggregatedMeasure aux = (AggregatedMeasure) connectMeasure (cccount,(AggregatedMeasure) classes[i]);
						derm.addUsedMeasure(aux.getName(), aux);
						
					}else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("CountMeasure")){
							
							CountMeasure countm = (CountMeasure) classes[i];
							if( countm.getName().contentEquals(cccount.getName()) ){
								countm.setWhen(cccount.getCcWhen());
							}
						}
					}
				}
				measure = derm;	
			}
			
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
								
				AggregatedMeasure aggm = (AggregatedMeasure)measure;
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){
					
					measure = connectMeasure(cccount, aggm.getBaseMeasure());
					
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
						
						measure = connectMeasure(cccount, aggm.getBaseMeasure());
						
					}else{
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("CountMeasure")){
							
							CountMeasure countm = (CountMeasure) aggm.getBaseMeasure();
							
							if(cleanString(aggm.getBaseMeasure().getName()).contentEquals(cccount.getName())){
								countm.setWhen(cccount.getCcWhen());
								aggm.setBaseMeasure(countm);
								measure = aggm;
							}
						}
					}
				}
				
			}else{
				if(cleanString(measure.getClass().getName()).contentEquals("CountMeasure")){
					
					CountMeasure countm = (CountMeasure) measure;
					if(measure.getName().contentEquals(countm.getName())){
						countm.setWhen(cccount.getCcWhen());
					}
					
					measure = countm;
				}
			}
		}
		return measure;
	}
	
	//Método utilizado para copiar las conexiones de un DataConnector a una ExpandedMeasure
	//2017-03-22
	public MeasureDefinition connectMeasure(DataConnector ccdata, MeasureDefinition measure){
		
		if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure")||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
	
			DerivedMeasure derm = (DerivedMeasure)measure;
								
			Object[] classes = new Object[derm.getUsedMeasureMap().size()];
			classes = derm.getUsedMeasureMap().values().toArray();
			                              
			for(int i=0; i<classes.length; i++){

				if(cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
					
					DerivedMeasure aux = (DerivedMeasure) connectMeasure(ccdata, (DerivedMeasure) classes[i]);
					derm.addUsedMeasure(aux.getName(), aux);
					
				}else{
					if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
						
						AggregatedMeasure aux = (AggregatedMeasure) connectMeasure (ccdata,(AggregatedMeasure) classes[i]);
						derm.addUsedMeasure(aux.getName(), aux);
						
					}else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("DataMeasure")){
						
							DataMeasure datam = (DataMeasure)classes[i];
							if(datam.getName().contentEquals(ccdata.getName())){
								datam.setDataContentSelection(ccdata.getCcDataContentSelection());
							}
						}
					}
				}
				measure = derm;	
			}
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
								
				AggregatedMeasure aggm = (AggregatedMeasure)measure;
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){
					
					measure = connectMeasure(ccdata, aggm.getBaseMeasure());
					
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
						
						measure = connectMeasure(ccdata, aggm.getBaseMeasure());
						
					}else{
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DataMeasure")){
							DataMeasure datam = (DataMeasure) aggm.getBaseMeasure();
							
							if(cleanString(aggm.getBaseMeasure().getName()).contentEquals(ccdata.getName())){
								datam.setDataContentSelection(ccdata.getCcDataContentSelection());
								measure = aggm;
							}
						}
					}
				}
				
			}else{
				if(cleanString(measure.getClass().getName()).contentEquals("DataMeasure")){
					
					DataMeasure datam = (DataMeasure) measure;
					if(measure.getName().contentEquals(datam.getName())){
						
						datam.setDataContentSelection(ccdata.getCcDataContentSelection());
					}
					measure = datam;
				}
			}
		}
		return measure;
	} 

	//Método utilizado para copiar las conexiones de un StateConditionConnector a una ExpandedMeasure
	//2017-03-22
	public MeasureDefinition connectMeasure(StateConditionConnector ccstate, MeasureDefinition measure){
		
		if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure")||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
	
			DerivedMeasure derm = (DerivedMeasure)measure;
			
			//System.out.println("***>> " + derm.getName());
					
			Object[] classes = new Object[derm.getUsedMeasureMap().size()];
			classes = derm.getUsedMeasureMap().values().toArray();
			                              
			for(int i=0; i<classes.length; i++){

				if(cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
					
					DerivedMeasure aux = (DerivedMeasure) connectMeasure(ccstate, (DerivedMeasure) classes[i]);
					derm.addUsedMeasure(aux.getName(), aux);
					
				}else{
					if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
						
						AggregatedMeasure aux = (AggregatedMeasure) connectMeasure (ccstate,(AggregatedMeasure) classes[i]);
						derm.addUsedMeasure(aux.getName(), aux);
						
					}else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("StateConditionMeasure")){
						
							StateConditionMeasure statem = (StateConditionMeasure)classes[i];
							if(statem.getName().contentEquals(ccstate.getName())){
								statem.setCondition(ccstate.getCcStateCondition());
							}
						}
					}
				}
				measure = derm;	
			}
			
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
								
				AggregatedMeasure aggm = (AggregatedMeasure)measure;
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){
					
					measure = connectMeasure(ccstate, aggm.getBaseMeasure());
					
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
						
						measure = connectMeasure(ccstate, aggm.getBaseMeasure());
						
					}else{
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("StateConditionMeasure")){
							
							StateConditionMeasure statem = (StateConditionMeasure) aggm.getBaseMeasure();
							
							if(cleanString(aggm.getBaseMeasure().getName()).contentEquals(ccstate.getName())){
								statem.setCondition(ccstate.getCcStateCondition());
								aggm.setBaseMeasure(statem);
								measure = aggm;
							}
						}
					}
				}
				
			}else{
				if(cleanString(measure.getClass().getName()).contentEquals("StateConditionMeasure")){
					
					StateConditionMeasure statem = (StateConditionMeasure) measure;
					if(measure.getName().contentEquals(statem.getName())){
						statem.setCondition(ccstate.getCcStateCondition());			
					}
					
					measure = statem;
				}
			}
		}
		return measure;
	}

	public MeasureDefinition connectMeasure(ListConnector cclist, MeasureDefinition measure){
		
		if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure")||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ){
			
		   DerivedMeasure derm = (DerivedMeasure)measure;
					
		   //System.out.println("***>> " + derm.getName());
							
		   Object[] classes = new Object[derm.getUsedMeasureMap().size()];
		   classes = derm.getUsedMeasureMap().values().toArray();
					                              
		   for(int i=0; i<classes.length; i++){

				if(cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
					
					System.out.println("-->Lista con Derived");
					
					DerivedMeasure aux = (DerivedMeasure) connectMeasure(cclist, (DerivedMeasure) classes[i]);
					derm.addUsedMeasure(aux.getName(), aux);
					
				}else{
					if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
							
						AggregatedMeasure aux = (AggregatedMeasure) connectMeasure (cclist,(AggregatedMeasure) classes[i]);
						derm.addUsedMeasure(aux.getName(), aux);
							
					}else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure")){
								
							ListMeasure listm = (ListMeasure)classes[i];
							
							if(listm.getName().contentEquals(cclist.getName())){
								
								//List has been identified then, measures in the connector should be connected
								
								Object[] measureslist = new Object[cclist.getUsedCcMeasureMap().size()];
								measureslist = cclist.getUsedCcMeasureMap().values().toArray();
								   
								for(int j=0; j < measureslist.length; j++){
									
									MeasureDefinition aux = (MeasureDefinition)measureslist[j];
									listm.addUsedMeasure(aux.getName(), aux);
									
								}
							}
						}
					}
				}
				measure = derm;	
			}
					
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
										
				AggregatedMeasure aggm = (AggregatedMeasure)measure;
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){
						
					measure = connectMeasure(cclist, aggm.getBaseMeasure());
					
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
							
						measure = connectMeasure(cclist, aggm.getBaseMeasure());
							
					}else{
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){

							ListMeasure listm = (ListMeasure)aggm.getBaseMeasure();
							
							if(listm.getName().contentEquals(cclist.getName())){
								
								//List has been identified then, measures in the connector should be connected
								
								Object[] measureslist = new Object[cclist.getUsedCcMeasureMap().size()];
								measureslist = cclist.getUsedCcMeasureMap().values().toArray();
								   
								for(int j=0; j < measureslist.length; j++){	
									MeasureDefinition aux = (MeasureDefinition)measureslist[j];
									listm.addUsedMeasure(aux.getName(), aux);
								}
							}
							measure = listm;
						}
					}
				}
						
			}else{
				if(cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
							
					ListMeasure listm = (ListMeasure)measure;
					
					if(listm.getName().contentEquals(cclist.getName())){
						
						//List has been identified then, measures in the connector should be connected
						
						Object[] measureslist = new Object[cclist.getUsedCcMeasureMap().size()];
						measureslist = cclist.getUsedCcMeasureMap().values().toArray();
						   
						for(int j=0; j < measureslist.length; j++){	
							MeasureDefinition aux = (MeasureDefinition)measureslist[j];
							listm.addUsedMeasure(aux.getName(), aux);
						}
					}
					
					measure = listm;
				}
			}
		}
		return measure;
	}
	

	public MeasureDefinition connectMeasure(AggregatedConnector ccaggregated, MeasureDefinition measure){
		
		if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure")||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
			
			DerivedMeasure derm = (DerivedMeasure)measure;
					
			//System.out.println("***>> " + derm.getName());
							
			Object[] classes = new Object[derm.getUsedMeasureMap().size()];
			classes = derm.getUsedMeasureMap().values().toArray();
					                              
			for(int i=0; i<classes.length; i++){

				if(cleanString(classes[i].getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(classes[i].getClass().getName()).contentEquals("ListMeasure") ){
							
					DerivedMeasure aux = (DerivedMeasure) connectMeasure(ccaggregated, (DerivedMeasure) classes[i]);
					derm.addUsedMeasure(aux.getName(), aux);
							
				}else{
					if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
								
						AggregatedMeasure aux = (AggregatedMeasure) connectMeasure (ccaggregated,(AggregatedMeasure) classes[i]);
						derm.addUsedMeasure(aux.getName(), aux);
						//CAMBIAR AQUI
								
					}/*else{
						if(cleanString(classes[i].getClass().getName()).contentEquals("AggregatedMeasure")){
							
							AggregatedMeasure aggregatedm = (AggregatedMeasure)classes[i];
							if(aggregatedm.getName().contentEquals(ccaggregated.getName())){
								aggregatedm.setBaseMeasure(ccaggregated.getCcBaseMeasure());
							}
							//XxX
						}
					}*/
				}
				measure = derm;	
			}
					
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
										
				AggregatedMeasure aggm = (AggregatedMeasure)measure;
				if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
				   cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("ListMeasure")){
							
					measure = connectMeasure(ccaggregated, aggm.getBaseMeasure());
							
				}else{
					if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("AggregatedMeasure")){
							
						measure = connectMeasure(ccaggregated, aggm.getBaseMeasure());
								
					}else{
						if(cleanString(aggm.getBaseMeasure().getClass().getName()).contentEquals("BaseMeasure") || aggm.getBaseMeasure() == null ){
							//No tiene aggregada, es una hoja y se conecta							
							aggm.setBaseMeasure(ccaggregated.getCcBaseMeasure());
							measure = aggm;
							
						}
					}
				}
						
			}
		}
		return measure;

	}
	
	
	public MeasureDefinition connectMeasure(DerivedConnector ccderived, MeasureDefinition measure){
		
		System.out.println("Hacer la recursividad para DERIVED y AGGREGATED - DerivedConnector");
		
		//Luego es posible que sea necesario separar los Derived
		/*if(cleanString(measure.getClass().getName()).contentEquals("DerivedMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedSingleInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("DerivedMultiInstanceMeasure") ||
		   cleanString(measure.getClass().getName()).contentEquals("ListMeasure")){
			
			System.out.println("Hacer la recursividad para DERIVED - StateConditionConnector");
					
		}else{
			if(cleanString(measure.getClass().getName()).contentEquals("AggregatedMeasure")){
				System.out.println("Hacer la recursividad para AGGREGATED - StateConditionConnector");
			}else{
				if(cleanString(measure.getClass().getName()).contentEquals("StateConditionMeasure")){
					
					StateConditionMeasure statem = (StateConditionMeasure) measure;
					if(measure.getName().contentEquals(statem.getName())){
						
						statem.setCondition(ccstate.getCcStateCondition());
						measure = statem;
						
					}//else -> Hago nada y porque no es esta medida la que me interesa
				}else{
					System.out.println("Paso, porque la medida no es de Tiempo, no la conecto");
				}
			}
		}*/
		
		return measure;
	}
	
	
	
	/////////////////////////////////////////////////////////////////
		
	public boolean hasEqualTypeOfElements(NumberOfTypes numMeasures, NumberOfTypes numConnectors){
		if(numMeasures.numAggregated == numConnectors.numAggregated &&
				numMeasures.numCount == numConnectors.numCount &&
				numMeasures.numData == numConnectors.numData &&
				numMeasures.numStateCondition == numConnectors.numStateCondition &&
				numMeasures.numTime == numConnectors.numTime &&
				numMeasures.numDerivedSingle == numConnectors.numDerivedSingle &&
				numMeasures.numDerivedMulti == numConnectors.numDerivedMulti &&
				numMeasures.numList == numConnectors.numList &&
				numMeasures.numExternalValue == numConnectors.numExternalValue &&
				numMeasures.numOther == numConnectors.numOther){
			return true;
		}else{
			return false;
		}
	}
	
	
	////////////////////////////////////////////////////////////////
	
	public void printNumberOfConnectorsByTypes(NumberOfTypes num){
		System.out.println("numAggregated: " + num.getNumAggregated());
		System.out.println("numCount: " + num.getNumCount());
		System.out.println("numData: " + num.getNumData());
		System.out.println("numStateCondition: " + num.getNumStateCondition());
		System.out.println("numTime: " + num.getNumTime());
		System.out.println("numDerivedSingle: " + num.getNumDerivedSingle());
		System.out.println("numDerivedMulti: " + num.getNumDerivedMulti());
		System.out.println("numList: " + num.getNumList());
		System.out.println("numExternalValue: " + num.getNumExternalValue());
		System.out.println("numOther: " + num.getNumOther());
	}
	
	public NumberOfTypes  getNumberOfConnectorsByTypes(){
		
		int numAggregated = 0;
		int numCount = 0;
		int numData = 0;
		int numStateCondition = 0;
		int numTime = 0;
		int numDerivedSingle = 0;
		int numDerivedMulti = 0;
		int numList = 0;
		int numExternalValue = 0;
		int numOther = 0;
				
		if(this.getUsedConnectorMap().size() > 0){
			
			Object[] classes = new Object[this.getUsedConnectorMap().size()];
			classes = this.getUsedConnectorMap().values().toArray();
			                              
			for(int i=0; i<classes.length; i++){
				
				if(cleanString(classes[i].getClass().getName()).equals("AggregatedConnector")){
					numAggregated++;
				}else{
					if(cleanString(classes[i].getClass().getName()).equals("CountConnector")){
						numCount++;
					}else{
						if(cleanString(classes[i].getClass().getName()).equals("DataConnector")){
							numData++;
						}else{
							if(cleanString(classes[i].getClass().getName()).equals("StateConditionConnector")){
								numStateCondition++;
							}else{
								if(cleanString(classes[i].getClass().getName()).equals("TimeConnector")){
									numTime++;
								}else{
									if(cleanString(classes[i].getClass().getName()).equals("DerivedSingleInstanceConnector")){
										numDerivedSingle++;
									}else{
										if(cleanString(classes[i].getClass().getName()).equals("DerivedMultiInstanceConnector")){
											numDerivedMulti++;
										}else{
											if(cleanString(classes[i].getClass().getName()).equals("ListConnector")){
												numList++;
											}else{
												numOther++;
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
		
		return new NumberOfTypes(numAggregated, numCount, numData, numStateCondition, numTime, numDerivedSingle, numDerivedMulti, numList, numExternalValue, numOther);
		
	}
	
	
	
	//Para valid:
	// - Que el String no sea vacío ni null
	// - Tenga al menos un connector
	
	///////////////////////////////////////////////////////////////
	
	public String cleanString(String stringToClean){
		return stringToClean.substring(stringToClean.lastIndexOf(".")+1, stringToClean.length());
	}

}
