package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import model.BaseStation.BSData;
import model.SimulationMap.FieldUsageType;
import model.User.MSData;
import model.graph.Graph;
import model.graph.Key;
import model.parser.CommandExecutor;
import model.parser.SCIP_FileOutputParser;
import model.parser.SCN_FileCreator;
import model.parser.ToyParser;
import model.pathloss.Cost231WalfishIkegami_PathLossModel;

import view.View;

/**
 * The model of the simulation environment.
 * @author vicky
 *
 */
public class Model {

	/**
	 * The static model object.
	 */
	private static Model model = new Model();

    /**
     * The simulation map.
     */
    private SimulationMap simulationMap;
    
    private int lengthOfOneBoxInTheMap_inMeter;
    
    private double gamma;

    /**
     * The thread for the simulation.
     */
    private SimulationThread simulationThread;
    
    /**
     * Construct the model.
     */
	public Model() {
		// FIXME place the simulation.ini correctly..
		readIniFile("src/simulation.ini");
		// TODO this should also be placed in ini file
		lengthOfOneBoxInTheMap_inMeter = 250;
		gamma = 0.00001;
		Graph.debugMode = false;
	}

	/**
	 * Get the static model object.
	 * @return
	 */
	public static Model getModel() {
		return model;
	}
	
	/**
	 * Get the side length of the box in meter.
	 * This is the same as the distance between two objects in adjacent blocks.
	 * Since we assume that the object placed in the middle of the box.
	 * @return The side length of the box in meter.
	 */
	public int getLengthOfOneBoxInTheMap_inMeter() {
		return lengthOfOneBoxInTheMap_inMeter;
	}

	public void setLengthOfOneBoxInTheMap_inMeter(int lengthOfOneBoxInTheMap_inMeter) {
		this.lengthOfOneBoxInTheMap_inMeter = lengthOfOneBoxInTheMap_inMeter;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
		Key userDataKey = simulationMap.getKeyOfUserDataAttribute();
		for( User u : simulationMap.getUsers() ) {
			MSData msData = (MSData) u.getAttribute(userDataKey).getWeight();
			msData.setGamma(gamma);
		}
	}

