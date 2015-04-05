package gui.frames.welcome;

import gui.frames.dbConfig.DBConfigFrame;
import gui.frames.mainFrame.MainFrame;
import gui.frames.modelGeneration.ModelGeneraterFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

public class WelcomeFrame extends JFrame {
	private static final long serialVersionUID = -1592789028157705734L;
	String configKey = "dao";
	Map<String, String> config = new HashMap<String, String>();
	
	SpringLayout layout;

	public WelcomeFrame() {
		Dimension dim = new Dimension(320, 250);
		setSize(dim);
		setPreferredSize(dim);

		layout = new SpringLayout();
		setLayout(layout);

		initComponent();

		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	
	void initComponent() {
		//config button	
		JButton configButton = new JButton("Config Settings");
		configButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DBConfigFrame configFrame = new DBConfigFrame();
			}
		});
		add(configButton);
		layout.putConstraint(SpringLayout.NORTH, configButton, 25, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, configButton, 25, SpringLayout.WEST, this);
		
		//model button
		JButton modelButton = new JButton("Create Forms");
		modelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelGeneraterFrame modelFrame = new ModelGeneraterFrame();
			}
		});
		add(modelButton);
		layout.putConstraint(SpringLayout.NORTH, modelButton, 25, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, modelButton, -25, SpringLayout.EAST, this);
		
		//convert button
		JButton convertButton = new JButton("Convert Images");
		convertButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame();
			}
		});
		add(convertButton);
		layout.putConstraint(SpringLayout.NORTH, convertButton, 100, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, convertButton, 25, SpringLayout.WEST, this);
		
		//query button
		JButton qryButton = new JButton("Query EMR");
		qryButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame();
			}
		});
		add(qryButton);
		layout.putConstraint(SpringLayout.NORTH, qryButton, 100, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, qryButton, -25, SpringLayout.EAST, this);
		
	}


	public static void main(String[] args) {
		new WelcomeFrame();
	}
}
