package es.us.isa.bpms.repository;

import org.json.JSONException;

/**
 * Storeable
 *
 * @author resinas
 */
public interface Storeable {
    public String getJSON() throws JSONException;
}
