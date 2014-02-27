package es.us.isa.ppinot.evaluation.computers;

import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.state.RuntimeState;


public class AggregatedMeasureLastInstanceComputerTest extends MeasureComputerHelper{
	
	@Test
	public void testComputeAggregatedLastInstanceScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.MAX,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new LastInstancesFilter(3));
		
		computer.update(helper.newInstance("i1",EventType.ready));
		computer.update(helper.newInstance("i3",EventType.ready));
		computer.update(helper.newInstance("i4",EventType.ready));
		computer.update(helper.newInstance("i5",EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i5"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i5"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newInstance("i1",EventType.complete));
		computer.update(helper.newInstance("i3",EventType.complete));
		computer.update(helper.newInstance("i4",EventType.complete));
		computer.update(helper.newInstance("i5",EventType.complete));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertValueOfInterval(0, 3);
        asserter.assertValueOfInterval(1, 1);
        asserter.assertNumOfInstances(4);
	}
	
	@Test
	public void testComputeAggregatedLastInstanceScopeNoAppear() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Approve RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new LastInstancesFilter(3));
		
		computer.update(helper.newInstance("i1",EventType.ready));
		computer.update(helper.newInstance("i3",EventType.ready));
		computer.update(helper.newInstance("i4",EventType.ready));
		computer.update(helper.newInstance("i5",EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i5"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i5"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newInstance("i1",EventType.complete));
		computer.update(helper.newInstance("i3",EventType.complete));
		computer.update(helper.newInstance("i4",EventType.complete));
		computer.update(helper.newInstance("i5",EventType.complete));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertNumOfMeasureCero();
	}
	
	
	@Test
	public void testComputeAggregatedLastInstanceScopeInverse() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.MAX,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new LastInstancesFilter(3));
		
		computer.update(helper.newInstance("i1",EventType.ready));
		computer.update(helper.newInstance("i3",EventType.ready));
		computer.update(helper.newInstance("i4",EventType.ready));
		computer.update(helper.newInstance("i5",EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i3"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i3"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i4"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i4"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i5"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i5"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newInstance("i5",EventType.complete));
		computer.update(helper.newInstance("i4",EventType.complete));
		computer.update(helper.newInstance("i3",EventType.complete));
		computer.update(helper.newInstance("i1",EventType.complete));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertValueOfInterval(0, 1);
        asserter.assertValueOfInterval(1, 3);
        asserter.assertNumOfInstances(4);
	}

}
