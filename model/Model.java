package model;

public class Model {

	private static Model model = new Model();
	
    private Long internalIdCounter;
    
    private GraphInstance graphInstance;
    
	public Model() {
		internalIdCounter = 0L;
	}
	
	public static Model getModel() {
		return model;
	}
	
	public Long getNewId() {
		return internalIdCounter++;
	}
	
	public GraphInstance createGraphInstance( int numberOfBaseStations, int numberOfUsers ) {
		graphInstance = new GraphInstance(numberOfBaseStations, numberOfUsers);
		return graphInstance;
	}
}