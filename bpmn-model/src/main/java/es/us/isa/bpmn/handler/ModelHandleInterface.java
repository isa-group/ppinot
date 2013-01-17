package es.us.isa.bpmn.handler;

import java.io.InputStream;
import java.io.OutputStream;

public interface ModelHandleInterface {

	public String getProcId();
	
	public void load(String path, String file) throws Exception;
    public void load(InputStream stream) throws Exception;
	public void save(String path, String file, String procId) throws Exception;
	public void save(OutputStream stream, String procId) throws Exception;
}
