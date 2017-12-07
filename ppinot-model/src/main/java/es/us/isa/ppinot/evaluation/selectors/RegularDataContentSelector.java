package es.us.isa.ppinot.evaluation.selectors;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.DataContentSelection;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RegularDataContentSelector
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class RegularDataContentSelector implements DataContentSelector {
    private static final Logger log = Logger.getLogger(RegularDataContentSelector.class.getName());

    private Serializable selectionExpression;

    public RegularDataContentSelector(DataContentSelection selection) {
        selectionExpression = MVEL.compileExpression(selection.getSelection());
    }

    @Override
    public Object select(LogEntry entry) {
        Object value = null;
        Map<String, Object> data = entry.getData();
        data.put("timestamp", entry.getTimeStamp());

        try {
            value = MVEL.executeExpression(selectionExpression, data);
        } catch (Exception e) {
            log.log(Level.INFO, "Expression evaluation failed: " + selectionExpression, e);
        }

        return value;
    }

    protected Serializable getSelectionExpression() {
        return selectionExpression;
    }
}
