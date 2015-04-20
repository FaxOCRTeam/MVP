package gui.frames.modelPreview;

import gui.models.FormField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class ModelPreviewFrame extends JFrame {
	private static final long serialVersionUID = 2835558457211500886L;
	static final int scrollbarWidth = 24;
	static final int scrollbarHeight = 47;
	
	SpringLayout layout;
	File modelFile;
	File previewPicture;

	JScrollPane scrollPane;
	
	Dimension frameDimension;

	public ModelPreviewFrame(File modelFile, File previewPicture) {
		this.modelFile = modelFile;
		this.previewPicture = previewPicture;

		setTitle("Model preview");

		initData();
		frameDimension = new Dimension(600, 600);
		setSize(frameDimension);
		setPreferredSize(frameDimension);

		layout = new SpringLayout();
		setLayout(layout);

		initComponent();
		
		initActions();
		
		setVisible(true);
//		setResizable(false);
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
		scrollPane = new JScrollPane(previewPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		Dimension formDime = new Dimension((int) (frameDimension.getWidth() - scrollbarWidth),//
				(int) (frameDimension.getHeight() - scrollbarHeight));
		scrollPane.setSize(formDime);
		scrollPane.setPreferredSize(formDime);
		// formPanel.setBorder(BorderFactory.createTitledBorder(//
		// BorderFactory.createLineBorder(Color.gray),
		// "DataBase Configuration"));
		initPreviewPanel(previewPanel);

		add(scrollPane);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, this);
		// layout.putConstraint(SpringLayout.EAST, formPanel, -15,
		// SpringLayout.EAST, this);

	}

	void initPreviewPanel(JPanel panel) {
		
	}

	void initActions() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Component c = e.getComponent();
				Dimension nd = new Dimension(c.getWidth()-scrollbarWidth,//
						c.getHeight() - scrollbarHeight);
				scrollPane.setSize(nd);
				scrollPane.setPreferredSize(nd);
				validate();
				repaint();
			}
		});
	}

	
	public static void main(String[] args) {
		new ModelPreviewFrame(new File("D:\\dataset\\model"), new File(
				"D:\\workspaces\\BICProject\\MVP\\Freddie's Form.jpg"));
	}
	
	
}
