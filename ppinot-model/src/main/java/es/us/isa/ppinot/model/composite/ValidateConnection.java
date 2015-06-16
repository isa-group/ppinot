package es.us.isa.ppinot.model.composite;

import java.util.List;

public class ValidateConnection {

	private List<String> unrealizedConnections;
	private int totalRequiredConnections;
	private int totalUnrealizedConnections;
	private boolean allMeasuresConnected;
	
	
	/**
	 * Constructor de la clase
	 */
	public ValidateConnection(){
		super();
		this.totalUnrealizedConnections = -1; //Indica error
		this.totalRequiredConnections = -1; //Indica error
		this.allMeasuresConnected = false;
		this.unrealizedConnections = null;
	}
	
	/**
	 * Constructor de la clase
	 * @param lsUnrealizedConnections
	 * @param iRequiredConnections
	 * @param iUnrealizedConnections
	 * @param bAllMeasuresConnected
	 */
	public ValidateConnection(List<String> unrealizedConnections,
			int iRequiredConnections, int iConexionesNoRealizadas,
			boolean bAllMeasuresConnected) {
		super();
		this.unrealizedConnections = unrealizedConnections;
		this.totalRequiredConnections = iRequiredConnections;
		this.totalUnrealizedConnections = iConexionesNoRealizadas;
		this.allMeasuresConnected = bAllMeasuresConnected;
	}

	/**
	 * @return the unrealizedConnections
	 */
	public List<String> getUnrealizedConnections() {
		return unrealizedConnections;
	}

	/**
	 * @param unrealizedConnections the unrealizedConnections to set
	 */
	public void setUnrealizedConnections(List<String> unrealizedConnections) {
		this.unrealizedConnections = unrealizedConnections;
	}

	/**
	 * @return the totalRequiredConnections
	 */
	public int getTotalRequiredConnections() {
		return totalRequiredConnections;
	}

	/**
	 * @param totalRequiredConnections the totalRequiredConnections to set
	 */
	public void setTotalRequiredConnections(int totalRequiredConnections) {
		this.totalRequiredConnections = totalRequiredConnections;
	}

	/**
	 * @return the totalUnrealizedConnections
	 */
	public int getTotalUnrealizedConnections() {
		return totalUnrealizedConnections;
	}

	/**
	 * @param totalUnrealizedConnections the totalUnrealizedConnections to set
	 */
	public void setTotalUnrealizedConnections(int totalUnrealizedConnections) {
		this.totalUnrealizedConnections = totalUnrealizedConnections;
	}

	/**
	 * @return the allMeasuresConnected
	 */
	public boolean isAllMeasuresConnected() {
		return allMeasuresConnected;
	}

	/**
	 * @param allMeasuresConnected the allMeasuresConnected to set
	 */
	public void setAllMeasuresConnected(boolean allMeasuresConnected) {
		this.allMeasuresConnected = allMeasuresConnected;
	}

	
	
	
	
	

	
	
	
}
