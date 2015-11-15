package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.DataObjectState;
import es.us.isa.ppinot.model.state.GenericState;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JSONMeasureEvaluatorTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONMeasureEvaluatorTest {
    private static final String REAPERTURA_URL = "http://www.isa.us.es/ansmeasures.json#porcentajeReapertura";

    @Test
    public void test() throws MalformedURLException {

        JSONMeasureEvaluator evaluator = new JSONMeasureEvaluator();

        evaluator.addEvent("{\"Id\":\"1\", \"Fecha\":\"12:00:00 1/6/2016\", \"Estado\":\"Abierta\", \"Criticidad\": \"P1\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Registrar\"}");
        evaluator.addEvent("{\"Id\":\"1\", \"Fecha\":\"13:00:00 1/6/2016\", \"Estado\":\"Abierta\", \"Criticidad\": \"P2\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Repriorizar\"}");
        evaluator.addEvent("{\"Id\":\"1\", \"Fecha\":\"14:00:00 1/6/2016\", \"Estado\":\"Fijada\", \"Criticidad\": \"P2\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Resolver\"}");
        evaluator.addEvent("{\"Id\":\"1\", \"Fecha\":\"15:00:00 1/6/2016\", \"Estado\":\"Cerrada\", \"Criticidad\": \"P2\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Cerrar\"}");
        evaluator.addEvent("{\"Id\":\"2\", \"Fecha\":\"12:00:00 1/6/2016\", \"Estado\":\"Abierta\", \"Criticidad\": \"P1\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Registrar\"}");
        evaluator.addEvent("{\"Id\":\"2\", \"Fecha\":\"14:00:00 1/6/2016\", \"Estado\":\"Fijada\", \"Criticidad\": \"P1\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Resolver\"}");
        evaluator.addEvent("{\"Id\":\"2\", \"Fecha\":\"15:00:00 1/6/2016\", \"Estado\":\"Abierta\", \"Criticidad\": \"P1\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Reabrir\"}");
        evaluator.addEvent("{\"Id\":\"2\", \"Fecha\":\"16:00:00 1/6/2016\", \"Estado\":\"Fijada\", \"Criticidad\": \"P1\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Resolver\"}");
        evaluator.addEvent("{\"Id\":\"3\", \"Fecha\":\"13:00:00 1/6/2016\", \"Estado\":\"Abierta\", \"Criticidad\": \"P3\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Reclamar\"}");
        evaluator.addEvent("{\"Id\":\"2\", \"Fecha\":\"17:00:00 1/6/2016\", \"Estado\":\"Cerrada\", \"Criticidad\": \"P1\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Cerrar\"}");
        evaluator.addEvent("{\"Id\":\"3\", \"Fecha\":\"12:00:00 1/6/2016\", \"Estado\":\"Abierta\", \"Criticidad\": \"P3\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Registrar\"}");
        evaluator.addEvent("{\"Id\":\"3\", \"Fecha\":\"14:00:00 1/6/2016\", \"Estado\":\"Fijada\", \"Criticidad\": \"P3\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Resolver\"}");
        evaluator.addEvent("{\"Id\":\"3\", \"Fecha\":\"17:00:00 1/6/2016\", \"Estado\":\"Cerrada\", \"Criticidad\": \"P3\",\"Grupo\": \"proveedor 1\",\"Acciones\":\"Cerrar\"}");

        List<Measure> measures = evaluator.getValues(REAPERTURA_URL, new SimpleTimeFilter(Period.MONTHLY, 1, false));

        printMeasures(measures);

    }

    public MeasureDefinition tiempoMedioResolucion(String proveedor) {
        TimeMeasure resolutionTime = new TimeMeasure();
        resolutionTime.setFrom(new TimeInstantCondition("Estado", new DataObjectState("Estado == \"Abierta\" && Grupo ==\""+proveedor+"\"")));
        resolutionTime.setTo(new TimeInstantCondition("Estado", new DataObjectState("Estado != \"Abierta\" && Grupo ==\""+proveedor+"\"")));
        resolutionTime.setUnitOfMeasure(TimeUnit.SECONDS);
        resolutionTime.setTimeMeasureType(TimeMeasureType.CYCLIC);
        resolutionTime.setSingleInstanceAggFunction(Aggregator.SUM);

        AggregatedMeasure avgResolutionTime = new AggregatedMeasure();
        avgResolutionTime.setId("avgResolutionTime");
        avgResolutionTime.setBaseMeasure(resolutionTime);
        avgResolutionTime.setAggregationFunction(Aggregator.AVG);

        return avgResolutionTime;
    }

    public MeasureDefinition porcentajeReapertura(String proveedor) {
        CountMeasure countMeasure = new CountMeasure();
        countMeasure.setWhen(new TimeInstantCondition("Reabrir", GenericState.END));
        CountMeasure countProveedor = new CountMeasure();
        countProveedor.setWhen(new TimeInstantCondition("Grupo", new DataObjectState("Grupo == \""+proveedor+"\"")));


        DerivedSingleInstanceMeasure reapertura = new DerivedSingleInstanceMeasure();
        reapertura.setFunction("reapertura > 0 && participaProveedor > 0");
        reapertura.addUsedMeasure("reapertura", countMeasure);
        reapertura.addUsedMeasure("participaProveedor",  countProveedor);

        AggregatedMeasure contarReapertura = new AggregatedMeasure();
        contarReapertura.setBaseMeasure(reapertura);
        contarReapertura.setAggregationFunction(Aggregator.SUM);

        DerivedMultiInstanceMeasure porcentajeReapertura = new DerivedMultiInstanceMeasure();
        porcentajeReapertura.setId("porcentajeReapertura");
        porcentajeReapertura.setFunction("reabiertas/totalResueltas*100");
        porcentajeReapertura.addUsedMeasure("reabiertas",contarReapertura);
        porcentajeReapertura.addUsedMeasure("totalResueltas", contarTerminadas(proveedor));

        return porcentajeReapertura;

    }

    private MeasureDefinition contarTerminadas(String proveedor) {
        CountMeasure countCerrar = new CountMeasure();
        countCerrar.setWhen(new TimeInstantCondition("Cerrar", GenericState.END));

        CountMeasure countCancelar = new CountMeasure();
        countCancelar.setWhen(new TimeInstantCondition("Cancelar", GenericState.END));

        CountMeasure countProveedor = new CountMeasure();
        countProveedor.setWhen(new TimeInstantCondition("Grupo", new DataObjectState("Grupo == \""+proveedor+"\"")));

        DerivedSingleInstanceMeasure terminada = new DerivedSingleInstanceMeasure();
        terminada.setFunction("(cerrar > 0 || cancelar > 0) && (proveedor > 0)");
        terminada.addUsedMeasure("cerrar", countCerrar);
        terminada.addUsedMeasure("cancelar", countCancelar);
        terminada.addUsedMeasure("proveedor", countProveedor);

        AggregatedMeasure contarTerminadas = new AggregatedMeasure();
        contarTerminadas.setBaseMeasure(terminada);
        contarTerminadas.setAggregationFunction(Aggregator.SUM);

        return contarTerminadas;
    }

    private void printMeasures(List<Measure> measures) {
        for (Measure m: measures) {
            System.out.println("Value: " + m.getValue());
            if (m.getMeasureScope() instanceof TemporalMeasureScope) {
                TemporalMeasureScope scope = (TemporalMeasureScope) m.getMeasureScope();
                System.out.println("From: " + scope.getStart());
                System.out.println("To: " + scope.getEnd());
            }
            System.out.println("Number of instances: " + m.getInstances().size());
            System.out.println("Instances: " + m.getInstances());
            System.out.println("--");
        }
    }


}