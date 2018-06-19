package es.us.isa.ppinot.handler.json;

import es.us.isa.ppinot.handler.JSONMeasuresCollectionHandler;
import es.us.isa.ppinot.model.schedule.DefaultHolidays;
import es.us.isa.ppinot.model.schedule.Holidays;
import es.us.isa.ppinot.model.schedule.ScheduleBasic;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * ScheduleDeserializerTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScheduleDeserializerTest {

    private ObjectMapper mapper;
    private Holidays holidays;

    public static class ScheduleTest {
        private ScheduleBasic schedule;

        public ScheduleTest() {}

        public ScheduleBasic getSchedule() {
            return schedule;
        }
    }

    @Before
    public void setup() {
        holidays = new Holidays();
        holidays.setList(Arrays.asList(new DateTime(2018, 12, 25, 0, 0), new DateTime(2018, 1, 1, 0, 0), new DateTime(2017, 12, 25, 0, 0)));
        JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler(holidays);
        mapper = handler.getMapper();
    }

    @Test
    public void testDeserialize() throws IOException {
        String text = "{\"schedule\": \"L-VT8:00-19:00\"}";

        ScheduleBasic schedule = mapper.readValue(text, ScheduleTest.class).getSchedule();

        assertEquals(1, schedule.getBeginDay());
        assertEquals(5, schedule.getEndDay());
        assertEquals(new LocalTime(8, 0), schedule.getBeginTime());
        assertEquals(new LocalTime(19, 0), schedule.getEndTime());
        assertTrue(schedule.getHolidays().isEmpty());
    }

    @Test
    public void testDeserializeWithHolidays() throws IOException {
        String text = "{\"schedule\": \"L-VT8:00-19:00/H\"}";

        ScheduleBasic schedule = mapper.readValue(text, ScheduleTest.class).getSchedule();

        assertEquals(1, schedule.getBeginDay());
        assertEquals(5, schedule.getEndDay());
        assertEquals(new LocalTime(8, 0), schedule.getBeginTime());
        assertEquals(new LocalTime(19, 0), schedule.getEndTime());
        assertEquals(holidays.getList(), schedule.getHolidays());
    }

    @Test
    public void testDeserializeWithDefaultHolidays() throws IOException {
        Holidays defaultHolidays = new Holidays();
        defaultHolidays.setList(Arrays.asList(new DateTime(2018,12,25,0,0)));
        DefaultHolidays.setDays(defaultHolidays);
        mapper = new JSONMeasuresCollectionHandler().getMapper();
        String text = "{\"schedule\": \"L-VT8:00-19:00/H\"}";

        ScheduleBasic schedule = mapper.readValue(text, ScheduleTest.class).getSchedule();

        assertEquals(1, schedule.getBeginDay());
        assertEquals(5, schedule.getEndDay());
        assertEquals(new LocalTime(8, 0), schedule.getBeginTime());
        assertEquals(new LocalTime(19, 0), schedule.getEndTime());
        assertEquals(defaultHolidays.getList(), schedule.getHolidays());
    }

    @Test
    public void testDeserializeVariable() throws IOException {
        String text = "{\"schedule\": \"${variable}\"}";

        ScheduleBasic schedule = mapper.readValue(text, ScheduleTest.class).getSchedule();

        assertNull(schedule);
    }

    @Test
    public void testSerialize() throws IOException {
        ScheduleTest st = new ScheduleTest();
        st.schedule = new ScheduleBasic(2, 6, new LocalTime(9, 0), new LocalTime(15, 0));

        String str = mapper.writeValueAsString(st);
        assertEquals("{\"schedule\":\"M-ST09:00-15:00\"}", str);

    }

}