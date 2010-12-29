package control;

import view.View;

public class Control {
	
	private static Control control = new Control();
	
	public Control() {
	}
	
	public void initialize() {
		View.getView().initialize();
	}

	public static Control getControl() {
		return control;
	}
}
