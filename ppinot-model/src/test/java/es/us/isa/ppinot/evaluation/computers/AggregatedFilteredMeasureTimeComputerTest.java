package es.us.isa.ppinot.evaluation.computers;

import org.joda.time.DateTime;
import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregatedFilteredMeasureTimeComputerTest extends MeasureComputerHelper {

    @Test
    public void testComputeAggregatedTimeRelativeScope1Day() {
        LogEntryHelper helper = new LogEntryHelper(10);

        CountMeasure countMeasure = new CountMeasure();
        countMeasure.setWhen(new TimeInstantCondition("Analyse RFC", GenericState.END));

        DerivedSingleInstanceMeasure filterMeasure = new DerivedSingleInstanceMeasure();
        filterMeasure.setFunction("param != 4");
        filterMeasure.addUsedMeasure("param", countMeasure);

        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, createCountMeasure(withCondition("Analyse RFC", GenericState.START)), filterMeasure);
        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new SimpleTimeFilter(Period.DAILY, 1, true));

        computer.update(helper.newInstance("i1", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i3", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i4", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i5", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-3)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", DateTime.now().plusDays(-3)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i5", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i5", DateTime.now().plusDays(-2)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", DateTime.now().plusDays(-2)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", DateTime.now().plusDays(-1)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-2)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-1)));
        computer.update(helper.newInstance("i1", EventType.complete, DateTime.now().minusDays(1)));
        computer.update(helper.newInstance("i3", EventType.complete, DateTime.now().minusDays(4).plusMillis(10)));
        computer.update(helper.newInstance("i4", EventType.complete, DateTime.now().minusDays(1).plusMillis(20)));
        computer.update(helper.newInstance("i5", EventType.complete, DateTime.now().minusDays(2).plusMillis(30)));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
    }

    @Test
    public void testComputeAggregatedGroupedTimeRelativeScope1Day() {
        LogEntryHelper helper = new LogEntryHelper(10);

        // Filter
        CountMeasure countMeasure = new CountMeasure();
        countMeasure.setWhen(new TimeInstantCondition("Analyse RFC", GenericState.END));
        
        DerivedSingleInstanceMeasure filterMeasure = new DerivedSingleInstanceMeasure();
        filterMeasure.setFunction("param != 4");
        filterMeasure.addUsedMeasure("param", countMeasure);
        
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, createCountMeasure(withCondition("Analyse RFC", GenericState.START)), filterMeasure);
        
        // Group by
        List<DataContentSelection> groupBy = new ArrayList<DataContentSelection>();
        groupBy.add(new DataContentSelection("nodo", ""));
        measure.setGroupedBy(groupBy);
        
        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new SimpleTimeFilter(Period.DAILY, 1, true));

        Map<String, Object> inst1Data = new HashMap<String, Object>();
        Map<String, Object> inst3Data = new HashMap<String, Object>();
        Map<String, Object> inst4Data = new HashMap<String, Object>();
        Map<String, Object> inst5Data = new HashMap<String, Object>();
        inst1Data.put("nodo", "sevilla");
        inst3Data.put("nodo", "sevilla");
        inst4Data.put("nodo", "sevilla");
        inst5Data.put("nodo", "sevilla");

        computer.update(helper.newInstance("i1", EventType.ready, DateTime.now().plusDays(-6)).withData(inst1Data));
        computer.update(helper.newInstance("i3", EventType.ready, DateTime.now().plusDays(-6)).withData(inst3Data));
        computer.update(helper.newInstance("i4", EventType.ready, DateTime.now().plusDays(-6)).withData(inst4Data));
        computer.update(helper.newInstance("i5", EventType.ready, DateTime.now().plusDays(-6)).withData(inst5Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-4)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-4)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", DateTime.now().plusDays(-4)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", DateTime.now().plusDays(-4)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-4)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-3)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", DateTime.now().plusDays(-4)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", DateTime.now().plusDays(-3)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i5", DateTime.now().plusDays(-4)).withData(inst5Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i5", DateTime.now().plusDays(-2)).withData(inst5Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", DateTime.now().plusDays(-2)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i4", DateTime.now().plusDays(-1)).withData(inst4Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-2)).withData(inst1Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-1)).withData(inst1Data));
        computer.update(helper.newInstance("i1", EventType.complete, DateTime.now().minusDays(1)).withData(inst1Data));
        computer.update(helper.newInstance("i3", EventType.complete, DateTime.now().minusDays(4).plusMillis(10)).withData(inst3Data));
        computer.update(helper.newInstance("i4", EventType.complete, DateTime.now().minusDays(1).plusMillis(20)).withData(inst4Data));
        computer.update(helper.newInstance("i5", EventType.complete, DateTime.now().minusDays(2).plusMillis(30)).withData(inst5Data));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
    }
    
    @Test
    public void testComputeUnfinishedAggregatedTimeRelativeScope1Day() {
        LogEntryHelper helper = new LogEntryHelper(10);

        CountMeasure countMeasure = new CountMeasure();
        countMeasure.setWhen(new TimeInstantCondition("Analyse RFC", GenericState.START));

        DerivedSingleInstanceMeasure filterMeasure = new DerivedSingleInstanceMeasure();
        filterMeasure.setFunction("param != 4");
        filterMeasure.addUsedMeasure("param", countMeasure);

        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, createCountMeasure(withCondition("Analyse RFC", GenericState.START)), filterMeasure);
        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new SimpleTimeFilter(Period.DAILY, 1, true));

        computer.update(helper.newInstance("i1", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i3", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i4", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i5", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i5", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i4", DateTime.now().plusDays(-2)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-2)));
        computer.update(helper.newInstance("i1", EventType.complete, DateTime.now().minusDays(1)));
        computer.update(helper.newInstance("i3", EventType.complete, DateTime.now().minusDays(4).plusMillis(10)));
        computer.update(helper.newInstance("i4", EventType.complete, DateTime.now().minusDays(1).plusMillis(20)));
        computer.update(helper.newInstance("i5", EventType.complete, DateTime.now().minusDays(2).plusMillis(30)));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
    }

}
