package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//import model.PathLoss;

import control.Control;

import model.Model;
import model.SimulationMap;

/**
 * The window frame of the GUI.
 * @author vicky
 *
 */
public class Window extends JFrame {
	
	private static final long serialVersionUID = -5266572208804064177L;

	/**
	 * The canvas to draw the simulation map.
	 */
	private SimulationMapCanvas modelCanvas;
	
	private TextArea textArea;

	/**
	 * Constructs the window.
	 * @param title The title of the frame.
	 * @param map The simulation map from {@link Model}.
	 */
	public Window( String title, SimulationMap map ) {
		super( title );
		modelCanvas = new SimulationMapCanvas(map);
		textArea = new TextArea(15,35);
		init();
	}

	/**
	 * Initialize the contents of the window.
	 */
	private void init() {
		createMenu();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize( new Dimension(600,570) );
		this.setPreferredSize( new Dimension(790,570) );

		modelCanvas.setMinimumSize( new Dimension(200,200) );
		modelCanvas.setPreferredSize( new Dimension(400,400) );

		modelCanvas.addMouseListener(Control.getControl().getCanvasMouseListener());

		textArea.setEditable(false);
		
		BorderLayout borderLayout = new BorderLayout(5,5);
		this.setLayout( borderLayout );
		this.add( modelCanvas, BorderLayout.CENTER );
		this.add( textArea, BorderLayout.EAST );

//		// Create a simple XY chart
//		XYSeries series = new XYSeries("");
//		for( int d=1; d<=20; d++ ) {
//			System.out.println(d);
//			System.out.println(PathLoss.getPathLoss(1000, d*1000, 200, 2));
//			series.add(d, PathLoss.getPathLoss(1000, d*1000, 200, 2));
//		}
//		// Add the series to your data set
//		XYSeriesCollection dataset = new XYSeriesCollection();
//		dataset.addSeries(series);
//		JFreeChart chart = ChartFactory.createXYLineChart("",
//				"distance", "", dataset, PlotOrientation.VERTICAL, true, true, false);
//        ChartPanel xyChartPanel = new ChartPanel(chart);
//		this.add( xyChartPanel, BorderLayout.SOUTH );
	}

	/**
	 * Create the menu of the window.
	 */
	private void createMenu() {
		JMenuBar m = new JMenuBar();
		
		JMenu file = new JMenu("File");
		
		JMenuItem exit = new JMenuItem("Exit");
		
		exit.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) { System.exit(0); }
		});
		
		JMenu option = new JMenu("Option");
		
		JMenuItem toggleGrids = new JMenuItem("Toggle grids");
		
		toggleGrids.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelCanvas.toggleGrids();
				modelCanvas.repaint();
			}
		});

		file.add(exit);
		option.add(toggleGrids);
		m.add(file);
		m.add(option);
		this.setJMenuBar(m);
	}

	public SimulationMapCanvas getSimulationMapCanvas() {
		return modelCanvas;
	}
	
	public void setText( String text ) {
		textArea.setText(text);
	}
}
