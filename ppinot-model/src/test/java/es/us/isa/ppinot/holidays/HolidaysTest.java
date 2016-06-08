package es.us.isa.ppinot.holidays;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.MeasuresAsserter;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerHelper;
import es.us.isa.ppinot.evaluation.computers.TimeMeasureComputer;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.junit.Assert;

public class HolidaysTest extends MeasureComputerHelper {
    
    @Test
    public void testComputeLinearInstances() throws Exception {
        LogEntryHelper helper = new LogEntryHelper(10);
        
        Schedule considerOnly = new Schedule(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8,0), new LocalTime(20,0));
        considerOnly.setConsiderHolidays(true);
        
        TimeMeasureComputer computer = createLinearTimeMeasureComputerWithSchedule("Analyse RFC", GenericState.START, "Approve RFC", GenericState.END, considerOnly, TimeUnit.MILLIS);

        computer.update(helper.newAssignEntry("Analyse RFC", "i1"));
        computer.update(helper.newCompleteEntry("Analyse RFC", "i1"));
        computer.update(helper.newAssignEntry("Approve RFC", "i1"));
        computer.update(helper.newCompleteEntry("Approve RFC", "i1"));

        MeasuresAsserter asserter = new MeasuresAsserter(computer.compute());

        asserter.assertTheNumberOfMeasuresIs(1);
        asserter.assertInstanceHasValue("i1", 30);
    }

    @Test
    public void testReadHolidaysFile() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Holidays h = new Holidays();

        try {
            
            String json = loadFile("src/test/resources/holidays.json");
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
        File f = new File(filePath);
        FileInputStream is;
        String res = "";
        try {
            is = new FileInputStream(f);
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
