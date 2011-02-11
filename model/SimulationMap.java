
package model;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import model.graph.Edge;
import model.graph.Graph;
import model.graph.Key;
import model.graph.Vertex;
import model.pathloss.Cost231WalfishIkegami_PathLossModel;

/**
 * Simulation Map represents the graph for the simulation environment.
 * That's why this class extends the {@link Graph} class.
 * The base stations and the users are the vertices of this graph.
 * @author vicky
 *
 */
public class SimulationMap extends Graph {

	/**
	 * The subgraph containing the base stations.
	 */
	private Graph basestationsGraph;

	/**
	 * The subgraph containing the users.
	 */
	private Graph usersGraph;

	/**
	 * This 2-dimensional array contains the fields of the map.
	 * The first index for the vertical direction (the y-axis).
	 * The second index for the horizontal direction (the x-axis).
	 * i.e. f11 f12 f13 ...
	 *      f21 f22 f23 ...
	 *      ...
	 */
	private Field[][] fieldsMatrix;

	private Key assignmentKey;

    public SimulationMap( int baseStationsNumber, int usersNumber, int fieldWidth, int fieldHeight ) {
    	super();
    	basestationsGraph = new Graph();
    	usersGraph = new Graph();
		setEuclidian(true);
    	
		fieldsMatrix = new Field[fieldHeight][fieldWidth];
		for( int i=0; i<fieldHeight; i++ ) {
			for( int j=0; j<fieldWidth; j++ ) {
				fieldsMatrix[i][j] = new Field();
			}
		}
    }

    public void addBaseStation( Point p ) {
		BaseStation bs = new BaseStation();
		addVertex(bs, p);
		basestationsGraph.addVertex(bs, p);
		fieldsMatrix[p.y][p.x].setFieldUser(bs);
    }
    
    public void addUser( Point p ) {
		User u = new User();
		addVertex(u, p);
		usersGraph.addVertex(u, p);
		fieldsMatrix[p.y][p.x].setFieldUser(u);
    }

    /**
     * Construct a simulation map.
     * @param baseStationsNumber The number of base stations in the map.
     * @param usersNumber The number of the users in the map.
     */
    public SimulationMap( int baseStationsNumber, int usersNumber ) {
    	super();
    	basestationsGraph = new Graph();
    	usersGraph = new Graph();
		setEuclidian(true);
		/* We divide the map into small blocks
		 *  B1 B2 B3 B4
		 *  B5 B6 B7 ...
		 *  ...
		 * Each block is a square containing small fields
		 * with at most one base station in the middle of the block.
		 */
		// TODO this parameters should be saved in an init file
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
		
		// Create and place the base stations in the middle of the blocks
		int i = 0;
		int j = 0;
		int numberOfFieldsToTheMiddle = fieldNumberPerBlockSide/2;
		int numberOfBaseStationsCreated = 0;
		for( int k=0; k<blockNumberPerColumn; k++ ) {
			i = k*fieldNumberPerBlockSide + numberOfFieldsToTheMiddle;
			for( int l=0; l<blockNumberPerRow; l++ ) {
				j = l*fieldNumberPerBlockSide + numberOfFieldsToTheMiddle;
				if( numberOfBaseStationsCreated < baseStationsNumber ) {
					BaseStation bs = new BaseStation();
					Point p = new Point(j, i);
					addVertex(bs, p);
					basestationsGraph.addVertex(bs, p);
					fieldsMatrix[i][j].setFieldUser(bs);
					numberOfBaseStationsCreated++;
				}
			}
		}
		// Create and place the users randomly
		int numberOfUsersCreated = 0;
		while( numberOfUsersCreated < usersNumber ) {
			/**
			 * TODO set a random user generator class
			 */
			Random random = new Random();
			i = random.nextInt(totalFieldNumberVertically);
			j = random.nextInt(totalFieldNumberHorizontally);
//			do {
//				i = (int) Math.round(random.nextGaussian()*totalFieldNumberVertically/5 + totalFieldNumberVertically/2);
//				j = (int) Math.round(random.nextGaussian()*totalFieldNumberHorizontally/5 + totalFieldNumberHorizontally/2);
//			} while( i<0 || j<0 || i>=totalFieldNumberVertically || j>=totalFieldNumberHorizontally );
			if( fieldsMatrix[i][j].getFieldUsageType() == FieldUsageType.Empty ) {
				User u = new User();
				Point p = new Point( j, i );
				addVertex(u, p);
				usersGraph.addVertex(u, p);
				fieldsMatrix[i][j].setFieldUser(u);
				numberOfUsersCreated++;
			}
		}
		
		setAttributesOfTheBasestations();
		setAttributesOfTheUser();
		
		setAllEdges();
    }
    
