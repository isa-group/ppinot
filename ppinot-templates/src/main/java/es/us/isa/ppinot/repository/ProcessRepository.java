package es.us.isa.ppinot.repository;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 09:28
 */
public interface ProcessRepository {
    public List<String> listProcesses();
    public InputStream getProcessReader(String id);
}
