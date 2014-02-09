package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.GenericState;
import junit.framework.Assert;
import org.junit.Test;

/**
 * TimeInstantMatcherTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeInstantMatcherTest {
    @Test
    public void testMatches() throws Exception {
        LogEntryHelper helper = new LogEntryHelper();
        TimeInstantCondition condition = new TimeInstantCondition("Analyse RFC", GenericState.START);
        TimeInstantMatcher matcher = new TimeInstantMatcher(condition);

        Assert.assertTrue(matcher.matches(helper.newAssignEntry("Analyse RFC")));
        Assert.assertFalse(matcher.matches(helper.newAssignEntry("Approve RFC")));
        Assert.assertFalse(matcher.matches(helper.newCompleteEntry("Analyse RFC")));
    }

}
