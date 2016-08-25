package es.us.isa.ppinot.model.condition;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
    @JsonSubTypes.Type(value = TimeInstantCondition.class, name = "TimeInstantCondition")
})
public interface Condition {

    /**
     *
     * @return Returns the id of the element to which the condition applies
     */
    String getAppliesTo();

}
