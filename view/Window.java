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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import control.Control;

import model.Model;
import model.SimulationMap;
import model.pathloss.Cost231WalfishIkegami_PathLossModel;
import model.pathloss.OkumuraHata_PathLossModel;

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

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel simulationPanel = new JPanel();
		modelCanvas.addMouseListener(Control.getControl().getCanvasMouseListener());
		textArea.setEditable(false);
		BorderLayout borderLayout = new BorderLayout(5,5);
		simulationPanel.setLayout( borderLayout );
		simulationPanel.add( modelCanvas, BorderLayout.CENTER );
		simulationPanel.add( textArea, BorderLayout.EAST );
		
		// Create a simple XY chart
//		XYSeries series = new XYSeries("");
//		double d = 0.25;
//		while( d <= 5 ) {
//			series.add(d, 1000000*1.0/Cost231WalfishIkegami_PathLossModel.getPathLoss(800, d));
////			System.out.println(d + ": " + 1.0/Cost231WalfishIkegami_PathLossModel.getPathLoss(800, d));
////			series.add(d, 1000000*1.0/OkumuraHata_PathLossModel.getPathLoss(800, d, 30, 1.5));
//			d += 0.05;
//		}
//		// Add the series to our data set
//		XYSeriesCollection dataset = new XYSeriesCollection();
//		dataset.addSeries(series);
//		JFreeChart chart = ChartFactory.createXYLineChart("",
//				"distance", "", dataset, PlotOrientation.VERTICAL, true, true, false);
//        ChartPanel xyChartPanel = new ChartPanel(chart);
//
        tabbedPane.addTab("Main", null, simulationPanel, "Main");
//        tabbedPane.addTab("Chart", null, xyChartPanel, "Chart");
		this.add( tabbedPane );
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

		JMenu run = new JMenu("Run");
		JMenuItem createSCN = new JMenuItem("Create SCN file for ZIMPL");
		JMenuItem runSCIP = new JMenuItem("Run SCIP");
		
		createSCN.addActionListener( Control.getControl().getRunOptionListener() );
		runSCIP.addActionListener( Control.getControl().getRunOptionListener() );
		
		JMenu option = new JMenu("Option");
		JMenuItem toggleGrids = new JMenuItem("Toggle grids");
		
		toggleGrids.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelCanvas.toggleGrids();
				modelCanvas.repaint();
			}
		});

		file.add(exit);
		run.add(createSCN);
		run.add(runSCIP);
		option.add(toggleGrids);
		m.add(file);
		m.add(run);
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
