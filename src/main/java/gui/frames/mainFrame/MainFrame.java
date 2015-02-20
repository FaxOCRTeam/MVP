package gui.frames.mainFrame;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	public final String title = "Fax to EMR";

	public MainFrame() {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Dimension dimension = new Dimension(800, 700);
		setSize(dimension);
		setPreferredSize(dimension);
		pack();
		setVisible(true);

		init();
	}

	private void init() {
	}
}
