package gui.frames.modelGeneration;

import gui.interfaces.DisplayCoordinatesInterface;
import gui.interfaces.MainCoordinateInterface;
import gui.interfaces.ModelModificationInterface;
import gui.interfaces.SavableFrame;
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
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import utils.FileChoosingUtils;

public class ModelPanel extends JPanel implements ModelModificationInterface {

	private static final long serialVersionUID = -1220255140745504900L;
	JPanel thisObj = null;

	List<FormField> formData;
	JPanel cPanel = null;
	JPanel modelDisplayPanel = null;
	SpringLayout modelDisplaySpringLayout;

	List<DisplayCoordinatesInterface> displayNotifier = new ArrayList<DisplayCoordinatesInterface>();
	MainCoordinateInterface mainCoordinate = null;

	SavableFrame parentFrame = null;
	boolean changed = false;

	public ModelPanel(SavableFrame parentFrame) {
		this.parentFrame = parentFrame;

		formData = new ArrayList<FormField>();

		modelDisplayPanel = new JPanel();
		modelDisplayPanel.setPreferredSize(new Dimension(210, 1000));
		modelDisplaySpringLayout = new SpringLayout();
		modelDisplayPanel.setLayout(modelDisplaySpringLayout);

		thisObj = this;

		_initComponent();
	}

