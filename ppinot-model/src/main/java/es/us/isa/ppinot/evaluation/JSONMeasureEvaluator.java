package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.logs.JSONLogEntryLoader;
import es.us.isa.ppinot.evaluation.logs.RecorderLog;
import es.us.isa.ppinot.evaluation.logs.StartEndDecoratorLog;
import es.us.isa.ppinot.handler.URLMeasuresCollectionHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import org.joda.time.format.DateTimeFormat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

/**
 * JSONMeasureEvaluator
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONMeasureEvaluator {
    private static final Logger log = Logger.getLogger(JSONMeasureEvaluator.class.getName());

    private RecorderLog recorderLog;
    private JSONLogEntryLoader jsonLoader;
    private URLMeasuresCollectionHandler measuresCollectionHandler;

    public JSONMeasureEvaluator() {
        recorderLog = new RecorderLog();
        measuresCollectionHandler = new URLMeasuresCollectionHandler();
        jsonLoader = new JSONLogEntryLoader("Id", "Acciones", "Fecha");
        jsonLoader.setDateTimeFormatter(DateTimeFormat.forPattern("HH:mm:ss d/M/YYYY"));
    }

    public void addEvent(String jsonEvent) {
        recorderLog.addEvent(jsonLoader.loadEvent(jsonEvent));
    }

    public List<Measure> getValues(String uriPPI, ProcessInstanceFilter filter) {
        try {
            URL url = new URL(uriPPI);
            MeasureDefinition definition = measuresCollectionHandler.load(url);

            return getValues(definition, filter);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + uriPPI, e);
        }
    }

    public List<Measure> getValues(MeasureDefinition definition, ProcessInstanceFilter filter) {
        LogMeasureEvaluator eval = new LogMeasureEvaluator(new StartEndDecoratorLog(recorderLog, "Cerrar", "Cancelar"));
        return eval.eval(definition, filter);
    }

    public Number getSingleValue(String uriPPI, ProcessInstanceFilter filter) {
        return getLastNumber(getValues(uriPPI, filter));
    }

    private Number getLastNumber(List<Measure> measures) {
        if (measures == null || measures.isEmpty()) {
            return null;
        }

        Measure lastMeasure = measures.get(measures.size() - 1);
        return lastMeasure.getValue();
    }

}
