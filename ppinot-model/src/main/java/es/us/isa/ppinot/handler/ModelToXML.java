package es.us.isa.ppinot.handler;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.*;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory;
import es.us.isa.ppinot.xmlClasses.ppinot.*;

import javax.xml.bind.JAXBElement;
import java.util.*;

public class ModelToXML {
    // objeto factory para generar instancias de clases Jaxb de BPMN 2.0
    private es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory bpmnFactory;
    private ObjectFactory ppinotFactory;

    // mapas con las instancias de las clases Jaxb, obtenidas de importar un xml. La key es el id del elemento.
    private Map<String, TCountMeasure> countMap;
    private Map<String, TTimeMeasure> timeMap;
    private Map<String, TStateConditionMeasure> stateConditionMap;
    private Map<String, TDataMeasure> dataMap;
    private Map<String, TDataPropertyConditionMeasure> dataPropertyConditionMap;
    private Map<String, TAggregatedMeasure> aggregatedMap;
    private Map<String, TDerivedSingleInstanceMeasure> derivedSingleInstanceMap;
    private Map<String, TDerivedMultiInstanceMeasure> derivedMultiInstanceMap;
    private Map<String, TAppliesToElementConnector> appliesToElementConnectorMap;
    private Map<String, TAppliesToDataConnector> appliesToDataConnectorMap;
    private Map<String, TTimeConnector> timeConnectorMap;
    private Map<String, TUses> usesMap;
    private Map<String, TAggregates> aggregatesMap;
    private Map<String, TIsGroupedBy> isGroupedByMap;
    private Map<String, TTask> taskMap;
    private Map<String, TDataObject> dataobjectMap;
    private List<TPpi> ppiList;

    // objeto para obtener instancias de clases Jabx a partir de instancias de clases del modelo
    private GeneratePpiNotInfo generatePpiNotInfo;
    private JAXBElement importElement;

    public ModelToXML(ObjectFactory ppinotFactory, JAXBElement importElement) {
        this.ppinotFactory = ppinotFactory;
        this.importElement = importElement;

        countMap = new HashMap<String, TCountMeasure>();
        timeMap = new HashMap<String, TTimeMeasure>();
        stateConditionMap = new HashMap<String, TStateConditionMeasure>();
        dataMap = new HashMap<String, TDataMeasure>();
        dataPropertyConditionMap = new HashMap<String, TDataPropertyConditionMeasure>();

        aggregatedMap = new HashMap<String, TAggregatedMeasure>();

        derivedSingleInstanceMap = new HashMap<String, TDerivedSingleInstanceMeasure>();
        derivedMultiInstanceMap = new HashMap<String, TDerivedMultiInstanceMeasure>();

        appliesToElementConnectorMap = new HashMap<String, TAppliesToElementConnector>();
        appliesToDataConnectorMap = new HashMap<String, TAppliesToDataConnector>();
        timeConnectorMap = new HashMap<String, TTimeConnector>();
        usesMap = new HashMap<String, TUses>();
        aggregatesMap = new HashMap<String, TAggregates>();
        isGroupedByMap = new HashMap<String, TIsGroupedBy>();

        taskMap = new HashMap<String, TTask>();
        dataobjectMap = new HashMap<String, TDataObject>();

        ppiList = new ArrayList<TPpi>();



        this.bpmnFactory = new es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory();
        this.generatePpiNotInfo = new GeneratePpiNotInfo();

    }

