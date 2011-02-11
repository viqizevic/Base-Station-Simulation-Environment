package toy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;

import model.graph.Key;

/**
 * Class analyses a toy model of CoMP
 * 
 * @author bzfkroli
 *
 */
public class ToyModel 
{
	HashMap<Key,BSData> baseStations;
	HashMap<Key,MSData> users;
	String name;
	
	/**
	 * initializing a model
	 * @param bss number of basestaions
	 * @param mss number of users
	 * @param nm name of the model
	 */
	public ToyModel(HashMap<Key,BSData> bss, HashMap<Key,MSData> mss, String nm)
	{
		baseStations = bss;
		users = mss;
		name = nm;
	}
	
	/**
	 * makes one user change its position
	 * @param id key of the user
	 * @param x if true: movement in direction x, otherwise in direction y
	 * @param l length of the step
	 */
	void userStep(int id, boolean x, int l)
	{
		users.get(Key.toKey(id)).changePosition(x,l);
	}

	/**
	 * makes one user change its position
	 * @param id key of the user
	 * @param x length of the step in x-direction
	 * @param y length of the step in y-direction
	 */
	void userStep(int id, int x, int y)
	{
		users.get(Key.toKey(id)).changePosition(true,x);
		users.get(Key.toKey(id)).changePosition(false,y);
	}
	
	/**
	 * executes command in terminal
	 * @param command command to be executed
	 */
	public void execute(String command) 
	{
		try 
		{
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line=null;
			while((line=input.readLine()) != null)
			{
				System.out.println(line);
			}
			int exitVal = pr.waitFor();
			if( exitVal > 0 ) 
			{
				System.out.println("Exited with error code " + exitVal);
			}
		} 
		catch(Exception e) 
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args file to read
	 */
	public static void main(String[] args) 
	{
		ToyParser parser;
		try
		{
			parser = new ToyParser(args[0]);
			parser.parse();
			ToyModel toy = new ToyModel(parser.getBaseStations(),parser.getUsers(),parser.getName());
			toy.users.get(Key.toKey(1)).changeGamma(1.4);
			toy.users.get(Key.toKey(2)).changeGamma(1.4);
			for(int i=1;i<=4;i++)
			{
//				toy.baseStations.get(Key.toKey(i)).setNUsersServed(2.5);
			}
			for(int i=1;i<7;i++)
			{
				CreateZPL.createZPL(toy.baseStations,toy.users,toy.name);
				toy.execute("zimpl model_globalCluster.zpl");
				toy.execute("scip -f model_globalCluster.lp -l "+toy.name+"-"+i);
				toy.userStep(1, 50, 0);
//				toy.userStep(2, true, -50);
				System.out.println(toy.users.get(Key.toKey(2)).getXPosition());
				System.out.println(toy.users.get(Key.toKey(2)).getYPosition());
//				toy.users.get(Key.toKey(1)).setGamma(toy.users.get(Key.toKey(1)).getGamma()*0.75);
//				toy.users.get(Key.toKey(2)).setGamma(toy.users.get(Key.toKey(2)).getGamma()*.75);
//				toy.users.get(Key.toKey(3)).setGamma(toy.users.get(Key.toKey(3)).getGamma()*.75);
//				toy.baseStations.get(Key.toKey(1)).changeNUsersServed(0.7);
			}
			System.out.println(toy.users.get(Key.toKey(2)).getXPosition());
			System.out.println(toy.users.get(Key.toKey(2)).getYPosition());
			CreateZPL.createZPL(toy.baseStations,toy.users,toy.name);
			toy.execute("zimpl model_globalCluster.zpl");
			toy.execute("scip -f model_globalCluster.lp -l "+toy.name+"-"+7);
//			toy.userStep(2, true, -50);
//			CreateZPL.createZPL(toy.baseStations,toy.users,toy.name);
//			toy.execute("zimpl model_globalCluster.zpl");
//			toy.execute("scip -f model_globalCluster.lp -l "+toy.name+"-"+8);
//			System.out.println(toy.users.get(Key.toKey(2)).getXPosition());
//			System.out.println(toy.users.get(Key.toKey(2)).getYPosition());
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

}
