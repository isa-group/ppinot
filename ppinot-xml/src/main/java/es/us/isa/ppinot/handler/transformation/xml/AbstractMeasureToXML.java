package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.xml.TMeasure;

import java.util.UUID;

/**
 * AbstractMeasureToXML
 *
 * @author resinas
 */
public abstract class AbstractMeasureToXML {
    protected void loadBaseAttributes(MeasureDefinition measure, TMeasure xml) {
        if (notEmpty(measure.getId())) {
            xml.setId(measure.getId());
        } else {
            xml.setId(createId());
        }

        xml.setName(measure.getName());

        if (notEmpty(measure.getDescription())) {
            xml.setDescription(measure.getDescription());
        }

        if (notEmpty(measure.getScale())) {
            xml.setScale(measure.getScale());
        }

        if (notEmpty(measure.getUnitOfMeasure())) {
            xml.setUnitOfMeasure(measure.getUnitOfMeasure());
        }
    }

    private boolean notEmpty(String s) {
        return s != null && ! s.isEmpty();
    }

    private String createId() {
        return UUID.randomUUID().toString();
    }
}
