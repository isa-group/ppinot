package es.us.isa.ppinot.analyser;

import java.util.Set;

/**
 * User: resinas
 * Date: 28/01/13
 * Time: 16:20
 */
public interface PPINotAnalyser {
    public Set<String> measuredBPElements(Set<String> ppiId);
    public Set<String> measuredPPIs(String bpElementId);

    public Set<String> involvedBPElements(Set<String> ppiId);
    public Set<String> notInvolvedBPElements();
    public Set<String> involvedInAllBPElements();

    public Set<String> associatedPPIs(String bpElementId);

    public Set<String> ppiSameElements(String ppiId);
    public Set<String> ppiSubsetElements(String ppiId);
    public Set<String> ppiSupersetElements(String ppiId);
}
