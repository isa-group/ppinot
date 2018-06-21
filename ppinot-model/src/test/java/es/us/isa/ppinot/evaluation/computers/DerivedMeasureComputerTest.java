package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureScopeImpl;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;
import org.junit.Test;

import java.util.Arrays;

/**
 * DerivedMeasureComputerTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class DerivedMeasureComputerTest extends MeasureComputerHelper {
    @Test
    public void testComputeInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper();
        CountMeasure measure1 = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        CountMeasure measure2 = createCountMeasure(withCondition("Approve RFC", GenericState.END));

        DerivedMeasureComputer computer = createDerivedSingleInstanceMeasureComputer("a1/a0 * 100", measure1, measure2);


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
        asserter.assertInstanceHasDoubleValue("i1", 50);
        asserter.assertInstanceHasDoubleValue("i2", 0);
    }

    @Test
    public void testComputeInstancesOverride() throws Exception {
        LogEntryHelper helper = new LogEntryHelper();

        CountMeasure measure1 = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        CountMeasure measure2 = createCountMeasure(withCondition("Approve RFC", GenericState.END));

        ComputerConfig config = new ComputerConfig(null);
        MeasureScopeImpl scope = new MeasureScopeImpl("x", Arrays.asList("i1"));
        config.addOverride(new Measure(measure2, scope, 2));

        DerivedMeasureComputer computer = createDerivedSingleInstanceMeasureComputer("a1/a0 * 100", config, measure1, measure2);

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
        asserter.assertInstanceHasDoubleValue("i1", 100);
        asserter.assertInstanceHasDoubleValue("i2", 0);
    }

    private DerivedMeasureComputer createDerivedSingleInstanceMeasureComputer(String function, MeasureDefinition... computers) {
        DerivedSingleInstanceMeasure measure = createDerivedSingleInstanceMeasure(function, computers);
        return new DerivedMeasureComputer(measure, new ComputerConfig(null));
    }

    private DerivedMeasureComputer createDerivedSingleInstanceMeasureComputer(String function, ComputerConfig config, MeasureDefinition... computers) {
        DerivedSingleInstanceMeasure measure = createDerivedSingleInstanceMeasure(function, computers);
        return new DerivedMeasureComputer(measure, config);
    }

    private DerivedSingleInstanceMeasure createDerivedSingleInstanceMeasure(String function, MeasureDefinition[] computers) {
        DerivedSingleInstanceMeasure measure = new DerivedSingleInstanceMeasure();
        measure.setFunction(function);

        for (int i = 0; i < computers.length; i++) {
            measure.addUsedMeasure("a"+i, computers[i]);
        }

        return measure;
    }
}
