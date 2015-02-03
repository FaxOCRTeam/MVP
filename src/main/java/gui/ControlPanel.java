package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import utils.FileChoosingUtils;

public class ControlPanel extends JPanel implements DisplayCoordinatesInterface {

	private static final long serialVersionUID = -4096249866634543665L;

	FormPanelInterface formPanel;
	ControlPanel thisObj = null;

	JLabel[] coordinatesLabels;

	int[] coordinates;

	public ControlPanel(FormPanelInterface formPanel) {
		this.formPanel = formPanel;
		thisObj = this;
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		init(springLayout);
	}

	public void init(SpringLayout springLayout) {
		JButton loadButton = new JButton("load form");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> chooseFile = FileChoosingUtils.chooseFile("sampleForm");
				if (null != chooseFile && chooseFile.size() > 0)
					formPanel.loadImage(chooseFile.get(0));
				else {
					JOptionPane.showMessageDialog(thisObj, "No file chosen");
				}
			}
		});
		JButton cancelButton = new JButton("cancel selection");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.cancelSelection();
			}
		});

		JPanel coordinatesPanel = new JPanel();
		SpringLayout csl = new SpringLayout();
		Dimension cd = new Dimension(160, 150);
		coordinatesPanel.setSize(cd);
		coordinatesPanel.setPreferredSize(cd);
		coordinatesPanel.setLayout(csl);
		coordinatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "selection coordinates"));
		coordinatesLabels = new JLabel[4];
		JTextField[] titleLabels = new JTextField[4];
		String[] titles = new String[] { "X", "Y", "Width", "Height" };
		// int maxWidth = 0;
		for (int i = 0; i < 4; i++) {
			titleLabels[i] = new JTextField(titles[i]);
			coordinatesLabels[i] = new JLabel();
			// int w = titleLabels[i].getWidth();

			// if (maxWidth < w)
			// maxWidth = w;
			coordinatesPanel.add(titleLabels[i]);
			coordinatesPanel.add(coordinatesLabels[i]);
			csl.putConstraint(SpringLayout.WEST, titleLabels[i], 5, SpringLayout.WEST, coordinatesPanel);
			csl.putConstraint(SpringLayout.EAST, coordinatesLabels[i], -15, SpringLayout.EAST, coordinatesPanel);
			if (i == 0) {
				csl.putConstraint(SpringLayout.NORTH, titleLabels[i], 5, SpringLayout.NORTH, coordinatesPanel);
				csl.putConstraint(SpringLayout.NORTH, coordinatesLabels[i], 5, SpringLayout.NORTH, coordinatesPanel);
			} else {
				csl.putConstraint(SpringLayout.NORTH, titleLabels[i], 5, SpringLayout.SOUTH, titleLabels[i - 1]);
				csl.putConstraint(SpringLayout.NORTH, coordinatesLabels[i], 5, SpringLayout.SOUTH, titleLabels[i - 1]);
			}
		}
		coordinatesPanel.validate();
		add(loadButton);
		add(cancelButton);
		add(coordinatesPanel);

		springLayout.putConstraint(SpringLayout.WEST, loadButton, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, loadButton, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, cancelButton, 5, SpringLayout.EAST, loadButton);
		springLayout.putConstraint(SpringLayout.NORTH, cancelButton, 5, SpringLayout.NORTH, this);

		springLayout.putConstraint(SpringLayout.WEST, coordinatesPanel, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, coordinatesPanel, 5, SpringLayout.SOUTH, loadButton);
		coordinatesPanel.setVisible(true);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void setCoordinates(int[] coordinates) {
		this.coordinates = coordinates.clone();
		for (int i = 0; i < coordinates.length; i++)
			this.coordinatesLabels[i].setText(coordinates[i] + "");
	}

}
