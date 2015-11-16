package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.DataMeasure;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DataMeasureComputer
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataMeasureComputer extends AbstractBaseMeasureComputer<DataMeasure> implements MeasureComputer {

    private static final Logger log = Logger.getLogger(DataMeasureComputer.class.getName());

    private Serializable preconditionExpression;
    private Serializable selectionExpression;

    public DataMeasureComputer(MeasureDefinition definition) {
        super(definition);

        if (this.definition.getPrecondition() != null) {
            preconditionExpression = MVEL.compileExpression(this.definition.getPrecondition().getRestriction());
        }
        selectionExpression = MVEL.compileExpression(this.definition.getDataContentSelection().getSelection());
    }

    @Override
    public void update(LogEntry entry) {

        if (precondition(entry.getData())) {
            try {
                Object value = MVEL.executeExpression(selectionExpression, entry.getData());
                MeasureInstance m = getOrCreateMeasure(entry, null);
                m.setValue(value);
            } catch (Exception e) {
                log.log(Level.INFO, "Expression evaluation failed: " + this.definition.getDataContentSelection().getSelection(), e);
            }
        }

    }

    private boolean precondition(Map<String, Object> data) {
        boolean condition = false;

        if (preconditionExpression == null) {
            condition = true;
        } else {
            try {
                Object value = MVEL.executeExpression(preconditionExpression, data);
                if (value instanceof Boolean) {
                    condition = (Boolean) value;
                }
            } catch (Exception e) {
                condition = false;
            }
        }

        return condition;
    }
}
