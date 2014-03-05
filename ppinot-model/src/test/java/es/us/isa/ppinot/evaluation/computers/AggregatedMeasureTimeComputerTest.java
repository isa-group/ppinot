package es.us.isa.ppinot.evaluation.computers;

import org.joda.time.DateTime;
import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.evaluation.scopes.TimeScopeClassifier;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;

public class AggregatedMeasureTimeComputerTest extends MeasureComputerHelper {

	@Test
	public void testComputeAggregatedTimeRelativeScope1Day() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, true));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now().minusDays(1)));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().minusDays(4).plusMillis(10)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().minusDays(1).plusMillis(20)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().minusDays(2).plusMillis(30)));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 5);
        asserter.assertValueOfInterval(2, 2);
	}
	
	@Test
	public void testComputeAggregatedTimeRelativeScope1DaySameFinish() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, true));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now()));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now()));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now()));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now()));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertValueOfInterval(0, 9);
	}
	
	@Test
	public void testComputeAggregatedTimeRelativeScope2Days() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.MIN,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 2, true));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now().minusDays(1)));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().minusDays(4).plusMillis(10)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().minusDays(1).plusMillis(20)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().minusDays(2).plusMillis(30)));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
	}
	
	@Test
	public void testComputeAggregatedTimeRelativeScope1Week() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.MAX,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.WEEKLY, 1, true));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now().plusWeeks(1)));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().minusDays(4).plusMillis(10)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().minusDays(1).plusMillis(20)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().minusDays(2).plusMillis(30)));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 4);
	}
	
	@Test
	public void testComputeAggregatedTimeAbsolute1DayScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, false));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now().minusDays(1)));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().minusDays(4).plusMillis(10)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().minusDays(1).plusMillis(20)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().minusDays(2).plusMillis(30)));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertValueOfInterval(2, 6);
	}
	
	@Test
	public void testComputeAggregatedTimeAbsolute2DaysScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.MIN,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 2, false));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now().minusDays(1)));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().minusDays(4).plusMillis(10)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().minusDays(1).plusMillis(20)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().minusDays(2).plusMillis(30)));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertValueOfInterval(0, 2);
        asserter.assertValueOfInterval(1, 1);
	}
	
	@Test
	public void testComputeAggregatedTimeAbsoluteScopeNoMeasure() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.AVG,null,createCountMeasure(withCondition("Approve RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, false));
		
		computer.update(helper.newInstance("i1",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i3",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i4",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newInstance("i5",EventType.ready,DateTime.now().plusDays(-6)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(-5)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(-4)));
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
		computer.update(helper.newInstance("i1",EventType.complete,DateTime.now()));
		computer.update(helper.newInstance("i3",EventType.complete,DateTime.now().plusDays(-4)));
		computer.update(helper.newInstance("i4",EventType.complete,DateTime.now().plusDays(-1)));
		computer.update(helper.newInstance("i5",EventType.complete,DateTime.now().plusDays(-5)));
		
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(4);
        asserter.assertNumOfMeasureCero();
	}

}
