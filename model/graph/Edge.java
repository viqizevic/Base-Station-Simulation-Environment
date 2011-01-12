package model.graph;

import java.util.HashMap;

import model.Model;

/**
 * Edge of the {@link Graph}.
 * @author vicky
 *
 */
public class Edge {

	/**
	 * The start vertex of this edge
	 */
    private Vertex head;
    
    /**
     * The end vertex of this edge
     */
    private Vertex tail;
    
    /**
     * All attributes this vertex has
     */
    private HashMap<Key, Attribute> attributes = new HashMap<Key, Attribute>();
    
    /**
     * Key object for hashing
     */
    private final Key key;
    
    /**
     * a counter edge reference if the graph is not directed
     */
    private Edge counter_edge;

    /**
     * Create a new edge with the unique id given from the Graph class
     * @param id
     */
    public Edge() {
        this.key = new Key(Model.getModel().getNewId());
    }

    public Vertex getHead() {
        return head;
    }

    public void setHead(Vertex head) {
        this.head = head;
    }

    public Vertex getTail() {
        return tail;
    }

    public void setTail(Vertex tail) {
        this.tail = tail;
    }

    /**
     * adds an Attribute object with a given Key object.
     * used only if graph is not directed to copy an Attribute reference.
     * @param a the given Attribute
     * @param k the given Key
     */
    public void addAttribute(Attribute a, Key k) {
        attributes.put(k, a);
        //TODO if graph undirected, copy attribute to the counter edge
    }

    /**
     * Get an attribute
     * @param k The key to this attribute
     * @return The attribute
     */
    public Attribute getAttribute(Key k) {
        if (!attributes.containsKey(k)) {
            attributes.put(k, new Attribute());
        }
        return (attributes.get(k));
    }

    public Attribute removeAttribute(Key k) {
        return (attributes.remove(k));
    }

    public Key getKey() {
        return (this.key);
    }

    public Edge getCounterEdge() {
        return (this.counter_edge);
    }

    public void setCounterEdge(Edge e) {
        this.counter_edge = e;
    }
    
    public String toString() {
    	String output = "edge: " + head.getKey().getId() + " -> " + tail.getKey().getId();
    	int k=1;
    	for( Attribute attr : attributes.values() ) {
    		output += " "+attr.toString();
    		k++;
    	}
    	return output;
    }
}
