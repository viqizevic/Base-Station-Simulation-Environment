package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ModelCanvas extends JPanel {
	
	private static final long serialVersionUID = -7367411764098276782L;

	public ModelCanvas() {
		super();
	}

	public void paintComponent( Graphics g ) {
		Graphics2D g2 = (Graphics2D) g;
		int width = this.getWidth();
		int height = this.getHeight();
		
		g2.setColor( Color.WHITE );
		g2.fillRect(0, 0, width, height);

		g2.setColor( Color.BLACK );
		int n = 4;
		int xgap = (int)(width*1.0/n);
		for( int i=0; i<n; i++ ) {
			g2.drawLine(i*xgap, 0, i*xgap, height);
		}
		int ygap = (int)(height*1.0/n);
		for( int i=0; i<n; i++ ) {
			g2.drawLine(0, i*ygap, width, i*ygap);
		}
	}
}
