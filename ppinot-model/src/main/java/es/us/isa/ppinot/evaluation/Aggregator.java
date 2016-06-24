package es.us.isa.ppinot.evaluation;

import java.util.Collection;

/**
 * Aggregator
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class Aggregator {

    public static final String SUM = "Sum";
    public static final String AVG = "Average";
    public static final String MAX = "Maximum";
    public static final String MIN = "Minimum";

    private String aggregationFunction;

    public Aggregator() {

    }

    public Aggregator(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction;
    }

    public double aggregate(Collection<Double> values) {
        return aggregate(aggregationFunction != null ? aggregationFunction : SUM, values);
    }

    public double aggregate(String aggregationFunction, Collection<Double> values) {
        double aggregation = Double.NaN;

        if (SUM.equals(aggregationFunction)) {
            aggregation = sum(values);
        } else if (AVG.equals(aggregationFunction)) {
            aggregation = avg(values);
        } else if (MAX.equals(aggregationFunction)) {
            aggregation = max(values);
        } else if (MIN.equals(aggregationFunction)) {
            aggregation = min(values);
        }

        return aggregation;
    }

    public double sum(Collection<Double> values) {
        double result = 0;
        boolean processed = false;
        for (Double v: values) {
            if (!v.isNaN()) {
                result += v;
                processed = true;
            }
        }

        return (processed ? result : Double.NaN);
    }

    public double avg(Collection<Double> values) {
        double sum = 0;
        double count = 0;
        for (Double v: values) {
            if (!v.isNaN()) {
                sum += v;
                count += 1;
            }
        }

        return (count > 0 ? sum / count : Double.NaN);
    }

    public double max(Collection<Double> values) {
        double max = Double.MIN_VALUE;
        for (Double v: values) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public double min(Collection<Double> values) {
        double min = Double.MAX_VALUE;
        for (Double v : values) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }
}