    /**
     * Genera los objetos Jaxb correspondientes a un map de objetos PPI
     *
     * @param modelMap Map con objetos del modelo
     */
    public void setPpiModelMap(Map<String, PPI> modelMap) {
        if (modelMap != null) {

            for (Map.Entry<String, PPI> pairs : modelMap.entrySet()) {
                PPI def = pairs.getValue();

                TPpi ppi = this.getFactory().createTPpi();
                ppi.setId(def.getId());

                ppi.setName(def.getName());
                ppi.setGoals(def.getGoals());
                ppi.setDescription(def.getDescription());
                ppi.setResponsible(def.getResponsible());
                ppi.setInformed(def.getInformed());
                ppi.setComments(def.getComments());

                ppi.setTarget(this.generateTarget(def.getTarget().getRefMin(), def.getTarget().getRefMax()));
                ppi.setScope(this.generateAnalisysPeriod(def.getScope().getYear(), def.getScope().getPeriod(), def.getScope().getStartDate(), def.getScope().getEndDate(), def.getScope().getInStart(), def.getScope().getInEnd()));

                MeasureDefinition m = def.getMeasuredBy();

                TMeasure md = this.findBaseMeasure(m);
                if (md == null && m instanceof AggregatedMeasure)
                    md = this.findAggregatedMeasure(m);
                if (md == null && m instanceof DerivedMeasure)
                    md = this.findDerivedMeasure(m);

                ppi.setMeasuredBy(md);

                this.ppiList.add(ppi);

            }
        }
    }


    /**
     * Genera el valor de la propiedad analisysPeriod a partir de la informacion del periodo de analisis
     *
     * @param year Año
     * @param period Si se desea los resultados por mes, trimestre o semestre
     * @param startDate Fecha inicial del periodo de analisis
     * @param endDate Fecha final del periodo de analisis
     * @param inStart Si se incluye en el analisis los procesos que se inician antes del inicio del periodo y terminan despues de este
     * @param inEnd Si se incluye en el analisis los procesos que se inician antes del final del periodo y terminan despues de este
     */
    private String generateAnalisysPeriod(String year, String period, Date startDate, Date endDate, Boolean inStart, Boolean inEnd) {

        String startDateString = PpiNotModelUtils.formatString(startDate);
        String endDateString = PpiNotModelUtils.formatString(endDate);
        String inStartString = (inStart)?"true":"false";
        String inEndString = (inEnd)?"true":"false";

        String analisysPeriod = "";

        if (!startDateString.contentEquals("") || !endDateString.contentEquals("")) {

            analisysPeriod = "interval(" + startDateString + "," + endDateString + "," + inStartString + "," + inEndString + ")";
        } else
        if (!year.contentEquals("")) {

            analisysPeriod = "period(" + year + "," + period + "," + inStartString + "," + inEndString + ")";
        }

        return analisysPeriod;
    }

    /**
     * Genera el valor de la propiedad target de un ppi, a partir de los valores de referencia de este
     *
     * @param refMin Valor minimo que deberia tomar la medida en el ppi
     * @param refMax Valor maximo que deberia tomar la medida en el ppi
     * @return Valor de la propiedad target del ppi
     */
    private String generateTarget(Double refMin, Double refMax) {

        String target = "";

        if (refMax!=null && refMin!=null && refMax.equals(refMin)) {

            target = "=" + PpiNotModelUtils.doubleToString(refMin);
        } else
        if (refMax==null && refMin!=null) {

            target = ">" + PpiNotModelUtils.doubleToString(refMin);
        } else
        if (refMin==null && refMax!=null) {

            target = "<" + PpiNotModelUtils.doubleToString(refMax);
        } else
        if (refMax!=null && refMin!=null) {

            target = PpiNotModelUtils.doubleToString(refMin) + "-" + PpiNotModelUtils.doubleToString(refMax);
        }
        return target;
    }


    /**
     * Devuelve la factory utilizada
     *
     * @return Objeto factory
     */
    private es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory getFactory() {

        return ppinotFactory;
    }

    /**
     * Genera una tarea para un conector del tipo AppliesTo cuando se va a exportar un xml
     *
     * @param con Conector
     * @return Tarea generada
     */
    private TTask chainTask(TMeasureConnector con) {

        TTask task = this.bpmnFactory.createTTask();
        task.setId((String) con.getTargetRef());
        con.setTargetRef(task);

        return task;
    }

    /**
     * Genera una dataobject para un conector del tipo AppliesTo cuando se va a exportar un xml
     *
     * @param con Conector
     * @return Dataobject generado
     */
    private TDataObject chainDataobject(TMeasureConnector con) {

        TDataObject dataobject = this.bpmnFactory.createTDataObject();
        dataobject.setId(this.generatePpiNotInfo.generarId("dataobject", ""));
        dataobject.setName((String) con.getTargetRef());
        con.setTargetRef(dataobject);

        return dataobject;
    }

