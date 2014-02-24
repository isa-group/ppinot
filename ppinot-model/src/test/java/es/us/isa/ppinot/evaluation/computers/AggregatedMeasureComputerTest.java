package es.us.isa.ppinot.evaluation.computers;

import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.state.GenericState;

public class AggregatedMeasureComputerTest extends MeasureComputerHelper {
    @Test
    public void testComputeLastInstanceSum() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.SUM, null, baseMeasure);
    	
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, new LastInstancesFilter(3));
    	
    	computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i3"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i3"));
        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i2"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i4"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i4"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i5"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i5"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "i1"));
        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "i1"));
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	//conteo : (id1:4,id2:3,id3:1,id4:1,id5:1)
    	asserter.assertTheNumberOfMeasuresIs(11);
    	//asserter.assertNumberOfInstances(0,2);
    }
//    @Test
//    public void testComputeLastInstanceMin() throws Exception {
//    	LogEntryHelper helper = new LogEntryHelper(10);
//    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
//    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MIN, null, baseMeasure);
//    	
//    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(3);
//    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
//    	    	
//    	computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id3"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id3"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id4"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id4"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id5"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id5"));
//        
//        
//    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
//    	
//    	asserter.assertTheNumberOfMeasuresIs(10);
//    	//devuelve 0
//    	//asserter.assertNumberOfInstances(1,2);//es 1 ya que solo sale una vez cada analyse
//    	//el minimo que devolveria? el minimo de veces que aparecere la entrada en el conjunto divido?
//    	
//    }
//    @Test
//    public void testComputeLastInstanceMax() throws Exception {
//    	LogEntryHelper helper = new LogEntryHelper(10);
//    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
//    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MAX, null, baseMeasure);
//    	
//    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(3);
//    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
//    	    	
//    	computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id3"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id3"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id4"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id4"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id5"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id5"));
//        
//        
//    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
//    	
//    	asserter.assertTheNumberOfMeasuresIs(10);
//    	//devuelve 0
//    	//asserter.assertNumberOfInstances(1,2);//ya que el maximo de veces que aprecea cada analyse es uno, habrai que hacer tro ejemplo repitiendo ids
//    	//el maximo que devolveria? el maximo de veces que aparecere la entrada en el conjunto divido?
//    	
//    }
//    @Test
//    public void testComputeLastInstanceAvg() throws Exception {
//    	LogEntryHelper helper = new LogEntryHelper(10);
//    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
//    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.AVG, null, baseMeasure);
//    	
//    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(3);
//    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
//    	    	
//    	computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id3"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id3"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Approve RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id1"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id2"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id4"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id4"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.ready, "id5"));
//        computer.update(helper.newEntryProcess("Analyse RFC", LogEntry.EventType.complete, "id5"));
//        
//        
//    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
//    	
//    	asserter.assertTheNumberOfMeasuresIs(10);
//    	//devuelve 0
//    	//asserter.assertNumberOfInstances(1,2);//ya que hay una medai de 1 instancia en cada grupo o es la media de las sumas?
//    	//la media  que devolveria? la media de la veces en la que aprecera en cada conjunto?   
//    	asserter.assertNumberOfInstances(0,2);
//    }
//    @Test
//    public void testComputeSimpleTime() throws Exception {
////    	LogEntryHelper helper = new LogEntryHelper(10);
////    	CountMeasure measure1 = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
////        CountMeasure measure2 = createCountMeasure(withCondition("Approve RFC", GenericState.END));
////
////        ProcessInstanceFilter simpleFilter = new SimpleTimeFilter(Period.WEEKLY, 1, true);
////    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, simpleFilter);
////    	    	
////    	computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
////        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
////        computer.update(helper.newAssignEntry("Analyse RFC", "i2"));
////        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
////        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
////        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
////        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
////        computer.update(helper.newCompleteEntry("Analyse RFC", "i2"));
////        
////    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
////    	
////    	asserter.assertTheNumberOfMeasuresIs(2);
////        asserter.assertInstanceHasValue("i1", 2);
////        asserter.assertInstanceHasValue("i2", 1);
//    	
//    }
}