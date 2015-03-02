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
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import utils.FileChoosingUtils;

public class FileLoadingPanel extends JPanel {

	private static final long serialVersionUID = 4071238035793237990L;

	FileLoadingPanel thisObj;
	DefaultListModel<String> listModel = null;
	
	JButton processButton;
	MainFrame parent;
	
	public FileLoadingPanel(MainFrame mainFrame) {
		thisObj = this;
		parent = mainFrame;
		Dimension dimension = new Dimension(310, 460);
		setPreferredSize(dimension);
		setSize(dimension);
		setVisible(true);
		
		init();
	}

	void init() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		Dimension listDim = new Dimension(300, 400);
		listModel = new DefaultListModel<String>();
		
		JList<String> list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(listDim);
		listScroller.setSize(listDim);
		
		add(listScroller);

		layout.putConstraint(SpringLayout.NORTH, listScroller, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, listScroller, -5, SpringLayout.EAST, this);

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
		layout.putConstraint(SpringLayout.NORTH, addButton, 5, SpringLayout.SOUTH, listScroller);
		layout.putConstraint(SpringLayout.EAST, addButton, -5, SpringLayout.EAST, this);
		
		processButton = new JButton("process");
//		processButton.setEnabled(false);
		processButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(listModel.size() > 0) {
					parent.processPicture(listModel.get(0));
					listModel.remove(0);
				}
			}
		});
		add(processButton);
		
		layout.putConstraint(SpringLayout.NORTH, processButton, 5, SpringLayout.SOUTH, listScroller);
		layout.putConstraint(SpringLayout.EAST, processButton, -5, SpringLayout.WEST, addButton);
	}
}
