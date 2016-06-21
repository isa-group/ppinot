package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
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
        asserter.assertInstanceHasValue("i1", 30);
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
        asserter.assertInstanceHasValue("i1", Double.NaN);
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
        asserter.assertInstanceHasValue("i1", 70);
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
        asserter.assertInstanceHasValue("i1", 70);
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
        asserter.assertInstanceHasValue("i1", 40);
    }
    
    @Test
    public void testUnfinishedComputeLinearInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        TimeMeasureComputer computer = createLinearUnfinishedTimeMeasureComputer("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValueGreaterThan("i1", 20);
    }

}
