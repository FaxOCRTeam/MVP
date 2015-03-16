package gui.interfaces;

import javax.swing.JFrame;


public abstract class SavableFrame extends JFrame {
	private static final long serialVersionUID = 708809667400214017L;

	public abstract void resetTitle(boolean flag);
}
