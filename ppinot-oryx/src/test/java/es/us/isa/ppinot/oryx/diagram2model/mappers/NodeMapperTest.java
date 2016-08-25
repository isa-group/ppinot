package es.us.isa.ppinot.oryx.diagram2model.mappers;

import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.state.GenericState;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.generic.GenericShape;
import org.oryxeditor.server.diagram2model.MappedElements;
import org.oryxeditor.server.diagram2model.mappers.NodeMapper;
import org.oryxeditor.server.diagram2model.mappers.child.LessIncomingChild;
import org.oryxeditor.server.diagram2model.mappers.fields.EnumFieldMapper;
import org.oryxeditor.server.diagram2model.mappers.fields.StringListFieldMapper;
import org.oryxeditor.server.diagram2model.mappings.AssociationClassMapping;
import org.oryxeditor.server.diagram2model.mappings.ChildMapping;
import org.oryxeditor.server.diagram2model.mappings.EdgeTargetPropertyMapping;
import org.oryxeditor.server.diagram2model.mappings.PropertyMapping;
import org.oryxeditor.server.test.DiagramTestUtils;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * NodeMapperTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class NodeMapperTest {

    @Test
    public void testPPI() throws Exception {
        BasicDiagram diagram = DiagramTestUtils.loadDiagram(new InputStreamReader(getClass().getResourceAsStream("/RFCManagement.json")));
        GenericShape<?, ?> shape = DiagramTestUtils.findByResourceId(diagram, "sid-8EF431CB-5BD0-4F70-A7C9-23E558A94BE3");

        NodeMapper nodeMapper = new NodeMapper(PPI.class,
                new PropertyMapping().source("goals").target("goals").using(new StringListFieldMapper()),
                new PropertyMapping().source("informed").target("informed").using(new StringListFieldMapper()),
                new PropertyMapping().source("target").target("target").using(new PPITargetMapper()),
                new PropertyMapping().source("lastinstancesscope", "timescope").target("scope").using(new ScopeMapper()),
                new ChildMapping().target("measuredBy").using(new LessIncomingChild())
        );

        PPI ppi = (PPI) nodeMapper.map(shape);

        Assert.assertEquals("PPI2", ppi.getId());
        Assert.assertEquals("Average time of RFC Analysis", ppi.getName());
        Assert.assertEquals(2, ppi.getGoals().size());
        Assert.assertEquals(((Double) 5.0), ppi.getTarget().getRefMin());
        Assert.assertEquals(100, ((LastInstancesFilter) ppi.getScope()).getNumberOfInstances());

        AggregatedMeasure md = new AggregatedMeasure();
        md.setId("sid-3BDB4D67-47A2-4D58-A993-9BF129718E72");
        md.setName("Avg time of RFC analysis");
        MappedElements<Object> map = new MappedElements<Object>();
        map.put("sid-3BDB4D67-47A2-4D58-A993-9BF129718E72", md, null);

        nodeMapper.linkConnections(shape, ppi, map);

        Assert.assertEquals("Avg time of RFC analysis", ppi.getMeasuredBy().getName());

    }

    @Test
    public void testTimeMeasure() throws IOException, JSONException {
        BasicDiagram diagram = DiagramTestUtils.loadDiagram(new InputStreamReader(getClass().getResourceAsStream("/RFCManagement.json")));
        GenericShape<?, ?> shape = DiagramTestUtils.findByResourceId(diagram, "sid-3BDB4D67-47A2-4D58-A993-9BF129718E72");

        NodeMapper timeInstantMapper = new NodeMapper(TimeInstantCondition.class,
                new PropertyMapping().source("state", "when", "#target").target("changesToState").using(new ConditionMapper()),
                new EdgeTargetPropertyMapping().source("name").target("appliesTo"));

        NodeMapper nodeMapper = new NodeMapper(TimeMeasure.class,
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

        TimeMeasure timeMeasure = (TimeMeasure) nodeMapper.map(shape);

        Assert.assertEquals("Avg time of RFC analysis", timeMeasure.getName());
        Assert.assertEquals("Analyse RFC", timeMeasure.getFrom().getAppliesTo());
        Assert.assertEquals("Analyse RFC", timeMeasure.getTo().getAppliesTo());
        Assert.assertEquals(GenericState.START, timeMeasure.getFrom().getChangesToState());
        Assert.assertEquals(GenericState.END, timeMeasure.getTo().getChangesToState());
        Assert.assertEquals(TimeMeasureType.LINEAR, timeMeasure.getTimeMeasureType());

    }

}