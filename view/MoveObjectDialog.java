package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import control.Control;

import model.User;

public class MoveObjectDialog extends JDialog {

	private static final long serialVersionUID = 4576041303583030366L;
	
	private JButton[] directionButtons;

	private JButton optimizeButton;

	private JButton okButton;

	public MoveObjectDialog( Window window, User user ) {
		super( window, "Move mobile station "+user.getId(), false );
		Control.getControl().getMoveObjectActionListener().setUserToMove(user);
		directionButtons = new JButton[4];
		directionButtons[0] = new JButton("^");
		directionButtons[1] = new JButton(">");
		directionButtons[2] = new JButton("v");
		directionButtons[3] = new JButton("<");
		optimizeButton = new JButton("Optimize");
		okButton = new JButton( "Finish" );
		init(window);
	}

	private void init(Window window) {
		int w = 170;
		int h = 150;

		Container cCenter = new Container();
		cCenter.setLayout( new GridLayout(3, 3) );
		cCenter.add(new JLabel());
		cCenter.add(directionButtons[0]);
		cCenter.add(new JLabel());
		cCenter.add(directionButtons[3]);
		cCenter.add(new JLabel());
		cCenter.add(directionButtons[1]);
		cCenter.add(new JLabel());
		cCenter.add(directionButtons[2]);
		cCenter.add(new JLabel());
		for( int i=0; i<directionButtons.length; i++ ) {
			directionButtons[i].addActionListener(
					Control.getControl().getMoveObjectActionListener() );
		}

		optimizeButton.addActionListener( Control.getControl().getRunOptionListener() );
		okButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				close();
			}
		});
		Container cSouth = new Container();
		cSouth.setLayout( new FlowLayout() );
		cSouth.add( okButton );
		cSouth.add( optimizeButton );
		
		this.setLayout( new BorderLayout(2,2) );
		this.add(cCenter, BorderLayout.CENTER);
		this.add(cSouth, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(w,h));
		this.setSize(w,h);
		Point windowPos = window.getLocationOnScreen();
		this.setLocation( new Point(windowPos.x+window.getWidth()-w,
				windowPos.y+window.getHeight()-h) );
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/**
	 * Close the dialog.
	 */
	public void close() {
		this.setVisible(false);
		this.dispose();
	}
}
