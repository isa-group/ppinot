package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.GenericState;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TimeInstantMatcherTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeInstantMatcherTest {
    @Test
    public void testMatches() {
        LogEntryHelper helper = new LogEntryHelper();
        TimeInstantCondition condition = new TimeInstantCondition("Analyse RFC", GenericState.START);
        TimeInstantMatcher matcher = new TimeInstantMatcher(condition);

        assertTrue(matcher.matches(helper.newAssignEntry("Analyse RFC")));
        assertFalse(matcher.matches(helper.newAssignEntry("Approve RFC")));
        assertFalse(matcher.matches(helper.newCompleteEntry("Analyse RFC")));
    }

    @Test
    public void testWithPreconditionMatches() {
        LogEntryHelper helper = new LogEntryHelper();
        TimeInstantCondition condition = new TimeInstantCondition("Analyse RFC", GenericState.START, new DataPropertyCondition("Provider", "provider == 'p1'"));
        TimeInstantMatcher matcher = new TimeInstantMatcher(condition);

        assertTrue(matcher.matches(helper.newAssignEntry("Analyse RFC").withData("provider", "p1")));
        assertFalse(matcher.matches(helper.newAssignEntry("Analyse RFC").withData("provider", "p2")));

    }

}
