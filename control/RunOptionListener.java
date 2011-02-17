package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import view.View;

import model.Model;

public class RunOptionListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		if( e.getSource().getClass().equals(JMenuItem.class) ) {
			JMenuItem item = (JMenuItem) e.getSource();
			String command = item.getActionCommand();
			
			if( command.equals("Optimize") ) {
				optimize();
			}
		} else if( e.getSource().getClass().equals(JButton.class) ) {
			JButton button = (JButton) e.getSource();
			String command = button.getActionCommand();
			
			if( command.startsWith("See SCN") ) {
				String scnFileName = "model";
				View.getView().setText("");
				Model.getModel().createSCN(scnFileName, true);
			} else if( command.equals("Optimize") ) {
				optimize();
			}

		}
	}

	private void optimize() {
		String scnFileName = "model";
		String zimplFileName = "model";
		String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String outputFileName = zimplFileName + "-" + date + ".out";
		
		View.getView().setText("");
		if( Model.getModel().createSCN(scnFileName, false) ) {
			if( Model.getModel().executeZIMPL("src/model/parser/model.zpl", scnFileName+".scn") ) {
				if( Model.getModel().executeSCIP(zimplFileName+".lp", outputFileName) ) {
					if( Model.getModel().readSolutionFromSCIP(outputFileName) ) {
						View.getView().showMessage("Solution available.");
						View.getView().repaint();
					}
				}
			}
		}
	}
}
