package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

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

    /**
     * The thread for the simulation.
     */
    private SimulationThread simulationThread;
    
    /**
     * Construct the model.
     */
	public Model() {
		readIniFile("src/simulation.ini");
	}

	/**
	 * Get the static model object.
	 * @return
	 */
	public static Model getModel() {
		return model;
	}

	/**
	 * Create a simulation map of the model.
	 * @param numberOfBaseStations The number of the base stations.
	 * @param numberOfUsers The number of the users.
	 * @return The simulation map created.
	 */
	public SimulationMap createSimulationMap( int numberOfBaseStations, int numberOfUsers ) {
		simulationMap = new SimulationMap(numberOfBaseStations, numberOfUsers);
		return simulationMap;
	}

	/**
	 * Get the simulation map of the model.
	 * @return The Simulation map.
	 * If no simulation map created yet, create a new one with 16 base stations
	 * and 32 users.
	 */
	public SimulationMap getSimulationMap() {
		if( simulationMap == null ) {
			createSimulationMap(16, 32);
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
			simulationMap = new SimulationMap(bs_numb, u_numb, mapWidth, mapHeight);
			for( Point p : bs_locations ) {
				simulationMap.addBaseStation(p);
			}
			for( Point p : u_locations ) {
				simulationMap.addUser(p);
			}
			
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