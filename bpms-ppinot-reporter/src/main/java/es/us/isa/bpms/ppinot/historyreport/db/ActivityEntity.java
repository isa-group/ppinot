package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.Date;

/**
 * Entidad con la informacion de una actividad 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public class ActivityEntity  {

	// id de la actividad en la BD
	private int ida;
	// id de los eventos de la actividad en la BD
	private int ide;
	// id del proceso en la plataforma BPM
	private String processDefId;
	// id de la intancia de proceso
	private String instanceId;
    // id de la ejecucion
	private String executionId;
	// id de la actividad
	private String activityId;
	// nombre de la actividad
	private String activityName;
	// tipo de la actividad
	private String activityType;
	// momento de inicio
	private Date startTime;
	// momento en que finalizo
	private Date endTime;
	
	/**
	 * Constructor de la clase
	 */
	public ActivityEntity() {
		
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param executionId Id de la ejecucion
	 * @param activityId Id de la actividad
	 * @param activityName Nombre de la actividad
	 * @param activityType Tipo de la actividad
	 * @param startTime Momento de inicio
	 * @param endTime Momento en que finalizo
	 */
	public ActivityEntity(String processDefId, String instanceId, String executionId, String activityId, String activityName, String activityType, Date startTime, Date endTime) {
		
        this.setProcessDefId(processDefId);
        this.setInstanceId(instanceId);
        this.setExecutionId(executionId);
        this.setActivityId(activityId);
        this.setActivityName(activityName);
        this.setActivityType(activityType);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
	}

	public int getIda() {
		return this.ida;
	}

	public void setIda(int ida) {
		this.ida = ida;
	}

	public int getIde() {
		return this.ide;
	}

	public void setIde(int ide) {
		this.ide = ide;
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

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getExecutionId() {
		return this.executionId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityId() {
		return this.activityId;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityName() {
		return this.activityName;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityType() {
		return this.activityType;
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
