package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JMenuItem;

import view.View;

import model.Model;

public class RunOptionListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		String scnFileName = "model";
		String zimplFileName = "model";
		String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String outputFileName = zimplFileName + "-" + date + ".out";
		JMenuItem item = (JMenuItem) e.getSource();
		String command = item.getActionCommand();
		if( command.equals("Optimize") ) {
			View.getView().setText("");
			Model.getModel().createSCN(scnFileName);
			Model.getModel().executeZIMPL("src/model/zimpl/model.zpl", scnFileName+".scn");
			Model.getModel().executeSCIP(zimplFileName+".lp", outputFileName);
			Model.getModel().readSolutionFromSCIP(outputFileName);
			View.getView().refresh();
		}
	}

}