    /**
     * Crea los objetos Jaxb asociados con una medida, si el objeto del modelo proporcionado corresponde a una medida base
     *
     * @param def Objeto del modelo
     */
    private TMeasure findBaseMeasure(MeasureDefinition def) {

        // de acuerdo a la clase de la medida, se invoca el metodo correspondiente de this.generatePpiNotInfo
        // con lo que obtienen los objetos Jaxb asociados con la medida
        // estos objetos con colocados en el map correspondientes a su clase Jaxb
        //
        // cuando los objetos Jaxb son conectores que se aplican a elementos BPMN, se generan los objetos Jaxb de esos elementos
        es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory factory = getFactory();
        TMeasure measure = null;
        if (def instanceof TimeInstanceMeasure) {

            // si ya fue generado el objeto Jaxb de la medida, se devuelve este
            // en caso contrario, se generan los objetos Jaxb asociados con la medida
            measure = this.timeMap.get(def.getId());
            if (measure == null) {

                // obtiene un map con los objetos Jaxb asociados con la medida
                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((TimeInstanceMeasure) def, factory);

                if (map.containsKey("measure")) {

                    // guarda el objeto Jaxb de la medida en el map correspondiente
                    measure = (TMeasure) map.get("measure");
                    this.timeMap.put(((TTimeMeasure) map.get("measure")).getId(), (TTimeMeasure) measure);

                    // guarda otros objetos Jaxb asociados con la medida, en sus correspondientes maps
                    if (map.containsKey("connectorFrom")) {

                        TTimeConnector con = (TTimeConnector) map.get("connectorFrom");
                        // genera la tarea a la que se aplica el conector
                        TTask task = chainTask(con);
                        this.taskMap.put(task.getId(), task);

                        this.timeConnectorMap.put(con.getId(), con);
                    }

                    if (map.containsKey("connectorTo")) {

                        TTimeConnector con = (TTimeConnector) map.get("connectorTo");
                        TTask task = chainTask(con);
                        this.taskMap.put(task.getId(), task);

                        this.timeConnectorMap.put(con.getId(), con);
                    }
                }
            }
        } else if (def instanceof CountInstanceMeasure) {

            measure = this.countMap.get(def.getId());
            if (measure == null) {

                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((CountInstanceMeasure) def, factory);

                if (map.containsKey("measure")) {

                    measure = (TMeasure) map.get("measure");
                    if (map.containsKey("measure"))
                        this.countMap.put(((TCountMeasure) map.get("measure")).getId(), (TCountMeasure) measure);

                    if (map.containsKey("connector")) {

                        TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
                        TTask task = chainTask(con);
                        this.taskMap.put(task.getId(), task);

                        this.appliesToElementConnectorMap.put(con.getId(), con);
                    }
                }
            }
        } else if (def instanceof StateConditionInstanceMeasure) {

            measure = this.stateConditionMap.get(def.getId());
            if (measure == null) {

                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((StateConditionInstanceMeasure) def, factory);

                if (map.containsKey("measure")) {

                    measure = (TMeasure) map.get("measure");
                    if (map.containsKey("measure"))
                        this.stateConditionMap.put(((TStateConditionMeasure) map.get("measure")).getId(), (TStateConditionMeasure) measure);

                    if (map.containsKey("connector")) {

                        TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
                        TTask task = chainTask(con);
                        this.taskMap.put(task.getId(), task);

                        this.appliesToElementConnectorMap.put(con.getId(), con);
                    }
                }
            }
        } else if (def instanceof DataInstanceMeasure) {

            measure = this.dataMap.get(def.getId());
            if (measure == null) {

                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((DataInstanceMeasure) def, factory);

                if (map.containsKey("measure")) {

                    measure = (TMeasure) map.get("measure");
                    if (map.containsKey("measure"))
                        this.dataMap.put(((TDataMeasure) map.get("measure")).getId(), (TDataMeasure) measure);

                    if (map.containsKey("connector")) {

                        TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
                        // genera el dataobject al que se aplica el conector
                        TDataObject dataobject = chainDataobject(con);
                        this.dataobjectMap.put(dataobject.getId(), dataobject);

                        this.appliesToDataConnectorMap.put(con.getId(), con);
                    }
                }
            }
        } else if (def instanceof DataPropertyConditionInstanceMeasure) {

            measure = this.dataPropertyConditionMap.get(def.getId());
            if (measure == null) {

                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((DataPropertyConditionInstanceMeasure) def, factory);

                if (map.containsKey("measure")) {

                    measure = (TMeasure) map.get("measure");
                    if (map.containsKey("measure"))
                        this.dataPropertyConditionMap.put(((TDataPropertyConditionMeasure) map.get("measure")).getId(), (TDataPropertyConditionMeasure) measure);

                    if (map.containsKey("connector")) {

                        TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
                        TDataObject dataobject = chainDataobject(con);
                        this.dataobjectMap.put(dataobject.getId(), dataobject);

                        this.appliesToDataConnectorMap.put(con.getId(), con);
                    }
                }
            }
        }

        return measure;
    }

