package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.Date;

/**
 * Entidad con la informacion de una propiedad de un dataobject en una instancia de proceso 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public class DataobjectEntity {

	// id en la BD
	private int id;
	// id del proceso en la plataforma BPM
	private String processDefId;
	// id de la intancia de proceso
	private String instanceId;
    // id de la ejecucion
	private String executionId;
	// id de la actividad
	private String activityId;
	// id del dataobject
	private String dataobjectId;
	// id de la propiedad
	private String property;
	// valor de la propiedad
	private String value;
	// tipo de la propiedad
	private String type;
	// momento en que se realizo el ultimo cambio
	private Date time;
	
	/**
	 * Constructor de la clase
	 */
	public DataobjectEntity() {
		
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param executionId Id de la ejecucion
	 * @param activityId Id de la actividad
	 * @param dataobjectId Id del dataobject
	 * @param property Id de la propiedad
	 * @param value Valor de la propiedad
	 * @param type Tipo de la propiedad
	 * @param time Momento en que se realizo el ultimo cambio
	 */
	public DataobjectEntity(String processDefId, String instanceId, String executionId, String activityId, String dataobjectId, String property, String value, String type, Date time) {
		
        this.setProcessDefId(processDefId);
        this.setInstanceId(instanceId);
        this.setExecutionId(executionId);
        this.setActivityId(activityId);
        this.setDataobjectId(dataobjectId);
        this.setProperty(property);
        this.setValue(value);
        this.setType(type);
        this.setTime(time);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String getDataobjectId() {
		return this.dataobjectId;
	}

	public void setDataobjectId(String dataobjectId) {
		this.dataobjectId = dataobjectId;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


}
