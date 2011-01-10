package control;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.Model;
import model.SimulationMap.Field;
import model.SimulationMap.FieldUsageType;

import view.View;

/**
 * The mouse listener for the canvas.
 * @author vicky
 *
 */
public class CanvasMouseListener implements MouseListener {

    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        Point fieldCoordinate = getCoordinateOfTheClickedFieldInMap(e.getX(), e.getY());
        if( fieldCoordinate == null ) {
        	return;
        }
        Field field = Model.getModel().getSimulationMap().getField(fieldCoordinate.x,fieldCoordinate.y);
        if( field.getFieldUsageType() != FieldUsageType.Empty ) {
        	String output = "" + field.getFieldUser();
        	View.getView().setText(output);
        }
    }

    /**
     * Get the coordinate of the field being clicked by the user.
     * @param x The value of the clicked point in x-axis.
     * @param y The value of the clicked point in y-axis.
     * @return {@link Point} (i,j) in the fields matrix of the map.
     * <code>null</code> if (x,y) located outside of the map.
     */
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
    	int m = Model.getModel().getSimulationMap().getFieldsMatrix().length;
    	int n = Model.getModel().getSimulationMap().getFieldsMatrix()[0].length;
    	if ( i >= n || j >= m ) {
    		return null;
    	}
    	return new Point(i,j);
    }
}
