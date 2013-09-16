package es.us.isa.bpms.repository;

/**
 * ModelUUID
 *
 * @author resinas
 */
public class ModelUUID {
    public static String generate(){
        return "isa-" + java.util.UUID.randomUUID().toString();
    }
}
