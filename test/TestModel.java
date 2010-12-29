package test;

import model.GraphInstance;
import model.Model;

public class TestModel {
	
	public static void main( String[] args ) {
		
		GraphInstance inst = Model.getModel().createGraphInstance(4,4);
		System.out.println( inst.toString() );
	}
}