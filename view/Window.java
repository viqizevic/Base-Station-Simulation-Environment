package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Window extends JFrame {
	
	private static final long serialVersionUID = -5266572208804064177L;

	private ModelCanvas modelCanvas;
	
	public Window( String title ) {
		super( title );
		modelCanvas = new ModelCanvas();
		init();
	}

	private void init() {
		createMenu();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize( new Dimension(550,450) );
		this.setPreferredSize( new Dimension(550,450) );

		modelCanvas.setMinimumSize( new Dimension(500,400) );
		modelCanvas.setPreferredSize( new Dimension(500,400) );

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
}
