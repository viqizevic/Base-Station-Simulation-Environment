package model.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Vertex of the {@link Graph}.
 * @author vicky
 *
 */
public class Vertex {
	
    //edges in
    private HashMap<Key, Edge> ingoingEdges = new HashMap<Key, Edge>();
    //edges out
    private HashMap<Key, Edge> outgoingEdges = new HashMap<Key, Edge>();
    //vertex attributes
    private HashMap<Key, Attribute> attributes = new HashMap<Key, Attribute>();
    //Key object for hashing
    private final Key key;
    
    public Vertex() {
        this.key = new Key();
    }

    public void removeEdge(Edge e) {
        ingoingEdges.remove(e.getKey());
        outgoingEdges.remove(e.getKey());
    }

    public void removeIngoingEdge(Edge e) {
        ingoingEdges.remove(e.getKey());
    }

    public void removeOutgoingEdge(Edge e) {
        outgoingEdges.remove(e.getKey());
    }

    public void addIngoingEdge(Edge e) {
        ingoingEdges.put(e.getKey(), e);
    }

    public void addOutgoingEdge(Edge e) {
        outgoingEdges.put(e.getKey(), e);
    }

    public Attribute getAttribute(Key k) {
        if (!attributes.containsKey(k)) {
            attributes.put(k, new Attribute());
        }
        return (attributes.get(k));
    }

    public Attribute removeAttribute(Key k) {
        return (attributes.remove(k));
    }

    public Collection<Edge> getIngoingEdges() {
        return (this.ingoingEdges.values());
    }

    public Collection<Edge> getOutgoingEdges() {
        return (this.outgoingEdges.values());
    }

    public Key getKey() {
        return (this.key);
    }
    
    public String toString() {
    	String output = "node: " +
    			"* "+key.toString() + "\n";
    	int k=0;
    	String[] attrString = new String[attributes.size()];
    	for( Attribute attr : attributes.values() ) {
    		attrString[k] = attr.getDescription() + "!!!" + attr.toString();
    		k++;
    	}
    	Arrays.sort(attrString);
    	for( k=0; k<attrString.length; k++ ) {
    		output += "*"+k+" "+attrString[k].split("!!!")[1]+"\n";
    	}
    	if( Graph.debugMode ) {
    		k=0;
    		String[] edgeStrings = new String[outgoingEdges.size()];
    		for( Edge e : outgoingEdges.values() ) {
    			edgeStrings[k] = e.toString();
    			k++;
    		}
    		Arrays.sort(edgeStrings);
    		for( k=0; k<edgeStrings.length; k++ ) {
    			output += "*"+k+" "+edgeStrings[k]+"\n";
    		}
    	}
    	return output;
    }
    
    public String getAttributesInString() {
    	String output = "";
    	int k=0;
    	String[] attrString = new String[attributes.size()];
    	for( Attribute attr : attributes.values() ) {
    		attrString[k] = attr.getDescription() + "!!!" + attr.toString();
    		k++;
    	}
    	Arrays.sort(attrString);
    	for( k=0; k<attrString.length; k++ ) {
    		output += "* "+attrString[k].split("!!!")[1]+"\n";
    	}
    	return output;
    }
}