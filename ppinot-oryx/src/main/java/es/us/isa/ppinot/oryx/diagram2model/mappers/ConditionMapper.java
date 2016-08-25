package es.us.isa.ppinot.oryx.diagram2model.mappers;

import es.us.isa.ppinot.handler.transformation.StateTransformer;
import org.oryxeditor.server.diagram.generic.GenericShape;
import org.oryxeditor.server.diagram2model.mappers.fields.FieldMapper;

import java.util.Map;

/**
 * ConditionMapper
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class ConditionMapper implements FieldMapper {
    private final static String DEFAULTSTATE = "Start";

    @Override
    public Object map(Map<String,Object> source, Class<?> targetClass) {
        String state = getState(source);
        return StateTransformer.transform(state);
    }

    private String getState(Map<String, Object> values) {
        String state = (String) values.get("when");
        if ("Other".equals(state)) {
            state = (String) values.get("state");
        }

        if (state == null || state.isEmpty()) {
            GenericShape target = (GenericShape) values.get("#target");

            if (target != null && "DataObject".equals(target.getStencilId())) {
                state = target.getProperty("state");
            } else {
                state = DEFAULTSTATE;
            }

        }
        return state;
    }

}
