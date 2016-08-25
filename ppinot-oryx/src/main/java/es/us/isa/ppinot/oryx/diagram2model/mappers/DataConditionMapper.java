package es.us.isa.ppinot.oryx.diagram2model.mappers;

import es.us.isa.ppinot.handler.transformation.StateTransformer;
import es.us.isa.ppinot.model.state.RuntimeState;
import org.oryxeditor.server.diagram.generic.GenericShape;
import org.oryxeditor.server.diagram2model.mappers.fields.FieldMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

/**
 * DataConditionMapper
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataConditionMapper implements FieldMapper {
    @Override
    public Object map(Map<String, Object> source, Class<?> target) {
        String state = (String) source.get("state");
        if (state == null || state.isEmpty()) {
            state = getStateFromDataObject((GenericShape) source.get("#target"));
        }

        return new HashSet<RuntimeState>(Arrays.asList(StateTransformer.transformDataState(state)));
    }

    private String getStateFromDataObject(GenericShape target) {
        String state = null;
        if (target != null && "DataObject".equals(target.getStencilId())) {
            state = target.getProperty("state");
        }
        return state;
    }

}
