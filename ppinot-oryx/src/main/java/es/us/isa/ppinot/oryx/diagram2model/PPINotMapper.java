package es.us.isa.ppinot.oryx.diagram2model;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.*;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.oryx.diagram2model.mappers.ConditionMapper;
import es.us.isa.ppinot.oryx.diagram2model.mappers.DataConditionMapper;
import es.us.isa.ppinot.oryx.diagram2model.mappers.PPITargetMapper;
import es.us.isa.ppinot.oryx.diagram2model.mappers.ScopeMapper;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram2model.mappers.NodeMapper;
import org.oryxeditor.server.diagram2model.mappers.child.LessIncomingChild;
import org.oryxeditor.server.diagram2model.mappers.fields.EnumFieldMapper;
import org.oryxeditor.server.diagram2model.mappers.fields.StringListFieldMapper;
import org.oryxeditor.server.diagram2model.mappings.*;

import java.util.Collection;

/**
 * PPINotMapper
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class PPINotMapper extends ModelMapping {
    public PPINotMapper() {
        NodeMapper timeInstantMapper = new NodeMapper(TimeInstantCondition.class,
                new PropertyMapping().source("state", "when", "#target").target("changesToState").using(new ConditionMapper()),
                new EdgeTargetPropertyMapping().source("name").target("appliesTo"));

        NodeMapper stateMapper = new NodeMapper(StateCondition.class,
                new PropertyMapping().source("state", "when", "#target").target("state").using(new ConditionMapper()),
                new EdgeTargetPropertyMapping().source("name").target("appliesTo"));

        NodeMapper dataStateMapper = new NodeMapper(DataPropertyCondition.class,
                new PropertyMapping().target("statesConsidered").source("state", "#target").using(new DataConditionMapper()),
                new EdgeTargetPropertyMapping().source("name").target("appliesTo"));

        NodeMapper dataConnectorMapper = new NodeMapper(DataContentSelection.class,
                new PropertyMapping().target("selection").source("datacontentselection"),
                new EdgeTargetPropertyMapping().source("name").target("dataobjectId"));

        AssociationClassMapping groupedByMapping = new AssociationClassMapping().target("groupedBy").source("isGroupedBy").using(
                new NodeMapper(DataMeasure.class,
                        new InheritMapping().target("dataContentSelection").using(dataConnectorMapper)
                ));


        NodeMapper timeMeasure = new NodeMapper(TimeMeasure.class,
                new PropertyMapping().source("timemeasuretype").target("timeMeasureType").using(new EnumFieldMapper("LINEAR")),
                new AssociationClassMapping()
                        .source("TimeConnector")
                        .condition("conditiontype", "From")
                        .target("from")
                        .using(timeInstantMapper),
                new AssociationClassMapping()
                        .source("TimeConnector")
                        .condition("conditiontype", "To")
                        .target("to")
                        .using(timeInstantMapper)
        );

        NodeMapper countMeasure = new NodeMapper(CountMeasure.class,
                new AssociationClassMapping()
                        .target("when")
                        .source("appliesToElementConnector")
                        .using(timeInstantMapper)
        );

        NodeMapper stateConditionMeasure = new NodeMapper(StateConditionMeasure.class,
                new AssociationClassMapping()
                        .target("condition")
                        .source("appliesToElementConnector")
                        .using(stateMapper)

        );

        NodeMapper dataPropertyConditionMeasure = new NodeMapper(DataPropertyConditionMeasure.class,
                new AssociationClassMapping()
                        .target("condition")
                        .source("appliesToDataConnector")
                        .using(dataStateMapper)
        );

        NodeMapper dataMeasure = new NodeMapper(DataMeasure.class,
                new AssociationClassMapping()
                        .target("dataContentSelection")
                        .source("appliesToDataConnector")
                        .using(dataConnectorMapper),
                new AssociationClassMapping()
                        .target("precondition")
                        .source("appliesToDataConnector")
                        .using(dataStateMapper)
        );



        mappings(
                new NodeMapping()
                    .source("ppi")
                    .using(new NodeMapper(PPI.class,
                            new PropertyMapping().source("goals").target("goals").using(new StringListFieldMapper()),
                            new PropertyMapping().source("informed").target("informed").using(new StringListFieldMapper()),
                            new PropertyMapping().source("target").target("target").using(new PPITargetMapper()),
                            new PropertyMapping().source("lastinstancesscope", "timescope").target("scope").using(new ScopeMapper()),
                            new ChildMapping().target("measuredBy").using(new LessIncomingChild())
                    )),
                new NodeMapping()
                    .source("timeMeasure")
                    .using(timeMeasure),
                new NodeMapping()
                    .source("countMeasure")
                    .using(countMeasure),
                new NodeMapping()
                        .source("stateConditionMeasure")
                        .using(stateConditionMeasure),
                new NodeMapping()
                        .source("dataPropertyConditionMeasure")
                        .using(dataPropertyConditionMeasure),
                new NodeMapping()
                        .source("dataMeasure")
                        .using(dataMeasure),
                new NodeMapping()
                        .source("aggregatedMeasureGeneric")
                        .using(new NodeMapper(AggregatedMeasure.class,
                                new AssociationTargetMapping().target("baseMeasure").source("aggregates"),
                                groupedByMapping)),
                new NodeMapping()
                        .source("timeAggregatedMeasure")
                        .using(new NodeMapper(AggregatedMeasure.class,
                                new InheritMapping().target("baseMeasure").using(timeMeasure),
                                groupedByMapping)),
                new NodeMapping()
                        .source("countAggregatedMeasure")
                        .using(new NodeMapper(AggregatedMeasure.class,
                                new InheritMapping().target("baseMeasure").using(countMeasure),
                                groupedByMapping)),
                new NodeMapping()
                        .source("stateConditionAggregatedMeasure")
                        .using(new NodeMapper(AggregatedMeasure.class,
                                new InheritMapping().target("baseMeasure").using(stateConditionMeasure),
                                groupedByMapping)),
                new NodeMapping()
                        .source("dataPropertyConditionAggregatedMeasure")
                        .using(new NodeMapper(AggregatedMeasure.class,
                                new InheritMapping().target("baseMeasure").using(dataPropertyConditionMeasure),
                                groupedByMapping)),
                new NodeMapping()
                        .source("dataAggregatedMeasure")
                        .using(new NodeMapper(AggregatedMeasure.class,
                                new InheritMapping().target("baseMeasure").using(dataMeasure),
                                groupedByMapping)),
                new NodeMapping()
                        .source("derivedSingleInstanceMeasure")
                        .using(new NodeMapper(DerivedSingleInstanceMeasure.class,
                                new AssociationMapMapping().target("usedMeasureMap").source("uses").key("variable"))),
                new NodeMapping()
                        .source("derivedMultiInstanceMeasure")
                        .using(new NodeMapper(DerivedMultiInstanceMeasure.class,
                                new AssociationMapMapping().target("usedMeasureMap").source("uses").key("variable")))
        );
    }

    public Collection<PPI> map(BasicDiagram diagram) {
        return genericMap(diagram).onlyOfType(PPI.class).asCollection();
    }
}
