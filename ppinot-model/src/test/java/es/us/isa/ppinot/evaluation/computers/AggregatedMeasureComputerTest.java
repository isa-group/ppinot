package es.us.isa.ppinot.evaluation.computers;

import org.junit.Test;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
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
    	
    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(2);
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
    	    	
    	computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	
    	asserter.assertTheNumberOfMeasuresIs(6);
    	//me devuelve 0
    	//que devuelve cojuntos de datos sin tener en cuenta el que medimos?
    	asserter.assertNumberOfInstances(2);	
    }
    @Test
    public void testComputeLastInstanceMin() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MIN, null, baseMeasure);
    	
    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(2);
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
    	    	
    	computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	
    	asserter.assertTheNumberOfMeasuresIs(6);
    	//devuelve 0
    	asserter.assertNumberOfInstances(1);//es 1 ya que solo sale una vez cada analyse
    	//el minimo que devolveria? el minimo de veces que aparecere la entrada en el conjunto divido?
    }
    @Test
    public void testComputeLastInstanceMax() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.MAX, null, baseMeasure);
    	
    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(2);
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
    	    	
    	computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	
    	asserter.assertTheNumberOfMeasuresIs(6);
    	//devuelve 0
    	asserter.assertNumberOfInstances(1);//ya que el maximo de veces que aprecea cada analyse es uno, habrai que hacer tro ejemplo repitiendo ids
    	//el maximo que devolveria? el maximo de veces que aparecere la entrada en el conjunto divido?   	
    }
    @Test
    public void testComputeLastInstanceAvg() throws Exception {
    	LogEntryHelper helper = new LogEntryHelper(10);
    	CountMeasure baseMeasure = createCountMeasure(withCondition("Analyse RFC",GenericState.END));
    	AggregatedMeasure measure = new AggregatedMeasure("id", "name", "description", null, null, Aggregator.AVG, null, baseMeasure);
    	
    	ProcessInstanceFilter lastFilter = new LastInstancesFilter(2);
    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, lastFilter);
    	    	
    	computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Approve RFC"));
        computer.update(helper.newCompleteEntry("Approve RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        computer.update(helper.newAssignEntry("Analyse RFC"));
        computer.update(helper.newCompleteEntry("Analyse RFC"));
        
        
    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
    	
    	asserter.assertTheNumberOfMeasuresIs(6);
    	//devuelve 0
    	asserter.assertNumberOfInstances(1);//ya que hay una medai de 1 instancia en cada grupo o es la media de las sumas?
    	//la media  que devolveria? la media de la veces en la que aprecera en cada conjunto?   	
    }
    @Test
    public void testComputeSimpleTime() throws Exception {
//    	LogEntryHelper helper = new LogEntryHelper(10);
//    	CountMeasure measure1 = createCountMeasure(withCondition("Analyse RFC", GenericState.END));
//        CountMeasure measure2 = createCountMeasure(withCondition("Approve RFC", GenericState.END));
//
//        ProcessInstanceFilter simpleFilter = new SimpleTimeFilter(Period.WEEKLY, 1, true);
//    	AggregatedMeasureComputer computer = new AggregatedMeasureComputer(measure, simpleFilter);
//    	    	
//    	computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
//        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
//        computer.update(helper.newAssignEntry("Analyse RFC", "i2"));
//        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
//        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));
//        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
//        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
//        computer.update(helper.newCompleteEntry("Analyse RFC", "i2"));
//        
//    	MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());
//    	
//    	asserter.assertTheNumberOfMeasuresIs(2);
//        asserter.assertInstanceHasValue("i1", 2);
//        asserter.assertInstanceHasValue("i2", 1);
    	
    }
}