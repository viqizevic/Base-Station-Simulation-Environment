package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JPanel;

import control.Control;

public class OutputTab extends JPanel {

	private static final long serialVersionUID = 297273744957091658L;
	
	private TextArea textArea;
	
	private JButton optimizeButton;
	
	private JButton scnOutputButton;
	
	public OutputTab() {
		super();
		textArea = new TextArea(35,35);
		optimizeButton = new JButton("Optimize");
		scnOutputButton = new JButton("See SCN File");
		init();
	}
	
	public void init() {
		optimizeButton.addActionListener( Control.getControl().getRunOptionListener() );
		scnOutputButton.addActionListener( Control.getControl().getRunOptionListener() );
		
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		Font font = new Font( Font.MONOSPACED, Font.PLAIN, 12);
		textArea.setFont(font);
		this.setLayout(new BorderLayout(5,5));
		this.add( textArea, BorderLayout.CENTER );
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(optimizeButton);
		buttonsPanel.add(scnOutputButton);
		this.add(buttonsPanel, BorderLayout.NORTH);
	}
	
	public void setText( String text ) {
		textArea.setText(text);
	}
	
	public void appendText( String text ) {
		if( !text.endsWith("\n") ) {
			text += "\n";
		}
		textArea.append(text);
	}
}
