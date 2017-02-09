package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * MeasureComputerHelper
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MeasureComputerHelper {
    protected CountMeasureComputer createCountMeasureComputer(String activity, RuntimeState state) {
        TimeInstantCondition when = withCondition(activity, state);
        CountMeasure measure = createCountMeasure(when);
        return new CountMeasureComputer(measure);
    }

    protected TimeInstantCondition withCondition(String activity, RuntimeState state) {
        return new TimeInstantCondition(activity, state);
    }

    protected CountMeasure createCountMeasure(TimeInstantCondition when) {
        return new CountMeasure("id", "name", "desc", null, null, when);
    }

    protected TimeMeasureComputer createLinearTimeMeasureComputer(String activityStart, GenericState stateStart,
                                                                  String activityEnd, GenericState stateEnd) {
        TimeMeasure measure = createTimeMeasure(activityStart, stateStart, activityEnd, stateEnd);
        measure.setTimeMeasureType(TimeMeasureType.LINEAR);

        return new TimeMeasureComputer(measure, null);
    }

    protected TimeMeasureComputer createLinearUnfinishedTimeMeasureComputer(String activityStart, GenericState stateStart,
                                                                  String activityEnd, GenericState stateEnd) {
        TimeMeasure measure = createTimeMeasure(activityStart, stateStart, activityEnd, stateEnd);
        measure.setTimeMeasureType(TimeMeasureType.LINEAR);
        measure.setComputeUnfinished(true);

        return new TimeMeasureComputer(measure, null);
    }

    protected TimeMeasureComputer createLinearTimeMeasureComputerWithSchedule(String activityStart, GenericState stateStart,
                                                                  String activityEnd, GenericState stateEnd, Schedule workingHours, String timeUnit) {
        TimeMeasure measure = createTimeMeasure(activityStart, stateStart, activityEnd, stateEnd);
        measure.setTimeMeasureType(TimeMeasureType.LINEAR);
        measure.setConsiderOnly(workingHours);
        measure.setUnitOfMeasure(timeUnit);

        return new TimeMeasureComputer(measure, null);
    }

    protected TimeMeasureComputer createCyclicTimeMeasureComputer(String activityStart, GenericState stateStart,
                                                                  String activityEnd, GenericState stateEnd,
                                                                  String aggregation) {
        TimeMeasure measure = createTimeMeasure(activityStart, stateStart, activityEnd, stateEnd);
        measure.setTimeMeasureType(TimeMeasureType.CYCLIC);
        measure.setSingleInstanceAggFunction(aggregation);

        return new TimeMeasureComputer(measure, null);
    }

    private TimeMeasure createTimeMeasure(String activityStart, GenericState stateStart, String activityEnd, GenericState stateEnd) {
        TimeInstantCondition from = new TimeInstantCondition(activityStart, stateStart);
        TimeInstantCondition end = new TimeInstantCondition(activityEnd, stateEnd);
        TimeMeasure measure = new TimeMeasure();
        measure.setFrom(from);
        measure.setTo(end);
        return measure;
    }
}
