package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.FileChoosingUtils;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = -4096249866634543665L;

	FormPanelInterface formPanel;
	ControlPanel thisObj = null;

	public ControlPanel(FormPanelInterface formPanel) {
		this.formPanel = formPanel;
		thisObj = this;
		
		initButtons();
	}

	public void initButtons() {
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

		add(loadButton);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
