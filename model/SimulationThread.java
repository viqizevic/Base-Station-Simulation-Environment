package model;

public class SimulationThread implements Runnable {

	private volatile boolean shallRun;
	
	public SimulationThread() {
		shallRun = true;
	}
	
	public void run() {
		while( shallRun ) {
			// TODO add simulation process
		}
	}
	
	public void shouldStop() {
		shallRun = false;
	}
}
