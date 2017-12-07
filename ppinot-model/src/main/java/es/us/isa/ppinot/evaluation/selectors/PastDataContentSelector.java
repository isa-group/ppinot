package es.us.isa.ppinot.evaluation.selectors;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.PreviousDataStateTransformer;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.PastDataContentSelection;
import org.mvel2.MVEL;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PastDataContentSelector
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class PastDataContentSelector extends RegularDataContentSelector {

    private static final Logger log = Logger.getLogger(PastDataContentSelector.class.getName());

    private String separator = "-";

    public PastDataContentSelector(DataContentSelection dataContentSelection) {
        super(dataContentSelection);
        separator = ((PastDataContentSelection) dataContentSelection).getSeparator();
    }

    @Override
    public Object select(LogEntry entry) {
        Object value = super.select(entry);
        Object pastValue = null;

        Map<String, Object> pastData = previousState(entry);
        if (!pastData.isEmpty()) {
            try {
                pastValue = MVEL.executeExpression(getSelectionExpression(), pastData);
            } catch (Exception e) {
                log.log(Level.INFO, "Expression evaluation failed: " + getSelectionExpression(), e);
            }
        }

        return String.valueOf(pastValue) + separator + value;
    }

    private Map<String, Object> previousState(LogEntry entry) {
        return (Map<String, Object>) entry.getExtension(PreviousDataStateTransformer.EXTENSION);
    }


}
