package es.us.isa.ppinot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * PPISet
 *
 * @author resinas
 */
public class PPISet {
    private String processId;
    private List<PPI> ppis;
    private List<MeasureDefinition> measures;

    public PPISet() {}

    public PPISet(String processId, Collection<PPI> ppis, Collection<MeasureDefinition> measures) {
        this.processId = processId;
        this.ppis = new ArrayList<PPI>(ppis);
        this.measures = new ArrayList<MeasureDefinition>(measures);
    }

    public String getProcessId() {
        return processId;
    }

    public List<PPI> getPpis() {
        return ppis;
    }

    public List<MeasureDefinition> getMeasures() {
        return measures;
    }
}
