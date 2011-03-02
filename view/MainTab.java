package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.Control;

import model.Model;
import model.SimulationMap;

public class MainTab extends JPanel {
	
	private static final long serialVersionUID = 8794435504702375776L;
	
	private SimulationMapCanvas modelCanvas;
	
	private JTextField scaleField;
	
	public MainTab( SimulationMap map ) {
		modelCanvas = new SimulationMapCanvas(map);
		modelCanvas.addMouseListener(Control.getControl().getCanvasMouseListener());
		scaleField = new JTextField(5);
		scaleField.setText( Model.getModel().getLengthOfOneBoxInTheMap_inMeter()+"" );
		init();
	}
	
	private void init() {
		scaleField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String val = scaleField.getText();
				try {
					if( !val.trim().isEmpty() ) {
						int intVal = Integer.parseInt(val);
						Model.getModel().setLengthOfOneBoxInTheMap_inMeter(intVal);
						View.getView().showMessage(
								"Change length of one box to: "+intVal+" m.");
					}
				} catch (NumberFormatException e) {
					View.getView().showMessage("Please enter a number as scale factor.");
				}
			}
		});
		
		BorderLayout borderLayout = new BorderLayout(5,5);
		this.setLayout(borderLayout);
		this.add(modelCanvas, BorderLayout.CENTER);
		
		JPanel scalePanel = new JPanel();
		scalePanel.add(new JLabel("Length of one box is: "));
		scalePanel.add(scaleField);
		scalePanel.add(new JLabel(" m."));
		this.add(scalePanel, BorderLayout.SOUTH);
	}

	public SimulationMapCanvas getModelCanvas() {
		return modelCanvas;
	}

}
