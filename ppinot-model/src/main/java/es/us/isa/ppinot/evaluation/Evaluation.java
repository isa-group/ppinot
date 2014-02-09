package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.Target;

/**
 * Evaluation
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class Evaluation {
    private PPI ppi;
    private double value;
    private MeasureScope scope;

    public Evaluation(PPI ppi, double value, MeasureScope scope) {
        this.ppi = ppi;
        this.value = value;
        this.scope = scope;
    }

    public double getValue() {
        return value;
    }

    public MeasureScope getScope() {
        return scope;
    }

    public boolean success() {
        boolean success = true;
        Target t = ppi.getTarget();

        if (t.getRefMin() != null)
            success = success && (value >= t.getRefMin());

        if (t.getRefMax() != null)
            success = success && (value <= t.getRefMax());

        return success;
    }


}
