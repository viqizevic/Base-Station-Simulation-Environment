package model;

public class Model {

	private static Model model = new Model();
	
    private Long internalIdCounter;
    
	public Model() {
		internalIdCounter = 0L;
	}
	
	public static Model getModel() {
		return model;
	}
	
	public Long getNewId() {
		return internalIdCounter++;
	}
}