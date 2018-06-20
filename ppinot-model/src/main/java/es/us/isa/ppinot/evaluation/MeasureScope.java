package es.us.isa.ppinot.evaluation;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author feserafim
 */
public interface MeasureScope {

    String getProcessId();

    Set<String> getInstances();

    Map<String, Object> getScopeInfo();

    boolean equivalentTo(MeasureScope scope);

    boolean isContainedIn(MeasureScope scope);
}
