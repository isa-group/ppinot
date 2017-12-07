package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.matchers.Matcher;
import es.us.isa.ppinot.evaluation.matchers.MatcherFactory;
import es.us.isa.ppinot.evaluation.selectors.DataContentSelector;
import es.us.isa.ppinot.evaluation.selectors.DataContentSelectorFactory;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.DataMeasure;

import java.util.logging.Logger;

/**
 * DataMeasureComputer
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataMeasureComputer extends AbstractBaseMeasureComputer<DataMeasure> implements MeasureComputer {

    private static final Logger log = Logger.getLogger(DataMeasureComputer.class.getName());

    private Matcher precondition = null;
    private DataContentSelector selector;
    private boolean onlyFirst = false;

    public DataMeasureComputer(MeasureDefinition definition) {
        super(definition);

        if (this.definition.getPrecondition() != null) {
            precondition = new MatcherFactory().create(this.definition.getPrecondition());
        }

        if (this.definition.isFirst()) {
            onlyFirst = true;
        }

        selector = DataContentSelectorFactory.create(this.definition.getDataContentSelection());
    }
	
	@Override
    public void update(LogEntry entry) {
        MeasureInstance m = getOrCreateMeasure(entry, null);

        if (precondition(entry)) {
            if (m.getValueAsObject() == null || ! onlyFirst) {
                Object value = selector.select(entry);
                m.setValue(value);
            }
        }

    }

    private boolean precondition(LogEntry entry) {
        boolean condition;

        if (precondition == null) {
            condition = true;
        } else {
            condition = precondition.matches(entry);
        }

        return condition;
    }
}
