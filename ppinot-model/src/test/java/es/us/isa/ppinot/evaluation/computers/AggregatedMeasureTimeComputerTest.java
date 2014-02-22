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
	public void testComputeAggregatedTimeScope() {
		LogEntryHelper helper = new LogEntryHelper(10);
		AggregatedMeasure measure = new AggregatedMeasure("id","name","desc",null,null,Aggregator.AVG,null,createCountMeasure(withCondition("Analyse RFC",GenericState.END)));
		AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure,new SimpleTimeFilter(Period.DAILY, 1, true));
		
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now()));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now()));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now()));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now()));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now()));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(1)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.ready,"i1",DateTime.now().plusDays(2)));
		computer.update(helper.newEntry("Analyse RFC", EventType.complete,"i1",DateTime.now().plusDays(2)));
		

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(3);
        asserter.assertInstanceHasValue("i1", 2);
	}
	
}
