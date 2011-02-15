package control;

import model.Model;
import view.View;

/**
 * The controller.
 * Preparing the listener for the user interface ({@link View}).
 * @author vicky
 *
 */
public class Control {

	/**
	 * The static controller object.
	 */
	private static Control control = new Control();

	/**
	 * The mouse listener for the canvas.
	 */
	private CanvasMouseListener canvasMouseListener;
	
	private RunOptionListener runOptionListener;

	/**
	 * Construct the controller.
	 */
	public Control() {
		canvasMouseListener = new CanvasMouseListener();
		runOptionListener = new RunOptionListener();
	}

	/**
	 * Initialize the {@link View} and the {@link Model}.
	 */
	public void initialize() {
		View.getView().initialize();
	}

	/**
	 * Get the static controller object.
	 * @return The controller.
	 */
	public static Control getControl() {
		return control;
	}

	/**
	 * Get the mouse listener for the canvas.
	 * @return The mouse listener.
	 */
	public CanvasMouseListener getCanvasMouseListener() {
		return canvasMouseListener;
	}

	public RunOptionListener getRunOptionListener() {
		return runOptionListener;
	}
}
