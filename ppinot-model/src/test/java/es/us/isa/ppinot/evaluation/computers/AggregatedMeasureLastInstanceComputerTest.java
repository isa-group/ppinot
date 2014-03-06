package es.us.isa.ppinot.evaluation.computers;

import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
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
	
	 @Test
	    public void testComputeLastInstanceSum() throws Exception {
	    	LogEntryHelper helper = new LogEntryHelper(10);
	    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
	    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.SUM, null, baseMeasure);
	    	
	    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
	    	
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i4", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newInstance("i4", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.complete));
	        
	        
	        
	    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
	    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
	    	asserter.assertTheNumberOfMeasuresIs(4);
	    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
	    	asserter.assertValueOfInterval(0,12.0);
	    	asserter.assertValueOfInterval(1,20.0);
	    	asserter.assertValueOfInterval(2,25.0);
	    	asserter.assertValueOfInterval(3,18.0);
	    }
	    @Test
	    public void testComputeLastInstanceMin() throws Exception {
	    	LogEntryHelper helper = new LogEntryHelper(10);
	    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
	    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MIN, null, baseMeasure);
	    	
	    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
	    	
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i4", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newInstance("i4", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.complete));
	        
	        
	        
	    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
	    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
	    	asserter.assertTheNumberOfMeasuresIs(4);
	    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
	    	asserter.assertValueOfInterval(0,1.0);
	    	asserter.assertValueOfInterval(1,4.0);
	    	asserter.assertValueOfInterval(2,7.0);
	    	asserter.assertValueOfInterval(3,0.0);
	    	
	    }
	    @Test
	    public void testComputeLastInstanceMax() throws Exception {
	    	LogEntryHelper helper = new LogEntryHelper(10);
	    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
	    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MAX, null, baseMeasure);
	    	
	    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
	    	
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i4", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newInstance("i4", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.complete));
	        
	        
	        
	    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
	    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
	    	asserter.assertTheNumberOfMeasuresIs(4);
	    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
	    	asserter.assertValueOfInterval(0,7.0);
	    	asserter.assertValueOfInterval(1,9.0);
	    	asserter.assertValueOfInterval(2,9.0);
	    	asserter.assertValueOfInterval(3,9.0);
	    	
	    }
	    @Test
	    public void testComputeLastInstanceAvg() throws Exception {
	    	LogEntryHelper helper = new LogEntryHelper(10);
	    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
	    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.AVG, null, baseMeasure);
	    	
	    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
	    	
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.ready));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
	        computer.update(helper.newInstance("i2", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
	    	computer.update(helper.newInstance("i1", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i3"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i3"));
	    	computer.update(helper.newInstance("i3", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i4", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	    	computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i4"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i4"));
	        computer.update(helper.newInstance("i4", LogEntry.EventType.complete));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
	        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
	    	computer.update(helper.newInstance("i5", LogEntry.EventType.complete));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.ready));
	    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
	        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
	    	computer.update(helper.newInstance("i6", LogEntry.EventType.complete));
	        
	        
	        
	    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
	    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
	    	asserter.assertTheNumberOfMeasuresIs(4);
	    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
	    	asserter.assertValueOfInterval(0,4.0);
	    	asserter.assertValueOfInterval(1,6.666666666666667);
	    	asserter.assertValueOfInterval(2,8.333333333333334);
	    	asserter.assertValueOfInterval(3,6.0);
	    }

}
