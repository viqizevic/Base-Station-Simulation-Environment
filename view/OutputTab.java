package view;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JPanel;

public class OutputTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 297273744957091658L;
	
	private TextArea textArea;
	
	public OutputTab() {
		super();
		textArea = new TextArea(35,35);
		textArea.setEditable(false);
		BorderLayout borderLayout = new BorderLayout(5,5);
		this.setLayout(borderLayout);
		this.add( textArea, BorderLayout.CENTER );
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
