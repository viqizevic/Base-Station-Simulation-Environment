package toy;

import java.awt.Point;

/**
 * saves position and gamma for one user
 * @author bzfkroli
 *
 */
public class MSData 
{
	private Point position;
	private Double gamma;
	
	public MSData(Point pos, Double demand)
	{
		position = pos;
		gamma = demand;
	}
	
	public MSData(int x, int y, double demand)
	{
		position = new Point(x,y);
		gamma = demand;
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
	
	public Double getGamma()
	{
		return this.gamma;
	}
	
	public void setPosition(Point pos)
	{
		this.position = pos;
	}
	
	public void setPosition(int x, int y)
	{
		this.position = new Point(x,y);
	}
	
	public void changePosition(boolean xdir, int l)
	{
		if(xdir) position.translate(l,0);
		else position.translate(0,l);
	}
	
	public void setGamma(Double demand)
	{
		this.gamma = demand;
	}
	
	public void changeGamma(Double factor)
	{
		this.gamma *= factor;
	}
}
