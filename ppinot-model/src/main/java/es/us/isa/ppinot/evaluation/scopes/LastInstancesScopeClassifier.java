package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.MeasureScopeImpl;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * LastInstancesScopeClassifier
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class LastInstancesScopeClassifier extends ScopeClassifier {
    private LastInstancesFilter filter;
    private List<String> current;
    private List<MeasureScope> scopes;

    public LastInstancesScopeClassifier(LastInstancesFilter filter) {
        super();
        this.filter = filter;
        scopes = new ArrayList<MeasureScope>();
        current = new ArrayList<String>();
    }

    @Override
    public Collection<MeasureScope> listScopes() {
        return scopes;
    }

    @Override
    protected void instanceEnded(ProcessInstance instance) {
        current.add(instance.getInstanceId());
        if (current.size() >= filter.getNumberOfInstances()) {
            scopes.add(new MeasureScopeImpl(instance.getProcessId(), new ArrayList<String>(current)));
            current.remove(0);
        }
    }
}
