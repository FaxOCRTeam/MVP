package gui.frames.mainFrame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

public class MainFrame extends JFrame{
	public final String title = "Fax to EMR";

	public MainFrame() {
		setTitle(title);
		Dimension dimension = new Dimension(800, 700);
		setSize(dimension);
		setPreferredSize(dimension);
		
		init();

		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void init() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		FileLoadingPanel flpanel = new FileLoadingPanel();
		flpanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		add(flpanel);
		
		layout.putConstraint(SpringLayout.NORTH, flpanel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, flpanel, 5, SpringLayout.WEST, this);
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
	}
}
