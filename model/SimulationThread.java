package model;

import model.graph.Edge;

public class SimulationThread implements Runnable {

	private volatile boolean shallRun;
	
	public SimulationThread() {
		shallRun = true;
	}
	
	public void run() {
		SimulationMap map = Model.getModel().getSimulationMap();
		for( User u : map.getUsers() ) {
			for( BaseStation bs : map.getBasestations() ) {
				int manhattanDistance = Model.getModel().computeManhattanDistance(
						map.getVertexCoordinates(u.getKey()), map.getVertexCoordinates(bs.getKey()));
				if( manhattanDistance <= 2 ) {
					for( Edge e : u.getOutgoingEdges() ) {
						if( e.getTail().equals(bs) ) {
							e.getAttribute(map.getAssignmentKey()).setWeight(true);
						}
					}
				}
			}
		}
		while( shallRun ) {
			// TODO add simulation process
		}
	}
	
	public void shouldStop() {
		shallRun = false;
	}
}
