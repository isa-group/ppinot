package es.us.isa.ppinot.model.state;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 23:32
 */

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.WRAPPER_OBJECT, property = "kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GenericState.class, name = "genericState"),
        @JsonSubTypes.Type(value = BPMNState.class, name = "bpmnState"),
        @JsonSubTypes.Type(value = DataObjectState.class, name = "dataObjectState")
})
public interface RuntimeState {
}
