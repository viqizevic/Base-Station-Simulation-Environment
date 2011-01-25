
package model.graph;

/**
 * Attribute of an {@link Edge} or a {@link Vertex}.
 * This class used to store additional data of an {@link Edge} or a {@link Vertex}.
 * @author vicky
 *
 */
public class Attribute {

    /**
     * Weight value of the attribute.
     */
    private Object weight;

    /**
     * Type of the weight.
     */
    private Type type;
    
    private String description;

    /**
     * Construct an attribute with weight <code>null</code>.
     */
    public Attribute() {
    	weight = null;
    	type = Type.UNDEFINED;
    	description = null;
    }
    
    public Attribute(Object weight, Type type) {
    	this.weight = weight;
    	this.type = type;
    	description = null;
    }

    public Attribute(Object weight) {
        this.weight = weight;
        setType(weight);
    	description = null;
    }

    private void setType( Object weight ) {
    	if( weight == null ) {
    		type = Type.UNDEFINED;
    		return;
    	}
        if (Integer.class.getName().equals(weight.getClass().getName())) {
            type = Type.INTEGER;
        } else if (Boolean.class.getName().equals(weight.getClass().getName())) {
            type = Type.BOOLEAN;
        } else if (Byte.class.getName().equals(weight.getClass().getName())) {
            type = Type.BYTE;
        } else if (Long.class.getName().equals(weight.getClass().getName())) {
            type = Type.LONG;
        } else if (Float.class.getName().equals(weight.getClass().getName())) {
            type = Type.FLOAT;
        } else if (Double.class.getName().equals(weight.getClass().getName())) {
            type = Type.DOUBLE;
        } else {
            type = Type.OTHER;
        }
    }
    
    public Object getWeight() {
    	return weight;
    }
    
    public void setWeight(Object weight) {
    	this.weight = weight;
    	setType(weight);
    }

    public Type getType() {
        return type;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		String str = "attr: "+weight;
		if( type == Type.DOUBLE ) {
			str = "attr: "+String.format("%10.15f", weight).trim();
		}
		if( description != null ) {
			str += " ("+description+")";
		}
		return str;
    }
    
    public enum Type {
    	UNDEFINED,
        BOOLEAN,
        BYTE,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        OTHER
    }
}
