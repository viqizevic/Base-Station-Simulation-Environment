/**
 * Hash key providing class
 */

package model.graph;

/**
 * Unique key for the object of the {@link Graph}.
 * @author vicky
 *
 */
public final class Key {

	/**
	 * An internal id counter for the objects of the graph.
	 */
    private static Long internalIdCounter = 0L;

	//unique id object
    private final Long id;

    public Key() {
      this.id = internalIdCounter++;
    }
    
    public Key(Long l) {
        this.id = l;
     }
    
    public Key(Integer i) {
        this.id = new Long(i);
     }
    
    public static Key toKey(int i)
    {
    	return new Key(i);
    }

    public int hashCode() {
      return id.hashCode();
    }

    public Long getId() {
    	return id;
    }
    
    public boolean equals(Object obj) {
      if (!(obj instanceof Key)) return false;
      return id.equals(((Key) obj).id);
    }
    
    public String toString() {
    	return "key:"+id;
    }

}