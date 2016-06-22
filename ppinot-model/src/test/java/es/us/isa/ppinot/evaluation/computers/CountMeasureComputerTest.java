package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.model.state.GenericState;
import org.junit.Test;

/**
 * CountMeasureComputerTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class CountMeasureComputerTest extends MeasureComputerHelper {
    @Test
    public void testCompute() throws Exception {
        LogEntryHelper helper = new LogEntryHelper();
        CountMeasureComputer computer = createCountMeasureComputer("Analyse RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue(LogEntryHelper.DEFAULT_INSTANCE, 1);
    }

    @Test
    public void testComputeNotAppear() throws Exception {
        LogEntryHelper helper = new LogEntryHelper();
        CountMeasureComputer computer = createCountMeasureComputer("Cancel RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue(LogEntryHelper.DEFAULT_INSTANCE, 0);
    }

    @Test
    public void testComputeInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper();
        CountMeasureComputer computer = createCountMeasureComputer("Analyse RFC", GenericState.END);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i2"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i2"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertInstanceHasValue("i1", 2);
        asserter.assertInstanceHasValue("i2", 1);
    }

}
