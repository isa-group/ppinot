package es.us.isa.ppinot.evaluation.selectors;

import es.us.isa.ppinot.evaluation.LogEntryHelper;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.PreviousDataStateTransformer;
import es.us.isa.ppinot.model.PastDataContentSelection;
import org.junit.Assert;
import org.junit.Test;

/**
 * PastDataContentSelectorTest
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class PastDataContentSelectorTest {
    @Test
    public void testSelect() {
        LogEntryHelper helper = new LogEntryHelper(10).asLogProvider();
        PreviousDataStateTransformer transformer = new PreviousDataStateTransformer();
        PastDataContentSelector selector = new PastDataContentSelector(new PastDataContentSelection("Priority", ""));

        LogEntry l1 = transformer.transform(helper.newAssignEntry("Analyse RFC", "i1").withData("Priority", "P1"));
        LogEntry l2 = transformer.transform(helper.newCompleteEntry("Analyse RFC", "i1").withData("Priority", "P1"));
        LogEntry l3 = transformer.transform(helper.newAssignEntry("Approve RFC", "i1").withData("Priority", "P1"));
        LogEntry l4 = transformer.transform(helper.newCompleteEntry("Approve RFC", "i1").withData("Priority", "P2"));

        Assert.assertEquals("null-P1", selector.select(l1));
        Assert.assertEquals("P1-P1", selector.select(l2));
        Assert.assertEquals("P1-P1", selector.select(l3));
        Assert.assertEquals("P1-P2", selector.select(l4));
    }

}