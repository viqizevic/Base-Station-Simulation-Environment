package view;

import java.awt.Point;

import javax.swing.JOptionPane;

import model.Model;

public class View {

	/**
	 * The view object.
	 */
	private static View view = new View();

	/**
	 * The window that shows the graph.
	 */
	private Window window;

	public View() {
		window = new Window("Base stations model", Model.getModel().getSimulationMap());
		window.setLocationRelativeTo( null );
		window.pack();
		window.setVisible(true);
	}

	/**
	 * Get the view object.
	 * @return The view object.
	 */
	public static View getView() {
		return view;
	}

	/**
	 * Initialize the layout.
	 * This command should be called at the first time to start initializing the layout.
	 */
	public void initialize() {}

	/**
	 * Show message in a new frame.
	 * @param text The message.
	 */
	public void showMessage( String text ) {
		JOptionPane.showMessageDialog( window, text );
	}

	public int getWidthOfEachFieldInCanvas() {
		return window.getWidthOfEachFieldInCanvas();
	}

	public int getHeightOfEachFieldInCanvas() {
		return window.getHeightOfEachFieldInCanvas();
	}

	public Point getOriginOfTheMapInCanvas() {
		return window.getOriginOfTheMapInCanvas();
	}
}