    /**
     * Crea los objetos Jaxb asociados con una medida, si el objeto del modelo proporcionado corresponde a una medida agregada
     *
     * @param def Objeto del modelo
     */
    private TMeasure findAggregatedMeasure(MeasureDefinition def) {

        // si ya fue generado el objeto Jaxb de la medida, se devuelve este
        // en caso contrario, se generan los objetos Jaxb asociados con la medida
        TMeasure measure = this.aggregatedMap.get(def.getId());
        if (measure != null)
            return measure;

        es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory factory = getFactory();

        // obtiene un map con los objetos Jaxb asociados con la medida
        Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((AggregatedMeasure) def, factory);

        measure = (TMeasure) map.get("measure");

        if (measure != null) {

            // guarda el objeto Jaxb de la medida en el map correspondiente
            this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) measure);

            // guarda otros objetos Jaxb asociados con la medida, en sus correspondientes maps
            if (map.containsKey("connectorIsGroupedBy")) {

                TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
                // genera el dataobject al que se aplica el conector
                TDataObject dataobject = chainDataobject(con);
                this.dataobjectMap.put(dataobject.getId(), dataobject);

                this.isGroupedByMap.put(con.getId(), con);
            }

            // obtiene la medida base
            MeasureDefinition baseMeasureModel = ((AggregatedMeasure) def).getBaseMeasure();

