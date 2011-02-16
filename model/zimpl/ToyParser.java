package model.zimpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import model.BaseStation;
import model.BaseStation.BSData;
import model.User;
import model.User.MSData;
import model.graph.Key;

/**
 * Class parses a .txt-file of the following format:
 * #basestations #mobilestations
 * xPos_BS_1 yPos_BS_1 totalPower_BS_1 maxUsersServed_BS_1
 * .
 * .
 * xPos_BS_m yPos_BS_m totalPower_BS_m maxUsersServed_BS_m
 * xPos_MS_1 yPos_MS_1 gamma_MS_1
 * .
 * .
 * xPos_MS_n yPos_MS_n gamma_MS_n
 * 
 * @author bzfkroli
 *
 */
public class ToyParser 
{
	private String filename;
	private HashMap<Key,BSData> baseStations;
	private HashMap<Key,MSData> users;
	
	/**
	 * initializes parser
	 * @param filename Name of the file to be parsed
	 * @throws FileNotFoundException if there is no such file
	 */
	public ToyParser(String filename) throws FileNotFoundException
	{
		this.filename = filename;
		baseStations = new HashMap<Key, BSData>();
		users = new HashMap<Key, MSData>();
	}
	
	/**
	 * executes parsing
	 */
	public void parse()
	{
		BufferedReader in;
		try
		{
			in = new BufferedReader((new FileReader(filename)));
		}
		catch (FileNotFoundException e)
		{
			return;
		}
		String line;
		String[] lineElems;
		int n;
		int m;
		try
		{
			line = in.readLine();
			lineElems = line.split(" ");
		}
		catch(IOException e)
		{
			//TODO
			return;
		}
		m = Integer.parseInt(lineElems[0]);
		n = Integer.parseInt(lineElems[1]);
		BaseStation b = new BaseStation();
		User u = new User();
		BSData bs;
		MSData ms;
		int x;
		int y;
		double p;
		double k;
		for(int i=0;i<m;i++)
		{
			try
			{
				line = in.readLine();
				lineElems = line.split(" ");
			}
			catch(IOException e)
			{
				//TODO
				return;
			}
			x = Integer.parseInt(lineElems[0]);
			y = Integer.parseInt(lineElems[1]);
			p = Double.parseDouble(lineElems[2]);
			k = Double.parseDouble(lineElems[3]);
			bs = b.new BSData(x,y,p,k);
			baseStations.put(new Key(Long.valueOf(i+1)), bs);
		}
		for(int i=0;i<n;i++)
		{
			try
			{
				line = in.readLine();
				lineElems = line.split(" ");
			}
			catch(IOException e)
			{
				//TODO
				return;
			}
			x = Integer.parseInt(lineElems[0]);
			y = Integer.parseInt(lineElems[1]);
			k = Double.parseDouble(lineElems[2]);
			ms = u.new MSData(x,y,k);
			users.put(new Key(Long.valueOf(i+1)), ms);
		}
	}
	
	/**
	 * returns a HashMap of the base stations data
	 * @return HashMap of the base stations data
	 */
	public HashMap<Key, BSData> getBaseStations()
	{
		return baseStations;
	}
	
	/**
	 * returns a HashMap of the users data
	 * @return HashMap of the users data
	 */	
	public HashMap<Key, MSData> getUsers()
	{
		return users;
	}
	
	/**
	 * returns the file name (without suffix)
	 * @return the file name (without suffix)
	 */	
	public String getName()
	{
		return filename.toString().substring(0,filename.length()-4);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ToyParser parser;
		try
		{
			parser = new ToyParser(args[0]);
			parser.parse();
			System.out.println(parser.getName());
		}
		catch (FileNotFoundException e)
		{
			
		}
		
	}

}
