package es.us.isa.bpms.repository;

import junit.framework.Assert;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Collections;

/**
 * SharedModelTest
 *
 * @author resinas
 */
public class SharedModelTest {
    @Test
    public void testRepresents() throws Exception {
        String modelId = "m1";
        String owner = "o1";
        String m2 = "m2";
        String o2 = "o2";
        SharedModel sharedModel = new SharedModel(modelId, owner);

        Assert.assertTrue(sharedModel.represents(modelId, owner));
        Assert.assertFalse(sharedModel.represents(m2, owner));
        Assert.assertFalse(sharedModel.represents(modelId, o2));
        Assert.assertFalse(sharedModel.represents(m2, o2));
    }

    @Test
    public void testIs() throws Exception {
        JSONObject jsonShared = new JSONObject(Collections.singletonMap("type", "shared"));
        JSONObject jsonOther = new JSONObject(Collections.singletonMap("type", "bpmn"));
        JSONObject jsonNone = new JSONObject();

        Assert.assertTrue(SharedModel.is(jsonShared));
        Assert.assertFalse(SharedModel.is(jsonOther));
        Assert.assertFalse(SharedModel.is(jsonNone));
    }
}
