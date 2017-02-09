package es.us.isa.ppinot.evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TestUtils
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class TestUtils {
    public static void compareScopes(List<? extends Measure> measures, List<? extends Measure> otherMeasures) {
        for (Measure m: measures) {
            boolean notFound = true;
            for (Measure mUTC : otherMeasures) {
                if (m.getMeasureScope().getScopeInfo().equals(mUTC.getMeasureScope().getScopeInfo())) {
                    notFound = false;
                    Set<String> i1 = new HashSet<String>(m.getMeasureScope().getInstances());
                    Set<String> i2 = new HashSet<String>(mUTC.getMeasureScope().getInstances());
                    List<String> i1mi2 = new ArrayList<String>();
                    List<String> i2mi1 = new ArrayList<String>();
                    for (String i : i1) {
                        if (!i2.contains(i)) {
                            i1mi2.add(i);
                        }
                    }
                    for (String i: i2) {
                        if (!i1.contains(i)) {
                            i2mi1.add(i);
                        }
                    }
                    System.out.println("M1 - M2: " + i1mi2.toString());
                    System.out.println("M2 - M1: " + i2mi1.toString());
                    System.out.println("M1 size: "+m.getMeasureScope().getInstances().size());
                    System.out.println("M2 size: " + mUTC.getMeasureScope().getInstances().size());

                    if (m.getValue() == mUTC.getValue()) {
                        System.out.println("EQUAL: " + m.getMeasureScope().getScopeInfo().toString());
                    } else {
                        System.out.println("DIFFERENT: " + m.getMeasureScope().getScopeInfo().toString() + " - " + m.getValue() + ", " + mUTC.getValue());
                    }
                    break;
                }

            }
            if (notFound) {
                System.out.println("NOT FOUND: " + m.getMeasureScope().getScopeInfo().toString());
            }

        }
    }

    public static void compareMeasures(List<? extends Measure> measures, List<? extends Measure> otherMeasures) {
        for (Measure m: measures) {
            boolean notFound = true;
            for (Measure mUTC : otherMeasures) {
                if (m.getMeasureScope().getInstances().equals(mUTC.getMeasureScope().getInstances())) {
                    notFound = false;
                    if (m.getValue() == mUTC.getValue()) {
                        System.out.println("EQUAL: " + m.getMeasureScope().getScopeInfo().toString());
                    } else {
                        System.out.println("DIFFERENT: " + m.getMeasureScope().getScopeInfo().toString() + " - " + m.getValue() + ", " + mUTC.getValue());
                    }
                    break;
                }

            }
            if (notFound) {
                System.out.println("NOT FOUND: " + m.getMeasureScope().getScopeInfo().toString());
            }

        }
    }
}