            // si la medida agregada utiliza un conector aggregates
            if (((AggregatedMeasure) def).getAggregates()) {

                // crea el objeto Jaxb del conector aggregates
                TAggregates con = getFactory().createTAggregates();

                // genera los objetos Jaxb asociados con la medida base
                TMeasure baseMeasure = findBaseMeasure(baseMeasureModel);
                if (baseMeasure == null)
                    baseMeasure = findDerivedMeasure(baseMeasureModel);

                // da valor a propiedades del objeto Jaxb del conector
                con.setId(this.generatePpiNotInfo.generarId("aggregates", ""));
                con.setSourceRef(measure);
                con.setTargetRef(baseMeasure);

                // conserva el objeto Jaxb del conector en el map correspondiente
                this.aggregatesMap.put(con.getId(), con);
            } else {

                // si la medida agregada no utiliza conector aggregates,
                // de acuerdo a la clase de la medida de la medida base, se invoca el metodo correspondiente de this.generatePpiNotInfo
                // con lo que obtienen los objetos Jaxb asociados con la medida base
                // estos objetos con colocados en el map correspondientes a su clase Jaxb
                //
                // cuando los objetos Jaxb son conectores que se aplican a elementos BPMN, se generan los objetos Jaxb de esos elementos
                // y al source de esos conectores se pone como valor el objeto Jaxb de la medida agregada
                if (baseMeasureModel instanceof TimeInstanceMeasure) {

                    // genera los objetos Jaxb de la medida base
                    Map<String, Object> instanceMap = this.generatePpiNotInfo.obtainInfo((TimeInstanceMeasure) baseMeasureModel, factory);

                    if (instanceMap.containsKey("measure")) {

                        // se le da valor a la propiedad baseMeasure de la medida agregada
                        ((TAggregatedMeasure) measure).setBaseMeasure(factory.createTimeMeasure((TTimeMeasure) instanceMap.get("measure")));
                        // situa otros objetos Jaxb asociados con la medida en sus correspondientes maps
                        if (instanceMap.containsKey("connectorFrom")) {

                            TTimeConnector con = (TTimeConnector) instanceMap.get("connectorFrom");
                            // indica que el source del conector es la medida agregada (al ser generado, toma como valor la medida base)
                            con.setSourceRef(measure);
                            // genera el objeto Jaxb de la tarea a la que se aplica el conector
                            TTask task = chainTask(con);
                            this.taskMap.put(task.getId(), task);

                            this.timeConnectorMap.put(con.getId(), con);
                        }
                        if (instanceMap.containsKey("connectorTo")) {

                            TTimeConnector con = (TTimeConnector) instanceMap.get("connectorTo");
                            con.setSourceRef(measure);
                            TTask task = chainTask(con);
                            this.taskMap.put(task.getId(), task);

                            this.timeConnectorMap.put(con.getId(), con);
                        }
                    }
                } else if (baseMeasureModel instanceof CountInstanceMeasure) {

                    Map<String, Object> instanceMap = this.generatePpiNotInfo.obtainInfo((CountInstanceMeasure) baseMeasureModel, factory);

                    if (instanceMap.containsKey("measure")) {

                        ((TAggregatedMeasure) measure).setBaseMeasure(factory.createCountMeasure((TCountMeasure) instanceMap.get("measure")));
                        if (instanceMap.containsKey("connector")) {

                            TAppliesToElementConnector con = (TAppliesToElementConnector) instanceMap.get("connector");
                            con.setSourceRef(measure);
                            TTask task = chainTask(con);
                            this.taskMap.put(task.getId(), task);

                            this.appliesToElementConnectorMap.put(con.getId(), con);
                        }
                    }
                } else if (baseMeasureModel instanceof StateConditionInstanceMeasure) {

                    Map<String, Object> instanceMap = this.generatePpiNotInfo.obtainInfo((StateConditionInstanceMeasure) baseMeasureModel, factory);

                    if (instanceMap.containsKey("measure")) {

                        ((TAggregatedMeasure) measure).setBaseMeasure(factory.createStateConditionMeasure((TStateConditionMeasure) instanceMap.get("measure")));
                        if (instanceMap.containsKey("connector")) {

                            TAppliesToElementConnector con = (TAppliesToElementConnector) instanceMap.get("connector");
                            con.setSourceRef(measure);
                            TTask task = chainTask(con);
                            this.taskMap.put(task.getId(), task);

                            this.appliesToElementConnectorMap.put(con.getId(), con);
                        }
                    }
                } else if (baseMeasureModel instanceof DataInstanceMeasure) {

                    Map<String, Object> instanceMap = this.generatePpiNotInfo.obtainInfo((DataInstanceMeasure) baseMeasureModel, factory);

                    if (instanceMap.containsKey("measure")) {

                        ((TAggregatedMeasure) measure).setBaseMeasure(factory.createDataMeasure((TDataMeasure) instanceMap.get("measure")));
                        if (instanceMap.containsKey("connector")) {

                            TAppliesToDataConnector con = (TAppliesToDataConnector) instanceMap.get("connector");
                            con.setSourceRef(measure);
                            TDataObject dataobject = chainDataobject(con);
                            this.dataobjectMap.put(((TAggregatedMeasure) map.get("measure")).getId(), dataobject);

                            this.appliesToDataConnectorMap.put(con.getId(), con);
                        }
                    }
                } else if (baseMeasureModel instanceof DataPropertyConditionInstanceMeasure) {

                    Map<String, Object> instanceMap = this.generatePpiNotInfo.obtainInfo((DataPropertyConditionInstanceMeasure) baseMeasureModel, factory);

                    if (instanceMap.containsKey("measure")) {

                        ((TAggregatedMeasure) measure).setBaseMeasure(factory.createDataPropertyConditionMeasure((TDataPropertyConditionMeasure) instanceMap.get("measure")));
                        if (instanceMap.containsKey("connector")) {

                            TAppliesToDataConnector con = (TAppliesToDataConnector) instanceMap.get("connector");
                            con.setSourceRef(measure);
                            TDataObject dataobject = chainDataobject(con);
                            this.dataobjectMap.put(dataobject.getId(), dataobject);

                            this.appliesToDataConnectorMap.put(con.getId(), con);
                        }
                    }
                }
            }
        }

        return measure;
    }

    /**
     * Crea los objetos Jaxb asociados con una medida, si el objeto del modelo proporcionado corresponde a una medida derivada
     *
     * @param def Objeto del modelo
     */
    private TMeasure findDerivedMeasure(MeasureDefinition def) {

        Boolean wasCreated = false;
        TMeasure measure = null;

        // de acuerdo a la clase de la medida de la medida, se invoca el metodo correspondiente de this.generatePpiNotInfo
        // con lo que obtienen los objetos Jaxb asociados con la medida
        // estos objetos con colocados en el map correspondientes a su clase Jaxb
        if (def instanceof DerivedSingleInstanceMeasure) {

            // si ya fue generado el objeto Jaxb de la medida, se devuelve este
            // en caso contrario, se generan los objetos Jaxb asociados con la medida
            measure = this.derivedSingleInstanceMap.get(def.getId());
            if (measure == null) {

                // genera los objetos Jaxb de la medida
                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((DerivedSingleInstanceMeasure) def, getFactory());

                measure = (TMeasure) map.get("measure");
                if (measure != null) {

                    // indica que la medida fue creada
                    wasCreated = true;
                    // guarda el objeto Jaxb de la medida en el map correspondiente
                    this.derivedSingleInstanceMap.put(measure.getId(), (TDerivedSingleInstanceMeasure) measure);
                }
            }
        } else if (def instanceof DerivedMultiInstanceMeasure) {

            measure = this.derivedMultiInstanceMap.get(def.getId());
            if (measure == null) {

                Map<String, Object> map = this.generatePpiNotInfo.obtainInfo((DerivedMultiInstanceMeasure) def, getFactory());

                measure = (TMeasure) map.get("measure");
                if (measure != null) {

                    wasCreated = true;
                    this.derivedMultiInstanceMap.put(measure.getId(), (TDerivedMultiInstanceMeasure) measure);
                }
            }
        }

        // si la medida fue creada
        if (wasCreated) {

            // para cada una de las medidas asociadas con la medida derivada
            for (Map.Entry<String, MeasureDefinition> pairs : ((DerivedMeasure) def).getUsedMeasureMap().entrySet()) {
                String variable = pairs.getKey();
                MeasureDefinition usedMeasureModel = pairs.getValue();

                // genera los objetos Jaxb asociados con la medida asociada
                TMeasure usedMeasure = findBaseMeasure(usedMeasureModel);
                if (usedMeasure == null)
                    usedMeasure = findAggregatedMeasure(usedMeasureModel);
                if (usedMeasure == null)
                    usedMeasure = findDerivedMeasure(usedMeasureModel);

                // crea el objeto Jaxb del conector uses correspondiente a la medida asociada
                TUses con = getFactory().createTUses();
                con.setId(this.generatePpiNotInfo.generarId("uses", ""));
                con.setVariable(variable);
                con.setSourceRef(measure);
                con.setTargetRef(usedMeasure);

                // situa el objeto Jaxb del conector en el map correspondiente
                this.usesMap.put(con.getId(), con);
            }
        }
        return measure;
    }

