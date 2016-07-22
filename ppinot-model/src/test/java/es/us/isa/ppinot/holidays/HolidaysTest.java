package es.us.isa.ppinot.holidays;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerHelper;
import es.us.isa.ppinot.evaluation.computers.TimeMeasureComputer;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.model.Holidays;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.state.GenericState;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.junit.Assert;

public class HolidaysTest extends MeasureComputerHelper {
    
    // Ends activities regardless intermediate days of holidays.
    @Test
    public void testComputeLinearInstancesWithHolidayTomorrow() throws Exception {
        
        LogEntryHelper helper = new LogEntryHelper();
        
        List<DateTime> holidays = new ArrayList<DateTime>();
        DateTime dt = new DateTime(2016, 1, 1, 0, 0, DateTimeZone.forID("Europe/Madrid")).withDayOfWeek(DateTimeConstants.MONDAY);
        holidays.add(dt.plusDays(1));
        holidays.add(dt.plusDays(3));
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0), holidays);
        
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.HOURS);

        computer.update(helper.newInstance("i1", EventType.ready, dt.withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", dt.withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", dt.plusDays(2).withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.ready, "i1", dt.plusDays(2).withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.complete, "i1", dt.plusDays(4).withTime(8, 0, 0, 0)));
        computer.update(helper.newInstance("i1", EventType.complete, dt.plusDays(4).withTime(8, 0, 0, 0)));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 24);
    }
    
    // An activity begins and ends on vacation.
    @Test
    public void testComputeLinearInstancesFinishingActivityInHolidays() throws Exception {
        
        LogEntryHelper helper = new LogEntryHelper();
        
        List<DateTime> holidays = new ArrayList<DateTime>();
        DateTime dt = new DateTime(2016, 1, 1, 0, 0, DateTimeZone.forID("Europe/Madrid")).withDayOfWeek(DateTimeConstants.MONDAY);
        holidays.add(dt.plusDays(1));
        holidays.add(dt.plusDays(2));
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0), holidays);
        
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.HOURS);

        computer.update(helper.newInstance("i1", EventType.ready, dt.withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", dt.withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", dt.plusDays(1).withTime(18, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.ready, "i1", dt.plusDays(1).withTime(18, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.complete, "i1", dt.plusDays(2).withTime(10, 0, 0, 0)));
        computer.update(helper.newInstance("i1", EventType.complete, dt.plusDays(2).withTime(10, 0, 0, 0)));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 12);
    }
    
    // Begins and ends holiday activities.
    @Test
    public void testComputeLinearInstancesInHolidays() throws Exception {
        
        LogEntryHelper helper = new LogEntryHelper();
        
        List<DateTime> holidays = new ArrayList<DateTime>();
        DateTime dt = new DateTime(2016, 1, 1, 0, 0, DateTimeZone.forID("Europe/Madrid")).withDayOfWeek(DateTimeConstants.MONDAY);
        holidays.add(dt);
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0), holidays);
        
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.HOURS);

        computer.update(helper.newInstance("i1", EventType.ready, dt.withTime(10, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", dt.withTime(10, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", dt.withTime(11, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.ready, "i1", dt.withTime(18, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.complete, "i1", dt.withTime(20, 0, 0, 0)));
        computer.update(helper.newInstance("i1", EventType.complete, dt.withTime(20, 0, 0, 0)));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 0);
    }
    
    // Begins activities in holidays and ends in working hours.
    @Test
    public void testComputeLinearInstancesInHolidaysAndFinishesAfter() throws Exception {
        
        LogEntryHelper helper = new LogEntryHelper();
        
        List<DateTime> holidays = new ArrayList<DateTime>();
        DateTime dt = new DateTime(2016, 1, 1, 0, 0, DateTimeZone.forID("Europe/Madrid")).withDayOfWeek(DateTimeConstants.MONDAY);
        holidays.add(dt);
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0), holidays);
        
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.HOURS);

        computer.update(helper.newInstance("i1", EventType.ready, dt.withTime(10, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.ready, "i1", dt.withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Analyse RFC", EventType.complete, "i1", dt.withTime(13, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.ready, "i1", dt.plusDays(1).withTime(8, 0, 0, 0)));
        computer.update(helper.newEntry("Approve RFC", EventType.complete, "i1", dt.plusDays(1).withTime(10, 0, 0, 0)));
        computer.update(helper.newInstance("i1", EventType.complete, dt.plusDays(1).withTime(10, 0, 0, 0)));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasDoubleValue("i1", 2);
    }
    
    // Load holidays from JSON file.
    @Test
    public void testReadHolidaysFile() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Holidays h = new Holidays();

        try {
            String json = loadFile("/holidays.json");
            h = mapper.readValue(json, Holidays.class);
        } catch (JsonGenerationException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Assert.assertTrue(h.getList().size() == 3);
    }

    private static String loadFile(String filePath) {
        InputStream is = HolidaysTest.class.getResourceAsStream(filePath);
        String res = "";
        try {
            res = getStringFromInputStream(is);
            is.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return res;
    }

    private static String getStringFromInputStream(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line.replaceAll("	", "\t")).append("\n");
            }
        } catch (IOException e) {
        }
        return sb.toString();
    }
}
