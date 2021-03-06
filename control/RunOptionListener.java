package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import view.View;

import model.Model;

public class RunOptionListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if( command.equals("Optimize") ) {
			optimize();
		} else if( command.startsWith("See SCN") ) {
			String scnFileName = "model";
			View.getView().setText("");
			Model.getModel().createSCN(scnFileName, true);
//		} else if( command.startsWith("Max coops") ) {
//			optimizeOverGamma();
		}
	}

	/**
	 * optimize
	 * @return true if there is a solution found, else otherwise
	 */
	private boolean optimize() {
		String scnFileName = "model";
		String zimplFileName = "model";
		String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String outputFileName = "output/"+zimplFileName + "-" + date + ".out";
		
		View.getView().setText("");
		if( Model.getModel().createSCN(scnFileName, false) ) {
			if( Model.getModel().executeZIMPL("src/model/parser/model.zpl", scnFileName+".scn") ) {
				if( Model.getModel().executeSCIP(zimplFileName+".lp", outputFileName) ) {
					Model.getModel().getSimulationMap().clearAssignmentAndConnectionFromAllEdges();
					if( Model.getModel().readSolutionFromSCIP(outputFileName) ) {
						View.getView().repaint();
						return true;
					}
				}
			}
		}
		return false;
	}
	
//	private void optimizeOverGamma() {
//		double gamma = 1.0E-7;
//		boolean optimal;
//		do{
//			Model.getModel().setGamma(gamma);
//			optimal = optimize();
//			View.getView().appendText("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//			View.getView().appendText("GAMMA: " + gamma);
//			gamma = gamma/12;
//		} while( !optimal );
//	}
}
