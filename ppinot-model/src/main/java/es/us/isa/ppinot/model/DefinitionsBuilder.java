package es.us.isa.ppinot.model;

import es.us.isa.ppinot.handler.JSONMeasuresCollectionHandler;
import es.us.isa.ppinot.model.schedule.Schedule;
import es.us.isa.ppinot.model.schedule.ScheduleBasic;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.io.StringWriter;

/**
 * DefinitionsBuilder
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class DefinitionsBuilder {

    @JsonSerialize(using = ToStringSerializer.class)
    private class ParamSchedule extends ScheduleBasic {
        String paramName;

        public ParamSchedule(String paramName) {
            super(1, 7, new LocalTime(0,0), new LocalTime(23,59,59));
            this.paramName = paramName;
        }

        public String toString() {
            return "${" + paramName + "}";
        }
    }

    public String toString() {
        JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler();
        MeasuresCollection mc = buildCollection();
        handler.setCollection(mc);

        StringWriter sw = new StringWriter();
        try {
            handler.save(sw);
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sw.toString();

    }

    public abstract MeasuresCollection buildCollection();


    protected Schedule paramSchedule(String param) {
        return new ParamSchedule(param);
    }
}
