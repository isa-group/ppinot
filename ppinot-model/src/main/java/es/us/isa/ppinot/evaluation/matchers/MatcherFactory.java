package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.condition.Condition;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author feserafim
 * @version 1.0
 */
public class MatcherFactory {

    public Matcher create(Condition condition) {
        Matcher matcher = null;

        if (condition instanceof DataPropertyCondition) {
            matcher = new DataPropertyMatcher((DataPropertyCondition) condition);
        } else if (condition instanceof StateCondition) {
            matcher = new StateConditionMatcher((StateCondition) condition);
        } else if (condition instanceof TimeInstantCondition) {
            matcher = new TimeInstantMatcher((TimeInstantCondition) condition);
        }

        return matcher;

    }
    
    public boolean matches(Matcher precondition, LogEntry entry) {
        
        boolean result = false;
        
        if (precondition instanceof DataPropertyMatcher) {
            result = ((DataPropertyMatcher) precondition).matches(entry);
        } else if (precondition instanceof StateConditionMatcher) {
            result = ((StateConditionMatcher) precondition).matches(entry);
        } else if (precondition instanceof TimeInstantMatcher) {
            result = ((TimeInstantMatcher) precondition).matches(entry);
        }
        
        return result;
        
    }

}
