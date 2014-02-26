package es.us.isa.ppinot.evaluation.computers;

import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;

public class AggregatedMeasureComputerTest extends MeasureComputerHelper {
    @Test
    public void testComputeLastInstanceSum() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.SUM, null, baseMeasure);
    	
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
    	
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.ready, "i3"));
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
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.complete, "i3"));
    	computer.update(helper.newEntryProcess("i4", LogEntry.EventType.ready, "i4"));
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
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.ready, "i5"));
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
        computer.update(helper.newEntryProcess("i4", LogEntry.EventType.complete, "i4"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.complete, "i5"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.ready, "i6"));
    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.complete, "i6"));
        
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
    	asserter.assertTheNumberOfMeasuresIs(4);
    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
    	asserter.assertNumberOfInstances(0,12.0);
    	asserter.assertNumberOfInstances(1,20.0);
    	asserter.assertNumberOfInstances(2,25.0);
    	asserter.assertNumberOfInstances(3,18.0);
    }
    @Test
    public void testComputeLastInstanceMin() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MIN, null, baseMeasure);
    	
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
    	
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.ready, "i3"));
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
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.complete, "i3"));
    	computer.update(helper.newEntryProcess("i4", LogEntry.EventType.ready, "i4"));
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
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.ready, "i5"));
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
        computer.update(helper.newEntryProcess("i4", LogEntry.EventType.complete, "i4"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.complete, "i5"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.ready, "i6"));
    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.complete, "i6"));
        
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
    	asserter.assertTheNumberOfMeasuresIs(4);
    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
    	asserter.assertNumberOfInstances(0,1.0);
    	asserter.assertNumberOfInstances(1,4.0);
    	asserter.assertNumberOfInstances(2,7.0);
    	asserter.assertNumberOfInstances(3,0.0);
    	
    }
    @Test
    public void testComputeLastInstanceMax() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MAX, null, baseMeasure);
    	
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
    	
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.ready, "i3"));
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
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.complete, "i3"));
    	computer.update(helper.newEntryProcess("i4", LogEntry.EventType.ready, "i4"));
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
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.ready, "i5"));
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
        computer.update(helper.newEntryProcess("i4", LogEntry.EventType.complete, "i4"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.complete, "i5"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.ready, "i6"));
    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.complete, "i6"));
        
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
    	asserter.assertTheNumberOfMeasuresIs(4);
    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
    	asserter.assertNumberOfInstances(0,7.0);
    	asserter.assertNumberOfInstances(1,9.0);
    	asserter.assertNumberOfInstances(2,9.0);
    	asserter.assertNumberOfInstances(3,9.0);
    	
    }
    @Test
    public void testComputeLastInstanceAvg() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.AVG, null, baseMeasure);
    	
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
    	
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.ready, "i3"));
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
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.complete, "i3"));
    	computer.update(helper.newEntryProcess("i4", LogEntry.EventType.ready, "i4"));
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
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.ready, "i5"));
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
        computer.update(helper.newEntryProcess("i4", LogEntry.EventType.complete, "i4"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.complete, "i5"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.ready, "i6"));
    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.complete, "i6"));
        
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
    	asserter.assertTheNumberOfMeasuresIs(4);
    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
    	asserter.assertNumberOfInstances(0,4.0);
    	asserter.assertNumberOfInstances(1,6.666666666666667);
    	asserter.assertNumberOfInstances(2,8.333333333333334);
    	asserter.assertNumberOfInstances(3,6.0);
    }
    @Test
    public void testComputeSimpleTime() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MIN, null, baseMeasure);
    	
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new SimpleTimeFilter(Period.DAILY, 1, false));
    	    	
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("i2", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i1", LogEntry.EventType.complete, "i1"));
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.ready, "i3"));
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
    	computer.update(helper.newEntryProcess("i3", LogEntry.EventType.complete, "i3"));
    	computer.update(helper.newEntryProcess("i4", LogEntry.EventType.ready, "i4"));
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
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.ready, "i5"));
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
        computer.update(helper.newEntryProcess("i4", LogEntry.EventType.complete, "i4"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntry("Analyse RFC", LogEntry.EventType.complete, "i5"));        
    	computer.update(helper.newEntryProcess("i5", LogEntry.EventType.complete, "i5"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.ready, "i6"));
    	computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.ready, "i6"));
        computer.update(helper.newEntry("Approve RFC", LogEntry.EventType.complete, "i6"));
    	computer.update(helper.newEntryProcess("i6", LogEntry.EventType.complete, "i6"));
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	//conteo : (i1:4,i2:1,i3:7,i4:9,i5:9,i6:0)
    	asserter.assertTheNumberOfMeasuresIs(4);
    	//[2,1,3]:,[1,3,4],[3,4,5],[4,5,6]
    	asserter.assertNumberOfInstances(0,4.0);
    	asserter.assertNumberOfInstances(1,6.666666666666667);
    	asserter.assertNumberOfInstances(2,8.333333333333334);
    	asserter.assertNumberOfInstances(3,6.0);
    }
}