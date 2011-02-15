package model.zimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandExecutor {

	/**
	 * executes command in terminal
	 * @param command command to be executed
	 * @return true if no error occurs, false otherwise
	 */
	public static boolean execute(String command) 
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
				return false;
			}
		} 
		catch(Exception e) 
		{
			System.out.println(e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