/*
    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos TimeInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setTimeModelMap(Map<String, TimeInstanceMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, TimeInstanceMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, TimeInstanceMeasure> pairs = (Map.Entry<String, TimeInstanceMeasure>)itInst.next();
                TimeInstanceMeasure def = pairs.getValue();

                this.findBaseMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos CountInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setCountModelMap(Map<String, CountInstanceMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, CountInstanceMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, CountInstanceMeasure> pairs = (Map.Entry<String, CountInstanceMeasure>)itInst.next();
                CountInstanceMeasure def = pairs.getValue();

                this.findBaseMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos StateConditionInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setStateConditionModelMap(Map<String, StateConditionInstanceMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, StateConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, StateConditionInstanceMeasure> pairs = (Map.Entry<String, StateConditionInstanceMeasure>)itInst.next();
                StateConditionInstanceMeasure def = pairs.getValue();

                this.findBaseMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos DataInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDataModelMap(Map<String, DataInstanceMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, DataInstanceMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
                DataInstanceMeasure def = pairs.getValue();

                this.findBaseMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos DataPropertyConditionInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDataPropertyConditionModelMap(Map<String, DataPropertyConditionInstanceMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, DataPropertyConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst.next();
                DataPropertyConditionInstanceMeasure def = pairs.getValue();

                this.findBaseMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    private void setAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
                AggregatedMeasure def = pairs.getValue();

                this.findAggregatedMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean TimeInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setTimeAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        this.setAggregatedModelMap(modelMap);
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean CountInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setCountAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        this.setAggregatedModelMap(modelMap);
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean StateConditionInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setStateConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        this.setAggregatedModelMap(modelMap);
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean DataInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDataAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        this.setAggregatedModelMap(modelMap);
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean DataPropertyConditionInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDataPropertyConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        this.setAggregatedModelMap(modelMap);
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean TimeInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDerivedSingleInstanceAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

        this.setAggregatedModelMap(modelMap);
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos DerivedSingleInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDerivedSingleInstanceModelMap(Map<String, DerivedMeasure> modelMap) {

        if (modelMap!=null) {

            Iterator<Map.Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
                DerivedSingleInstanceMeasure def = (DerivedSingleInstanceMeasure) pairs.getValue();

                this.findDerivedMeasure(def);
            }
        }
    }

    */
