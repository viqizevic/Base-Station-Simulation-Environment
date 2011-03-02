package model.parser;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;

import view.View;

import model.BaseStation;
import model.BaseStation.BSData;
import model.Model;
import model.SimulationMap;
import model.User;
import model.User.MSData;
import model.graph.Key;
import model.pathloss.Cost231WalfishIkegami_PathLossModel;

/**
 * creates a .scn file
 * @author bzfkroli
 *
 */
public class SCN_FileCreator {
	
	/**
	 * Create .scn file needed by the zimpl file
	 * @param map the simulation map object
	 * @param name the name of the file (not ended with .scn)
	 * @param showMessage tells if a message should be shown in output text area or not
	 * @return true if file successfully created, false otherwise 
	 */
	public static boolean createSCN( SimulationMap map, String name,
			boolean showMessage ) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(name+".scn"));
			
			Vector<BaseStation> basestations = map.getBasestations();
			Vector<User> users = map.getUsers();
			int m = basestations.size();
			int n = users.size(); 
			
			String output = "";
			output += "#model "+ name + "\n";
			output += "#noBS\n";
			output += Integer.toString(m) + "\n";
			output += "#noMS\n";
			output += Integer.toString(n) + "\n";
			output += "#P (total power per BS)\n";
			int i=1;
			Key bsDataKey = map.getKeyOfBaseStationDataAttribute();
			for( BaseStation b : basestations ) {
				BSData bsData = (BSData) b.getAttribute(bsDataKey).getWeight();
				output += i + " " + bsData.getTotalPower() + "\n";
				i++;
			}
			output += "#K (max number of MS served by BS)\n";
			i=1;
			for( BaseStation b : basestations ) {
				BSData bsData = (BSData) b.getAttribute(bsDataKey).getWeight();
				output += i + " " + bsData.getNUsersServed() + "\n";
				i++;
			}
			double distance;
			output += "#H (channel per MS and BS)\n";
			i=1;
			for( User u : users ) {
				int k=1;
				for( BaseStation b : basestations ) {
					distance = Model.getModel().getLengthOfOneBoxInTheMap_inMeter()*Model.computeEuclidianDistance(
							map.getVertexCoordinates(u.getKey()), map.getVertexCoordinates(b.getKey()));
//                	distance = Point.distance(users.get(keyMS).getXPosition(), users.get(keyMS).getYPosition(), baseStations.get(keyBS).getXPosition(), baseStations.get(keyBS).getYPosition());
					output += i + " " + k + " "
							+1.0/Cost231WalfishIkegami_PathLossModel.getPathLoss(800, distance/1000)+ "\n";
					k++;
				}
				i++;
			}
			output += "#gamma (demand per MS)\n";
			i=1;
			Key msDataKey = map.getKeyOfUserDataAttribute();
			for( User u : users ) {
				MSData msData = (MSData) u.getAttribute(msDataKey).getWeight();
				output += i + " " + msData.getGamma() + "\n";
				i++;
			}
			output += "#sigma^2 (noise per MS)\n";
			i=1;
			for( @SuppressWarnings("unused") User u : users ) {
				output += i + " 5.E-12\n";
				i++;
			}
			out.write( output );
			out.close();
			if( showMessage ) {
				View.getView().appendText(name+".scn:\n");
				View.getView().appendText(output);
			}
			
		} catch (IOException e){
			System.out.println("Unexpected error while writing results to file");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
