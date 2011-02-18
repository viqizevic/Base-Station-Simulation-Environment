package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.Control;

import model.Model;
import model.User;
import model.User.MSData;
import model.graph.Vertex;

public class EditObjectDialog extends JDialog {
		
	private static final long serialVersionUID = 6995298345390733714L;
	
	private Vertex v;
	
	private JButton editButton;
	
	private JButton cancelButton;
	
	private JTextField[] textFields;
	
	public EditObjectDialog( Window window, Vertex v ) {
		super( window, "Edit Object", true );
		this.v = v;
		editButton = new JButton( "Edit" );
		cancelButton = new JButton("Cancel");
		if( v.getClass().equals(User.class) ) {
			this.setTitle(this.getTitle()+" MS "+v.getId());
			textFields = new JTextField[1];
		}
		init(window);
	}
	
	private void init(Window window) {
		int w = 220;
		int h = 150;
		
		Container cCenter = new Container();
		cCenter.setLayout(new BorderLayout(5,5));
		if( v.getClass().equals(User.class) ) {
			MSData msData = (MSData) v.getAttribute(
					Model.getModel().getSimulationMap().getKeyOfUserDataAttribute()).getWeight();
			textFields[0] = new JTextField(msData.getGamma()+"");
			cCenter.add(new JLabel(" Position: "+"("+msData.getPosition().x+","+msData.getPosition().y+")"),
					BorderLayout.NORTH);
			JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			fieldPanel.add(new JLabel("Gamma:"));
			fieldPanel.add(textFields[0]);
			cCenter.add(fieldPanel, BorderLayout.CENTER);
		}
		
		editButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				try {
					double[] values = new double[textFields.length];
					for( int i=0; i<values.length; i++ ) {
						values[i] = Double.parseDouble(textFields[i].getText().trim());
					}
					Control.getControl().getEditObjectActionListener().setNewValues(v, values);
					Control.getControl().getEditObjectActionListener().actionPerformed(e);
					close();
				} catch (NumberFormatException exc) {
					View.getView().showMessage("Invalid number..");
					return;
				}
			}
		});
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		Container cSouth = new Container();
		cSouth.setLayout(new FlowLayout());
		cSouth.add( editButton );
		cSouth.add( cancelButton );
		
		this.setLayout( new BorderLayout(5,5) );
		this.add(cCenter, BorderLayout.CENTER);
		this.add(cSouth, BorderLayout.SOUTH);
		
		this.setPreferredSize(new Dimension(w,h));
		this.setSize(w,h);
		Point windowPos = window.getLocationOnScreen();
		this.setLocation( new Point(windowPos.x+window.getWidth()-w,
				windowPos.y+window.getHeight()-h) );
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
