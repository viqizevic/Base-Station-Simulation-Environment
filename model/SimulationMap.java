package model;

import java.util.Random;

public class SimulationMap {

	/**
	 * This 2-dimensional array tells if the field is used or not.
	 * The first index for the vertical direction.
	 * The second index for the horizontal direction.
	 */
	private FieldUsageType[][] fieldUsage;
	
	private int numberOfBaseStations;
	
	private int numberOfUsers;
	
	public SimulationMap( int baseStationsNumber, int usersNumber ) {
		// Compute at first the size of the simulation map.
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
		// Define the location of the base stations
		int x = 0;
		int y = 0;
		this.numberOfBaseStations = 0;
		for( int i=0; i<blockNumberPerColumn; i++ ) {
			y = i*fieldNumberPerBlockSide + 2;
			for( int j=0; j<blockNumberPerRow; j++ ){
				x = j*fieldNumberPerBlockSide + 2;
				if( this.numberOfBaseStations < baseStationsNumber ) {
					fieldUsage[y][x] = FieldUsageType.BaseStation;
					this.numberOfBaseStations++;
				}
			}
		}
		// Define the location of the users
		this.numberOfUsers = 0;
		while( this.numberOfUsers < usersNumber ) {
			Random random = new Random();
			y = random.nextInt(totalFieldNumberVertically);
			x = random.nextInt(totalFieldNumberHorizontally);
			if( fieldUsage[y][x] == FieldUsageType.Empty ) {
				fieldUsage[y][x] = FieldUsageType.User;
				this.numberOfUsers++;
			}
		}
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