package es.us.isa.ppinot.oryx.diagram2model.mappers;

import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.oryxeditor.server.diagram2model.ShapeUtils;
import org.oryxeditor.server.diagram2model.mappers.fields.FieldMapper;

import java.util.Map;

/**
 * ScopeMapper
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScopeMapper implements FieldMapper {
    private final Logger logger = Logger.getLogger(ScopeMapper.class);

    @Override
    public Object map(Map<String,Object> values, Class<?> target) {
        ProcessInstanceFilter result = getLastInstancesFilter(values);

        if (result == null) {
            result = getTimeFilter(values);
        }

        return result;
    }

    private ProcessInstanceFilter getTimeFilter(Map<String, Object> values) {
        JSONObject time = ShapeUtils.getFirstObject((JSONObject) values.get("timescope"));
        SimpleTimeFilter filter = null;
        if (time != null) {

            Integer frequencyScope = 1;
            try {
                frequencyScope = time.getInt("frequencyScope");
            } catch (Exception e) {
                logger.warn("Unable to read frequencyScope", e);
            }

            Boolean relative = time.optBoolean("relativeScope");

            Period period = null;
            try {
                period = Period.valueOf(time.getString("scopePeriod"));
            } catch (Exception e) {
                logger.warn("Unable to read scopePeriod", e);
            }

            filter = new SimpleTimeFilter(period, frequencyScope, relative);

        }
        return filter;
    }

    private ProcessInstanceFilter getLastInstancesFilter(Map<String, Object> values) {
        LastInstancesFilter filter = null;
        Integer lastInstances = (Integer) values.get("lastinstancesscope");

        if (lastInstances != null && lastInstances != 0) {
            filter = new LastInstancesFilter(lastInstances);
        }

        return filter;
    }
}
