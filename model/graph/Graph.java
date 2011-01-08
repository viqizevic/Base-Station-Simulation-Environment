package model.graph;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import model.Model;

/**
 * Graph G=(V,E).
 * @author vicky
 *
 */
public class Graph {
	
	//flag for debug mode
    public static boolean debugMode = true;
    
    //some flags for graph type
    private boolean isDirected;
    private boolean isEuclidean;
    private boolean isInteger;

    //vertices
    private HashMap<Key, Vertex> vertices = new HashMap<Key, Vertex>();
    
    //vertex coordinates
    private HashMap<Key, Point> vertexCoordinates = new HashMap<Key, Point>();
    
    //edges
    private HashMap<Key, Edge> edges = new HashMap<Key, Edge>();

    // for fast access to attribute keys via their description or vice versa
    private Vector<Key> vertexAttributeKeys = new Vector<Key>();
    private Vector<String> vertexAttributeDescriptions = new Vector<String>();
    private Vector<Key> edgeAttributeKeys = new Vector<Key>();
    private Vector<String> edgeAttributeDescriptions = new Vector<String>();
    
    public Graph() {
    }

    /**
     * gets the current edge attributes keys available
     * @return a vector contains all keys of the attribute of the edges
     */
    public Vector<Key> getCurrentEdgeAttributeKeys() {
        return edgeAttributeKeys;
    }

    /**
     * gets the current vertex attributes keys available
     * @return a vector contains all keys of the attribute of the vertices
     */
    public Vector<Key> getCurrentVertexAttributeKeys() {
        return vertexAttributeKeys;
    }

    /**
     * returns if the given graph is directed
     * @return true if directed
     */
    public boolean isDirected() {
        return isDirected;
    }

    /**
     * sets if the given graph is directed
     * @param isDirected
     */
    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    /**
     * returns if the given graph is euclidean
     * @return true if it is
     */
    public boolean isEuclidean() {
        return isEuclidean;
    }

    /**
     * sets if the graph is euclidean
     * @param isEuclidean
     */
    public void setEuclidian(boolean isEuclidean) {
        this.isEuclidean = isEuclidean;
    }

    /**
     * returns if the graphs edges and vertices attributes are integer
     * @return true if yes
     */
    public boolean isInteger() {
        return isInteger;
    }

    /**
     * sets if the graphs edges and vertices attributes are integer
     * @param isInteger
     */
    public void setInteger(boolean isInteger) {
        this.isInteger = isInteger;
    }

    /**
     * adds a given Vertex object to the graph
     * @param v the given Vertex
     * @param p the coordinates of the Vertex
     */
    public void addVertex(Vertex v, Point p) {
    	vertices.put(v.getKey(), v);
    	vertexCoordinates.put(v.getKey(), p);
    }

    /**
     * gets a Vertex via its Key object
     * @param key the Key object
     * @return the desired Vertex
     */
    public Vertex getVertex(Key key) {
        return (vertices.get(key));
    }

    /**
     * gets a Vertex coordinate Point via its Key object
     * @param key the Key object
     * @return the desired Vertex coordinate Point object
     */
    public Point getVertexCoordinates(Key key) {
        return (this.vertexCoordinates.get(key));
    }

    /**
     * returns a Collection object of all vertices
     * (use .iterator() to get an Iterator) or for each loop
     * @return Collection of Vertex objects
     */
    public Collection<Vertex> getVertices() {
        return (this.vertices.values());
    }

    /**
     * returns a Vector object of all edges (no counter edges)
     * (use .iterator() to get an Iterator) or for each loop
     * @return Vector of Edge objects
     */
    public Vector<Edge> getEdges() {

        Vector<Edge> v = new Vector<Edge>();

        if (!this.isDirected) {

            Key key = this.addEdgeAttribute("temp_marker");

            for (Edge e : this.edges.values()) {
                if (e.getAttribute(key).getWeight() == null || !(Boolean) e.getAttribute(key).getWeight()) {
                    v.add(e);
                    e.getAttribute(key).setWeight(true);
                }
            }

            this.removeEdgeAttribute(key);
        } else {
            for (Edge e : this.edges.values()) {
                v.add(e);
            }
        }

        return (v);
    }

    /**
     * returns a Collection object of all vertex coordinates
     * (use .iterator() to get an Iterator)
     * @return Collection of Point objects
     */
    public Collection<Point> getVertexCoordinates() {
        return (this.vertexCoordinates.values());
    }
    
    /**
     * add an Edge object to the graph and locally to its head and tail
     * @param e the Edge to add
     */
    public void addEdge(Edge e) {

        //add the edge to the hashmap
        this.edges.put(e.getKey(), e);

        //add the Edge to the head and tail Vertex object
        e.getHead().addOutgoingEdge(e);
        e.getTail().addIngoingEdge(e);

        //if the graph is not directed, add the counter edge
        if (!this.isDirected) {

            Edge counter_edge = new Edge();
            e.setCounterEdge(counter_edge);
            counter_edge.setCounterEdge(e);
            counter_edge.setHead(e.getTail());
            counter_edge.setTail(e.getHead());

            //copy the attribute object references
            for (int j = 0; j < edgeAttributeKeys.size(); j++) {
            	Attribute eAttr = e.getAttribute(edgeAttributeKeys.elementAt(j));
            	Attribute newAttribute = new Attribute(eAttr.getWeight(),eAttr.getType());
                counter_edge.addAttribute(newAttribute,
                        edgeAttributeKeys.elementAt(j));
            }

            e.getCounterEdge().getHead().addOutgoingEdge(e.getCounterEdge());
            e.getCounterEdge().getTail().addIngoingEdge(e.getCounterEdge());
            
            this.edges.put(counter_edge.getKey(), counter_edge);
        }
    }
    
