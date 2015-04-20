<<<<<<< HEAD
package gui.frames.modelGeneration;

import gui.interfaces.ControlPanelInterface;
import gui.interfaces.DisplayCoordinatesInterface;
import gui.interfaces.FormPanelInterface;
import gui.interfaces.MainCoordinateInterface;
import gui.interfaces.ModelModificationInterface;
import gui.models.FormField;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import api.DBModelRegester;

import com.mysql.jdbc.StringUtils;

import utils.FileChoosingUtils;

public class ControlPanel extends JPanel implements DisplayCoordinatesInterface,
		ControlPanelInterface, MainCoordinateInterface {

	private static final long serialVersionUID = -4096249866634543665L;

	List<ModelModificationInterface> mmNotifierList = new ArrayList<ModelModificationInterface>();

	FormPanelInterface formPanel;
	ControlPanel thisObj = null;

	JLabel[] coordinatesLabels;
	// JTextField nameField;
	DefaultComboBoxModel<String> tableModel;
	JComboBox<String> tableBox;
	DefaultComboBoxModel<String> fieldModel;
	JComboBox<String> fieldBox;

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
		loadButton.setToolTipText("Load a sample form to segment");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> chooseFile = FileChoosingUtils.chooseFile("sampleForm",
						FileChoosingUtils.OPEN_DIALOG);
				if (null != chooseFile && chooseFile.size() > 0)
					formPanel.loadImage(chooseFile.get(0));
				else {
					JOptionPane.showMessageDialog(thisObj, "No file chosen");
				}
			}
		});
		JButton cancelButton = new JButton("cancel selection");
		cancelButton.setToolTipText("Cancel current segmentation of the image in left panel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.cancelSelection();
				fieldModel.removeAllElements();
				tableBox.setSelectedIndex(-1);
				for (JLabel jl : coordinatesLabels)
					jl.setText("");
			}
		});
		JButton saveButton = new JButton("save");
		saveButton.setToolTipText("Save current segmentation into model");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean succuss = saveCoordinates();
				if (succuss) {
					formPanel.cancelSelection();
					fieldModel.removeAllElements();
					tableBox.setSelectedIndex(-1);
					for (JLabel jl : coordinatesLabels)
						jl.setText("");
				} else {
					JOptionPane.showMessageDialog(thisObj, "table/field name is required");
				}
			}
		});
		
		JButton zoomInButton = new JButton("+");
		zoomInButton.setToolTipText("Zoom in the image in left panel");
		JButton zoomOutButton = new JButton("-");
		zoomOutButton.setToolTipText("Zoom out the image in left panel");
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.zoomIn();
			}
		});
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.zoomOut();
			}
		});
		
		JPanel coordinatesPanel = new JPanel();
		SpringLayout csl = new SpringLayout();
		Dimension cd = new Dimension(200, 180);
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

		tableModel = new DefaultComboBoxModel<String>();
		tableBox = new JComboBox<String>(tableModel);

		List<String> shortNameList = DBModelRegester.getShortNameList();
		for (String sn : shortNameList) {
			tableModel.addElement(sn);
		}
		tableBox.setSelectedIndex(-1);

		tableBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) tableBox.getSelectedItem();
				if(null != selectedItem)
					loadTableInfo(selectedItem);
				repaint();
			}
		});

		fieldModel = new DefaultComboBoxModel<String>();
		fieldBox = new JComboBox<String>(fieldModel);
		fieldBox.setFont(new Font(getFont().getFamily(), Font.PLAIN, 10));

		JLabel tableLabel = new JLabel("table");
		coordinatesPanel.add(tableLabel);
		coordinatesPanel.add(tableBox);

		JLabel fieldLabel = new JLabel("field");
		coordinatesPanel.add(fieldLabel);
		coordinatesPanel.add(fieldBox);

		csl.putConstraint(SpringLayout.WEST, tableLabel, 5, SpringLayout.WEST,
				coordinatesPanel);
		csl.putConstraint(SpringLayout.EAST, tableBox, -5, SpringLayout.EAST, coordinatesPanel);
		csl.putConstraint(SpringLayout.NORTH, tableLabel, 5, SpringLayout.SOUTH,
				titleLabels[3]);
		csl.putConstraint(SpringLayout.NORTH, tableBox, 5, SpringLayout.SOUTH, titleLabels[3]);

		csl.putConstraint(SpringLayout.WEST, fieldLabel, 5, SpringLayout.WEST,
				coordinatesPanel);
		csl.putConstraint(SpringLayout.EAST, fieldBox, -5, SpringLayout.EAST, coordinatesPanel);
		csl.putConstraint(SpringLayout.NORTH, fieldLabel, 5, SpringLayout.SOUTH, tableBox);
		csl.putConstraint(SpringLayout.NORTH, fieldBox, 5, SpringLayout.SOUTH, tableBox);

		add(loadButton);
		add(cancelButton);
		add(coordinatesPanel);
		add(saveButton);
		add(zoomInButton);
		add(zoomOutButton);

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

		springLayout.putConstraint(SpringLayout.WEST, zoomInButton, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, zoomInButton, 5, SpringLayout.SOUTH,
				coordinatesPanel);
		
		springLayout.putConstraint(SpringLayout.WEST, zoomOutButton, 5, SpringLayout.EAST, zoomInButton);
		springLayout.putConstraint(SpringLayout.NORTH, zoomOutButton, 5, SpringLayout.SOUTH,
				coordinatesPanel);
		
		springLayout.putConstraint(SpringLayout.WEST, saveButton, 5, SpringLayout.EAST, zoomOutButton);
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
	public void setCoordinatesAndName(FormField ff, boolean notify) {
		tableModel.setSelectedItem(ff.getTable());
		loadTableInfo(ff.getTable());
		fieldModel.setSelectedItem(ff.getField());
		
		coordinates = ff.getRectConfig();
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
		if (tableBox.getSelectedIndex() == -1 || fieldBox.getSelectedIndex() == -1) {
			return false;
		}

		FormField field = new FormField((String) tableBox.getSelectedItem(), //
				(String) fieldBox.getSelectedItem(), coordinates);
		for (ModelModificationInterface mmi : mmNotifierList) {
			mmi.addField(field);
		}
		return true;
	}

	public void loadTableInfo(String tableName) {
		fieldModel.removeAllElements();
		Class<?> c = DBModelRegester.getClassByShortName(tableName);
		if(null == c)
			System.out.println(tableName);
		Field[] declaredFields = c.getDeclaredFields();
		for (Field f : declaredFields) {
			if ("id".equals(f.getName().toLowerCase()))
				continue;
			fieldModel.addElement(f.getName());
		}
		fieldBox.setSelectedIndex(-1);
	}

}
=======
package gui.frames.modelGeneration;