	private void _initComponent() {
		SpringLayout sl = new SpringLayout();
		setLayout(sl);

		JScrollPane panel1 = new JScrollPane(modelDisplayPanel);
		Dimension scrollDimension = new Dimension(220, 330);
		panel1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel1.setBounds(50, 30, 300, 50);
		panel1.setPreferredSize(scrollDimension);
		panel1.setSize(scrollDimension);
		add(panel1);

		sl.putConstraint(SpringLayout.NORTH, panel1, 5, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, panel1, 5, SpringLayout.WEST, this);

		JButton saveButton = new JButton("export model");
		saveButton.setToolTipText("Export current model into local file");
		saveButton.addActionListener(exportAxtion);
		JButton importButton = new JButton("import model");
		importButton.setToolTipText("Import a existed segmentation model");
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (changed) {
					int selection = JOptionPane.showOptionDialog(
									parentFrame, //
									"There's unsaved change in model, do you want to export the model now?", //
									"unsaved change in model", //
									JOptionPane.YES_NO_CANCEL_OPTION, //
									JOptionPane.QUESTION_MESSAGE, null, //
									new String[] { "Export", "No", "Cancel" }, //
									null);
					if (selection == 0)
						exportAxtion.actionPerformed(e);
					else if (selection == -1 || selection == 2)
						return;
				}
				List<File> chooseFile = FileChoosingUtils.chooseFile("model",
						FileChoosingUtils.OPEN_DIALOG);
				if (null != chooseFile && chooseFile.size() > 0) {
					loadFromFile(chooseFile.get(0));
					_regenerateColumnDisplay();
				} else {
					JOptionPane.showMessageDialog(thisObj, "No file chosen");
				}
				changed = false;
				parentFrame.resetTitle(changed);
			}
		});

		add(saveButton);
		sl.putConstraint(SpringLayout.NORTH, saveButton, -3, SpringLayout.SOUTH, panel1);
		sl.putConstraint(SpringLayout.WEST, saveButton, -2, SpringLayout.WEST, this);

		add(importButton);
		sl.putConstraint(SpringLayout.NORTH, importButton, -3, SpringLayout.SOUTH, panel1);
		sl.putConstraint(SpringLayout.WEST, importButton, -5, SpringLayout.EAST, saveButton);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void addField(FormField ff) {
		formData.add(ff);
		_addFormDisplay(formData.size() - 1);
		changed = true;
		parentFrame.resetTitle(changed);
	}

	private void _regenerateColumnDisplay() {
		modelDisplayPanel.removeAll();
		modelDisplayPanel.revalidate();
		modelDisplayPanel.repaint();
		// modelDisplaySpringLayout = new SpringLayout();
		// modelDisplayPanel.setLayout(modelDisplaySpringLayout);
		cPanel = null;
		for (int i = 0; i < formData.size(); i++) {
			_addFormDisplay(i);
		}
		modelDisplayPanel.repaint();
	}

	private void _addFormDisplay(int index) {
		JPanel ffPanel = new JPanel();
		Dimension cd = new Dimension(190, 76);
		ffPanel.setSize(cd);
		ffPanel.setPreferredSize(cd);

		SpringLayout csl = new SpringLayout();
		ffPanel.setLayout(csl);
		FormField ff = formData.get(index);
		// ffPanel.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		ffPanel.setBorder(BorderFactory.createTitledBorder(//
				BorderFactory.createEtchedBorder(), ff.getField()));
		// Add labels to demo field.
		JLabel[] infoLabels = new JLabel[ff.getRectConfig().length];
		for (int i = 0; i < ff.getRectConfig().length; i++) {
			infoLabels[i] = new JLabel("" + ff.getRectConfig()[i]);
			ffPanel.add(infoLabels[i]);
			csl.putConstraint(SpringLayout.NORTH, infoLabels[i], 0, SpringLayout.NORTH,
					ffPanel);
			if (i == 0)
				csl.putConstraint(SpringLayout.WEST, infoLabels[0], 8, SpringLayout.WEST,
						ffPanel);
			else {
				csl.putConstraint(SpringLayout.WEST, infoLabels[i], 5, SpringLayout.EAST,
						infoLabels[i - 1]);
			}
		}
		// Add button
		JButton loadButton = new JButton("load");
		loadButton.setToolTipText("Load and preview this segmentation in image left panel");
		loadButton.setActionCommand("" + index);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				mainCoordinate.saveCoordinates();
				for (DisplayCoordinatesInterface dci : displayNotifier) {
					dci.setCoordinatesAndName(formData.get(index), true);
				}
				formData.remove(index);
				_regenerateColumnDisplay();
				changed = true;
				parentFrame.resetTitle(changed);
				repaint();
			}
		});

		JButton deleteButton = new JButton("delete");
		deleteButton.setToolTipText("Delete this segmentation");
		deleteButton.setActionCommand("" + index);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				formData.remove(index);
				_regenerateColumnDisplay();
				changed = true;
				parentFrame.resetTitle(changed);
				repaint();
			}
		});
		ffPanel.add(loadButton);
		csl.putConstraint(SpringLayout.NORTH, loadButton, -3, SpringLayout.SOUTH,
				infoLabels[0]);
		csl.putConstraint(SpringLayout.EAST, loadButton, -5, SpringLayout.EAST, ffPanel);

		ffPanel.add(deleteButton);
		csl.putConstraint(SpringLayout.NORTH, deleteButton, -3, SpringLayout.SOUTH,
				infoLabels[0]);
		csl.putConstraint(SpringLayout.EAST, deleteButton, -5, SpringLayout.WEST, loadButton);

		modelDisplayPanel.add(ffPanel);
		modelDisplaySpringLayout.putConstraint(SpringLayout.WEST, ffPanel, 5,
				SpringLayout.WEST, modelDisplayPanel);
		if (cPanel == null)
			modelDisplaySpringLayout.putConstraint(SpringLayout.NORTH, ffPanel, 5,
					SpringLayout.NORTH, modelDisplayPanel);
		else
			modelDisplaySpringLayout.putConstraint(SpringLayout.NORTH, ffPanel, 5,
					SpringLayout.SOUTH, cPanel);
		cPanel = ffPanel;
	}

	@Override
	public void removeField() {
		// TODO Auto-generated method stub

	}

	public void saveToModelFile(File f) {
		FormField.saveModel(f, formData);
	}

	public void loadFromFile(File f) {
		if (null == formData)
			formData = new ArrayList<FormField>();
		else if (formData.size() > 0)
			formData.clear();

		formData.addAll(FormField.loadModel(f));
	}

	public void addCoordinatesNotifier(DisplayCoordinatesInterface dci) {
		displayNotifier.add(dci);
	}

	public void setMainCoordiante(MainCoordinateInterface mci) {
		this.mainCoordinate = mci;
	}

	ActionListener exportAxtion = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<File> chooseFile = FileChoosingUtils.chooseFile("model",
					FileChoosingUtils.SAVE_DIALOG);
			if (null != chooseFile && chooseFile.size() > 0) {
				saveToModelFile(chooseFile.get(0));
			} else {
				JOptionPane.showMessageDialog(thisObj, "No file chosen");
			}
			changed = false;
			parentFrame.resetTitle(changed);
		}
	};
}
