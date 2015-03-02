package gui.frames.modelPreview;

import gui.models.FormField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import utils.Pair;

public class ModelPreviewFrame extends JFrame {
	private static final long serialVersionUID = 2835558457211500886L;
	SpringLayout layout;
	File modelFile;
	File previewPicture;

	public ModelPreviewFrame(File modelFile, File previewPicture) {
		this.modelFile = modelFile;
		this.previewPicture = previewPicture;
		
		setTitle("Model preview");
		
		initData();
		Dimension dim = new Dimension(600, 600);
		setSize(dim);
		setPreferredSize(dim);

		layout = new SpringLayout();
		setLayout(layout);

		initComponent();

		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	void initData() {
		// Collections.sort(keys);
	}

	void initComponent() {

		BufferedImage image = null;
		try {
			image = ImageIO.read(previewPicture);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<FormField> loadModel = FormField.loadModel(modelFile);
		
		ModelPreviewPanel previewPanel = new ModelPreviewPanel(image, loadModel);
		JScrollPane scrollPane = new JScrollPane(previewPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(50, 30, 300, 50);
		
		Dimension formDime = new Dimension(590, 560);
		scrollPane.setSize(formDime);
		scrollPane.setPreferredSize(formDime);
//		formPanel.setBorder(BorderFactory.createTitledBorder(//
//				BorderFactory.createLineBorder(Color.gray), "DataBase Configuration"));
		initPreviewPanel(previewPanel);

		add(scrollPane);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, this);
//		layout.putConstraint(SpringLayout.EAST, formPanel, -15, SpringLayout.EAST, this);

	}

	void initPreviewPanel(JPanel panel) {
		
	}

	public static void main(String[] args) {
		new ModelPreviewFrame(new File("D:\\dataset\\model"), new File("D:\\workspaces\\BICProject\\MVP\\Freddie's Form.jpg"));
	}
}
