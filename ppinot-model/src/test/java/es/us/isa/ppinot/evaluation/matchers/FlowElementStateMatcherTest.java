package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.state.BPMNState;
import es.us.isa.ppinot.model.state.GenericState;
import junit.framework.Assert;
import org.junit.Test;

/**
 * StateMatcherTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class FlowElementStateMatcherTest {
    @Test
    public void testMatches() throws Exception {
        Assert.assertTrue(FlowElementStateMatcher.matches(LogEntry.EventType.assign, BPMNState.ACTIVE));
        Assert.assertTrue(FlowElementStateMatcher.matches(LogEntry.EventType.assign, GenericState.START));
        Assert.assertFalse(FlowElementStateMatcher.matches(LogEntry.EventType.complete, GenericState.START));
        Assert.assertTrue(FlowElementStateMatcher.matches(LogEntry.EventType.complete, GenericState.END));
    }
}
