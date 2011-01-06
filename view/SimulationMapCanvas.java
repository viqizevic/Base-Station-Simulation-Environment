package view;

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

public class SimulationMapCanvas extends JPanel {
	
	private static final long serialVersionUID = -7367411764098276782L;
	
	private SimulationMap map;

	private int fieldWidth;
	
	private int fieldHeight;
	
	private Point[][] fieldsStartCoordinateInCanvas;
	
	private BufferedImage baseStationImg;
	
	private BufferedImage userImg;
	
	private boolean hideGrids;

	public SimulationMapCanvas( SimulationMap map ) {
		super();
		this.map = map;
		int m = map.getFieldUsage().length;
		int n = map.getFieldUsage()[0].length;
		fieldsStartCoordinateInCanvas = new Point[m][n];
		try {
			baseStationImg = ImageIO.read(new File("img/basestation.png"));
			userImg = ImageIO.read(new File("img/handy.png"));
		} catch (IOException e) {
			System.err.println("Cannot find image file");
		}
		hideGrids = true;//false;
	}

	public void paintComponent( Graphics g ) {
		Graphics2D g2d = (Graphics2D) g;
		int width = this.getWidth();
		int height = this.getHeight();
		
		g2d.setColor( Color.WHITE );
		g2d.fillRect(0, 0, width, height);
		
		setFieldsCoordinate(g2d);
		
		drawBaseStationsAndUsers(g2d);
	}
	
	private void setFieldsCoordinate( Graphics2D g2d ) {
		g2d.setColor( Color.BLACK );
		int width = this.getWidth();
		int height = this.getHeight();
		int m = map.getFieldUsage().length;
		int n = map.getFieldUsage()[0].length;
		fieldWidth = width/n;
		fieldHeight = height/m;
		int mapWidth = n*fieldWidth;
		int mapHeight = m*fieldHeight;
		int mapOrigin_x = (width-mapWidth)/2;
		int mapOrigin_y = (height-mapHeight)/2;
		for( int i=0; i<m; i++ ) {
			for( int j=0; j<n; j++ ) {
				int fieldPos_x = mapOrigin_x + i*fieldWidth;
				int fieldPos_y = mapOrigin_y + j*fieldHeight;
				if( !hideGrids ) {
					g2d.drawRect( fieldPos_x, fieldPos_y, fieldWidth, fieldHeight );
				}
				fieldsStartCoordinateInCanvas[i][j] = new Point(fieldPos_x, fieldPos_y);
			}
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
}
