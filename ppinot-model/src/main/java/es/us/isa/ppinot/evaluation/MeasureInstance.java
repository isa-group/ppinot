package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.MeasureDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public MeasureInstance(MeasureDefinition definition, Object value, String processId, String instanceId) {
        super(definition, processId, Arrays.asList(instanceId), value);
        this.instanceId = instanceId;
    }

    public MeasureInstance(MeasureDefinition definition, MeasureScope scope, Object value) {
        super(definition, scope, value);

        Collection<String> instances = scope.getInstances();
        if (instances.size() != 1) {
            throw new RuntimeException("Invalid measure scope for measure instance. Instances size: " + instances.size());
        } else {
            instanceId = instances.iterator().next();
        }
    }

    public String getInstanceId() {
        return instanceId;
    }

    public static Map<String, MeasureInstance> buildMeasureMap(Collection<? extends Measure> measures) {
        Map<String, MeasureInstance> measureMap = new HashMap<String, MeasureInstance>();
        for (Measure m : measures) {
            if (m instanceof MeasureInstance) {
                MeasureInstance mi = (MeasureInstance) m;
                measureMap.put(mi.getInstanceId(), mi);
            } else {
                throw new RuntimeException("All measures should be MeasureInstance");
            }
        }
        return measureMap;
    }

}
