package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.Date;

/**
 * Entidad con la informacion de una instancia de proceso 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public class ProcessEntity {

	// id en la tabla bpms_process
	private int idp;
	// id en la tabla bpms_instance
	private int idi;
	// id del proceso en el diagrama
	private String processId;
	// id del proceso en la plataforma BPM
	private String processDefId;
	// id de la intancia de proceso
	private String instanceId;
	// momento de inicio
	private Date startTime;
	// momento en que finalizo
	private Date endTime;
	
	/**
	 * Constructor de la clase
	 */
	public ProcessEntity() {
		
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processId Id del proceso en el diagrama
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param startTime Momento de inicio
	 * @param endTime Momento en que finalizo
	 */
	public ProcessEntity(String processId, String processDefId, String instanceId, Date startTime, Date endTime) {
		
        this.setProcessId(processId);
        this.setProcessDefId(processDefId);
        this.setInstanceId(instanceId);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
	}

	public int getIdp() {
		return this.idp;
	}

	public void setIdp(int idp) {
		this.idp = idp;
	}

	public int getIdi() {
		return this.idi;
	}

	public void setIdi(int idi) {
		this.idi = idi;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessId() {
		return this.processId;
	}

	public String getProcessDefId() {
		return this.processDefId;
	}

	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}

	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
