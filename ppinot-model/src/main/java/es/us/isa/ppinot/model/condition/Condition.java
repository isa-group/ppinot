package es.us.isa.ppinot.model.condition;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "kind")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DataPropertyCondition.class, name = "DataPropertyCondition"),
    @JsonSubTypes.Type(value = StateCondition.class, name = "StateCondition"),
    @JsonSubTypes.Type(value = TimeInstantCondition.class, name = "TimeInstantCondition"),
    @JsonSubTypes.Type(value = TimeMeasureType.class, name = "TimeMeasureType")
})
public interface Condition {

    /**
     *
     * @return Returns the id of the element to which the condition applies
     */
    String getAppliesTo();

}
