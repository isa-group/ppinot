package es.us.isa.bpms.repository;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 09:28
 */
public interface ProcessRepository {
    public List<String> listProcesses();
    public InputStream getProcessReader(String id);
    public OutputStream getProcessWriter(String id);

    public Model getProcessModelInfo(String id);

    public List<String> listJsonProcesses();
    public InputStream getProcessJsonReader(String id);
    public OutputStream getProcessJsonWriter(String id);

    public boolean removeProcess(String id);

    public boolean addProcess(Model model);
}
