package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.evaluators.LogMeasureEvaluator;
import es.us.isa.ppinot.evaluation.evaluators.MeasureEvaluator;
import es.us.isa.ppinot.evaluation.logs.LogProvider;
import es.us.isa.ppinot.evaluation.logs.MXMLLog;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * FullTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class FullTest {
    private TimeMeasure responseTime;
    private TimeMeasure presenceTime;
    private TimeMeasure resolutionTime;
    private TimeMeasure documentationTime;

    private DerivedSingleInstanceMeasure accomplishedIntervention;
    private AggregatedMeasure totalAccomplished;
    private AggregatedMeasure totalInterventions;
    private DerivedMultiInstanceMeasure afip;

    @Test
    public void testAFIP() throws Exception {
        List<Measure> measures = compute(afip);
        printMeasures(measures);
//        printSummary(measures);
    }

    @Test
    public void testResponseTime() throws Exception {
        List<Measure> measures = compute(responseTime);
        assertSingleInstance(measures);
        assertGreaterThan(0, measures);
    }

    @Test
    public void testPresenceTime() throws Exception {
        List<Measure> measures = compute(presenceTime);
        assertSingleInstance(measures);
        assertGreaterThan(0, measures);
    }
    @Test
    public void testResolutionTime() throws Exception {
        List<Measure> measures = compute(resolutionTime);
        assertSingleInstance(measures);
        assertGreaterThan(0, measures);
    }
    @Test
    public void testDocumentationTime() throws Exception {
        List<Measure> measures = compute(documentationTime);
        assertSingleInstance(measures);
        Assert.assertEquals(3000, measures.size());
    }
    @Test
    public void testTotalInterventions() throws Exception {
        List<Measure> measures = compute(totalInterventions);
        for (Measure m: measures) {
            Assert.assertEquals(m.getValue(), (double) m.getInstances().size(), 0);
        }
    }
    @Test
    public void testAccomplishedIntervention() throws Exception {
        List<Measure> measures = compute(accomplishedIntervention);
        Assert.assertEquals(3000, measures.size());
        printMeasures(measures);
    }

    private List<Measure> compute(MeasureDefinition measure) throws Exception {
        LogProvider mxmlLog = new MXMLLog(getClass().getResourceAsStream("simulation_logs.mxml"), null);
        MeasureEvaluator evaluator = new LogMeasureEvaluator(mxmlLog);

        return evaluator.eval(measure, new SimpleTimeFilter(Period.MONTHLY, 1, false));
    }

    @Before
    public void buildAFIP() throws Exception {
        Schedule workingHours = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0));

        responseTime = new TimeMeasure();
        responseTime.setFrom(new TimeInstantCondition("EVENT 2 START MESSAGE", GenericState.START));
        responseTime.setTo(new TimeInstantCondition("Plan FI", GenericState.END));
        responseTime.setConsiderOnly(workingHours);
        responseTime.setUnitOfMeasure(TimeUnit.HOURS);

        presenceTime = new TimeMeasure();
        presenceTime.setFrom(new TimeInstantCondition("Plan FI", GenericState.END));
        presenceTime.setTo(new TimeInstantCondition("Go to venue", GenericState.END));
        presenceTime.setConsiderOnly(workingHours);
        presenceTime.setUnitOfMeasure(TimeUnit.HOURS);

        resolutionTime = new TimeMeasure();
        resolutionTime.setFrom(new TimeInstantCondition("Perform FI", GenericState.START));
        resolutionTime.setTo(new TimeInstantCondition("Perform FI", GenericState.END));
        resolutionTime.setConsiderOnly(workingHours);
        resolutionTime.setUnitOfMeasure(TimeUnit.HOURS);

        documentationTime = new TimeMeasure();
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

        accomplishedIntervention = new DerivedSingleInstanceMeasure();
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

        totalAccomplished = new AggregatedMeasure();
        totalAccomplished.setBaseMeasure(accomplishedIntervention);
        totalAccomplished.setAggregationFunction(Aggregator.SUM);
        compute(totalAccomplished);

        totalInterventions = new AggregatedMeasure();
        CountMeasure count = new CountMeasure();
        count.setWhen(new TimeInstantCondition("EVENT 2 START MESSAGE", GenericState.START));
        totalInterventions.setBaseMeasure(count);
        totalInterventions.setAggregationFunction(Aggregator.SUM);
        compute(totalInterventions);

        afip = new DerivedMultiInstanceMeasure("d", "afip", "", "", "", "a/b*100");
        afip.addUsedMeasure("a", totalAccomplished);
        afip.addUsedMeasure("b", totalInterventions);
    }

    public void testNewLog() throws Exception {
        LogProvider mxmlLog = new MXMLLog(getClass().getResourceAsStream("simulation_logs.mxml"), null);
        MeasureEvaluator evaluator = new LogMeasureEvaluator(mxmlLog);

        CountMeasure countCritical = new CountMeasure();
        countCritical.setWhen(new TimeInstantCondition("Documentation Accepted", GenericState.START));
        AggregatedMeasure measure = new AggregatedMeasure();
        measure.setAggregationFunction(Aggregator.SUM);
        measure.setBaseMeasure(countCritical);


        List<? extends Measure> measures = evaluator.eval(measure, new SimpleTimeFilter(Period.MONTHLY, 1, false));

        printMeasures(measures);
    }

    public void testTimeLog() throws Exception {
        LogProvider mxmlLog = new MXMLLog(getClass().getResourceAsStream("simulation_logs.mxml"), null);
        MeasureEvaluator evaluator = new LogMeasureEvaluator(mxmlLog);

        TimeMeasure responseTime = new TimeMeasure();
        responseTime.setFrom(new TimeInstantCondition("EVENT 2 START MESSAGE", GenericState.END));
        responseTime.setTo(new TimeInstantCondition("Plan FI", GenericState.END));
        responseTime.setConsiderOnly(new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8, 0), new LocalTime(20, 0)));

        AggregatedMeasure measure = new AggregatedMeasure();
        measure.setAggregationFunction(Aggregator.AVG);
        measure.setBaseMeasure(responseTime);

        List<? extends Measure> measures = evaluator.eval(measure, new SimpleTimeFilter(Period.MONTHLY, 1, false));

        printTimeMeasures(measures);
    }

    private void assertSingleInstance(List<Measure> measures) {
        for (Measure m: measures) {
            Assert.assertTrue(m.getInstances().size() > 0);
        }
    }

    private void assertGreaterThan(double value, List<Measure> measures) {
        for (Measure m: measures) {
            Assert.assertTrue(m.getValue() > value);
        }
    }


    private void printMeasures(List<? extends Measure> measures) {
        for (Measure m: measures) {
            double value = m.getValue();
            System.out.println("Value: " + value);
            System.out.println("Instances: " + m.getInstances());
            System.out.println("Number of instances: " + m.getInstances().size());
//            Assert.assertTrue(value > 0);
        }
    }

    private void printSummary(List<Measure> measures) {

        System.out.println(measures.size());
    }

    private void printTimeMeasures(List<? extends Measure> measures) {
        for (Measure m: measures) {
            System.out.println("Value: " + new org.joda.time.Period((long)m.getValue(), PeriodType.minutes()));

            System.out.println("Number of instances: " + m.getInstances().size());
        }

    }



}
