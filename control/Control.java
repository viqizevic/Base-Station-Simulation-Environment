package control;

import view.View;

public class Control {
	
	private static Control control = new Control();
	
	private CanvasMouseListener canvasMouseListener;
	
	public Control() {
		canvasMouseListener = new CanvasMouseListener();
	}
	
	public void initialize() {
		View.getView().initialize();
	}

	public static Control getControl() {
		return control;
	}

	public CanvasMouseListener getCanvasMouseListener() {
		return canvasMouseListener;
	}
}
