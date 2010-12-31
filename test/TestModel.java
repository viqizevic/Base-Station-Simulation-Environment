package test;

import model.GraphInstance;
import model.Model;
import model.SimulationMap;

public class TestModel {
	
	public static void main( String[] args ) {
		
		GraphInstance inst = Model.getModel().createGraphInstance(4,4);
		
		SimulationMap sf = new SimulationMap(7,10);
		System.out.println( sf );
		
	}
}