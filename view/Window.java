package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import control.Control;

import model.Model;

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
	public Window( String title ) {
		super( title );
		tabbedPane = new JTabbedPane();
		mainTab = new MainTab( Model.getModel().getSimulationMap() );
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
		JMenuItem coopsOpt = new JMenuItem("Max coops");
		
		runSCIP.addActionListener( Control.getControl().getRunOptionListener() );
		coopsOpt.addActionListener( Control.getControl().getRunOptionListener() );
		
		JMenu option = new JMenu("Option");
		JMenuItem setGamma = new JMenuItem("Set gamma");
		JMenuItem toggleGrids = new JMenuItem("Toggle grids");
		
		setGamma.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = View.getView().showInputDialog();
				try {
					if( str != null ) {
						double val = Double.parseDouble(str);
						Model.getModel().setGamma(val);
					}
				} catch( NumberFormatException nfe ) {
					View.getView().showMessage("Your input is not valid: "+str);
				}
			}
		});
		toggleGrids.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainTab.getModelCanvas().toggleGrids();
				mainTab.getModelCanvas().repaint();
			}
		});

		file.add(open);
		file.add(exit);
		run.add(runSCIP);
//		run.add(coopsOpt);
		option.add(setGamma);
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
	
	public void reloadTheMap() {
		mainTab = new MainTab(Model.getModel().getSimulationMap());
		tabbedPane.removeAll();
        tabbedPane.addTab("Main", null, mainTab, "Main");
        tabbedPane.addTab("Output", null, outputTab, "Output");
		this.repaint();
	}
}
