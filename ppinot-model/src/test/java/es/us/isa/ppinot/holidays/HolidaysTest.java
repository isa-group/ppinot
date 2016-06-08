package es.us.isa.ppinot.holidays;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerHelper;
import es.us.isa.ppinot.evaluation.computers.TimeMeasureComputer;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.state.GenericState;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

public class HolidaysTest extends MeasureComputerHelper {
    
    @Test
    public void testComputeLinearInstancesWithHolidayToday() throws Exception {
        
        LogEntryHelper helper = new LogEntryHelper(DateTimeConstants.MILLIS_PER_MINUTE);
        
        List<DateTime> holidays = new ArrayList<DateTime>();
        holidays.add(DateTime.now());
        
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0), holidays);
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.MILLIS);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue("i1", 0);
    }
    
    @Test
    public void testComputeLinearInstancesWithUnappliedHoliday() throws Exception {
        
        LogEntryHelper helper = new LogEntryHelper(DateTimeConstants.MILLIS_PER_MINUTE);
        
        List<DateTime> holidays = new ArrayList<DateTime>();
        holidays.add(new DateTime(1969,12,31,23,59,59));
        
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0), holidays);
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.MILLIS);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue("i1", 3*DateTimeConstants.MILLIS_PER_MINUTE);
    }
}
