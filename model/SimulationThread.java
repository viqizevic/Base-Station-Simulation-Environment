package model;

import model.graph.Edge;

public class SimulationThread extends Thread {

	private volatile boolean shallRun;
	
	public SimulationThread() {
		shallRun = true;
	}
	
	public void run() {
		SimulationMap map = Model.getModel().getSimulationMap();
		for( User u : map.getUsers() ) {
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
		while( shallRun ) {
			// TODO add simulation process
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void shouldStop() {
		shallRun = false;
	}
}
