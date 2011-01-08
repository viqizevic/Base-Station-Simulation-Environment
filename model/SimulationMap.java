
package model;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import model.graph.Graph;
import model.graph.Key;
import model.graph.Vertex;

/**
 * Simulation Map represents the graph for the simulation environment.
 * That's why this class extends the {@link Graph} class.
 * The base stations and the users are the vertices of this graph.
 * @author vicky
 *
 */
public class SimulationMap extends Graph {

	/**
	 * This 2-dimensional array contains the fields of the map.
	 * The first index for the vertical direction (the y-axis).
	 * The second index for the horizontal direction (the x-axis).
	 * i.e. f11 f12 f13 ...
	 *      f21 f22 f23 ...
	 *      ...
	 */
	private Field[][] fieldsMatrix;

	/**
	 * A hash map for the base stations.
	 */
	private HashMap<Key, BaseStation> basestations;

	/**
	 * A hash map for the users.
	 */
    private HashMap<Key, User> users;

    /**
     * Construct a simulation map.
     * @param baseStationsNumber The number of base stations in the map.
     * @param usersNumber The number of the users in the map.
     */
    public SimulationMap( int baseStationsNumber, int usersNumber ) {
    	super();
    	basestations = new HashMap<Key, BaseStation>();
    	users = new HashMap<Key, User>();
		setDirected(false);
		setEuclidian(true);
		setInteger(false);
		/* We divide the map into small blocks
		 *  B1 B2 B3 B4
		 *  B5 B6 B7 ...
		 *  ...
		 * Each block is a square containing small fields
		 * with at most one base station in the middle of the block.
		 */
		int fieldNumberPerBlockSide = 5; // number of the fields in one block is the square of this number
		int blockNumberPerRow = 4; // number of blocks in a row
		if( baseStationsNumber < blockNumberPerRow ) {
			blockNumberPerRow = Math.max(1, baseStationsNumber);
		}
		int blockNumberPerColumn = baseStationsNumber/blockNumberPerRow; // number of blocks in a column
		if( baseStationsNumber%blockNumberPerRow > 0 || baseStationsNumber == 0 ) {
			blockNumberPerColumn++;
		}
		int totalFieldNumberHorizontally = blockNumberPerRow*fieldNumberPerBlockSide;
		int totalFieldNumberVertically = blockNumberPerColumn*fieldNumberPerBlockSide;
		fieldsMatrix = new Field[totalFieldNumberVertically][totalFieldNumberHorizontally];
		for( int i=0; i<totalFieldNumberVertically; i++ ) {
			for( int j=0; j<totalFieldNumberHorizontally; j++ ) {
				fieldsMatrix[i][j] = new Field();
			}
		}
		// Create and place the base stations in the middle of a block
		int i = 0;
		int j = 0;
		int numberOfBaseStationsCreated = 0;
		for( int k=0; k<blockNumberPerColumn; k++ ) {
			i = k*fieldNumberPerBlockSide + 2;
			for( int l=0; l<blockNumberPerRow; l++ ) {
				j = l*fieldNumberPerBlockSide + 2;
				if( numberOfBaseStationsCreated < baseStationsNumber ) {
					BaseStation bs = new BaseStation();
					Point p = new Point(j, i);
					addVertex(bs, p);
					basestations.put(bs.getKey(), bs);
					fieldsMatrix[i][j].setFieldUser(bs);
					numberOfBaseStationsCreated++;
				}
			}
		}
		// Create and place the users randomly
		int numberOfUsersCreated = 0;
		while( numberOfUsersCreated < usersNumber ) {
			Random random = new Random();
			i = random.nextInt(totalFieldNumberVertically);
			j = random.nextInt(totalFieldNumberHorizontally);
//			do {
//				y = (int) Math.round(random.nextGaussian()*totalFieldNumberVertically/5 + totalFieldNumberVertically/2);
//				x = (int) Math.round(random.nextGaussian()*totalFieldNumberHorizontally/5 + totalFieldNumberHorizontally/2);
//			} while( x<0 || y<0 || y>=totalFieldNumberVertically || x>=totalFieldNumberHorizontally );
			if( fieldsMatrix[i][j].getFieldUsageType() == FieldUsageType.Empty ) {
				User u = new User();
				Point p = new Point( j, i );
				addVertex(u, p);
				users.put(u.getKey(), u);
				fieldsMatrix[i][j].setFieldUser(u);
				numberOfUsersCreated++;
			}
		}
    }

