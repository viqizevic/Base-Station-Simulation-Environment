package test;

import model.SimulationMap;
import model.Model;

public class TestModel {
	
	public static void main( String[] args ) {
		
		SimulationMap sf = Model.getModel().createSimulationMap(7,16);
		System.out.println( sf );
		
	}
}