    /**
     * add an Edge object to the graph and locally to its head and tail
     * @param head The start vertex of the new edge
     * @param tail The end vertex of the new edge
     * @return The new edge
     */
    public Edge addEdge( Vertex head, Vertex tail ) {

    	Edge e = new Edge();
        e.setHead(head);
        e.setTail(tail);
        
        addEdge(e);
        return e;
    }

    /**
     * returns an Edge object via its Key Object
     * @param key the key of the object
     * @return the Edge object
     */
    public Edge getEdge(Key key) {
        return (edges.get(key));
    }
    
    /**
     * adds an Vertex Attribute
     * @param typeDescription a description of the Attribute
     * @return the Key to use for accessing the Attribute
     */
    public Key addVertexAttribute(String typeDescription) {
        Key k = new Key(Model.getModel().getNewId());
        vertexAttributeKeys.add(k);
        vertexAttributeDescriptions.add(typeDescription);
        return (k);
    }

    /**
     * removes a Vertex Attribute from all Vertex objects
     * @param k the Key of the Attribute to remove
     */
    public void removeVertexAttribute(Key k) {
        for (Vertex v : vertices.values()) {
            v.removeAttribute(k);
        }
        vertexAttributeDescriptions.remove(vertexAttributeKeys.indexOf(k));
        vertexAttributeKeys.remove(k);
    }

    /**
     * adds an Edge Attribute
     * @param typeDescription a description of the Attribute
     * @return the Key for accessing the Attribute object
     */
    public Key addEdgeAttribute(String typeDescription) {
        Key k = new Key(Model.getModel().getNewId());
        edgeAttributeKeys.add(k);
        edgeAttributeDescriptions.add(typeDescription);

        //initialize the Attribute objects and copy the references
        //to counter edges if the graph is not directed

        Collection<Edge> e = this.edges.values();

        for (Edge edge : e) {
            Attribute a = edge.getAttribute(k);
            if (!this.isDirected && edge.getCounterEdge() != null) {
                edge.getCounterEdge().addAttribute(a, k);
            }
        }

        return (k);
    }

    /**
     * removes an Edge Attribute via its Key object
     * @param k the Key object of the Attribute to remove
     */
    public void removeEdgeAttribute(Key k) {
        for (Edge e : this.edges.values()) {
            e.removeAttribute(k);
        }
        edgeAttributeDescriptions.remove(edgeAttributeKeys.indexOf(k));
        edgeAttributeKeys.remove(k);
    }

    /**
     * returns the description of a Vertex Attribute via its Key object
     * @param k the given Key
     * @return the description
     */
    public String getVertexAttributeDescription(Key k) {
        return (vertexAttributeDescriptions.elementAt(vertexAttributeKeys.indexOf(k)));
    }

    /**
     * returns a String array of all Vertex Attribute descriptions
     * @return
     */
    public String[] getVertexAttributeDescriptions() {
        String[] out = new String[vertexAttributeDescriptions.size()];
        vertexAttributeDescriptions.toArray(out);
        return (out);
    }

    /**
     * returns the description of a Edge Attribute via its Key object
     * @param k the given Key
     * @return the description
     */
    public String getEdgeAttributeDescription(Key k) {
        return (edgeAttributeDescriptions.elementAt(edgeAttributeKeys.indexOf(k)));
    }

    /**
     * returns returns an array of all Edge Attribute descriptions
     * @return the array
     */
    public String[] getEdgeAttributeDescriptions() {
        String[] out = new String[edgeAttributeDescriptions.size()];
        edgeAttributeDescriptions.toArray(out);
        return (out);
    }

    /**
     * returns the Key object to a given Vertex Attribute description
     * @param description the given description
     * @return the Key object
     */
    public Key getKeyToVertexAttributeDescription(String description) {
        return (vertexAttributeKeys.elementAt(vertexAttributeDescriptions.indexOf(description)));
    }

    /**
     * returns the Key object to a given Edge Attribute description
     * @param description the given description
     * @return the Key object
     */
    public Key getKeyToEdgeAttributeDescription(String description) {
        return (edgeAttributeKeys.elementAt(edgeAttributeDescriptions.indexOf(description)));
    }
    
    public String toString() {
    	String str = "graph:\n";
    	str += "number of vertices: "+vertices.size()+"\n";
    	str += "number of edges: "+edges.size()+"\n";
    	for( Vertex v : getVertices() ) {
    		str += v.toString()+"pos:"+vertexCoordinates.get(v.getKey())+"\n";
    	}
    	return str;
    }
}
