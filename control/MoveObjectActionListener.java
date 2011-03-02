package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.View;

import model.Direction;
import model.Model;
import model.User;

public class MoveObjectActionListener implements ActionListener {
	
	private User userToMove;

	public void actionPerformed(ActionEvent e) {
		if( userToMove == null ) {
			return;
		}
		String dir = e.getActionCommand();
		boolean moved = false;
		if( dir.equals("^") ) {
			moved = Model.getModel().getSimulationMap().moveUser(
					userToMove.getKey(), Direction.UP );
		} else if( dir.equals(">") ) {
			moved = Model.getModel().getSimulationMap().moveUser(
					userToMove.getKey(), Direction.RIGHT );
		} else if( dir.equals("v") ) {
			moved = Model.getModel().getSimulationMap().moveUser(
					userToMove.getKey(), Direction.DOWN );
		} else if( dir.equals("<") ) {
			moved = Model.getModel().getSimulationMap().moveUser(
					userToMove.getKey(), Direction.LEFT );
		}
		if( moved ) {
			Model.getModel().getSimulationMap().clearAssignmentAndConnectionFromAllEdges();
			View.getView().repaint();
		}
	}

	public void setUserToMove(User userToMove) {
		this.userToMove = userToMove;
	}

}
