package model;

import java.awt.Point;
import java.util.Random;

import view.View;

import model.SimulationMap.Field;
import model.SimulationMap.FieldUsageType;
import model.graph.Edge;

/**
 * TODO think over again, maybe this class should belong to control package
 * @author vicky
 */
public class SimulationThread extends Thread {

	private volatile boolean shallRun;
	
	public SimulationThread() {
		shallRun = true;  
	}
	
	public void run() {
		SimulationMap map = Model.getModel().getSimulationMap();
		Field[][] fields = map.getFieldsMatrix();
		while( shallRun ) {
			for( User u : map.getUsers() ) {
				Point p_u = map.getVertexCoordinates(u.getKey());
				Point p = new Point(p_u);
				Random rand = new Random();
				int n = rand.nextInt(4);
				if( n == 0 ) {
					if( p.y > 0 ) {
						p.y--;
					}
				} else if( n == 1 ) {
					if( p.x > 0 ) {
						p.x--;
					}
				} else if( n == 2 ) {
					if( p.x < fields[0].length-1 ) {
						p.x++;
					}
				} else {
					if( p.y < fields.length-1 ) {
						p.y++;
					}
				}
				if( fields[p.y][p.x].getFieldUsageType() == FieldUsageType.Empty ) {
					fields[p.y][p.x].setFieldUser(u);
					fields[p_u.y][p_u.x].setFieldUser(null);
					p_u.setLocation(p);
				}
			}
			for( User u : map.getUsers() ) {
				for( Edge e : u.getIngoingEdges() ) {
					e.getAttribute(map.getAssignmentKey()).setWeight(false);
				}
				double minDistance = Double.MAX_VALUE;
				BaseStation bsToRequest = null;
				for( BaseStation bs : map.getBasestations() ) {
					double euclidianDistance = Model.getModel().computeEuclidianDistance(
							map.getVertexCoordinates(u.getKey()), map.getVertexCoordinates(bs.getKey()));
					if( euclidianDistance < minDistance ) {
						minDistance = euclidianDistance;
						bsToRequest = bs;
					}
				}
				for( Edge e : u.getOutgoingEdges() ) {
					if( e.getTail().equals(bsToRequest) ) {
						e.getAttribute(map.getAssignmentKey()).setWeight(true);
					}
				}
			}
			View.getView().refresh();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void shouldStop() {
		shallRun = false;
	}
}
