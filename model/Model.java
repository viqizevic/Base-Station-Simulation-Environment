package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
			
			int bs_numb = -1;
			int u_numb = -1;
			
			String input;
			while( (input=br.readLine()) != null ) {
				if( !input.trim().startsWith("#") ) {
					if( input.trim().startsWith("basestations.number") ) {
						bs_numb = Integer.parseInt( input.split(":")[1].trim() );
					} else if( input.trim().startsWith("users.number") ) {
						u_numb = Integer.parseInt( input.split(":")[1].trim() );
					}
				}
			}
			
			if( bs_numb < 0 || u_numb < 0 ) {
				return false;
			}
			createSimulationMap(bs_numb, u_numb);
			
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