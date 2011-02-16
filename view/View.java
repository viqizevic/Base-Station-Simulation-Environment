package view;

import java.awt.Point;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Model;
import model.graph.Vertex;

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
	 * Call this command to show the GUI.
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
		return window.getSimulationMapCanvas().getWidthOfEachField();
	}

	public int getHeightOfEachFieldInCanvas() {
		return window.getSimulationMapCanvas().getHeightOfEachField();
	}

	public Point getOriginOfTheMapInCanvas() {
		return window.getSimulationMapCanvas().getOriginOfTheMap();
	}
	
	public void highlightVertex( Vertex v ) {
		window.getSimulationMapCanvas().highlightVertex(v);
	}
	
	public void setText( String text ) {
		window.setText(text);
	}

	public void appendText( String text ) {
		window.appendText(text);
	}
	
	public void refresh() {
		window.getSimulationMapCanvas().repaint();
	}
	
	public String loadFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int state = fileChooser.showOpenDialog(null);
		
		if(state == JFileChooser.APPROVE_OPTION){
			return fileChooser.getSelectedFile().getPath();
		}
		return "";
	}
}
