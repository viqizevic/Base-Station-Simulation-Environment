package model;

/**
 * The model of the simulation environment.
 * @author vicky
 *
 */
public class Model {

	/**
	 * The static model object.
	 */
	private static Model model = new Model();

	/**
	 * An internal id counter for the objects of the graph.
	 */
    private Long internalIdCounter;

    /**
     * The simulation map.
     */
    private SimulationMap simulationMap;

    /**
     * Construct the model.
     */
	public Model() {
		internalIdCounter = 0L;
	}

	/**
	 * Get the static model object.
	 * @return
	 */
	public static Model getModel() {
		return model;
	}

	/**
	 * Get new id (a number from the type {@link Long}).
	 * @return A number from the type {@link Long}.
	 */
	public Long getNewId() {
		return internalIdCounter++;
	}

	/**
	 * Create a simulation map of the model.
	 * @param numberOfBaseStations The number of the base stations.
	 * @param numberOfUsers The number of the users.
	 * @return The simulation map created.
	 */
	public SimulationMap createSimulationMap( int numberOfBaseStations, int numberOfUsers ) {
		simulationMap = new SimulationMap(numberOfBaseStations, numberOfUsers);
		return simulationMap;
	}

	/**
	 * Get the simulation map of the model.
	 * @return The Simulation map.
	 * If no simulation map created yet, create a new one with 16 base stations
	 * and 32 users.
	 */
	public SimulationMap getSimulationMap() {
		if( simulationMap == null ) {
			createSimulationMap(16, 32);
		}
		return simulationMap;
	}
}