package es.us.isa.ppinot.diagram2xml;

import de.hpi.bpmn2_0.annotations.StencilId;
import es.us.isa.ppinot.diagram2xml.factory.ConnectorFactory;
import es.us.isa.ppinot.diagram2xml.factory.MeasureFactory;
import es.us.isa.ppinot.diagram2xml.factory.PpiFactory;
import org.oryxeditor.server.diagram.generic.GenericShape;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PPINotFactory
 *
 * @author resinas
 */
public class PPINotFactory {
    private Collection<?> factories;
    private Map<String, ObjectMethod> builders;

    public PPINotFactory() {
        this(Arrays.asList(new MeasureFactory(), new PpiFactory(), new ConnectorFactory()));
    }

    public PPINotFactory(Collection<?> factories) {
        this.factories = factories;
        this.builders = new HashMap<String, ObjectMethod>();

        for (Object o : factories) {
            addFactory(o);
        }
    }

    private void addFactory(Object o) {
        for (Method m : o.getClass().getMethods()) {
            StencilId stencilId = m.getAnnotation(StencilId.class);
            if (stencilId != null) {
                for (String s : stencilId.value()) {
                    builders.put(s, new ObjectMethod(o, m));
                }
            }
        }
    }

    public JAXBElement<?> createElement(GenericShape<?, ?> shape) {
        ObjectMethod b = builders.get(shape.getStencilId());
        if (b == null) {
            return null;
        }

        try {
            return (JAXBElement<?>) b.method.invoke(b.object, shape);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private class ObjectMethod {
        public Object object;
        public Method method;

        private ObjectMethod(Object object, Method method) {
            this.object = object;
            this.method = method;
        }
    }

}
