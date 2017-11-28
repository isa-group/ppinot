package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.MeasuresCollection;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.ScheduleBasic;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.DataObjectState;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * JSONMeasuresCollectionHandlerTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONMeasuresCollectionHandlerTest {

    @Test
    public void shouldSaveMeasures() throws Exception {
        JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler();
        MeasuresCollection mc = new MeasuresCollection("test", "description");
        mc.addDefinition(buildAFIP());

        handler.setCollection(mc);
        StringWriter sw = new StringWriter();
        handler.save(sw);
        sw.close();

        System.out.println(sw.toString());

        handler.load(new StringReader(sw.toString()));
        MeasuresCollection readCollection = handler.getCollection();
        assertEquals(1, readCollection.getDefinitions().size());
        MeasureDefinition md = readCollection.getDefinitions().get(0);
        assertTrue(md instanceof DerivedMultiInstanceMeasure);
    }

    @Test
    public void shouldReplaceWithTemplate() throws IOException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("proveedor", "p1");
        JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler();

        handler.load(new StringReader(countWithTemplate()), parameters);
        MeasuresCollection mc = handler.getCollection();

        CountMeasure count = (CountMeasure) mc.getDefinitions().get(0);
        DataObjectState state = (DataObjectState) count.getWhen().getChangesToState();
        assertEquals("Grupo == 'p1'", state.getName());
    }

    private String countWithTemplate() {
        return "{\n" +
                "  \"name\": \"ANS measures\",\n" +
                "  \"description\": \"Medidas para la gestión de incidentes\",\n" +
                "  \"definitions\": [{\n" +
                "                \"kind\": \"CountMeasure\",\n" +
                "                \"when\": {\n" +
                "                  \"kind\": \"TimeInstantCondition\",\n" +
                "                  \"appliesTo\": \"Grupo\",\n" +
                "                  \"changesToState\": {\n" +
                "                    \"dataObjectState\": {\n" +
                "                      \"name\": \"Grupo == '${proveedor}'\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }\n" +
                "              }]}";
    }

    private MeasureDefinition buildAFIP() throws Exception {
        Schedule workingHours = new ScheduleBasic(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0));

        TimeMeasure responseTime = new TimeMeasure();
        responseTime.setFrom(new TimeInstantCondition("EVENT 2 START MESSAGE", GenericState.START));
        responseTime.setTo(new TimeInstantCondition("Plan FI", GenericState.END));
        responseTime.setConsiderOnly(workingHours);
        responseTime.setUnitOfMeasure(TimeUnit.HOURS);

        TimeMeasure presenceTime = new TimeMeasure();
        presenceTime.setFrom(new TimeInstantCondition("Plan FI", GenericState.END));
        presenceTime.setTo(new TimeInstantCondition("Go to venue", GenericState.END));
        presenceTime.setConsiderOnly(workingHours);
        presenceTime.setUnitOfMeasure(TimeUnit.HOURS);

        TimeMeasure resolutionTime = new TimeMeasure();
        resolutionTime.setFrom(new TimeInstantCondition("Perform FI", GenericState.START));
        resolutionTime.setTo(new TimeInstantCondition("Perform FI", GenericState.END));
        resolutionTime.setConsiderOnly(workingHours);
        resolutionTime.setUnitOfMeasure(TimeUnit.HOURS);

        TimeMeasure documentationTime = new TimeMeasure();
        documentationTime.setFrom(new TimeInstantCondition("Create and submit FI documentation", GenericState.START));
        documentationTime.setTo(new TimeInstantCondition("Create and submit FI documentation", GenericState.END));
        documentationTime.setTimeMeasureType(TimeMeasureType.CYCLIC);
        documentationTime.setSingleInstanceAggFunction(Aggregator.SUM);
        documentationTime.setConsiderOnly(workingHours);
        documentationTime.setUnitOfMeasure(TimeUnit.HOURS);

        CountMeasure isCritical = new CountMeasure();
        isCritical.setWhen(new TimeInstantCondition("Record critical request", GenericState.START));
        CountMeasure isHigh = new CountMeasure();
        isHigh.setWhen(new TimeInstantCondition("Record high request", GenericState.START));
        CountMeasure isMid = new CountMeasure();
        isMid.setWhen(new TimeInstantCondition("Record mid request", GenericState.START));
        CountMeasure isLow = new CountMeasure();
        isLow.setWhen(new TimeInstantCondition("Record low request", GenericState.START));

        DerivedSingleInstanceMeasure accomplishedIntervention = new DerivedSingleInstanceMeasure();
        accomplishedIntervention.setFunction("(! (isCritical > 0) || (tresp < 0.5 && tpres < 4 && tres < 2 && (tdoc < 4 || Double.isNaN(tdoc)))) &&"
                + "(! (isHigh > 0) || (tresp < 2 && tpres < 8 && tres < 4 && (tdoc < 12 || Double.isNaN(tdoc)))) &&"
                + "(! (isMid > 0) || (tresp < 5 && tpres < 30 && tres < 6 && (tdoc < 24 || Double.isNaN(tdoc)))) &&"
                + "(! (isLow > 0) || (tresp < 5 && tpres < 60 && tres < 8 && (tdoc < 48 || Double.isNaN(tdoc))))");
        accomplishedIntervention.addUsedMeasure("isCritical", isCritical);
        accomplishedIntervention.addUsedMeasure("isHigh", isHigh);
        accomplishedIntervention.addUsedMeasure("isMid", isMid);
        accomplishedIntervention.addUsedMeasure("isLow", isLow);
        accomplishedIntervention.addUsedMeasure("tresp", responseTime);
        accomplishedIntervention.addUsedMeasure("tpres", presenceTime);
        accomplishedIntervention.addUsedMeasure("tres", resolutionTime);
        accomplishedIntervention.addUsedMeasure("tdoc", documentationTime);

        AggregatedMeasure totalAccomplished = new AggregatedMeasure();
        totalAccomplished.setBaseMeasure(accomplishedIntervention);
        totalAccomplished.setAggregationFunction(Aggregator.SUM);

        AggregatedMeasure totalInterventions = new AggregatedMeasure();
        CountMeasure count = new CountMeasure();
        count.setWhen(new TimeInstantCondition("EVENT 2 START MESSAGE", GenericState.START));
        totalInterventions.setBaseMeasure(count);
        totalInterventions.setAggregationFunction(Aggregator.SUM);

        DerivedMultiInstanceMeasure afip = new DerivedMultiInstanceMeasure();
        afip.setId("afip");
        afip.setFunction("a/b*100");
        afip.addUsedMeasure("a", totalAccomplished);
        afip.addUsedMeasure("b", totalInterventions);

        return afip;
    }
}
