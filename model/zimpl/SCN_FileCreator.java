package model.zimpl;

import java.awt.Point;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

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
	
	public static void createSCN( SimulationMap map, String name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(name+".scn"));
			
			Vector<BaseStation> basestations = map.getBasestations();
			Vector<User> users = map.getUsers();
			int m = basestations.size();
			int n = users.size(); 
			out.write("#model "+ name);
			out.newLine();
			out.write("#noBS\n");
			out.write(Integer.toString(m));
			out.newLine();
			out.write("#noMS\n");
			out.write(Integer.toString(n));
			out.newLine();
			out.write("#P (total power per BS)\n");
			int i=1;
			Key bsDataKey = map.getKeyOfBaseStationDataAttribute();
			for( BaseStation b : basestations ) {
				BSData bsData = (BSData) b.getAttribute(bsDataKey).getWeight();
				out.write(i + " " + bsData.getTotalPower() + "\n");
				i++;
			}
			out.write("#K (max number of MS served by BS)\n");
			i=1;
			for( BaseStation b : basestations ) {
				BSData bsData = (BSData) b.getAttribute(bsDataKey).getWeight();
				out.write(i + " " + bsData.getNUsersServed() + "\n");
				i++;
			}
			double distance;
			out.write("#H (channel per MS and BS)\n");
			i=1;
			for( User u : users ) {
				int k=1;
				for( BaseStation b : basestations ) {
					distance = Model.getModel().getDistanceBetweenTwoBlocksInMeter()*Model.computeEuclidianDistance(
							map.getVertexCoordinates(u.getKey()), map.getVertexCoordinates(b.getKey()));
//                	distance = Point.distance(users.get(keyMS).getXPosition(), users.get(keyMS).getYPosition(), baseStations.get(keyBS).getXPosition(), baseStations.get(keyBS).getYPosition());
					out.write(i + " " + k + " "
							+1.0/Cost231WalfishIkegami_PathLossModel.getPathLoss(800, distance/1000)+ "\n");
					k++;
				}
				i++;
			}
			out.write("#gamma (demand per MS)\n");
			i=1;
			Key msDataKey = map.getKeyOfUserDataAttribute();
			for( User u : users ) {
				MSData msData = (MSData) u.getAttribute(msDataKey).getWeight();
				out.write(i + " " + msData.getGamma() + "\n");
				i++;
			}
			out.write("#sigma^2 (noise per MS)\n");
			i=1;
			for( User u : users ) {
				out.write(i + " 5.E-12\n");
				i++;
			}
			out.close();    
			
		}catch (IOException e){
			System.out.println("Unexpected error while writing results to file");
			e.printStackTrace();
			return;
		}
	}
}
