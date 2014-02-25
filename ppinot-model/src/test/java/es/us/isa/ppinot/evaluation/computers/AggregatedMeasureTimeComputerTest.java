package es.us.isa.ppinot.evaluation.computers;

import org.joda.time.DateTime;
import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.evaluation.scopes.TimeScopeClassifier;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;

public class AggregatedMeasureTimeComputerTest extends MeasureComputerHelper {

	@Test
	public void testComputeAggregatedTimeRelativeScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.AVG,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, true));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-5)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-5)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-5)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i2",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i2",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-3)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4",DateTime.now().plusDays(-3)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i5",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i5",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now().plusDays(-1)));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().plusDays(-1)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().plusDays(-1)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().plusDays(-1)));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue("i1", 3);
	}
	
	@Test
	public void testComputeAggregatedTimeAbsolute2DaysScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.AVG,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 2, false));
		
		computer.update(helper.newInstance("i1",EventType.ready));
		computer.update(helper.newInstance("i3",EventType.ready));
		computer.update(helper.newInstance("i4",EventType.ready));
		computer.update(helper.newInstance("i5",EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i2",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i2",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i5",DateTime.now().plusDays(-3)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i5",DateTime.now().plusDays(-3)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newInstance("i1",EventType.complete));
		computer.update(helper.newInstance("i3",EventType.complete));
		computer.update(helper.newInstance("i4",EventType.complete));
		computer.update(helper.newInstance("i5",EventType.complete));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue("i1", 4);
	}
	
	@Test
	public void testComputeAggregatedTimeAbsolute0DaysScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.AVG,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, false));
		
		computer.update(helper.newInstance("i1",EventType.ready));
		computer.update(helper.newInstance("i3",EventType.ready));
		computer.update(helper.newInstance("i4",EventType.ready));
		computer.update(helper.newInstance("i5",EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i2",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i2",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3",DateTime.now().plusDays(-1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4",DateTime.now().plusDays(-2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i5",DateTime.now().plusDays(-3)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i5",DateTime.now().plusDays(-3)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4",DateTime.now().plusDays(-4)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newInstance("i1",EventType.complete));
		computer.update(helper.newInstance("i3",EventType.complete));
		computer.update(helper.newInstance("i4",EventType.complete));
		computer.update(helper.newInstance("i5",EventType.complete));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
//        asserter.assertInstanceHasValue("i1", 1);
	}
}
