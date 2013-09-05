package es.us.isa.ppinot.model;


import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleTimeFilter.class, name = "SimpleTimeFilter"),
        @JsonSubTypes.Type(value = LastInstancesFilter.class, name = "LastInstancesFilter")
})
public abstract class ProcessInstanceFilter {


}
