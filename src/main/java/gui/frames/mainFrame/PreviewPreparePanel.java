package gui.frames.mainFrame;

import gui.frames.modelPreview.ModelPreviewFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import utils.FileChoosingUtils;
import utils.StringUtils;

public class PreviewPreparePanel extends JPanel {

	private static final long serialVersionUID = 2151918608527123874L;

	SpringLayout layout;
	JLabel modelPathLabel, previewPicPathLabel;
	JButton preview;

	PreviewPreparePanel thisobj;
	
	public PreviewPreparePanel() {
		thisobj = this;
		layout = new SpringLayout();
		setLayout(layout);

		Dimension dime = new Dimension(300, 300);
		setSize(dime);
		setPreferredSize(dime);

		setBorder(BorderFactory.createLineBorder(Color.gray));

		init();
	}

	void init() {
		modelPathLabel = new JLabel("no file choosen");
		previewPicPathLabel = new JLabel("no file choosen");

		preview = new JButton("preview");
		preview.setEnabled(false);

		JButton modelButton = new JButton("load model");
		modelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> chooseFile = FileChoosingUtils.chooseFile("model",
						FileChoosingUtils.OPEN_DIALOG);
				if (null != chooseFile && chooseFile.size() > 0) {
					modelPathLabel.setText(chooseFile.get(0).getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(null, "No file chosen");
				}
				if (checkPreviewEnable())
					preview.setEnabled(true);
				thisobj.validate();
				thisobj.repaint();
			}
		});

		preview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelPreviewFrame preview = new ModelPreviewFrame(new File(modelPathLabel
						.getText()), new File(previewPicPathLabel.getText()));
			}
		});

		JButton picButton = new JButton("load preview picture");
		picButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> chooseFile = FileChoosingUtils.chooseFile("preview",
						FileChoosingUtils.OPEN_DIALOG);
				if (null != chooseFile && chooseFile.size() > 0) {
					previewPicPathLabel.setText(chooseFile.get(0).getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(null, "No file chosen");
				}
				if (checkPreviewEnable())
					preview.setEnabled(true);
				thisobj.validate();
				thisobj.repaint();
			}
		});

		add(modelButton);
		add(modelPathLabel);
		add(picButton);
		add(previewPicPathLabel);
		add(preview);

		layout.putConstraint(SpringLayout.NORTH, modelButton, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, modelPathLabel, 5, SpringLayout.SOUTH,
				modelButton);
		layout.putConstraint(SpringLayout.NORTH, picButton, 5, SpringLayout.SOUTH,
				modelPathLabel);
		layout.putConstraint(SpringLayout.NORTH, previewPicPathLabel, 5, SpringLayout.SOUTH,
				picButton);
		layout.putConstraint(SpringLayout.NORTH, preview, 5, SpringLayout.SOUTH,
				previewPicPathLabel);
	}

	boolean checkPreviewEnable() {
		if (null == modelPathLabel || null == previewPicPathLabel)
			return false;
		if (StringUtils.isEmpty(modelPathLabel.getText())
				|| StringUtils.isEmpty(previewPicPathLabel.getText()))
			return false;
		if (modelPathLabel.getText().equals("no file choosen")
				|| previewPicPathLabel.getText().equals("no file choosen"))
			return false;
		return true;
	}

	public File getModelFile() {
		if (null == modelPathLabel || StringUtils.isEmpty(modelPathLabel.getText())//
				|| modelPathLabel.getText().equals("no file choosen"))
			return null;
		return new File(modelPathLabel.getText());
	}

}
