package es.us.isa.ppinot.handler;

import es.us.isa.bpmn.handler.ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.Scope;
import es.us.isa.ppinot.model.Target;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.*;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Clase que permite exportar e importar a XMLs de PPINOT. 
 * 
 * @author Edelia
 *
 */
public class PpiNotModelHandler extends ModelHandler implements PpiNotModelHandlerInterface {

    private TPpiset tPpiset;
    private String procId;

    // mapas con las instancias de las clases del modelo, obtenidas a partir de las clases Jaxb. La key es el id del elemento.
	private Map<String, TimeInstanceMeasure> timeInstanceModelMap;
	private Map<String, CountInstanceMeasure> countInstanceModelMap;
	private Map<String, StateConditionInstanceMeasure> stateConditionInstanceModelMap;
	private Map<String, DataInstanceMeasure> dataInstanceModelMap;
	private Map<String, DataPropertyConditionInstanceMeasure> dataPropertyConditionInstanceModelMap;

	private Map<String, AggregatedMeasure> timeAggregatedModelMap;
	private Map<String, AggregatedMeasure> countAggregatedModelMap;
	private Map<String, AggregatedMeasure> stateConditionAggregatedModelMap;
	private Map<String, AggregatedMeasure> dataAggregatedModelMap;
	private Map<String, AggregatedMeasure> dataPropertyConditionAggregatedModelMap;
	private Map<String, AggregatedMeasure> derivedSingleInstanceAggregatedModelMap;

	private Map<String, DerivedMeasure> derivedSingleInstanceModelMap;
	private Map<String, DerivedMeasure> derivedMultiInstanceModelMap;

	private Map<String, PPI> ppiModelMap;
	
	// objeto para obtener instancias de clases del modelo a partir de instancias de clases Jaxb
	private GeneratePpiNotModel generatePpiNotModel;


    /**
	 * Constructor de la clase
	 * 
	 * @throws JAXBException
	 */
	public PpiNotModelHandler() throws JAXBException {

		super();
	}

	/**
	 * Realiza las inicializaciones. 
	 * 
	 * @throws JAXBException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void iniLoader() throws JAXBException {
		
		timeInstanceModelMap = new HashMap<String, TimeInstanceMeasure>();
		countInstanceModelMap = new HashMap<String, CountInstanceMeasure>();
		stateConditionInstanceModelMap = new HashMap<String, StateConditionInstanceMeasure>();
		dataInstanceModelMap = new HashMap<String, DataInstanceMeasure>();
		dataPropertyConditionInstanceModelMap = new HashMap<String, DataPropertyConditionInstanceMeasure>();

		timeAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		countAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		stateConditionAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		dataAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		dataPropertyConditionAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		derivedSingleInstanceAggregatedModelMap = new HashMap<String, AggregatedMeasure>();

		derivedSingleInstanceModelMap = new HashMap<String, DerivedMeasure>();
		derivedMultiInstanceModelMap = new HashMap<String, DerivedMeasure>();

		ppiModelMap = new HashMap<String, PPI>();

		// configura las clases para leer y guardar como xml
		Class[] classList = {es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class, 
				es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory.class, 
				es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory.class};
		this.xmlConfig( classList, es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory.class );
        

		this.generatePpiNotModel = new GeneratePpiNotModel();
		
	}

	/**
	 * Devuelve la factory utilizada
	 *
	 * @return Objeto factory
	 */
	protected ObjectFactory getFactory() {

		return (ObjectFactory) super.getFactory();
	}

