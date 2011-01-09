package test;

import model.BaseStation;
import model.SimulationMap;
import model.Model;
import model.User;

public class TestModel {
	
	public static void main( String[] args ) {
		
		SimulationMap map = Model.getModel().createSimulationMap(7,16);
		System.out.println( map );
		
		for( BaseStation bs : map.getBasestations() ) {
			System.out.println(bs.toString());
		}
		
		for( User u : map.getUsers() ) {
			System.out.println(u.toString());
		}
	}
}