/**
 * Stores additional data in vertices or edges
 */
package model.graph;

/**
 * Attribute of an {@link Edge} or a {@link Vertex}.
 * @author vicky
 *
 */
public class Attribute {

    public static final int BOOLEAN = 0;
    public static final int BYTE = 1;
    public static final int INTEGER = 2;
    public static final int LONG = 3;
    public static final int FLOAT = 4;
    public static final int DOUBLE = 5;
    public static final int OTHER = 6;

    //weight of attribute
    private Object weight = null;

    //type of attribute
    private int type = -1;

    public Attribute(Object weight) {

        this.weight = weight;

        if ("java.lang.Integer".equals(weight.getClass().getName())) {
            this.type = Attribute.INTEGER;
        } else if ("java.lang.Boolean".equals(weight.getClass().getName())) {
            this.type = Attribute.BOOLEAN;
        } else if ("java.lang.Byte".equals(weight.getClass().getName())) {
            this.type = Attribute.BYTE;
        } else if ("java.lang.Long".equals(weight.getClass().getName())) {
            this.type = Attribute.LONG;
        } else if ("java.lang.Float".equals(weight.getClass().getName())) {
            this.type = Attribute.FLOAT;
        } else if ("java.lang.Double".equals(weight.getClass().getName())) {
            this.type = Attribute.DOUBLE;
        } else {
            this.type = Attribute.OTHER;
        }
    }

    public Attribute() {
    }

    public Attribute(Object weight, int type) {
        this.weight = weight;
        this.type = type;
    }

    //getter and setter
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
    	
        if (type == -1 && weight != null) {
            if ("java.lang.Integer".equals(weight.getClass().getName())) {
                this.type = Attribute.INTEGER;
            } else if ("java.lang.Boolean".equals(weight.getClass().getName())) {
                this.type = Attribute.BOOLEAN;
            } else if ("java.lang.Byte".equals(weight.getClass().getName())) {
                this.type = Attribute.BYTE;
            } else if ("java.lang.Long".equals(weight.getClass().getName())) {
                this.type = Attribute.LONG;
            } else if ("java.lang.Float".equals(weight.getClass().getName())) {
                this.type = Attribute.FLOAT;
            } else if ("java.lang.Double".equals(weight.getClass().getName())) {
                this.type = Attribute.DOUBLE;
            } else {
                this.type = Attribute.OTHER;
            }
        }
        this.weight = weight;

    }
    
    public String toString() {
    	return "attr:"+this.type+"-"+this.weight+";";
    }
}