	/**
	 * Devuelve el mapa de CountInstanceMeasure
	 */
	public Map<String, CountInstanceMeasure> getCountInstanceModelMap() {
		
		return countInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de TimeInstanceMeasure
	 */
	public Map<String, TimeInstanceMeasure> getTimeInstanceModelMap() {

		return timeInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de StateConditionInstanceMeasure
	 */
	public Map<String, StateConditionInstanceMeasure> getStateConditionInstanceModelMap() {
		
		return stateConditionInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de DataInstanceMeasure
	 */
	public Map<String, DataInstanceMeasure> getDataInstanceModelMap() {
		
		return dataInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de DataPropertyConditionInstanceMeasure
	 */
	public Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceModelMap() {
		
		return dataPropertyConditionInstanceModelMap;
	}
	
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una TimeInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getTimeAggregatedModelMap() {

		return timeAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una CountInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getCountAggregatedModelMap() {

		return countAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una StateConditionInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getStateConditionAggregatedModelMap() {

		return stateConditionAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DataInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDataAggregatedModelMap() {

		return dataAggregatedModelMap;
	}
	
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DataPropertyConditionInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedModelMap() {

		return dataPropertyConditionAggregatedModelMap;
	}
	
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DerivedSingleInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDerivedSingleInstanceAggregatedModelMap() {

		return derivedSingleInstanceAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas DerivedSingleInstanceMeasure
	 */
	public Map<String, DerivedMeasure> getDerivedSingleInstanceModelMap() {
		
		return derivedSingleInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas DerivedMultiInstanceMeasure
	 */
	public Map<String, DerivedMeasure> getDerivedMultiInstanceModelMap() {
		
		return derivedMultiInstanceModelMap;
	}
	
	/**
	 * Devuelve el mapa de los PPI
	 */
	public Map<String, PPI> getPpiModelMap() {
		
		return ppiModelMap;
	}
	



	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos PPI
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setPpiModelMap(Map<String, PPI> modelMap) {
        ppiModelMap = modelMap;

    }

    @Override
    protected void generateExportElement(String procId) {
        ModelToXML modelToXML = new ModelToXML(getFactory(), getImportElement());
        modelToXML.setPpiModelMap(ppiModelMap);

        setExportElement(modelToXML.generateExportElement(procId));
    }

    /**
	 * Genera un objeto del modelo a partir de un objeto Jaxb si este es una medida base 
	 * 
	 * @param jaxbValue Objeto Jaxb
	 * @return Objeto del modelo
	 */
	private MeasureDefinition loadBaseMeasureModel(Object jaxbValue) {
		
		// obtiene el ppiset en el xml, que es utilizado al generar el objeto del modelo, para obtener los conectores y otros elementos
		// relacionados con la medida 
		TPpiset ppiset = this.getPpiset();
		// inicializaciones
		String id = ((TMeasure) jaxbValue).getId();
		Boolean wasCreated = false;
		MeasureDefinition def = null;
		
		// de acuerdo a la clase del objeto Jaxb
		if(jaxbValue instanceof TCountMeasure) {
			
			// se devuelve el objeto del modelo correspondiente, si ya estaba creado  
			def = this.countInstanceModelMap.get(id);
			if (def==null) {
				// genera el objeto del modelo
				def = this.generatePpiNotModel.obtainModel((TCountMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					// adiciona el objeto del modelo al map correspondiente
					this.countInstanceModelMap.put( def.getId(), (CountInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TTimeMeasure) {

			def = this.timeInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TTimeMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.timeInstanceModelMap.put( def.getId(), (TimeInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TStateConditionMeasure) {

			def = this.stateConditionInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TStateConditionMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.stateConditionInstanceModelMap.put( def.getId(), (StateConditionInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TDataMeasure) {

			def = this.dataInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TDataMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.dataInstanceModelMap.put( def.getId(), (DataInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TDataPropertyConditionMeasure) {

			def = this.dataPropertyConditionInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TDataPropertyConditionMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.dataPropertyConditionInstanceModelMap.put( def.getId(), (DataPropertyConditionInstanceMeasure) def );
				}
			}
		}
		
		if (wasCreated) {
			
			// crea el objeto del modelo del PPI asociado a la medida
			this.loadPpi(jaxbValue, def);
		}

		return def;
	}
	
	/**
	 * Genera un objeto del modelo a partir de un objeto Jaxb si este es una medida agregada 
	 * 
	 * @param measure Objeto Jaxb
	 * @return Objeto del modelo
	 */
	private MeasureDefinition loadAggregatedMeasureModel(TAggregatedMeasure measure) {
		
		// obtiene el ppiset en el xml, que es utilizado al generar el objeto del modelo, para obtener los conectores y otros elementos
		// relacionados con la medida 
		TPpiset ppiset = this.getPpiset();
		// inicializaciones
		String id = measure.getId();
		Boolean wasCreated = false;
		AggregatedMeasure def = null;
		
		// si la medida agregada no tiene seteada la propiedad baseMeasure, es que esta asociada con un conector aggregates
		if(measure.getBaseMeasure()==null) {
			
			// se obtiene el conector asociado a la medida
			TAggregates connector = (TAggregates) this.generatePpiNotModel.findMeasureConnector(measure, TAggregates.class, ppiset);
			
			// si existe el conector
			if (connector!=null) {
				
				// se obtiene el objeto del modelo de la medida conectada con la medida agregada
				MeasureDefinition baseModel = this.loadConnectedMeasure(connector);

				// de acuerdo a la clase de la medida conectada
				if(baseModel instanceof TimeInstanceMeasure ) {
					
					// se verifica si la medida agregada ya habia sido generada
					def = this.timeAggregatedModelMap.get(id);
					if (def==null) {
						// si no habia sido generada, se obtiene el objeto del modelo de la medida agregada
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							// se adiciona la medida agregada al map correspondiente
							this.timeAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof CountInstanceMeasure ) {
					
					def = this.countAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.countAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof StateConditionInstanceMeasure ) {
					
					def = this.stateConditionAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.stateConditionAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof DataInstanceMeasure ) {
					
					def = this.dataAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.dataAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof DataPropertyConditionInstanceMeasure ) {
					
					def = this.dataPropertyConditionAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof DerivedSingleInstanceMeasure ) {
					
					def = this.derivedSingleInstanceAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {

//                            List<TUses> uses = this.generatePpiNotModel.findUses(this.derivedSingleInstanceMap.get(baseModel.getId()), ppiset);
                            List<TUses> uses = this.generatePpiNotModel.findUses(measure.getBaseMeasure().getValue(), ppiset);
                            for (TUses con : uses) {
								
								((DerivedMeasure) baseModel).addUsedMeasure( con.getVariable(), this.loadConnectedMeasure(con) );
							}
							this.derivedSingleInstanceAggregatedModelMap.put( def.getId(), def );
						}
					}
				}
				
				if (wasCreated) {
					
					// si la medida agregada fue creada se indica que esta medida esta asociada a un conector aggregates
					def.setAggregates(true);
				}
			}
		} else {
			
			// si la medida agregada no esta asociada a un conector aggregates, se obtiene su medida base
			TMeasure baseMeasure = measure.getBaseMeasure().getValue();
		
			// de acuerdo a la clase de la medida base
			if(baseMeasure instanceof TTimeMeasure ) {

				// se verifica si la medida agregada ya habia sido generada
				def = this.timeAggregatedModelMap.get(id);
				if (def==null) {
					// si no habia sido generada, se obtiene el objeto del modelo de la medida agregada
					def = this.generatePpiNotModel.obtainModel(measure, (TTimeMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						// se adiciona la medida agregada al map correspondiente
						this.timeAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TCountMeasure ) {
				
				def = this.countAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TCountMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.countAggregatedModelMap.put( def.getId(), def );
					}
				
				}
			} else
			if(baseMeasure instanceof TStateConditionMeasure ) {
				
				def = this.stateConditionAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TStateConditionMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.stateConditionAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TDataMeasure ) {
				
				def = this.dataAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TDataMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.dataAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TDataPropertyConditionMeasure ) {
				
				def = this.dataPropertyConditionAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TDataPropertyConditionMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TDerivedSingleInstanceMeasure ) {
				
				def = this.derivedSingleInstanceAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TDerivedSingleInstanceMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {

						MeasureDefinition baseModel = def.getBaseMeasure();
						for (TUses connector : this.generatePpiNotModel.findUses(baseMeasure, ppiset)) {
							
							((DerivedMeasure) baseModel).addUsedMeasure( connector.getVariable(), this.loadConnectedMeasure(connector) );
						}
						this.derivedSingleInstanceAggregatedModelMap.put( def.getId(), def );
					}
				}
			}
		}
		
		if (wasCreated) {
			
			// crea el objeto del modelo del PPI asociado a la medida
			this.loadPpi(measure, def);
		}
		
		return def;
	}
	
	/**
	 * Genera un objeto del modelo a partir de un objeto Jaxb si este es una medida derivada 
	 * 
	 * @param jaxbValue Objeto Jaxb
	 * @return Objeto del modelo
	 */
	private MeasureDefinition loadDerivedMeasureModel(Object jaxbValue) {
		
		// obtiene el ppiset en el xml, que es utilizado al generar el objeto del modelo, para obtener los conectores y otros elementos
		// relacionados con la medida 
		TPpiset ppiset = this.getPpiset();
		// inicializaciones
		String id = ((TMeasure) jaxbValue).getId();
		Boolean wasCreated = false;
		MeasureDefinition def = null;
		
		// de acuerdo a la clase de la medida 
		if(jaxbValue instanceof TDerivedSingleInstanceMeasure) {
			
			// se verifica si la medida ya habia sido generada
			def = this.derivedSingleInstanceModelMap.get(id);
			if (def==null) {
				// si no habia sido generada, se obtiene el objeto del modelo de la medida 
				def = this.generatePpiNotModel.obtainModel((TDerivedSingleInstanceMeasure) jaxbValue);
				wasCreated = def!=null;
				if (wasCreated) {
					// se adiciona la medida al map correspondiente
					this.derivedSingleInstanceModelMap.put(def.getId(), (DerivedMeasure) def);
				}
			}
		} else
		if(jaxbValue instanceof TDerivedMultiInstanceMeasure) {
			
			def = this.derivedMultiInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TDerivedMultiInstanceMeasure) jaxbValue);
				wasCreated = def!=null;
				if (wasCreated) {
					this.derivedMultiInstanceModelMap.put( def.getId(), (DerivedMeasure) def );
				}
			}
		}
		
		if (wasCreated) {
			
			// si el objeto del modelo fue creado, se buscan las medidas conectadas a la medida derivada con un conector uses
			// y esas medidas se adicionan a la lista de variables utilizadas por la medida derivada
			for (TUses connector : this.generatePpiNotModel.findUses((TDerivedMeasure) jaxbValue, ppiset)) {
				
				((DerivedMeasure) def).addUsedMeasure( connector.getVariable(), this.loadConnectedMeasure(connector) );
			}
			
			// crea el objeto del modelo del PPI asociado a la medida
			this.loadPpi(jaxbValue, def);
		}
		
		return def;
	}

	/**
	 * Obtiene el objeto Jaxb del tipo ppiset en el proceso
	 * 
	 * @return TPpiset
	 */
	private TPpiset getPpiset() {
        if (tPpiset == null)
            readTPpiset();

        return tPpiset;
    }

    private TProcess readTProcess() {
        TProcess process = null;
        TDefinitions tDefinitions = (TDefinitions) this.getImportElement().getValue();
/*
        Object object = tDefinitions.getRootElement().get(0).getValue();
		if (object instanceof TProcess) {

			process = (TProcess) object;
			ppiset = (TPpiset) ((JAXBElement<?>) process.getExtensionElements().getAny().get(0)).getValue();
		} else {
*/

        for (JAXBElement<?> element : tDefinitions.getRootElement()) {

            Object participant = element.getValue();
            if (participant instanceof TProcess &&
                    ((TProcess) participant).getExtensionElements() != null &&
                    ((TProcess) participant).getExtensionElements().getAny() != null &&
                    ((TProcess) participant).getExtensionElements().getAny().get(0) != null &&
                    ((JAXBElement<?>) ((TProcess) participant).getExtensionElements().getAny().get(0)).getValue() instanceof TPpiset
                    ) {

                process = (TProcess) participant;
                break;
            }
        }
//		}

        return process;
    }

    private TPpiset readTPpiset() {
        TProcess process = readTProcess();

        return (process == null) ?
                null :
                (TPpiset) ((JAXBElement<?>) process.getExtensionElements().getAny().get(0)).getValue();
    }

    /**
     * Devuelve el id del proceso involucrado en el xml
     *
     * @return Id del proceso
     */
    public String getProcId() {
        return procId;
    }

    private String readProcId() {

        TProcess process = readTProcess();

        // obtiene el id del proceso
        return (process==null)?"":process.getId();
    }


    /**
	 * Obtiene los datos del periodo que se desea analizar, a partir de la propiedad analisysPeriod de una medida agregada 
	 * 
	 * @param period Cadena con el periodo de analisis
	 * @return Mapa con los datos del periodo de analisis
	 */
	private Map<String, String> parseAnalysisPeriod(String period) {
		
		Map<String, String> map = new HashMap<String, String>();

		if (period.contentEquals("")) {
			
			map.put("year", "");
			map.put("period", "");
			map.put("startDate", "");
			map.put("endDate", "");
			map.put("inStart", "");
			map.put("inEnd", "");
		}
		else
			try {
				Pattern patron = Pattern.compile("(interval)\\((\\d{4}\\/\\d{2}\\/\\d{2}),(\\d{4}\\/\\d{2}\\/\\d{2})\\,(true|false)\\,(true|false)\\)|(period)\\((\\d{4}),(trimestre|semestre|mes)\\,(true|false)\\,(true|false)\\)");
				Matcher matcher = patron.matcher( period );
				matcher.find();
	
				if (matcher.group(6)!=null && matcher.group(6).contentEquals("period")) {
					
					map.put("year", matcher.group(7));
					map.put("period", matcher.group(8));
					map.put("startDate", "");
					map.put("endDate", "");
					map.put("inStart", (matcher.group(9).compareTo("true")==0)?"yes":"");
					map.put("inEnd", (matcher.group(10).compareTo("true")==0)?"yes":"");
				}
				else 
				if (matcher.group(1).contentEquals("interval")) {
					
					map.put("year", "");
					map.put("period", "");
					map.put("startDate", matcher.group(2));
					map.put("endDate", matcher.group(3));
					map.put("inStart", (matcher.group(4).compareTo("true")==0)?"yes":"");
					map.put("inEnd", (matcher.group(5).compareTo("true")==0)?"yes":"");
				} 

			}  catch (Exception e) {
				
				map.put("year", "");
				map.put("period", "");
				map.put("startDate", "");
				map.put("endDate", "");
				map.put("inStart", "");
				map.put("inEnd", "");
			}
		
		return map;
	}

	/**
	 * Obtiene los valores de referencia para evaluar el grado de satisfaccion de un ppi, a partir de la propiedad target
	 * 
	 * @param target Valor de la propiedad target de un ppi
	 * @return Mapa con los valores de referencia. El valor minimo se devuelve con la llave refMin y el valor maximo con la llave refMax
	 */
	private Map<String, Double> parseTarget(String target) {
		
		Map<String, Double> map = new HashMap<String, Double>();

		try {
			Pattern patron = Pattern.compile("(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)\\-(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)|(>|<|=|@)(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)");
			Matcher matcher = patron.matcher( target );
			matcher.find();

			if (matcher.group(3)!=null && matcher.group(3).contentEquals("=")) {
			
				map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(4)));
				map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(4)));
			} else
			if (matcher.group(3)!=null && matcher.group(3).contentEquals(">")) {
				
				map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(4)));
			} else
			if (matcher.group(3)!=null && matcher.group(3).contentEquals("<")) {
				
				map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(4)));
			} else 
			if (matcher.group(1)!=null && matcher.group(2)!=null) {
				map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(1)));
				map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(2)));
			} 
		}  catch (Exception e) {
			
			map.put("refMin", null);
			map.put("refMax", null);
		}
		
		return map;
	}
	
	/**
	 * Crea el objeto del modelo del PPI asociado a una medida 
	 * 
	 * @param object Objeto Jaxb de la medida
	 * @param def Objeto del modelo de la medida
	 */
	private void loadPpi(Object object, MeasureDefinition def) {
		
		for (TPpi ppi : this.getPpiset().getPpi()) {
			
			if(ppi.getMeasuredBy().equals(object)) {
				
				Map<String, Double> targetMap = this.parseTarget(ppi.getTarget());
				
				Map<String, String> analysisperiodMap = parseAnalysisPeriod(ppi.getScope());
				
				PPI ppiModel = new PPI(
						ppi.getId(), 
						ppi.getName(), 
						ppi.getDescription(), 
						ppi.getGoals(), 
						ppi.getResponsible(), 
						ppi.getInformed(), 
						ppi.getInformed(),
						new Target( targetMap.get("refMax"), targetMap.get("refMin")), 
			    		new Scope( analysisperiodMap.get("year"), 
			    				analysisperiodMap.get("period"), 
			    				PpiNotModelUtils.parseDate(analysisperiodMap.get("startDate")), 
			    				PpiNotModelUtils.parseDate(analysisperiodMap.get("endDate")), 
			    				analysisperiodMap.get("inStart").contentEquals("yes"), 
			    				analysisperiodMap.get("inEnd").contentEquals("yes")));
				
				ppiModel.setMeasuredBy(def);
				
				this.ppiModelMap.put(ppi.getId(), ppiModel);
				
				break;
			}
		}
	}

	/**
	 * Obtiene el objeto del modelo de una medida, a la cual se aplica un conector
	 * 
	 * @param con Objeto Jaxb de un conector
	 * @return Objeto del modelo de una medida
	 */
	private MeasureDefinition loadConnectedMeasure(TMeasureConnector con) {
	
		Object target = con.getTargetRef();
		MeasureDefinition def  = this.loadBaseMeasureModel(target);

		if (def==null && target instanceof TAggregatedMeasure)
			def = this.loadAggregatedMeasureModel((TAggregatedMeasure) target);

		if (def==null)
			def = this.loadDerivedMeasureModel(target);

		return def;
	}

	/**
	 * Genera las instancias de clases del modelo a partir de instancias de clases Jabx. 
	 * Despues de invocar este metodo se pueden obtener los objetos del modelo mediante los metodos get correspondientes a cada tipo de medida.
	 * 
	 */
	@Override
	protected void generateModelLists() {

        tPpiset = readTPpiset();
        procId = readProcId();

        if (tPpiset != null) {
            for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {

                this.loadBaseMeasureModel(element.getValue());
            }

            for( TAggregatedMeasure measure : this.getPpiset().getAggregatedMeasure()) {

                this.loadAggregatedMeasureModel(measure);
            }

            for( JAXBElement<?> element : this.getPpiset().getDerivedMeasure()) {

                this.loadDerivedMeasureModel(element.getValue());
            }
        }

	}

}
