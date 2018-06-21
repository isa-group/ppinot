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

    public ComputerConfig(ProcessInstanceFilter filter) {
        this.filter = filter;
        overrides = new HashMap<MeasureDefinition, OverriddenMeasures>();
    }

    public ProcessInstanceFilter getFilter() {
        return filter;
    }

    public ComputerConfig add(Measure m) {
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

}
