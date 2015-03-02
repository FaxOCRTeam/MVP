package gui.frames.mainFrame;

import gui.frames.modelGeneration.ModelGeneraterFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SpringLayout;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8211256500380495618L;
	public final String title = "Fax to EMR";

	JMenuBar bar = null;

	public MainFrame() {
		setTitle(title);
		Dimension dimension = new Dimension(800, 700);
		setSize(dimension);
		setPreferredSize(dimension);

		initMenu();
		init();

		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initMenu() {
		bar = new JMenuBar();
		bar.setPreferredSize(new Dimension((int) getSize().getWidth(), 23));
		JMenu modelMenu = new JMenu("model");
		modelMenu.setMnemonic('m');

		JMenuItem generate = new JMenuItem("generate");
		generate.setMnemonic('g');
		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelGeneraterFrame modelFrame = new ModelGeneraterFrame();
			}
		});

		modelMenu.add(generate);
		bar.add(modelMenu);

		add(bar);
		validate();
	}

	private void init() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		FileLoadingPanel flpanel = new FileLoadingPanel();
		flpanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		add(flpanel);

		layout.putConstraint(SpringLayout.NORTH, flpanel, 5, SpringLayout.SOUTH, bar);
		layout.putConstraint(SpringLayout.EAST, flpanel, -25, SpringLayout.EAST, this);
		
		PreviewPreparePanel previewPanel = new PreviewPreparePanel();
		add(previewPanel);
		
		layout.putConstraint(SpringLayout.NORTH, previewPanel, 5, SpringLayout.SOUTH, bar);
		layout.putConstraint(SpringLayout.WEST, previewPanel, 5, SpringLayout.WEST, this);
		
		
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
	}
}
