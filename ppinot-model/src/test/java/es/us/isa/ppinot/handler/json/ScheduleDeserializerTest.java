package es.us.isa.ppinot.handler.json;

import es.us.isa.ppinot.model.Schedule;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * ScheduleDeserializerTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScheduleDeserializerTest {

    public static class ScheduleTest {
        private Schedule schedule;

        public ScheduleTest() {}

        public Schedule getSchedule() {
            return schedule;
        }
    }

    @Test
    public void testDeserialize() throws IOException {
        String text = "{\"schedule\": \"L-VT8:00-19:00\"}";
        ObjectMapper mapper = new ObjectMapper();

        Schedule schedule = mapper.readValue(text, ScheduleTest.class).getSchedule();

        assertEquals(1, schedule.getBeginDay());
        assertEquals(5, schedule.getEndDay());
        assertEquals(new LocalTime(8, 0), schedule.getBeginTime());
        assertEquals(new LocalTime(19, 0), schedule.getEndTime());
    }

    @Test
    public void testDeserializeWithHolidays() throws IOException {
        String text = "{\"schedule\": \"L-VT8:00-19:00/H\"}";
        ObjectMapper mapper = new ObjectMapper();

        Schedule schedule = mapper.readValue(text, ScheduleTest.class).getSchedule();

        assertEquals(1, schedule.getBeginDay());
        assertEquals(5, schedule.getEndDay());
        assertEquals(new LocalTime(8, 0), schedule.getBeginTime());
        assertEquals(new LocalTime(19, 0), schedule.getEndTime());
    }

    @Test
    public void testSerialize() throws IOException {
        ScheduleTest st = new ScheduleTest();
        st.schedule = new Schedule(2, 6, new LocalTime(9, 0), new LocalTime(15, 0));
        ObjectMapper mapper = new ObjectMapper();

        String str = mapper.writeValueAsString(st);
        assertEquals("{\"schedule\":\"M-ST09:00-15:00\"}", str);

    }

}