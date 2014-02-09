package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.MeasureDefinition;

import java.util.Arrays;

/**
 * MeasureInstance
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MeasureInstance extends Measure {
    private String instanceId;

    public MeasureInstance(MeasureDefinition definition, double value, String processId, String instanceId) {
        super(definition, processId, Arrays.asList(instanceId), value);
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
