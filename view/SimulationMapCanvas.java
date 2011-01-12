package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.BaseStation;
import model.SimulationMap;
import model.User;
import model.graph.Edge;
import model.graph.Key;
import model.graph.Vertex;

public class SimulationMapCanvas extends JPanel {
	
	private static final long serialVersionUID = -7367411764098276782L;
	
	private SimulationMap map;

	private int fieldWidth;
	
	private int fieldHeight;
	
	private Point[][] fieldsStartCoordinateInCanvas;
	
	private BufferedImage baseStationImg;
	
	private BufferedImage userImg;
	
	private boolean hideGrids;
	
	private Vertex highlightedVertex;

	public SimulationMapCanvas( SimulationMap map ) {
		super();
		this.map = map;
		int m = map.getFieldsMatrix().length;
		int n = map.getFieldsMatrix()[0].length;
		fieldsStartCoordinateInCanvas = new Point[m][n];
		try {
			baseStationImg = ImageIO.read(new File("img/basestation.png"));
			userImg = ImageIO.read(new File("img/handy.png"));
		} catch (IOException e) {
			System.err.println("Cannot find image file");
		}
//		hideGrids = true;
		hideGrids = false;
	}

	public void paintComponent( Graphics g ) {
		Graphics2D g2d = (Graphics2D) g;
		setFields(g2d);
		highlightVertex(g2d);
		drawBaseStationsAndUsers(g2d);
	}
	
	private void setFields( Graphics2D g2d ) {
		int width = this.getWidth();
		int height = this.getHeight();
		int m = map.getFieldsMatrix().length;
		int n = map.getFieldsMatrix()[0].length;
		fieldWidth = width/n;
		fieldHeight = height/m;
		fieldWidth = Math.min(fieldWidth, fieldHeight);
		fieldHeight = fieldWidth;
		int mapWidth = n*fieldWidth;
		int mapHeight = m*fieldHeight;
		int mapOrigin_x = (width-mapWidth)/2;
		int mapOrigin_y = (height-mapHeight)/2;

		// TODO draw another color as the background of the map
		g2d.setColor( Color.WHITE );
		g2d.fillRect(mapOrigin_x, mapOrigin_y, mapWidth, mapHeight);
		
		float dash[] = {4.0f};
		BasicStroke standardStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		g2d.setStroke(standardStroke);
		g2d.setColor( Color.GRAY );
		for( int i=0; i<m; i++ ) {
			for( int j=0; j<n; j++ ) {
				int fieldPos_x = mapOrigin_x + j*fieldWidth;
				int fieldPos_y = mapOrigin_y + i*fieldHeight;
				if( !hideGrids ) {
					g2d.drawRect( fieldPos_x, fieldPos_y, fieldWidth, fieldHeight );
				}
				fieldsStartCoordinateInCanvas[i][j] = new Point(fieldPos_x, fieldPos_y);
			}
		}
	}
	
