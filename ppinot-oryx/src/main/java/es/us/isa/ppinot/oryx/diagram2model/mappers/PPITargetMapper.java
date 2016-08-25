package es.us.isa.ppinot.oryx.diagram2model.mappers;

import es.us.isa.ppinot.model.Target;
import org.json.JSONObject;
import org.oryxeditor.server.diagram2model.ShapeUtils;
import org.oryxeditor.server.diagram2model.mappers.fields.AbstractSingleFieldMapper;
import org.oryxeditor.server.diagram2model.mappers.fields.FieldMapper;

/**
 * PPITargetMapper
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class PPITargetMapper extends AbstractSingleFieldMapper implements FieldMapper {
    @Override
    public Object map(Object source, Class<?> target) {
        if (!(source instanceof JSONObject)) {
            return null;
        }

        Target ppiTarget = new Target();
        JSONObject jsonTarget = ShapeUtils.getFirstObject((JSONObject)source);

        double lowerBound = jsonTarget.optDouble("lowerBound");
        if (! Double.isNaN(lowerBound)) {
            ppiTarget.setRefMin(lowerBound);
        }

        double upperBound = jsonTarget.optDouble("upperBound");
        if (!Double.isNaN(upperBound)) {
            ppiTarget.setRefMax(upperBound);
        }

        return ppiTarget;

    }

}
