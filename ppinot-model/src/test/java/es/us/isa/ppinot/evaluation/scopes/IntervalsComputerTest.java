package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * IntervalsComputerTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class IntervalsComputerTest {

    @NotNull
    private SimpleTimeFilter buildSimpleTimeFilter() {
        return new SimpleTimeFilter(Period.MONTHLY, 1, 16);
    }

    @Test
    public void testListIntervals() throws Exception {
        SimpleTimeFilter filter = buildSimpleTimeFilter();
        DateTime from = new DateTime(2014, 10, 16, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC);
        filter.setFrom(from);

        List<Interval> intervalList = IntervalsComputer.listIntervals(filter);

        Interval firstInterval = intervalList.get(0);
        Interval lastInterval = intervalList.get(intervalList.size() - 1);
        Assert.assertEquals(from.getMillis(), firstInterval.getStart().getMillis());
        Assert.assertEquals(new DateTime(2014, 11, 15, 23, 59, 59,999, firstInterval.getChronology()), firstInterval.getEnd());
        Assert.assertTrue(lastInterval.contains(filter.getUntil()));

        filter.setUntil(new DateTime(2016, 9, 1, 11, 23));
        intervalList = IntervalsComputer.listIntervals(filter);
        Assert.assertEquals(23, intervalList.size());

    }

    @Test
    public void testListIntervalsDifferentFrom() throws Exception {
        SimpleTimeFilter filter = buildSimpleTimeFilter();
        DateTime from = new DateTime(2014, 10, 1, 0, 0, 0);
        filter.setFrom(from);

        List<Interval> intervalList = IntervalsComputer.listIntervals(filter);

        Interval firstInterval = intervalList.get(0);
        Assert.assertTrue(firstInterval.contains(from));
        Assert.assertEquals(new DateTime(2014, 9, 16, 0, 0, 0,0, firstInterval.getChronology()), firstInterval.getStart());
        Assert.assertEquals(new DateTime(2014, 10, 15, 23, 59, 59,999, firstInterval.getChronology()), firstInterval.getEnd());


    }

    @Test
    public void testMeasuresProcessEnd() {
        CountMeasure actividadesDeReaperturaDespuesDeResolucionSPU = new CountMeasure();

        DerivedSingleInstanceMeasure derived = new DerivedSingleInstanceMeasure();
        derived.addUsedMeasure("actividadesDeReaperturaDespuesDeResolucionSPU", actividadesDeReaperturaDespuesDeResolucionSPU);
        derived.setFunction("actividadesDeReaperturaDespuesDeResolucionSPU > 0");

        DataMeasure solicitudesPadre = new DataMeasure();

        DataMeasure estado = new DataMeasure();

        CountMeasure accionesDeResolucionPorSPU = new CountMeasure();

        DerivedSingleInstanceMeasure filter = new DerivedSingleInstanceMeasure();
        filter.addUsedMeasure("solicitudesPadre", solicitudesPadre);
        filter.addUsedMeasure("estado", estado);
        filter.addUsedMeasure("accionesDeResolucionPorSPU", accionesDeResolucionPorSPU);

        AggregatedMeasure spu_io_k06 = new AggregatedMeasure();
        spu_io_k06.setAggregationFunction(Aggregator.AVG);
        spu_io_k06.setBaseMeasure(derived);
        spu_io_k06.setGroupedBySelections(Arrays.asList(new DataContentSelection("NODO", "NODO")));
        spu_io_k06.setFilter(filter);

        DerivedMultiInstanceMeasure percentage = new DerivedMultiInstanceMeasure();
        percentage.addUsedMeasure("value", spu_io_k06);
        percentage.setFunction(" value * 100");

        SimpleTimeFilter simpleTimeFilter = buildSimpleTimeFilter();

        boolean shouldBeTrue = IntervalsComputer.measuresProcessEnd(simpleTimeFilter, percentage);
        Assert.assertTrue(shouldBeTrue);


    }

    @Test
    public void testMeasuresProcessEndFalse() {
        MeasureDefinition derived = new CountMeasure();

        DataMeasure solicitudesPadre = new DataMeasure();

        CountMeasure count = new CountMeasure();

        DerivedSingleInstanceMeasure filter = new DerivedSingleInstanceMeasure();
        filter.addUsedMeasure("solicitudesPadre", solicitudesPadre);
        filter.addUsedMeasure("count", count);
        filter.setFunction("solicitudesPadre == '' && count > 0");

        AggregatedMeasure spu_io_k14 = new AggregatedMeasure();
        spu_io_k14.setAggregationFunction(Aggregator.AVG);
        spu_io_k14.setBaseMeasure(derived);
        spu_io_k14.setGroupedBySelections(Arrays.asList(new DataContentSelection("NODO", "NODO")));
        spu_io_k14.setFilter(filter);
        spu_io_k14.setIncludeUnfinished(true);

        DerivedMultiInstanceMeasure percentage = new DerivedMultiInstanceMeasure();
        percentage.addUsedMeasure("value", spu_io_k14);
        percentage.setFunction(" value * 100");

        SimpleTimeFilter simpleTimeFilter = buildSimpleTimeFilter();

        boolean shouldBeFalse = IntervalsComputer.measuresProcessEnd(simpleTimeFilter, percentage);
        Assert.assertFalse(shouldBeFalse);


    }




}