package toy;

import java.awt.Point;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import model.graph.Key;
import model.pathloss.Cost231WalfishIkegami_PathLossModel;

/**
 * creates a .zpl-file out of data obtained by ToyParser
 * @author bzfkroli
 *
 */
public class CreateZPL 
{
	static void createZPL(HashMap<Key,BSData> baseStations, HashMap<Key,MSData> users, String name)
	{
        try 
        {           
            BufferedWriter out = new BufferedWriter(new FileWriter(name+".scn"));
            
            int m = baseStations.size();
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
            for(Key key : baseStations.keySet())
            {
            	out.write(Long.toString(key.getId()) + " " + baseStations.get(key).getTotalPower() + "\n");
            }
            out.write("#K (max number of MS served by BS)\n");
            for(Key key : baseStations.keySet())
            {
            	out.write(Long.toString(key.getId()) + " " + baseStations.get(key).getNUsersServed() + "\n");
            }
            double distance;
            out.write("#H (channel per MS and BS)\n");
            for(Key keyMS : users.keySet())
            {
            	for(Key keyBS : baseStations.keySet())
                {
                	distance = Point.distance(users.get(keyMS).getXPosition(), users.get(keyMS).getYPosition(), baseStations.get(keyBS).getXPosition(), baseStations.get(keyBS).getYPosition());
                	out.write(Long.toString(keyMS.getId()) + " " + Long.toString(keyBS.getId()) + " " +1.0/Cost231WalfishIkegami_PathLossModel.getPathLoss(800, distance/1000)+ "\n");
                }
            }
            out.write("#gamma (demand per MS)\n");
            for(Key key : users.keySet())
            {
            	out.write(Long.toString(key.getId()) + " " + users.get(key).getGamma() + "\n");
            }
            out.write("#sigma^2 (noise per MS)\n");
            for(Key key : users.keySet())
            {
            	out.write(Long.toString(key.getId()) + " 5.E-12\n");
            }
            out.close();    
            
    }catch (IOException e){
            System.out.println("Unexpected error writing results to file");
            return;
    }

	}
	
	public static void main(String[] args)
	{
		HashMap<Key,BSData> baseStations = new HashMap<Key, BSData>();
		HashMap<Key,MSData> users = new HashMap<Key,MSData>();
		baseStations.put(new Key(Long.valueOf(1)), new BSData(250,250,10,3));
		baseStations.put(new Key(Long.valueOf(2)), new BSData(500,250,5,2));
		users.put(new Key(Long.valueOf(1)), new MSData(300,200,0.00034));
		CreateZPL.createZPL(baseStations,users,"test");

	}
}
