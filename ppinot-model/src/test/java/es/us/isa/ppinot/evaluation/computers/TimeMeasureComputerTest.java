package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * TimeMeasureComputerTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeMeasureComputerTest extends MeasureComputerHelper {

    @Test
    public void testComputeLinearInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createLinearTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END);
        
        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 30);
    }

    @Test
    public void testComputeLinearInstancesWithSeveralFrom() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createLinearTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 30);
    }

    @Test
    public void testComputeLinearInstancesInSpecificPointOfTime() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        
        DataMeasure measure = new DataMeasure("id", "name", "desc", null, null, new DataContentSelection("provider", ""), new TimeInstantCondition("Analyse RFC", GenericState.START));
        DataMeasureComputer computer = new DataMeasureComputer(measure);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1").withData("provider", "p1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasStringValue("i1", "p1");
    }

    @Test
    public void testComputeLinearInstancesInSpecificPointOfTimeWithMultipleInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        
        DataMeasure measure = new DataMeasure("id", "name", "desc", null, null, new DataContentSelection("provider", ""), new TimeInstantCondition("Analyse RFC", GenericState.START));
        DataMeasureComputer computer = new DataMeasureComputer(measure);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1").withData("provider", "p1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i1").withData("provider", "p2"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasStringValue("i1", "p2");
    }

    @Test
    public void testComputeLinearInstancesInSpecificPointOfTimeNotFound() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        
        DataMeasure measure = new DataMeasure("id", "name", "desc", null, null, new DataContentSelection("provider", ""), new TimeInstantCondition("Analyse RFC", GenericState.START));
        DataMeasureComputer computer = new DataMeasureComputer(measure);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
    }

    @Test
    public void testComputeUnfinishedLinearInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createLinearTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END);
        DateTime initial = DateTime.now();

        computer.update(helper.newAssignEntry("Analyse RFC", "i1", initial));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", Double.NaN);
    }

    @Test
    public void testComputeTwoEndsLinearInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createLinearTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 70);
    }

    @Test
    public void testComputeCyclicSumInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createCyclicTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, Aggregator.SUM);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i2"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertInstanceHasDoubleValue("i1", 70);
    }

    @Test
    public void testComputeCyclicMaxInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createCyclicTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, Aggregator.MAX);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i2"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertInstanceHasDoubleValue("i1", 40);
    }
    
    @Test
    public void testUnfinishedComputerLinearInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createLinearUnfinishedTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValueGreaterThanOrEqual("i1", 20);
    }

}
