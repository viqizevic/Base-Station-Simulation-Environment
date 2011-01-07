package control;

import model.Model;
import view.View;

public class Control {
	
	private static Control control = new Control();
	
	private CanvasMouseListener canvasMouseListener;
	
	public Control() {
		canvasMouseListener = new CanvasMouseListener();
	}
	
	public void initialize() {
		Model.getModel().createSimulationMap(7, 14);
		System.out.println( Model.getModel().getSimulationMap() );
		View.getView().initialize();
	}

	public static Control getControl() {
		return control;
	}

	public CanvasMouseListener getCanvasMouseListener() {
		return canvasMouseListener;
	}
}
