package model;

import java.awt.Point;

import model.graph.Vertex;

/**
 * The base station for the simulation.
 * @author vicky
 *
 */
public class BaseStation extends Vertex {

	public String toString() {
		return "Base Station: " +
				super.toString();
	}
	
	/**
	 * saves position, total power and number of users served for one basestation
	 * @author bzfkroli
	 *
	 */
	public class BSData 
	{
		private Point position;
		private Double totalPower;
		private Double nUsersServed;
		
		public BSData(Point pos, Double tP, Double nUS)
		{
			position = pos;
			totalPower = tP;
			nUsersServed = nUS;
		}
		
		public BSData(int x, int y, double tP, double nUS)
		{
			position = new Point(x,y);
			totalPower = tP;
			nUsersServed = nUS;
		}
		
		public Point getPosition()
		{
			return this.position;
		}
		
		public double getXPosition()
		{
			return this.position.getX();
		}
		
		public double getYPosition()
		{
			return this.position.getY();
		}
		
		public Double getTotalPower()
		{
			return this.totalPower;
		}
		
		public Double getNUsersServed()
		{
			return this.nUsersServed;
		}
		
		public void setPosition(Point pos)
		{
			this.position = pos;
		}
		
		public void setPosition(int x, int y)
		{
			this.position = new Point(x,y);
		}
		
		public void setTotalPower(Double tP)
		{
			this.totalPower = tP;
		}
		
		public void setNUsersServed(Double nUS)
		{
			this.nUsersServed = nUS;
		}
		
		public void changeNUsersServed(Double factor)
		{
			this.nUsersServed *= factor;
		}
		
		public String toString() {
			String str = "Pos = ("+ position.x+","+position.y+")";
			str += ", Tp = " + totalPower;
			str += ", Kp = " + nUsersServed;
			return str;
		}
	}
}
