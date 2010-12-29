
package model;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

import model.graph.Graph;
import model.graph.Key;

public class GraphInstance extends Graph {
    
    private HashMap<Key, BaseStation> basestations;
    
    private HashMap<Key, User> users;

    public GraphInstance( int numberOfBaseStations, int numberOfUsers ) {
    	super();
    	basestations = new HashMap<Key, BaseStation>();
    	users = new HashMap<Key, User>();
		setDirected(false);
		setEuclidian(true);
		setInteger(false);
		setSimple(true);
		for( int i=1; i <= numberOfBaseStations; i++ ) {
			BaseStation bs = new BaseStation();
			Point p = new Point(i, i);
			addVertex(bs, p);
			basestations.put(bs.getKey(), bs);
		}
		for( int i=1; i <= numberOfUsers; i++ ) {
			User u = new User();
			Random r = new Random();
			Point p = new Point( r.nextInt(10), r.nextInt(10) );
			addVertex(u, p);
			users.put(u.getKey(), u);
		}
		for( BaseStation bs : basestations.values() ) {
			for( User u : users.values() ) {
				addEdge(bs, u);
			}
		}
    }
}