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
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new LastInstancesFilter(3));
		
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(5);
        asserter.assertNumOfInstances(3);
	}
	
	@Test
	public void testComputeAggregatedLastInstanceScopeNoAppear() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Approve RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new LastInstancesFilter(3));
		
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(2);
        asserter.assertNumOfInstancesNull();
	}
	
	@Test
	public void testComputeAggregatedLastInstanceScopeMultiInstance() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.SUM,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new LastInstancesFilter(3));
		
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i2"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i2"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i2"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1"));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i2"));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(5);
        asserter.assertNumOfInstances(3);
	}

}
