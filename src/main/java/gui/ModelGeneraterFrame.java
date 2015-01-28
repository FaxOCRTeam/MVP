package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ModelGeneraterFrame extends JFrame {

	private static final long serialVersionUID = 5866100335982142250L;

	public ModelGeneraterFrame() {
		setTitle("Model Generator");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Dimension dimension = new Dimension(800, 700);
		setSize(dimension);
		setPreferredSize(dimension);
		setLayout(new GridLayout(1, 2));
		pack();
		setVisible(true);

		init();
	}

	private void init() {
		initPanels();
	}

	private void initPanels() {
		setLayout(new GridBagLayout());// set LayoutManager
		SelectionPanel panel1 = new SelectionPanel();
		panel1.setBorder(BorderFactory.createLineBorder(Color.gray));
		GridBagConstraints gbc1 = getDefaultGBC();
		gbc1.weightx = 70;
		gbc1.weighty = 60;
		add(panel1, gbc1);

		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createLineBorder(Color.blue));
		GridBagConstraints gbc2 = getDefaultGBC();
		gbc2.gridy = 1;
		gbc2.weighty = 40;
		add(panel2, gbc2);

		JPanel panel3 = new JPanel();
		panel3.setBorder(BorderFactory.createLineBorder(Color.red));
		GridBagConstraints gbc3 = getDefaultGBC();
		gbc3.gridx = 1;
		gbc3.gridheight = 2;
		gbc3.weightx = 30;
		add(panel3, gbc3);
		pack();

	}

	private GridBagConstraints getDefaultGBC() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		return gbc;
	}

	public static void main(String[] args) {
		ModelGeneraterFrame frame = new ModelGeneraterFrame();
	}

}
