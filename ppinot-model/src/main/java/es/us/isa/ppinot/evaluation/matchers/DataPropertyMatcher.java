package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import org.mvel2.MVEL;

import java.io.Serializable;

/**
 * DataPropertyMatcher
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataPropertyMatcher {
    private Serializable restrictionExpression = null;

    public DataPropertyMatcher(DataPropertyCondition condition) {
        if (condition != null && condition.getRestriction() != null)
            this.restrictionExpression = MVEL.compileExpression(condition.getRestriction());
    }

    public boolean matches(LogEntry entry) {
        boolean condition = false;

        if (restrictionExpression == null) {
            condition = true;
        } else {
            try {
                Object value = MVEL.executeExpression(restrictionExpression, entry.getData());
                if (value instanceof Boolean) {
                    condition = (Boolean) value;
                } else if (value instanceof Number) {
                    condition = ((Number) value).doubleValue() > 0;
                }
            } catch (Exception e) {
                condition = false;
            }
        }

        return condition;

    }
}