/**
     * Genera los objetos Jaxb correspondientes a un map de objetos DerivedMultiInstanceMeasure
     *
     * @param modelMap Map con objetos del modelo
     *//*

    public void setDerivedMultiInstanceModelMap(Map<String, DerivedMeasure> modelMap) {


        if (modelMap!=null) {

            Iterator<Map.Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
            while (itInst.hasNext()) {
                Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
                DerivedMultiInstanceMeasure def = (DerivedMultiInstanceMeasure) pairs.getValue();

                this.findDerivedMeasure(def);
            }
        }
    }

*/


    /**
     * Genera las instancias de clases Jaxb a partir de instancias de clases del modelo que debieron ser seteadas previamente con el metodo
     * set correspondiente a cada tipo de medida.
     * Genera el JAXBElement para exportar, por lo que debe finalizar invocando a this.setExportElement
     *
     * @param procId Id del proceso en el xml. Es utilizado para formar el nombre del archivo xml generado
     */
    public JAXBElement generateExportElement(String procId) {

        // inicializa el objeto para generar objetos Jaxb a partir de los objetos del modelos
        this.generatePpiNotInfo = new GeneratePpiNotInfo();

        // crea el objeto Jaxb Tppiset, en el que se incluyen todos los objetos Jaxb de las medidas
        TPpiset ppiset = getFactory().createTPpiset();
        ppiset.setId("ppiset_1");

        // adiciona al ppiset cada uno de los objetos Jaxb de las medidas
        for (Map.Entry<String, TCountMeasure> pairs : this.countMap.entrySet()) {
            ppiset.getBaseMeasure().add(getFactory().createCountMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TTimeMeasure> pairs : this.timeMap.entrySet()) {
            ppiset.getBaseMeasure().add(getFactory().createTimeMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TStateConditionMeasure> pairs : this.stateConditionMap.entrySet()) {
            ppiset.getBaseMeasure().add(getFactory().createStateConditionMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TDataMeasure> pairs : this.dataMap.entrySet()) {
            ppiset.getBaseMeasure().add(getFactory().createDataMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TDataPropertyConditionMeasure> pairs : this.dataPropertyConditionMap.entrySet()) {
            ppiset.getBaseMeasure().add(getFactory().createDataPropertyConditionMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TAggregatedMeasure> pairs : this.aggregatedMap.entrySet()) {
            ppiset.getAggregatedMeasure().add(pairs.getValue());
        }

        for (Map.Entry<String, TDerivedSingleInstanceMeasure> pairs : this.derivedSingleInstanceMap.entrySet()) {
            ppiset.getDerivedMeasure().add(getFactory().createDerivedSingleInstanceMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TDerivedMultiInstanceMeasure> pairs : this.derivedMultiInstanceMap.entrySet()) {
            ppiset.getDerivedMeasure().add(getFactory().createDerivedMultiInstanceMeasure(pairs.getValue()));
        }

        for (Map.Entry<String, TAppliesToElementConnector> pairs : this.appliesToElementConnectorMap.entrySet()) {
            ppiset.getMeasureConnector().add(getFactory().createAppliesToElementConnector(pairs.getValue()));
        }

        for (Map.Entry<String, TTimeConnector> pairs : this.timeConnectorMap.entrySet()) {
            ppiset.getMeasureConnector().add(getFactory().createTimeConnector(pairs.getValue()));
        }

        for (Map.Entry<String, TUses> pairs : this.usesMap.entrySet()) {
            ppiset.getMeasureConnector().add(getFactory().createUses(pairs.getValue()));
        }

        for (Map.Entry<String, TAppliesToDataConnector> pairs : this.appliesToDataConnectorMap.entrySet()) {
            ppiset.getMeasureConnector().add(getFactory().createAppliesToDataConnector(pairs.getValue()));
        }

        for (Map.Entry<String, TAggregates> pairs : this.aggregatesMap.entrySet()) {
            ppiset.getMeasureConnector().add(getFactory().createAggregates(pairs.getValue()));
        }

        for (Map.Entry<String, TIsGroupedBy> pairs : this.isGroupedByMap.entrySet()) {
            ppiset.getMeasureConnector().add(getFactory().createIsGroupedBy(pairs.getValue()));
        }

        // adiciona al ppiset cada uno de los objetos Jaxb de los ppi
        for (TPpi ppi : this.ppiList)
            ppiset.getPpi().add(ppi);

        // crea un objeto Jaxb del tipo extensionElements
        TExtensionElements extensionElements = new TExtensionElements();
        extensionElements.getAny().add(getFactory().createPpiset(ppiset));

        // crea un objeto Jaxb del tipo process
        JAXBElement exportElement;
        TDefinitions definitions = ((TDefinitions) importElement.getValue());
        if (definitions != null) {
            for (JAXBElement<? extends TRootElement> el : definitions.getRootElement()) {
                if (el.getValue() instanceof TProcess) {
                    TProcess process = (TProcess) el.getValue();
                    if (procId.equals(process.getId())) {
                        process.setExtensionElements(extensionElements);
                        break;
                    }
                }
            }
            exportElement = importElement;
        } else {
            definitions = createTDefinitions(procId, extensionElements);
            exportElement = this.bpmnFactory.createDefinitions(definitions);
        }

        // da valor al objeto JAXBElement que se utiliza para hacer el marshall de un xml
        return exportElement;

    }

    private TDefinitions createTDefinitions(String procId, TExtensionElements extensionElements) {
        TProcess process = this.bpmnFactory.createTProcess();
        process.setId(procId);
        process.setName(procId);
        process.setExtensionElements(extensionElements);

        // adiciona al proceso generado, las tareas generadas (TTask) al crear los objetos Jaxb de las medidas
        for (Map.Entry<String, TTask> pairs : this.taskMap.entrySet()) {
            process.getFlowElement().add(this.bpmnFactory.createTask(pairs.getValue()));
        }

        // adiciona al proceso generado, los dataobjets generados (TDataObject) al crear los objetos Jaxb de las medidas
        for (Map.Entry<String, TDataObject> pairs : this.dataobjectMap.entrySet()) {
            TDataObject value = pairs.getValue();
            process.getFlowElement().add(this.bpmnFactory.createDataObject(value));
        }

        // crea un objeto Jaxb del tipo definitions
        TDefinitions definitions = new TDefinitions();
        definitions.setId("ppinot-definitions");
        definitions.setExpressionLanguage("http://www.w3.org/1999/XPath");
        definitions.setTargetNamespace("http://schema.omg.org/spec/BPMN/2.0");
        definitions.setTypeLanguage("http://www.w3.org/2001/XMLSchema");
        definitions.getRootElement().add(this.bpmnFactory.createProcess(process));
        return definitions;
    }


}