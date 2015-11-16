package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.MeasureDefinition;

import java.util.Collection;

/**
 * Measure
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class Measure {
    protected final MeasureScope measureScope;
    protected MeasureDefinition definition;
    protected double value;

    public Measure(MeasureDefinition definition, MeasureScope scope, Object value) {
        this(definition, scope, toDouble(value));
    }

    public Measure(MeasureDefinition definition, String processId, Collection<String> instances, Object value) {
        this(definition, new MeasureScope(processId, instances), value);
    }


    private static double toDouble(Object value) {
        double doubleValue;

        if (value == null) {
            doubleValue = Double.NaN;
        } else if (value instanceof Integer) {
            doubleValue = ((Integer) value).doubleValue();
        } else if (value instanceof Boolean) {
            doubleValue = (Boolean) value ? 1 : 0;
        } else if (value instanceof String) {
            doubleValue = Double.parseDouble((String) value);
        } else {
            doubleValue = (Double) value;
        }
        return doubleValue;
    }

    public Measure(MeasureDefinition definition, String processId, Collection<String> instances, double value) {
        this(definition, new MeasureScope(processId, instances), value);
    }

    public Measure(MeasureDefinition definition, MeasureScope scope, double value) {
        this.definition = definition;
        this.measureScope = scope;
        this.value = value;
    }

    public MeasureDefinition getDefinition() {
        return definition;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = toDouble(value);
    }

    public String getProcessId() {
        return measureScope.getProcessId();
    }

    public Collection<String> getInstances() {
        return measureScope.getInstances();
    }

    public MeasureScope getMeasureScope() {
        return measureScope;
    }
}
