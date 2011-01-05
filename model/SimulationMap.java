
package model;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import model.graph.Graph;
import model.graph.Key;

public class SimulationMap extends Graph {

	/**
	 * This 2-dimensional array tells if the field is used or not.
	 * The first index for the vertical direction.
	 * The second index for the horizontal direction.
	 */
	private FieldUsageType[][] fieldUsage;

	private HashMap<Key, BaseStation> basestations;
    
    private HashMap<Key, User> users;

    public SimulationMap( int baseStationsNumber, int usersNumber ) {
    	super();
    	basestations = new HashMap<Key, BaseStation>();
    	users = new HashMap<Key, User>();
		setDirected(false);
		setEuclidian(true);
		setInteger(false);
		for( int i=1; i <= baseStationsNumber; i++ ) {
		}
		for( int i=1; i <= usersNumber; i++ ) {
		}
		for( BaseStation bs : basestations.values() ) {
			for( User u : users.values() ) {
				addEdge(bs, u);
			}
		}
		/* We divide the map into small blocks
		 *  B1 B2 B3 B4
		 *  B5 B6 B7 ...
		 * Each block is a square containing small fields
		 * with at most one base station in the middle of the block.
		 */
		int fieldNumberPerBlockSide = 5; // => The block size is then the square of this number
		int blockNumberPerRow = 4;
		if( baseStationsNumber < blockNumberPerRow ) {
			blockNumberPerRow = Math.max(1, baseStationsNumber);
		}
		int blockNumberPerColumn = baseStationsNumber/blockNumberPerRow;
		if( baseStationsNumber%blockNumberPerRow != 0 || blockNumberPerColumn == 0 ) {
			blockNumberPerColumn++;
		}
		int totalFieldNumberHorizontally = blockNumberPerRow*fieldNumberPerBlockSide;
		int totalFieldNumberVertically = blockNumberPerColumn*fieldNumberPerBlockSide;
		fieldUsage = new FieldUsageType[totalFieldNumberVertically][totalFieldNumberHorizontally];
		for( int i=0; i<totalFieldNumberVertically; i++ ) {
			for( int j=0; j<totalFieldNumberHorizontally; j++ ) {
				fieldUsage[i][j] = FieldUsageType.Empty;
			}
		}
		// Create and locate the base stations
		int x = 0;
		int y = 0;
		int numberOfBaseStationsCreated = 0;
		for( int i=0; i<blockNumberPerColumn; i++ ) {
			y = i*fieldNumberPerBlockSide + 2;
			for( int j=0; j<blockNumberPerRow; j++ ){
				x = j*fieldNumberPerBlockSide + 2;
				if( numberOfBaseStationsCreated < baseStationsNumber ) {
					fieldUsage[y][x] = FieldUsageType.BaseStation;
					BaseStation bs = new BaseStation();
					Point p = new Point(x, y);
					addVertex(bs, p);
					basestations.put(bs.getKey(), bs);
					numberOfBaseStationsCreated++;
				}
			}
		}
		// Create and locate the users
		int numberOfUsersCreated = 0;
		while( numberOfUsersCreated < usersNumber ) {
			Random random = new Random();
			y = random.nextInt(totalFieldNumberVertically);
			x = random.nextInt(totalFieldNumberHorizontally);
//			do {
//				y = (int) Math.round(random.nextGaussian()*totalFieldNumberVertically/5 + totalFieldNumberVertically/2);
//				x = (int) Math.round(random.nextGaussian()*totalFieldNumberHorizontally/5 + totalFieldNumberHorizontally/2);
//			} while( x<0 || y<0 || y>=totalFieldNumberVertically || x>=totalFieldNumberHorizontally );
			if( fieldUsage[y][x] == FieldUsageType.Empty ) {
				fieldUsage[y][x] = FieldUsageType.User;
				User u = new User();
				Point p = new Point( x, y );
				addVertex(u, p);
				users.put(u.getKey(), u);
				numberOfUsersCreated++;
			}
		}
    }

	public FieldUsageType[][] getFieldUsage() {
		return fieldUsage;
	}

	public Collection<BaseStation> getBasestations() {
		return basestations.values();
	}

	public Collection<User> getUsers() {
		return users.values();
	}

	public String toString() {
		String str = "Simulation map ";
		int m = fieldUsage.length;
		int n = fieldUsage[0].length;
		str += m +"x"+ n +"\n";
		for( int i=0; i<fieldUsage.length; i++ ) {
			for( int j=0; j<fieldUsage[i].length; j++ ) {
				if( fieldUsage[i][j] == FieldUsageType.BaseStation) {
					str += "O ";
				} else if( fieldUsage[i][j] == FieldUsageType.User ) {
					str += "u ";
				} else {
					str += ". ";
				}
			}
			str += "\n";
		}
		return str;
	}

	public enum FieldUsageType {
		Empty,
		BaseStation,
		User
	}
}