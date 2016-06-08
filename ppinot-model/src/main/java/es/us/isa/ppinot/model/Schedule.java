package es.us.isa.ppinot.model;

import es.us.isa.ppinot.handler.json.LocalTimeDeserializer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

/**
 * Schedule
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class Schedule {

    private int beginDay;
    private int endDay;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime beginTime;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime endTime;
    private boolean considerHolidays;

    private Schedule() {
    }

    public Schedule(int beginDay, int endDay, LocalTime beginTime, LocalTime endTime) {
        this.beginDay = beginDay;
        this.endDay = endDay;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.considerHolidays = false;

        if (beginDay < DateTimeConstants.MONDAY || beginDay > DateTimeConstants.SUNDAY || endDay < DateTimeConstants.MONDAY || endDay > DateTimeConstants.SUNDAY) {
            throw new RuntimeException("Invalid day of week");
        }
    }

    public int getBeginDay() {
        return beginDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setConsiderHolidays(boolean consider) {
        this.considerHolidays = consider;
    }

    public boolean dayOfWeekExcluded(int dayOfWeek) {
        return (dayOfWeek < getBeginDay() || dayOfWeek > getEndDay());
    }

    public boolean dayOfHolidayExcluded(List<DateTime> holidays, DateTime day) {

        boolean result = false;

        if (this.considerHolidays) {
            // Ignore year
            for (DateTime h : holidays) {
                if (h.getMonthOfYear() == day.getMonthOfYear() && h.getDayOfMonth() == day.getDayOfMonth()) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
    
    public List<DateTime> getHolidaysFromJson() {
        
        ObjectMapper mapper = new ObjectMapper();
        Holidays h = new Holidays();
        String json;
        
        try {
            json = loadFile("src/main/resources/holidays.json");
            h = mapper.readValue(json, Holidays.class);
        } catch (IOException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return h.getList();
    }

    private static String loadFile(String filePath) throws FileNotFoundException, IOException {
        
        File file = new File(filePath);
        FileInputStream is;
        String result = "";

        is = new FileInputStream(file);
        result = getStringFromInputStream(is);
        is.close();

        return result;
        
    }

    private static String getStringFromInputStream(InputStream in) throws IOException {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line.replaceAll("	", "\t")).append("\n");
        }
        
        return sb.toString();
        
    }

}
