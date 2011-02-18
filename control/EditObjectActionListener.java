package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Model;
import model.User;
import model.User.MSData;
import model.graph.Vertex;

public class EditObjectActionListener implements ActionListener {
	
	private Vertex vertexToEdit;
	
	private double[] values;

	public void actionPerformed(ActionEvent e) {
		if( vertexToEdit.getClass().equals(User.class) ) {
			MSData msData = (MSData) vertexToEdit.getAttribute(
					Model.getModel().getSimulationMap().getKeyOfUserDataAttribute()).getWeight();
			msData.setGamma( values[0] );
		}
	}
	
	public void setNewValues( Vertex v, double[] values ) {
		vertexToEdit = v;
		this.values = values;
	}
}
