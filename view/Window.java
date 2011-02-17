package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

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
	
	private JTabbedPane tabbedPane;
	
	private MainTab mainTab;
	
	private OutputTab outputTab;

	/**
	 * Constructs the window.
	 * @param title The title of the frame.
	 * @param map The simulation map from {@link Model}.
	 */
	public Window( String title, SimulationMap map ) {
		super( title );
		tabbedPane = new JTabbedPane();
		mainTab = new MainTab( map );
		outputTab = new OutputTab();
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

		tabbedPane.addTab("Main", null, mainTab, "Main");
        tabbedPane.addTab("Output", null, outputTab, "Output");
//        tabbedPane.addTab("Chart", null, xyChartPanel, "Chart");
		this.add( tabbedPane );
	}

	/**
	 * Create the menu of the window.
	 */
	private void createMenu() {
		JMenuBar m = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem exit = new JMenuItem("Exit");
		
		open.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Control.getControl().loadFile();
			}
		});
		exit.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) { System.exit(0); }
		});

		JMenu run = new JMenu("Run");
		JMenuItem runSCIP = new JMenuItem("Optimize");
		
		runSCIP.addActionListener( Control.getControl().getRunOptionListener() );
		
		JMenu option = new JMenu("Option");
		JMenuItem toggleGrids = new JMenuItem("Toggle grids");
		
		toggleGrids.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainTab.getModelCanvas().toggleGrids();
				mainTab.getModelCanvas().repaint();
			}
		});

		file.add(open);
		file.add(exit);
		run.add(runSCIP);
		option.add(toggleGrids);
		m.add(file);
		m.add(run);
		m.add(option);
		this.setJMenuBar(m);
	}

	public SimulationMapCanvas getSimulationMapCanvas() {
		return mainTab.getModelCanvas();
	}
	
	public void setText( String text ) {
		outputTab.setText(text);
	}
	
	public void appendText( String text ) {
		outputTab.appendText(text);
	}
	
	public void refresh() {
		tabbedPane.removeAll();
		mainTab = new MainTab(Model.getModel().getSimulationMap());
        tabbedPane.addTab("Main", null, mainTab, "Main");
        tabbedPane.addTab("Output", null, outputTab, "Output");
		this.repaint();
	}
}
