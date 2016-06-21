package es.us.isa.ppinot.scope;

import es.us.isa.ppinot.evaluation.computers.*;
import org.joda.time.DateTime;
import org.junit.Test;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.evaluation.scopes.TimeScopeClassifier;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.junit.Assert;

public class TimeAbsoluteScopeTest extends MeasureComputerHelper {

    @Test
    public void testTimeAbsoluteScopeSameMonth() {
        
        int dayOfMonth = 15;
        LogEntryHelper helper = new LogEntryHelper();
        SimpleTimeFilter filter = new SimpleTimeFilter(Period.MONTHLY, 1, dayOfMonth);
        TimeScopeClassifier classifier = new TimeScopeClassifier(filter);
        
        classifier.update(helper.newInstance("i1", EventType.ready, DateTime.now().withDayOfMonth(dayOfMonth)));
        classifier.update(helper.newInstance("i2", EventType.ready, DateTime.now().withDayOfMonth(dayOfMonth)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().withDayOfMonth(dayOfMonth+5)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().withDayOfMonth(dayOfMonth+5)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.ready, "i2", DateTime.now().withDayOfMonth(dayOfMonth-1).plusMonths(1)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.complete, "i2", DateTime.now().withDayOfMonth(dayOfMonth-1).plusMonths(1)));
        classifier.update(helper.newInstance("i1", EventType.complete, DateTime.now().withDayOfMonth(dayOfMonth)));
        classifier.update(helper.newInstance("i2", EventType.complete, DateTime.now().withDayOfMonth(dayOfMonth-1).plusMonths(1)));
        
        Assert.assertTrue(classifier.listScopes().size() == 1);
    }

    @Test
    public void testTimeAbsoluteScopeDifferentMonth() {
        
        int dayOfMonth = 15;
        LogEntryHelper helper = new LogEntryHelper();
        SimpleTimeFilter filter = new SimpleTimeFilter(Period.MONTHLY, 1, dayOfMonth);
        TimeScopeClassifier classifier = new TimeScopeClassifier(filter);

        classifier.update(helper.newInstance("i1", EventType.ready, DateTime.now().withDayOfMonth(dayOfMonth)));
        classifier.update(helper.newInstance("i2", EventType.ready, DateTime.now().withDayOfMonth(dayOfMonth)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", DateTime.now().withDayOfMonth(dayOfMonth+5)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", DateTime.now().withDayOfMonth(dayOfMonth+5)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.ready, "i2", DateTime.now().withDayOfMonth(dayOfMonth+5).plusMonths(1)));
        classifier.update(helper.newEntry("Analyse RFC", EventType.complete, "i2", DateTime.now().withDayOfMonth(dayOfMonth+5).plusMonths(1)));
        classifier.update(helper.newInstance("i1", EventType.complete, DateTime.now().withDayOfMonth(dayOfMonth+5)));
        classifier.update(helper.newInstance("i2", EventType.complete, DateTime.now().withDayOfMonth(dayOfMonth+5).plusMonths(1)));

        Assert.assertTrue(classifier.listScopes().size() == 2);
    }

}