    /**
     * Get the matrix of the fields.
     * @return The matrix with elements from the type {@link Field}.
     */
	public Field[][] getFieldsMatrix() {
		return fieldsMatrix;
	}

	/**
	 * Get the field with the specified coordinate in the map.
	 * @param x The coordinate in x-axis.
	 * @param y The coordinate in y-axis.
	 * @return The {@link Field} with the coordinate given in the fields matrix.
	 * <code>null</code> if the indexes x or y not in the fields matrix.
	 */
	public Field getField( int x, int y ) {
		if( x<0 || y<0 ) {
			return null;
		} else if( y>=fieldsMatrix.length || x>=fieldsMatrix[0].length) {
			return null;
		}
		return fieldsMatrix[y][x];
	}

	/**
	 * Get all the base stations.
	 * @return A collection of the base stations.
	 */
	public Collection<BaseStation> getBasestations() {
		return basestations.values();
	}

	/**
	 * Get all the users.
	 * @return A collection of the users.
	 */
	public Collection<User> getUsers() {
		return users.values();
	}

	public String toString() {
		String str = "Simulation map ";
		int m = fieldsMatrix.length;
		int n = fieldsMatrix[0].length;
		str += m +"x"+ n +"\n";
		for( int i=0; i<fieldsMatrix.length; i++ ) {
			for( int j=0; j<fieldsMatrix[i].length; j++ ) {
				if( fieldsMatrix[i][j].getFieldUsageType() == FieldUsageType.BaseStation) {
					str += "O ";
				} else if( fieldsMatrix[i][j].getFieldUsageType() == FieldUsageType.User ) {
					str += "u ";
				} else {
					str += ". ";
				}
			}
			str += "\n";
		}
		return str;
	}

	/**
	 * The field in the simulation map.
	 * @author vicky
	 *
	 */
	public class Field {

		/**
		 * User of the field ({@link BaseStation} or {@link User} or empty)
		 */
		private Vertex fieldUser;

		/**
		 * Showing the usage type of the field.
		 */
		private FieldUsageType fieldUsageType;

		/**
		 * Construct an empty field.
		 */
		public Field() {
			fieldUser = null;
			fieldUsageType = FieldUsageType.Empty;
		}

		/**
		 * Get the user of the field.
		 * @return The user of the field, it can be {@link BaseStation} or {@link User} or <code>null</code>.
		 */
		public Vertex getFieldUser() {
			return fieldUser;
		}

		/**
		 * Set the user of the field.
		 * @param fieldUser The user of the field.
		 */
		public void setFieldUser(Vertex fieldUser) {
			this.fieldUser = fieldUser;
			if( fieldUser.getClass().equals(BaseStation.class) ) {
				fieldUsageType = FieldUsageType.BaseStation;
			} else if( fieldUser.getClass().equals(User.class) ) {
				fieldUsageType = FieldUsageType.User;
			} else {
				fieldUsageType = FieldUsageType.Empty;
			}
		}

		/**
		 * Get the usage type of the field.
		 * @return The usage type.
		 */
		public FieldUsageType getFieldUsageType() {
			return fieldUsageType;
		}
	}

	/**
	 * An enum for the type of the field usage.
	 * @author vicky
	 *
	 */
	public enum FieldUsageType {
		Empty,
		BaseStation,
		User
	}
}