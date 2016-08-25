package es.us.isa.ppinot.model.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 23:32
 */


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GenericState.class, name = "genericState"),
        @JsonSubTypes.Type(value = BPMNState.class, name = "bpmnState"),
        @JsonSubTypes.Type(value = DataObjectState.class, name = "dataObjectState"),
        @JsonSubTypes.Type(value = ComplexState.class, name = "complexState")
})
public interface RuntimeState {
}
