package model.graph;

import java.util.Collection;
import java.util.HashMap;

import model.Model;

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
        this.key = new Key(Model.getModel().getNewId());
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
    	return "node:"+key;
    }
}