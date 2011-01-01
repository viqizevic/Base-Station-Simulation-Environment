package model;

public class Model {

	private static Model model = new Model();
	
    private Long internalIdCounter;
    
    private SimulationMap simulationMap;
    
	public Model() {
		internalIdCounter = 0L;
	}
	
	public static Model getModel() {
		return model;
	}
	
	public Long getNewId() {
		return internalIdCounter++;
	}
	
	public SimulationMap createSimulationMap( int numberOfBaseStations, int numberOfUsers ) {
		simulationMap = new SimulationMap(numberOfBaseStations, numberOfUsers);
		return simulationMap;
	}
	
	public SimulationMap getSimulationMap() {
		if( simulationMap == null ) {
			createSimulationMap(16, 32);
		}
		return simulationMap;
	}
}