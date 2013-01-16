package es.us.isa.bpmn.handler;

import java.io.InputStream;
import java.io.OutputStream;

import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;

public interface ModelHandleInterface {

	public abstract TProcess getProcess();
	
	public abstract void load(String path, String file) throws Exception;
    public abstract void load(InputStream stream) throws Exception;
	public abstract void save(String path, String file, String procId) throws Exception;
	public abstract void save(OutputStream stream, String procId) throws Exception;
}
