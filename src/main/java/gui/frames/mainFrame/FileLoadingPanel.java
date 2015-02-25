package gui.frames.mainFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import utils.FileChoosingUtils;

public class FileLoadingPanel extends JPanel {

	private static final long serialVersionUID = 4071238035793237990L;

	FileLoadingPanel thisObj;
	JList<String> list = null;
	DefaultListModel<String> listModel = null;

	public FileLoadingPanel() {
		thisObj = this;
		Dimension dimension = new Dimension(400, 600);
		setPreferredSize(dimension);
		setSize(dimension);
		
		setVisible(true);
		
		System.out.println("init file loading panel");
		init();
	}

	void init() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		Dimension listDim = new Dimension(300, 400);
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setPreferredSize(listDim);
		list.setSize(listDim);
		add(list);

		layout.putConstraint(SpringLayout.NORTH, list, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, list, -5, SpringLayout.EAST, this);

		JButton addButton = new JButton("add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> chooseFile = FileChoosingUtils.chooseFile("sampleForm", FileChoosingUtils.OPEN_DIALOG_MUlTIPLE);
				if (null != chooseFile && chooseFile.size() > 0) {
					for (File f : chooseFile) {
						listModel.addElement(f.getName());
					}
				} else {
					JOptionPane.showMessageDialog(thisObj, "No file chosen");
				}
				validate();
				repaint();
			}
		});
		add(addButton);
		layout.putConstraint(SpringLayout.NORTH, addButton, 5, SpringLayout.SOUTH, list);
		layout.putConstraint(SpringLayout.EAST, addButton, -5, SpringLayout.EAST, this);
	}
}
