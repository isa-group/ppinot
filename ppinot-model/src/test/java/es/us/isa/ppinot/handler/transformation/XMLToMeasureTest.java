package es.us.isa.ppinot.handler.transformation;

import com.sun.xml.bind.IDResolver;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.ppinot.xml.ObjectFactory;
import es.us.isa.ppinot.xml.TPpiset;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * XMLToMeasureTest
 *
 * @author resinas
 */
public class XMLToMeasureTest {
    protected TPpiset unmarshallWithTargetIds(String dataMeasureXML, final String... id) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        final List<String> bpId = Arrays.asList(id);
        unmarshaller.setProperty(IDResolver.class.getName(), new IDResolver() {
            private Map<String, Object> ids = new HashMap<String, Object>();

            @Override
            public void bind(String s, Object o) throws SAXException {
                ids.put(s, o);
            }

            @Override
            public Callable<?> resolve(final String s, Class aClass) throws SAXException {
                if (bpId.contains(s)) {
                    return new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            TDataObject d = new TDataObject();
                            d.setId(s);
                            return d;
                        }
                    };
                } else {
                    return new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            return ids.get(s);
                        }
                    };
                }
            }
        });


        JAXBElement<TPpiset> elem = (JAXBElement<TPpiset>) unmarshaller.unmarshal(new StringReader(dataMeasureXML));
        return elem.getValue();
    }
}