import gui.interfaces.ControlPanelInterface;
import gui.interfaces.DisplayCoordinatesInterface;
import gui.interfaces.FormPanelInterface;
import gui.interfaces.MainCoordinateInterface;
import gui.interfaces.ModelModificationInterface;
import gui.models.FormField;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.bytedeco.javacpp.opencv_core.IplImage;

import api.DBModelRegester;

import com.mysql.jdbc.StringUtils;

import utils.FileChoosingUtils;

public class ControlPanel extends JPanel implements DisplayCoordinatesInterface,
		ControlPanelInterface, MainCoordinateInterface {

	private static final long serialVersionUID = -4096249866634543665L;

	List<ModelModificationInterface> mmNotifierList = new ArrayList<ModelModificationInterface>();

	FormPanelInterface formPanel;
	ControlPanel thisObj = null;

	JLabel[] coordinatesLabels;
	// JTextField nameField;
	DefaultComboBoxModel<String> tableModel;
	JComboBox<String> tableBox;
	DefaultComboBoxModel<String> fieldModel;
	JComboBox<String> fieldBox;

	int[] coordinates;

	public ControlPanel(FormPanelInterface formPanel) {
		this.formPanel = formPanel;
		thisObj = this;
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		init(springLayout);

	}

	public void init(SpringLayout springLayout) {
		
		JButton deskewButton  =new JButton("deskew form");
		deskewButton.setToolTipText("Deskew a sample form in left panel");
		deskewButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(formPanel.getImage() == null)
					JOptionPane.showMessageDialog(thisObj, "No image loaded");
				else{
					
					IplImage image = IplImage.createFrom(formPanel.getImage());
					formPanel.deskew(image);
				}
				
				
			}
		});
		
		
		
		
		JButton loadButton = new JButton("load form");
		loadButton.setToolTipText("Load a sample form to segment");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> chooseFile = FileChoosingUtils.chooseFile("sampleForm",
						FileChoosingUtils.OPEN_DIALOG);
				if (null != chooseFile && chooseFile.size() > 0)
					formPanel.loadImage(chooseFile.get(0));
				else {
					JOptionPane.showMessageDialog(thisObj, "No file chosen");
				}
			}
		});
		JButton cancelButton = new JButton("cancel selection");
		cancelButton.setToolTipText("Cancel current segmentation of the image in left panel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.cancelSelection();
				fieldModel.removeAllElements();
				tableBox.setSelectedIndex(-1);
				for (JLabel jl : coordinatesLabels)
					jl.setText("");
			}
		});
		JButton saveButton = new JButton("save");
		saveButton.setToolTipText("Save current segmentation into model");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean succuss = saveCoordinates();
				if (succuss) {
					formPanel.cancelSelection();
					fieldModel.removeAllElements();
					tableBox.setSelectedIndex(-1);
					for (JLabel jl : coordinatesLabels)
						jl.setText("");
				} else {
					JOptionPane.showMessageDialog(thisObj, "table/field name is required");
				}
			}
		});
		
		JButton zoomInButton = new JButton("+");
		zoomInButton.setToolTipText("Zoom in the image in left panel");
		JButton zoomOutButton = new JButton("-");
		zoomOutButton.setToolTipText("Zoom out the image in left panel");
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.zoomIn();
			}
		});
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formPanel.zoomOut();
			}
		});
		
		JPanel coordinatesPanel = new JPanel();
		SpringLayout csl = new SpringLayout();
		Dimension cd = new Dimension(200, 180);
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

		tableModel = new DefaultComboBoxModel<String>();
		tableBox = new JComboBox<String>(tableModel);

		List<String> shortNameList = DBModelRegester.getShortNameList();
		for (String sn : shortNameList) {
			tableModel.addElement(sn);
		}
		tableBox.setSelectedIndex(-1);

		tableBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) tableBox.getSelectedItem();
				if(null != selectedItem)
					loadTableInfo(selectedItem);
				repaint();
			}
		});

		fieldModel = new DefaultComboBoxModel<String>();
		fieldBox = new JComboBox<String>(fieldModel);
		fieldBox.setFont(new Font(getFont().getFamily(), Font.PLAIN, 10));

		JLabel tableLabel = new JLabel("table");
		coordinatesPanel.add(tableLabel);
		coordinatesPanel.add(tableBox);

		JLabel fieldLabel = new JLabel("field");
		coordinatesPanel.add(fieldLabel);
		coordinatesPanel.add(fieldBox);

		csl.putConstraint(SpringLayout.WEST, tableLabel, 5, SpringLayout.WEST,
				coordinatesPanel);
		csl.putConstraint(SpringLayout.EAST, tableBox, -5, SpringLayout.EAST, coordinatesPanel);
		csl.putConstraint(SpringLayout.NORTH, tableLabel, 5, SpringLayout.SOUTH,
				titleLabels[3]);
		csl.putConstraint(SpringLayout.NORTH, tableBox, 5, SpringLayout.SOUTH, titleLabels[3]);

		csl.putConstraint(SpringLayout.WEST, fieldLabel, 5, SpringLayout.WEST,
				coordinatesPanel);
		csl.putConstraint(SpringLayout.EAST, fieldBox, -5, SpringLayout.EAST, coordinatesPanel);
		csl.putConstraint(SpringLayout.NORTH, fieldLabel, 5, SpringLayout.SOUTH, tableBox);
		csl.putConstraint(SpringLayout.NORTH, fieldBox, 5, SpringLayout.SOUTH, tableBox);

		add(loadButton);
		add(cancelButton);
		add(coordinatesPanel);
		add(saveButton);
		add(zoomInButton);
		add(zoomOutButton);
		add(deskewButton);
		
		
		
		springLayout
				.putConstraint(SpringLayout.NORTH, loadButton, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, cancelButton, 5, SpringLayout.EAST,
				loadButton);
		springLayout.putConstraint(SpringLayout.NORTH, cancelButton, 5, SpringLayout.NORTH,
				this);

		springLayout.putConstraint(SpringLayout.WEST, coordinatesPanel, 5, SpringLayout.WEST,
				this);
		
		
		springLayout.putConstraint(SpringLayout.NORTH, deskewButton, 5, SpringLayout.SOUTH, loadButton);
		
		springLayout.putConstraint(SpringLayout.NORTH, coordinatesPanel, 5,
				SpringLayout.SOUTH, deskewButton);
	

		springLayout.putConstraint(SpringLayout.WEST, zoomInButton, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, zoomInButton, 5, SpringLayout.SOUTH,
				coordinatesPanel);
		
		springLayout.putConstraint(SpringLayout.WEST, zoomOutButton, 5, SpringLayout.EAST, zoomInButton);
		springLayout.putConstraint(SpringLayout.NORTH, zoomOutButton, 5, SpringLayout.SOUTH,
				coordinatesPanel);
		
		springLayout.putConstraint(SpringLayout.WEST, saveButton, 5, SpringLayout.EAST, zoomOutButton);
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
	public void setCoordinatesAndName(FormField ff, boolean notify) {
		tableModel.setSelectedItem(ff.getTable());
		loadTableInfo(ff.getTable());
		fieldModel.setSelectedItem(ff.getField());
		
		coordinates = ff.getRectConfig();
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
		if (tableBox.getSelectedIndex() == -1 || fieldBox.getSelectedIndex() == -1) {
			return false;
		}

		FormField field = new FormField((String) tableBox.getSelectedItem(), //
				(String) fieldBox.getSelectedItem(), coordinates);
		for (ModelModificationInterface mmi : mmNotifierList) {
			mmi.addField(field);
		}
		return true;
	}

	public void loadTableInfo(String tableName) {
		fieldModel.removeAllElements();
		Class<?> c = DBModelRegester.getClassByShortName(tableName);
		if(null == c)
			System.out.println(tableName);
		Field[] declaredFields = c.getDeclaredFields();
		for (Field f : declaredFields) {
			if ("id".equals(f.getName().toLowerCase()))
				continue;
			fieldModel.addElement(f.getName());
		}
		fieldBox.setSelectedIndex(-1);
	}

}
>>>>>>> refs/remotes/origin/master
