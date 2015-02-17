package gui;

import gui.interfaces.ControlPanelInterface;
import gui.interfaces.DisplayCoordinatesInterface;
import gui.interfaces.FormPanelInterface;
import gui.interfaces.MainCoordinateInterface;
import gui.interfaces.ModelModificationInterface;
import gui.models.FormField;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.mysql.jdbc.StringUtils;

import utils.FileChoosingUtils;

public class ControlPanel extends JPanel implements DisplayCoordinatesInterface,
		ControlPanelInterface, MainCoordinateInterface {

	private static final long serialVersionUID = -4096249866634543665L;

	List<ModelModificationInterface> mmNotifierList = new ArrayList<ModelModificationInterface>();

	FormPanelInterface formPanel;
	ControlPanel thisObj = null;

	JLabel[] coordinatesLabels;
	JTextField nameField;

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
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean succuss = saveCoordinates();
				if (succuss) {
					formPanel.cancelSelection();
					nameField.setText("");
					for (JLabel jl : coordinatesLabels)
						jl.setText("");
				} else {
					JOptionPane.showMessageDialog(thisObj, "field name is required");
					nameField.requestFocus();
				}
			}
		});

		JPanel coordinatesPanel = new JPanel();
		SpringLayout csl = new SpringLayout();
		Dimension cd = new Dimension(200, 150);
		coordinatesPanel.setSize(cd);
		coordinatesPanel.setPreferredSize(cd);
		coordinatesPanel.setLayout(csl);
		coordinatesPanel.setBorder(BorderFactory.createTitledBorder(//
				BorderFactory.createEtchedBorder(), "form column"));
		coordinatesLabels = new JLabel[4];
		JLabel[] titleLabels = new JLabel[4];
		String[] titles = new String[] { "X", "Y", "Width", "Height" };
		// int maxWidth = 0;
		for (int i = 0; i < 4; i++) {
			titleLabels[i] = new JLabel(titles[i]);
			coordinatesLabels[i] = new JLabel();

			coordinatesPanel.add(titleLabels[i]);
			coordinatesPanel.add(coordinatesLabels[i]);
			csl.putConstraint(SpringLayout.WEST, titleLabels[i], 5, SpringLayout.WEST,
					coordinatesPanel);
			csl.putConstraint(SpringLayout.EAST, coordinatesLabels[i], -15, SpringLayout.EAST,
					coordinatesPanel);
			if (i == 0) {
				csl.putConstraint(SpringLayout.NORTH, titleLabels[i], 5, SpringLayout.NORTH,
						coordinatesPanel);
				csl.putConstraint(SpringLayout.NORTH, coordinatesLabels[i], 5,
						SpringLayout.NORTH, coordinatesPanel);
			} else {
				csl.putConstraint(SpringLayout.NORTH, titleLabels[i], 5, SpringLayout.SOUTH,
						titleLabels[i - 1]);
				csl.putConstraint(SpringLayout.NORTH, coordinatesLabels[i], 5,
						SpringLayout.SOUTH, titleLabels[i - 1]);
			}
		}
		nameField = new JTextField();
		JLabel nfLabel = new JLabel("name");
		Dimension nfDimention = new Dimension(120, 20);
		nameField.setSize(nfDimention);
		nameField.setPreferredSize(nfDimention);
		nameField.setHorizontalAlignment(JTextField.RIGHT);
		coordinatesPanel.add(nfLabel);
		coordinatesPanel.add(nameField);
		csl.putConstraint(SpringLayout.WEST, nfLabel, 5, SpringLayout.WEST, coordinatesPanel);
		csl.putConstraint(SpringLayout.EAST, nameField, -5, SpringLayout.EAST,
				coordinatesPanel);
		csl.putConstraint(SpringLayout.NORTH, nfLabel, 5, SpringLayout.SOUTH, titleLabels[3]);
		csl.putConstraint(SpringLayout.NORTH, nameField, 5, SpringLayout.SOUTH, titleLabels[3]);

		add(loadButton);
		add(cancelButton);
		add(coordinatesPanel);
		add(saveButton);

		springLayout.putConstraint(SpringLayout.WEST, loadButton, 5, SpringLayout.WEST, this);
		springLayout
				.putConstraint(SpringLayout.NORTH, loadButton, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, cancelButton, 5, SpringLayout.EAST,
				loadButton);
		springLayout.putConstraint(SpringLayout.NORTH, cancelButton, 5, SpringLayout.NORTH,
				this);

		springLayout.putConstraint(SpringLayout.WEST, coordinatesPanel, 5, SpringLayout.WEST,
				this);
		springLayout.putConstraint(SpringLayout.NORTH, coordinatesPanel, 5,
				SpringLayout.SOUTH, loadButton);

		springLayout.putConstraint(SpringLayout.WEST, saveButton, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, saveButton, 5, SpringLayout.SOUTH,
				coordinatesPanel);
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

	@Override
	public void setCoordinatesAndName(int[] coordinates, String name, boolean notify) {
		nameField.setText(name);
		if (notify) {
			formPanel.setSelection(coordinates);
		} else {
			setCoordinates(coordinates);
		}
		repaint();

	}

	@Override
	public void addModelAddNotifier(ModelModificationInterface mmi) {
		mmNotifierList.add(mmi);
	}

	@Override
	public boolean saveCoordinates() {
		if (StringUtils.isNullOrEmpty(nameField.getText()))
			return false;

		FormField field = new FormField(nameField.getText(), coordinates);
		for (ModelModificationInterface mmi : mmNotifierList) {
			mmi.addField(field);
		}
		return true;
	}

}
