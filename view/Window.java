package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import control.Control;

import model.SimulationMap;

public class Window extends JFrame {
	
	private static final long serialVersionUID = -5266572208804064177L;

	private SimulationMapCanvas modelCanvas;
	
	public Window( String title, SimulationMap map ) {
		super( title );
		modelCanvas = new SimulationMapCanvas(map);
		init();
	}

	private void init() {
		createMenu();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize( new Dimension(550,450) );
		this.setPreferredSize( new Dimension(550,450) );

		modelCanvas.setMinimumSize( new Dimension(500,400) );
		modelCanvas.setPreferredSize( new Dimension(500,400) );

		modelCanvas.addMouseListener(Control.getControl().getCanvasMouseListener());

		this.add( modelCanvas );
	}
	
	private void createMenu() {
		JMenuBar m = new JMenuBar();
		
		JMenu file = new JMenu("File");
		
		JMenuItem exit = new JMenuItem("Exit");

		exit.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) { System.exit(0); }
		});

		file.add( exit );
		m.add( file );
		this.setJMenuBar(m);
	}

	public int getWidthOfEachFieldInCanvas() {
		return modelCanvas.getWidthOfEachField();
	}

	public int getHeightOfEachFieldInCanvas() {
		return modelCanvas.getHeightOfEachField();
	}

	public Point getOriginOfTheMapInCanvas() {
		return modelCanvas.getOriginOfTheMap();
	}
}
