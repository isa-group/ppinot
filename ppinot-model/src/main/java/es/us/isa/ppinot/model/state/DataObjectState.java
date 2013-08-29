package es.us.isa.ppinot.model.state;

/**
 * DataObjectState
 *
 * @author resinas
 */
public class DataObjectState implements RuntimeState{
    private String name;

    public DataObjectState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
