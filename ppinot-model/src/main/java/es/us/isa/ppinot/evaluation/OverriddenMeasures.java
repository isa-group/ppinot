package es.us.isa.ppinot.evaluation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OverriddenMeasures
 * Copyright (C) 2018 Universidad de Sevilla
 *
 * @author resinas
 */
public class OverriddenMeasures {
    private List<Measure> measures;

    public OverriddenMeasures() {
        measures = new ArrayList<Measure>();
    }

    public void add(Measure m) {
        measures.add(m);
    }

    @NotNull
    public Map<String, Measure> getOverriddenValuesContainedInScope(MeasureScope scope) {
        Map<String, Measure> overriddenValues = new HashMap<String, Measure>();

        for (Measure overriddenMeasure : measures) {
            if (overriddenMeasure.getMeasureScope().isContainedIn(scope)) {
                for (String instance : overriddenMeasure.getMeasureScope().getInstances()) {
                    overriddenValues.put(instance, overriddenMeasure);
                }
            }
        }
        return overriddenValues;
    }

    public Measure getOverriddenValueForScope(MeasureScope scope) {
        Measure overriddenValue = null;

        for (Measure overriddenMeasure : measures) {
            if (overriddenMeasure.getMeasureScope().equivalentTo(scope)) {
                overriddenValue = overriddenMeasure;
                break;
            }
        }
        return overriddenValue;
    }


}
