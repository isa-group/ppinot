package es.us.isa.bpms.model;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

/**
 * ModelTest
 *
 * @author resinas
 */
public class ModelTest {
    @Test
    public void testCreateLinks() {
        Model bpmnModel = new Model("mid", "name", Model.BPMN20);
        Model orgModel = new Model("mid", "name", Model.ORG);
        Map<String, URI> links = bpmnModel.createLinks(UriBuilder.fromPath("/"));

        Assert.assertEquals("/service/editor?id=mid", links.get("editor").toString());
        Assert.assertEquals("/ppi-template.html#/mid", links.get("View PPIs").toString());

        links = orgModel.createLinks(UriBuilder.fromPath("/"));

        Assert.assertEquals("/organizational.html#/mid", links.get("editor").toString());
    }

    @Test public void testCreateExports() {
        Model orgModel = new Model("mid", "name", Model.ORG);
        Map<String, URI> exports = orgModel.createExports(UriBuilder.fromPath("/"));

        Assert.assertEquals("/service/model/mid/json", exports.get("JSON").toString());
    }
}
