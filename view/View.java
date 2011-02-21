package view;

import java.awt.Point;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Model;
import model.User;
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
		window = new Window("Base stations model");
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
	
	public void repaint() {
		window.repaint();
	}
	
	public void reloadTheMap() {
		window.reloadTheMap();
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
	
	public String showInputDialog() {
		String s = (String)JOptionPane.showInputDialog(
		                    window,
		                    "Edit the gamma value"
		                    + "\nfor all MS:",
		                    "Set gamma",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    Model.getModel().getGamma());
		if ((s != null) && (s.length() > 0)) {
		    return s;
		}
		return null;
	}
	
	public void showMoveObjectDialog( User user ) {
		@SuppressWarnings("unused")
		MoveObjectDialog mod = new MoveObjectDialog(window, user);
	}
	
	public void showEditObjectDialog( Vertex v ) {
		@SuppressWarnings("unused")
		EditObjectDialog eod = new EditObjectDialog(window, v);
	}
}
