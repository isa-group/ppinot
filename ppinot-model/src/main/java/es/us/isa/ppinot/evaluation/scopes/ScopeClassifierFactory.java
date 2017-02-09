package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;

/**
 * ScopeClassifierFactory
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScopeClassifierFactory {
    public ScopeClassifier create(ProcessInstanceFilter filter, DataMeasure referencePoint) {
        ScopeClassifier classifier = null;

        if (filter instanceof LastInstancesFilter) {
            classifier = new LastInstancesScopeClassifier((LastInstancesFilter) filter);
        } else if (filter instanceof SimpleTimeFilter) {
            SimpleTimeFilter referenceFilter = ((SimpleTimeFilter) filter).copy();
            if (referencePoint != null) {
                referenceFilter.setReferencePoint(referencePoint);
            }
            classifier = new TimeScopeClassifier(referenceFilter);
        }

        return classifier;
    }
}
