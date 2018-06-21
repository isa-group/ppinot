package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.*;

public class AggregatedMeasureOverrideTest extends MeasureComputerHelper {

    
    @Test
    public void testOverrideOneMeasure() {
        LogEntryHelper helper = new LogEntryHelper(10);
        DateTime reference = new DateTime(2018, 06,19, 16,38);

        CountMeasure countMeasure = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, countMeasure);

        ComputerConfig computerConfig = new ComputerConfig(new SimpleTimeFilter(Period.DAILY, 1, false));
        TemporalMeasureScopeImpl scope = new TemporalMeasureScopeImpl("x", Arrays.asList("i3"), reference.minusDays(4).withTimeAtStartOfDay(), reference.minusDays(3).withTimeAtStartOfDay().minusMillis(1));
        computerConfig.add(new Measure(countMeasure, scope, 10));

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, computerConfig);
        runLog(helper, reference, computer);
        List<? extends Measure> compute = computer.compute();

        MeasuresAsserter asserter = new MeasuresAsserter(compute);

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 10);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 6);
    }

    @Test
    public void testOverrideNoneMeasure() {
        LogEntryHelper helper = new LogEntryHelper(10);
        DateTime reference = new DateTime(2018, 06,19, 16,38);

        CountMeasure countMeasure = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, countMeasure);

        ComputerConfig computerConfig = new ComputerConfig(new SimpleTimeFilter(Period.DAILY, 1, false));
        TemporalMeasureScopeImpl scope = new TemporalMeasureScopeImpl("x", Arrays.asList("i3"), reference.minusDays(4).withTimeAtStartOfDay(), reference.minusDays(3).withTimeAtStartOfDay().minusMillis(1));
        computerConfig.add(new Measure(createCountMeasure(withCondition("Analyse RFC", GenericState.START)), scope, 10));

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, computerConfig);
        runLog(helper, reference, computer);
        List<? extends Measure> compute = computer.compute();

        MeasuresAsserter asserter = new MeasuresAsserter(compute);

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 6);
    }

    @Test
    public void testOverrideSeveralMeasures() {
        LogEntryHelper helper = new LogEntryHelper(10);
        DateTime reference = new DateTime(2018, 06,19, 16,38);

        CountMeasure countMeasure = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, countMeasure);

        ComputerConfig computerConfig = new ComputerConfig(new SimpleTimeFilter(Period.DAILY, 1, false));
        TemporalMeasureScopeImpl scope = new TemporalMeasureScopeImpl("x", Arrays.asList("i3"), reference.minusDays(4).withTimeAtStartOfDay(), reference.minusDays(3).withTimeAtStartOfDay().minusMillis(1));
        TemporalMeasureScopeImpl scope2 = new TemporalMeasureScopeImpl("x", Arrays.asList("i1"), reference.minusDays(1).withTimeAtStartOfDay(), reference.withTimeAtStartOfDay().minusMillis(1));

        computerConfig.add(new Measure(countMeasure, scope, 10));
        computerConfig.add(new Measure(countMeasure, scope2, 1));

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, computerConfig);
        runLog(helper, reference, computer);
        List<? extends Measure> compute = computer.compute();

        MeasuresAsserter asserter = new MeasuresAsserter(compute);

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 10);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 3);
    }

    @Test
    public void testOverrideSeveralMeasuresInSameInterval() {
        LogEntryHelper helper = new LogEntryHelper(10);
        DateTime reference = new DateTime(2018, 06,19, 16,38);

        CountMeasure countMeasure = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, countMeasure);

        ComputerConfig computerConfig = new ComputerConfig(new SimpleTimeFilter(Period.DAILY, 1, false));
        TemporalMeasureScopeImpl scope = new TemporalMeasureScopeImpl("x", Arrays.asList("i1", "i4"), reference.minusDays(1).withTimeAtStartOfDay(), reference.withTimeAtStartOfDay().minusMillis(1));
        computerConfig.add(new Measure(countMeasure, scope, 10));

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, computerConfig);
        runLog(helper, reference, computer);
        List<? extends Measure> compute = computer.compute();

        MeasuresAsserter asserter = new MeasuresAsserter(compute);

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 20);
    }

    @Test
    public void testOverrideSeveralMeasuresInSameIntervalWithDifferentValues() {
        LogEntryHelper helper = new LogEntryHelper(10);
        DateTime reference = new DateTime(2018, 06,19, 16,38);

        CountMeasure countMeasure = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, countMeasure);

        ComputerConfig computerConfig = new ComputerConfig(new SimpleTimeFilter(Period.DAILY, 1, false));
        TemporalMeasureScopeImpl scope = new TemporalMeasureScopeImpl("x", Arrays.asList("i1"), reference.minusDays(1).withTimeAtStartOfDay(), reference.withTimeAtStartOfDay().minusMillis(1));
        TemporalMeasureScopeImpl scope2 = new TemporalMeasureScopeImpl("x", Arrays.asList("i4"), reference.minusDays(1).withTimeAtStartOfDay(), reference.withTimeAtStartOfDay().minusMillis(1));
        computerConfig.add(new Measure(countMeasure, scope, 10));
        computerConfig.add(new Measure(countMeasure, scope2, 7));

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, computerConfig);
        runLog(helper, reference, computer);
        List<? extends Measure> compute = computer.compute();

        MeasuresAsserter asserter = new MeasuresAsserter(compute);

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 17);
    }

    @Test
    public void testOverrideWithGroupBy() {
        LogEntryHelper helper = new LogEntryHelper(10);
        DateTime reference = new DateTime(2018, 06, 20, 11, 11);

        CountMeasure countMeasure = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, countMeasure);
        List<DataContentSelection> groupBy = new ArrayList<DataContentSelection>();
        groupBy.add(new DataContentSelection("nodo", ""));
        groupBy.add(new DataContentSelection("centro", ""));
        measure.setGroupedBySelections(groupBy);

        Map<String, Object> inst1Data = new HashMap<String, Object>();
        Map<String, Object> inst3Data = new HashMap<String, Object>();
        Map<String, Object> inst4Data = new HashMap<String, Object>();
        Map<String, Object> inst5Data = new HashMap<String, Object>();

        inst1Data.put("nodo", "sevilla");
        inst1Data.put("centro", "x");
        inst3Data.put("nodo", "sevilla");
        inst3Data.put("centro", "y");
        inst4Data.put("nodo", "cordoba");
        inst4Data.put("centro", "z");
        inst5Data.put("nodo", "huelva");
        inst5Data.put("centro", "t");

        ComputerConfig computerConfig = new ComputerConfig(new SimpleTimeFilter(Period.DAILY, 1, false));
        GroupByTemporalMeasureScopeImpl scope = new GroupByTemporalMeasureScopeImpl("x", Arrays.asList("i4"), reference.minusDays(1).withTimeAtStartOfDay(), reference.withTimeAtStartOfDay().minusMillis(1));
        HashMap<String, String> groupParameters = new HashMap<String, String>();
        groupParameters.put("nodo", "cordoba");
        scope.setGroupParameters(groupParameters);
        computerConfig.add(new Measure(countMeasure, scope, 10));


        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, computerConfig);

        computer.update(helper.newInstance("i1", EventType.ready, reference.plusDays(-6)).withData(inst1Data));
        computer.update(helper.newInstance("i3", EventType.ready, reference.plusDays(-6)).withData(inst3Data));
        computer.update(helper.newInstance("i4", EventType.ready, reference.plusDays(-6)).withData(inst4Data));
        computer.update(helper.newInstance("i5", EventType.ready, reference.plusDays(-6)).withData(inst5Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-5)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-4)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-5)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-4)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", reference.plusDays(-5)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", reference.plusDays(-4)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", reference.plusDays(-5)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", reference.plusDays(-4)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-4)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-3)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", reference.plusDays(-4)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", reference.plusDays(-3)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i5", reference.plusDays(-4)).withData(inst5Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i5", reference.plusDays(-2)).withData(inst5Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", reference.plusDays(-2)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", reference.plusDays(-1)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-2)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-1)).withData(inst1Data));
        computer.update(helper.newInstance("i1", EventType.complete, reference.minusDays(1)).withData(inst1Data));
        computer.update(helper.newInstance("i3", EventType.complete, reference.minusDays(4)).withData(inst3Data));
        computer.update(helper.newInstance("i4", EventType.complete, reference.minusDays(1)).withData(inst4Data));
        computer.update(helper.newInstance("i5", EventType.complete, reference.minusDays(2)).withData(inst5Data));

        List<Measure> result = (List<Measure>) computer.compute();

        MeasuresAsserter asserter = new MeasuresAsserter(result);
        asserter.assertTheNumberOfMeasuresIs(4);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 4);
        asserter.assertValueOfInterval(3, 10);

    }


    private void runLog(LogEntryHelper helper, DateTime reference, AggregatedMeasureComputer computer) {
        computer.update(helper.newInstance("i3", EventType.ready, reference.plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", reference.plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", reference.plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", reference.plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", reference.plusDays(-4)));
        computer.update(helper.newInstance("i3", EventType.complete, reference.minusDays(4)));

        computer.update(helper.newInstance("i5", EventType.ready, reference.plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i5", reference.plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i5", reference.plusDays(-2)));
        computer.update(helper.newInstance("i5", EventType.complete, reference.minusDays(2)));

        computer.update(helper.newInstance("i1", EventType.ready, reference.plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-3)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", reference.plusDays(-2)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", reference.plusDays(-1)));
        computer.update(helper.newInstance("i1", EventType.complete, reference.minusDays(1)));

        computer.update(helper.newInstance("i4", EventType.ready, reference.plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", reference.plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", reference.plusDays(-3)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", reference.plusDays(-2)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", reference.plusDays(-1)));
        computer.update(helper.newInstance("i4", EventType.complete, reference.minusDays(1)));
    }


}
