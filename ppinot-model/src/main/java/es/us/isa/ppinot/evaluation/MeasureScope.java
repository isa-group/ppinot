package es.us.isa.ppinot.evaluation;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author feserafim
 */
public interface MeasureScope {

    String getProcessId();

    Collection<String> getInstances();

    Map<String, Object> getScopeInfo();

}