    private void setAttributesOfTheBasestations() {
    	Key transmitPowerKey = basestationsGraph.addVertexAttribute("Transmit power");
    	Key baseStationHeightKey = basestationsGraph.addVertexAttribute("Base station height");
    	Key maximalUserNumberKey = basestationsGraph.addVertexAttribute("Maximal number of users");
    	double transmitPower = 1; // 1 Watt
    	double baseStationHeight = 30; // 30 m
    	int maximalUserNumber = 5;
    	for( Vertex bs : basestationsGraph.getVertices() ) {
    		bs.getAttribute(transmitPowerKey).setWeight(transmitPower);
    		bs.getAttribute(transmitPowerKey).setDescription(
    				basestationsGraph.getVertexAttributeDescription(transmitPowerKey));
    		bs.getAttribute(baseStationHeightKey).setWeight(baseStationHeight);
    		bs.getAttribute(baseStationHeightKey).setDescription(
    				basestationsGraph.getVertexAttributeDescription(baseStationHeightKey));
    		bs.getAttribute(maximalUserNumberKey).setWeight(maximalUserNumber);
    		bs.getAttribute(maximalUserNumberKey).setDescription(
    				basestationsGraph.getVertexAttributeDescription(maximalUserNumberKey));
    	}
    	/**
    	 * TODO add more attribute
    	 * like antenna height, type (i.e. non-directional),
    	 * maximal user, user assigned to and sending frequency 
    	 */
    }
    
    private void setAttributesOfTheUser() {
    	Key userMobileStationHeightKey = usersGraph.addVertexAttribute("Mobile station height");
    	int n = basestationsGraph.getVertices().size();
    	Key[] distanceToBasestationsKey = new Key[n];
    	for( int i=0; i<n; i++ ) {
    		distanceToBasestationsKey[i] = usersGraph.addVertexAttribute("Path loss from bs_"+(i+1));
    	}
    	double userMobileStationHeight = 1.5; // 1.5 m
    	for( Vertex u : usersGraph.getVertices() ) {
//    		u.getAttribute(userMobileStationHeightKey).setWeight(userMobileStationHeight);
//    		u.getAttribute(userMobileStationHeightKey).setDescription(
//    				usersGraph.getVertexAttributeDescription(userMobileStationHeightKey));
    		for( Vertex b : basestationsGraph.getVertices() ) {
    			int i = b.getKey().getId().intValue();
    			double d_ub = Model.computeEuclidianDistance(
    					usersGraph.getVertexCoordinates(u.getKey()),
    					basestationsGraph.getVertexCoordinates(b.getKey()) );
    			double pathLoss = 1.0/Cost231WalfishIkegami_PathLossModel.getPathLoss(800, 0.25*d_ub);
    			u.getAttribute(distanceToBasestationsKey[i]).setWeight(pathLoss);
    			u.getAttribute(distanceToBasestationsKey[i]).setDescription(
    					usersGraph.getVertexAttributeDescription(distanceToBasestationsKey[i]));
    			// FIXME is correct but still we should be careful here, since we use the id of the key..
    		}
    	}
    	/**
    	 * TODO add more attribute
    	 * like antenna height, type (i.e. non-directional),
    	 * base station connected to, channel frequency,
    	 * fading coefficient to all base stations or SINR
    	 */
    }
    
    private void setAllEdges() {
    	for( Vertex u : usersGraph.getVertices() ) {
    		for( Vertex bs : basestationsGraph.getVertices() ) {
    			this.addEdge(u, bs);
    		}
    	}
    	assignmentKey = this.addEdgeAttribute("Connecting");
    	for( Edge e : this.getEdges() ) {
    		e.getAttribute(assignmentKey).setWeight(false);
    		e.getAttribute(assignmentKey).setDescription(
    				this.getEdgeAttributeDescription(assignmentKey));
    	}
    	Vertex[] basestations = new Vertex[basestationsGraph.getVertices().size()];
    	basestationsGraph.getVertices().toArray(basestations);
    	for( int i=0; i<basestations.length; i++ ) {
    		Vertex bs_i = basestations[i];
    		for( int j=i+1; j<basestations.length; j++ ) {
    			Vertex bs_j = basestations[j];
    			basestationsGraph.addEdge(bs_i, bs_j);
    		}
    	}
    	Key cooperationKey = basestationsGraph.addEdgeAttribute("Cooperating");
    	for( Edge e : basestationsGraph.getEdges() ) {
    		e.getAttribute(cooperationKey).setWeight(false);
    		e.getAttribute(cooperationKey).setDescription(
    				basestationsGraph.getEdgeAttributeDescription(cooperationKey));
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
	public Vector<BaseStation> getBasestations() {
		Vector<BaseStation> basestations = new Vector<BaseStation>();
		for( Vertex v : basestationsGraph.getVertices() ) {
			basestations.add( (BaseStation) v );
		}
		return basestations;
	}

	/**
	 * Get all the users.
	 * @return A collection of the users.
	 */
	public Vector<User> getUsers() {
		Vector<User> users = new Vector<User>();
		for( Vertex v : usersGraph.getVertices() ) {
			users.add( (User) v );
		}
		return users;
	}

	public Key getAssignmentKey() {
		return assignmentKey;
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
			if( fieldUser == null ) {
				fieldUsageType = FieldUsageType.Empty;
				return;
			}
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