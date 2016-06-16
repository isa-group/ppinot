package es.us.isa.ppinot.evaluation.computers;

import org.joda.time.DateTime;
import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregatedGroupByMeasureTimeComputerTest extends MeasureComputerHelper {
    
    //TODO: Realizar algunos tests con período superior al tiempo de medida.

    @Test
    public void testGroupByComputeAggregatedTimeRelativeScope1Day() {
        LogEntryHelper helper = new LogEntryHelper(10);
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, createCountMeasure(withCondition("Analyse RFC", GenericState.END)));

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

        List<Measure> result = (List<Measure>) computer.compute();
        MeasuresAsserter asserter = new MeasuresAsserter(result);
        asserter.assertTheNumberOfMeasuresIs(3);
    }

    @Test
    public void testGroupByComputeAggregationWithIgnoredColumn() {
        LogEntryHelper helper = new LogEntryHelper(10);
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, createCountMeasure(withCondition("Analyse RFC", GenericState.END)));

        List<DataContentSelection> groupBy = new ArrayList<DataContentSelection>();
        groupBy.add(new DataContentSelection("nodo", ""));
        groupBy.add(new DataContentSelection("centro", ""));
        measure.setGroupedBy(groupBy);

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new SimpleTimeFilter(Period.DAILY, 1, true));

        Map<String, Object> inst1Data = new HashMap<String, Object>();
        Map<String, Object> inst3Data = new HashMap<String, Object>();
        Map<String, Object> inst4Data = new HashMap<String, Object>();
        Map<String, Object> inst5Data = new HashMap<String, Object>();

        inst1Data.put("nodo", "sevilla");
        inst3Data.put("nodo", "sevilla");
        inst4Data.put("nodo", "cordoba");
        inst5Data.put("nodo", "huelva");

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

        List<Measure> result = (List<Measure>) computer.compute();
        MeasuresAsserter asserter = new MeasuresAsserter(result);
        asserter.assertTheNumberOfMeasuresIs(4);
    }

    @Test
    public void testGroupByComputeAggregationNotFound() {
        LogEntryHelper helper = new LogEntryHelper(10);
        AggregatedMeasure measure = new AggregatedMeasure("id", "name", "desc", null, null, Aggregator.SUM, null, createCountMeasure(withCondition("Analyse RFC", GenericState.END)));

        List<DataContentSelection> groupBy = new ArrayList<DataContentSelection>();
        groupBy.add(new DataContentSelection("centro", ""));
        measure.setGroupedBy(groupBy);

        AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new SimpleTimeFilter(Period.DAILY, 1, true));

        Map<String, Object> inst3Data = new HashMap<String, Object>();
        inst3Data.put("nodo", "sevilla");

        computer.update(helper.newInstance("i1", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i3", EventType.ready, DateTime.now().plusDays(-6)).withData(inst3Data));
        computer.update(helper.newInstance("i4", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newInstance("i5", EventType.ready, DateTime.now().plusDays(-6)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().plusDays(-5)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().plusDays(-4)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", DateTime.now().plusDays(-4)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i3", DateTime.now().plusDays(-5)).withData(inst3Data));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i3", DateTime.now().plusDays(-4)).withData(inst3Data));
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
        computer.update(helper.newInstance("i3", EventType.complete, DateTime.now().minusDays(4).plusMillis(10)).withData(inst3Data));
        computer.update(helper.newInstance("i4", EventType.complete, DateTime.now().minusDays(1).plusMillis(20)));
        computer.update(helper.newInstance("i5", EventType.complete, DateTime.now().minusDays(2).plusMillis(30)));

        List<Measure> result = (List<Measure>) computer.compute();
        MeasuresAsserter asserter = new MeasuresAsserter(result);
        asserter.assertTheNumberOfMeasuresIs(0);
    }

}
