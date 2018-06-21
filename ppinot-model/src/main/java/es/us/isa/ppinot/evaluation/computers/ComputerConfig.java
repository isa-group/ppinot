package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.OverriddenMeasures;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

import java.util.HashMap;
import java.util.Map;

public class ComputerConfig {
    private final ProcessInstanceFilter filter;
    private Map<MeasureDefinition, OverriddenMeasures> overrides;
    private Evidences evidences;
    private boolean flattenedEvidences;


    public ComputerConfig(ProcessInstanceFilter filter) {
        this(filter, Evidences.NONE);
    }

    public ComputerConfig(ProcessInstanceFilter filter, Evidences evidences) {
        this(filter, evidences, true);
    }

    public ComputerConfig(ProcessInstanceFilter filter, Evidences evidences, boolean flattenedEvidences) {
        this.filter = filter;
        this.overrides = new HashMap<MeasureDefinition, OverriddenMeasures>();
        this.evidences = evidences;
        this.flattenedEvidences = flattenedEvidences;
    }

    public ProcessInstanceFilter getFilter() {
        return filter;
    }

    public ComputerConfig addOverride(Measure m) {
        OverriddenMeasures list = this.overrides.get(m.getDefinition());
        if (list == null) {
            list = new OverriddenMeasures();
            this.overrides.put(m.getDefinition(), list);
        }
        list.add(m);

        return this;
    }

    public OverriddenMeasures getOverrides(MeasureDefinition d) {
        OverriddenMeasures list = this.overrides.get(d);
        if (list == null) {
            list = new OverriddenMeasures();
        }
        return list;
    }

    public Evidences getEvidences() {
        return evidences;
    }

    public boolean includeEvidences() {
        return ! Evidences.NONE.equals(evidences);
    }

    public boolean isFlattenedEvidences() {
        return flattenedEvidences;
    }

    public enum Evidences {NONE, ID, ALL}

}
