package es.us.isa.ppinot.model;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.joda.time.DateTime;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class Holidays {
    
    @JsonDeserialize(as=ArrayList.class, contentAs=DateTime.class)
    private List<DateTime> list;
    
    public List<DateTime> getList() {
        return this.list;
    }
    
    public void setList(List<DateTime> list) {
        this.list = list;
    }

}
