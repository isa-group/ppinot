package es.us.isa.ppinot.handler.transformation;

import es.us.isa.ppinot.handler.transformation.model.XMLToDataMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.xml.TPpiset;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;

/**
 * XMLToDataMeasureTest
 *
 * @author resinas
 */
public class XMLToDataMeasureTest extends XMLToMeasureAbstract {

    @Test
    public void shouldCreateDataMeasure() throws JAXBException {
        String dataMeasureXML = "<ppiset xmlns=\"http://www.isa.us.es/ppinot\"><dataMeasure id='dataId' name='data'/>" +
                "<appliesToDataConnector sourceRef='dataId' targetRef='xxx' dataContentSelection='sel' restriction='r'/></ppiset>";

        TPpiset tPpiset = unmarshallWithTargetIds(dataMeasureXML, "xxx");
        XMLToDataMeasure conversor = new XMLToDataMeasure();
        DataMeasure dm = (DataMeasure) conversor.create(tPpiset.getMeasure().get(0).getValue(), new ConnectorsHandler(tPpiset.getMeasureConnector()));

        Assert.assertEquals("dataId", dm.getId());
        Assert.assertEquals("data", dm.getName());
        Assert.assertEquals("sel", dm.getDataContentSelection().getSelection());
        Assert.assertEquals("r", ((DataPropertyCondition) dm.getPrecondition()).getRestriction());
        Assert.assertEquals("xxx", dm.getPrecondition().getAppliesTo());
    }

}
