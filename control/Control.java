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
	
	private MoveObjectActionListener moveObjectActionListener;
	
	private EditObjectActionListener editObjectActionListener;

	/**
	 * Construct the controller.
	 */
	public Control() {
		canvasMouseListener = new CanvasMouseListener();
		runOptionListener = new RunOptionListener();
		moveObjectActionListener = new MoveObjectActionListener();
		editObjectActionListener = new EditObjectActionListener();
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
	
	public MoveObjectActionListener getMoveObjectActionListener() {
		return moveObjectActionListener;
	}

	public EditObjectActionListener getEditObjectActionListener() {
		return editObjectActionListener;
	}

	public void loadFile() {
		String file = View.getView().loadFile();
		if( !file.isEmpty() ) {
			Model.getModel().loadFile(file);
			View.getView().reloadTheMap();
		}
	}
}