	private void highlightVertex( Graphics2D g2d ) {
		if( highlightedVertex != null ) {
			float dash[] = {4.0f};
			BasicStroke standardStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
			g2d.setStroke(standardStroke);
			float dash2[] = {3.0f};
			BasicStroke specialStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f);
			g2d.setColor( Color.YELLOW );
			Point vCoordInMap = map.getVertexCoordinates(highlightedVertex.getKey());
			Point vCoordInCanvas = fieldsStartCoordinateInCanvas[vCoordInMap.y][vCoordInMap.x];
			g2d.drawRect(vCoordInCanvas.x, vCoordInCanvas.y, fieldWidth, fieldHeight);
			for( Edge e_vw : highlightedVertex.getOutgoingEdges() ) {
				g2d.setStroke(standardStroke);
				g2d.setColor( Color.YELLOW );
				if( e_vw.hasAttribute(map.getAssignmentKey()) ) {
					if( (Boolean) e_vw.getAttribute(map.getAssignmentKey()).getWeight() ) {
						g2d.setStroke(specialStroke);
						g2d.setColor( Color.RED );
					}
				}
				Point wCoordInMap = map.getVertexCoordinates(e_vw.getTail().getKey());
				Point wCoordInCanvas = fieldsStartCoordinateInCanvas[wCoordInMap.y][wCoordInMap.x];
				g2d.drawLine(vCoordInCanvas.x+fieldWidth/2, vCoordInCanvas.y+fieldHeight/2,
						wCoordInCanvas.x+fieldWidth/2, wCoordInCanvas.y+fieldHeight/2);
			}
		}
	}
	
	private void drawAllEdges( Graphics2D g2d ) {
		g2d.setColor( Color.GRAY );
		for( Edge e_uv : map.getEdges() ) {
			Point uCoordInMap = map.getVertexCoordinates(e_uv.getHead().getKey());
			Point uCoordInCanvas = fieldsStartCoordinateInCanvas[uCoordInMap.y][uCoordInMap.x];
			Point vCoordInMap = map.getVertexCoordinates(e_uv.getTail().getKey());
			Point vCoordInCanvas = fieldsStartCoordinateInCanvas[vCoordInMap.y][vCoordInMap.x];
			g2d.drawLine(uCoordInCanvas.x+fieldWidth/2, uCoordInCanvas.y+fieldHeight/2,
					vCoordInCanvas.x+fieldWidth/2, vCoordInCanvas.y+fieldHeight/2);
		}
	}
	
	private void drawBaseStationsAndUsers( Graphics2D g2d ) {
		g2d.setColor( Color.RED );
		for( BaseStation bs : map.getBasestations() ) {
			Point bsCoordInMap = map.getVertexCoordinates(bs.getKey());
			Point bsCoordInCanvas = fieldsStartCoordinateInCanvas[bsCoordInMap.y][bsCoordInMap.x];
			int bsImgHeight = fieldHeight; // 40
			int bsImgWidth = fieldHeight/4; // 10
			int bsImgStartPoint_x = bsCoordInCanvas.x + (fieldWidth-bsImgWidth)/2;
			int bsImgStartPoint_y = bsCoordInCanvas.y + (fieldHeight-bsImgHeight);
			if( baseStationImg == null ) {
				g2d.fillRect(bsImgStartPoint_x, bsImgStartPoint_y, bsImgWidth, bsImgHeight);
			} else {
				g2d.drawImage(baseStationImg, bsImgStartPoint_x, bsImgStartPoint_y, bsImgWidth, bsImgHeight, null);
			}
		}
		g2d.setColor( Color.BLUE );
		for( User u : map.getUsers() ) {
			Point uCoordInMap = map.getVertexCoordinates(u.getKey());
			Point uCoordInCanvas = fieldsStartCoordinateInCanvas[uCoordInMap.y][uCoordInMap.x];
			int userImgHeight = fieldHeight/2; //16
			int userImgWidth = userImgHeight*2/3; //10
			int userImgStartPoint_x = uCoordInCanvas.x + (fieldWidth-userImgWidth)/2;
			int userImgStartPoint_y = uCoordInCanvas.y + (fieldHeight-userImgHeight)/2;
			if( userImg == null ) {
				g2d.fillRect(userImgStartPoint_x, userImgStartPoint_y, userImgWidth, userImgHeight);
			} else {
				g2d.drawImage(userImg, userImgStartPoint_x, userImgStartPoint_y, userImgWidth, userImgHeight, null);
			}
		}
	}

	public int getWidthOfEachField() {
		return fieldWidth;
	}

	public int getHeightOfEachField() {
		return fieldHeight;
	}

	public Point getOriginOfTheMap() {
		Point o = fieldsStartCoordinateInCanvas[0][0];
		return new Point(o.x, o.y);
	}
	
	public void toggleGrids() {
		hideGrids = !hideGrids;
	}
	
	public void highlightVertex( Vertex v ) {
		highlightedVertex = v;
		repaint();
	}
	
	public void repaint() {
		super.repaint();
	}
}
