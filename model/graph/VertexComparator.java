package model.graph;

import java.util.Comparator;

public class VertexComparator implements Comparator<Vertex> {

	public int compare(Vertex o1, Vertex o2) {
		String id1 = o1.getId();
		String id2 = o2.getId();
		int val1 = Integer.parseInt(id1);
		int val2 = Integer.parseInt(id2);
		if( val1 > val2 ) {
			return 1;
		} else if( val1 < val2 ) {
			return -1;
		} else {
			return 0;
		}
	}

}
