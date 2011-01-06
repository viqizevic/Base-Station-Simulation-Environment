package control;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.Model;

import view.View;

public class CanvasMouseListener implements MouseListener {

    void eventOutput(String eventDescription, MouseEvent e) {
        System.out.println(eventDescription + " detected on "
                + e.getComponent().getClass().getName()
                + ".");
        System.out.print("x: "+e.getX());
        System.out.println(", y: "+e.getY());
        System.out.println(getCoordinateOfTheClickedFieldInMap(e.getX(), e.getY()));
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        eventOutput("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")", e);
    }

    private Point getCoordinateOfTheClickedFieldInMap( int x, int y ) {
    	Point origin = View.getView().getOriginOfTheMapInCanvas();
    	if( x < origin.x || y < origin.y ) {
    		return null;
    	}
    	int i = 0;
    	int fieldWidthInCanvas = View.getView().getWidthOfEachFieldInCanvas();
    	while( x >= origin.x+fieldWidthInCanvas*(i+1) ) {
    		i++;
    	}
    	int j = 0;
    	int fieldHeightInCanvas = View.getView().getHeightOfEachFieldInCanvas();
    	while( y >= origin.y+fieldHeightInCanvas*(j+1) ) {
    		j++;
    	}
    	int m = Model.getModel().getSimulationMap().getFieldUsage().length;
    	int n = Model.getModel().getSimulationMap().getFieldUsage()[0].length;
    	if ( i >= n || j >= m ) {
    		return null;
    	}
    	return new Point(i,j);
    }
}