	/**
	 * Create a simulation map of the model.
	 * @param numberOfBaseStations The number of the base stations.
	 * @param numberOfUsers The number of the users.
	 * @return The simulation map created.
	 */
	public SimulationMap createRandomSimulationMap( int baseStationsNumber, int usersNumber ) {
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
		simulationMap = new SimulationMap(totalFieldNumberHorizontally, totalFieldNumberVertically);
		
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
					Point p = new Point(j, i);
					simulationMap.addBaseStation(p, ""+(numberOfBaseStationsCreated+1));
					numberOfBaseStationsCreated++;
				}
			}
		}
		
		// Create and place the users randomly
		int numberOfUsersCreated = 0;
		while( numberOfUsersCreated < usersNumber ) {
			// TODO set a random user generator class
			Random random = new Random();
			i = random.nextInt(totalFieldNumberVertically);
			j = random.nextInt(totalFieldNumberHorizontally);
//			do {
//				i = (int) Math.round(random.nextGaussian()*totalFieldNumberVertically/5 + totalFieldNumberVertically/2);
//				j = (int) Math.round(random.nextGaussian()*totalFieldNumberHorizontally/5 + totalFieldNumberHorizontally/2);
//			} while( i<0 || j<0 || i>=totalFieldNumberVertically || j>=totalFieldNumberHorizontally );
			if( simulationMap.getField(j, i).getFieldUsageType() == FieldUsageType.Empty ) {
				Point p = new Point( j, i );
				simulationMap.addUser(p, ""+(numberOfUsersCreated+1));
				numberOfUsersCreated++;
			}
		}
		addBSDatasAndMSDatas();
		simulationMap.setAllEdges();
		return simulationMap;
	}

	// FIXME this is hard coded..
	private void addBSDatasAndMSDatas() {
		Key bsDataKey = simulationMap.getKeyOfBaseStationDataAttribute();
		Key userDataKey = simulationMap.getKeyOfUserDataAttribute();
		
		for( BaseStation b : simulationMap.getBasestations() ) {
			Point bsPos = simulationMap.getVertexCoordinates(b.getKey());
			BSData bsData = b.new BSData(bsPos, 1.0, 3.0);
			b.getAttribute(bsDataKey).setWeight(bsData);
		}
		
		for( User u : simulationMap.getUsers() ) {
			Point uPos = simulationMap.getVertexCoordinates(u.getKey());
			MSData msData = u.new MSData(uPos, gamma);
			u.getAttribute(userDataKey).setWeight(msData);
		}
	}

	/**
	 * Get the simulation map of the model.
	 * @return The Simulation map.
	 * If no simulation map created yet, create a new one with 16 base stations
	 * and 32 users.
	 */
	public SimulationMap getSimulationMap() {
		if( simulationMap == null ) {
			createRandomSimulationMap(16, 16);
//			createRandomSimulationMap(8, 8);
		}
		return simulationMap;
	}
	
	/**
	 * Read an ini file
	 * @return true if success reading, otherwise false
	 */
	public boolean readIniFile( String iniFilePath ) {
		try {
			BufferedReader br = new BufferedReader( new FileReader( iniFilePath ) );

			int mapWidth = -1;
			int mapHeight = -1;
			int bs_numb = -1;
			LinkedList<Point> bs_locations = new LinkedList<Point>();
			int u_numb = -1;
			LinkedList<Point> u_locations = new LinkedList<Point>();
			
			String input;
			while( (input=br.readLine()) != null ) {
				if( !input.trim().startsWith("#") ) {
					input = input.trim();
					if( input.startsWith("map.width") ) {
						mapWidth = Integer.parseInt( input.split(":")[1].trim() );
					} else if( input.startsWith("map.height") ) {
						mapHeight = Integer.parseInt( input.split(":")[1].trim() );
					} else if( input.startsWith("basestations.number") ) {
						bs_numb = Integer.parseInt( input.split(":")[1].trim() );
					} else if( input.startsWith("users.number") ) {
						u_numb = Integer.parseInt( input.split(":")[1].trim() );
					} else if( input.startsWith("b_") ) {
						String point = input.split(":")[1].trim();
						String[] p = point.split(",");
						int x = Integer.parseInt( p[0].substring(1).trim() );
						int y = Integer.parseInt( p[1].substring(0, p[1].length()-1) );
						bs_locations.add( new Point(x,y) );
					} else if( input.startsWith("u_") ) {
						String point = input.split(":")[1].trim();
						String[] p = point.split(",");
						int x = Integer.parseInt( p[0].substring(1).trim() );
						int y = Integer.parseInt( p[1].substring(0, p[1].length()-1) );
						u_locations.add( new Point(x,y) );
					}
				}
			}
			
			if( bs_numb < 0 || u_numb < 0 || mapWidth < 0 || mapHeight < 0 ) {
				return false;
			}
			if( bs_numb != bs_locations.size() || u_numb != u_locations.size() ) {
				return false;
			}
			simulationMap = new SimulationMap(mapWidth, mapHeight);
			int i=1;
			for( Point p : bs_locations ) {
				simulationMap.addBaseStation(p,""+i);
				i++;
			}
			i=1;
			for( Point p : u_locations ) {
				simulationMap.addUser(p,""+i);
				i++;
			}
			addBSDatasAndMSDatas();
			simulationMap.setAllEdges();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void loadFile( String filename ) {
		try {
			ToyParser tp = new ToyParser(filename);
			tp.parse();
			int maxX = 0;
			int maxY = 0;
			for( BSData bsData : tp.getBaseStations().values() ) {
				if( bsData.getXPosition() > maxX ) {
					maxX = (int) Math.ceil( bsData.getXPosition() );
				}
				if( bsData.getYPosition() > maxY ) {
					maxY = (int) Math.ceil( bsData.getYPosition() );
				}
			}
			for( MSData msData : tp.getUsers().values() ) {
				if( msData.getXPosition() > maxX ) {
					maxX = (int) Math.ceil( msData.getXPosition() );
				}
				if( msData.getYPosition() > maxY ) {
					maxY = (int) Math.ceil( msData.getYPosition() );
				}
			}
			simulationMap = new SimulationMap(maxX+1, maxY+1);
			Key bsDataKey = simulationMap.getKeyOfBaseStationDataAttribute();
			Key userDataKey = simulationMap.getKeyOfUserDataAttribute();
			int i=1;
			for( BSData bsData : tp.getBaseStations().values() ) {
				BaseStation b = simulationMap.addBaseStation( bsData.getPosition(), ""+i );
				b.getAttribute(bsDataKey).setWeight(bsData);
				i++;
			}
			i=1;
			for( MSData msData : tp.getUsers().values() ) {
				User u = simulationMap.addUser( msData.getPosition(), ""+i );
				u.getAttribute(userDataKey).setWeight(msData);
				i++;
			}
			simulationMap.setAllEdges();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createSCN( String fileName, boolean showMessage ) {
		return SCN_FileCreator.createSCN(simulationMap, fileName, showMessage);
	}
	
	public boolean executeZIMPL( String zimplFileName, String resourceFileName ) {
		if( CommandExecutor.execute("zimpl " + zimplFileName + " -D file=" + resourceFileName) ) {
			return true;
		} else {
			try {
				new FileReader(zimplFileName);
			} catch (FileNotFoundException e) {
				View.getView().showMessage("Cannot find the zimpl file: "
						+ zimplFileName);
			}
			return false;
		}
	}
	
	public boolean executeSCIP( String lpFileName, String outputFileName ) {
		return CommandExecutor.execute("scip -f " + lpFileName + " -l " + outputFileName);
	}
	
	public boolean readSolutionFromSCIP( String scipFileOutputName ) {
		return SCIP_FileOutputParser.readSolutionAndEditTheMap(scipFileOutputName, simulationMap);
	}
	
	public static double computeEuclidianDistance( Point p1, Point p2 ) {
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		return Point.distance(x1, y1, x2, y2);
	}
	
	public void startSimulation() {
		if( simulationThread == null ) {
			simulationThread = new SimulationThread();
			simulationThread.run();
		}
	}
	
	public void stopSimulation() {
		if( simulationThread != null ) {
			simulationThread.shouldStop();
		}
	}
	
	// TODO this should be compatible with the loading method
	public void saveModelFile(String path){
		try{
			FileWriter out = new FileWriter(path);
			
			String output = "# Base Stations\n";
			
			int i=1;
			for( BaseStation bs : simulationMap.getBasestations() ) {
				Point bs_pos = simulationMap.getVertexCoordinates(bs.getKey());
				output += "b_"+i + " : (" + bs_pos.x +","+ bs_pos.y + ")\n";
				i++;
			}
			output += "# Users\n";
			i=1;
			for( User u : simulationMap.getUsers() ) {
				Point u_pos = simulationMap.getVertexCoordinates(u.getKey());
				output += "u_"+i + " : (" + u_pos.x +","+ u_pos.y + ")\n";
				output += u.getAttributesInString();
				i++;
			}
			out.write( output );

			out.flush();
			out.close();
		}catch(IOException e){
			View.getView().showMessage( "An error occured while saving" );
		}
	}
}