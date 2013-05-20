package es.us.isa.bpms.process;

/**
 * User: resinas
 * Date: 10/04/13
 * Time: 23:04
 */
public class BPElementInfo {
    private String id;
    private String name;
    private String type;

    public BPElementInfo() {
    }

    public BPElementInfo(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
