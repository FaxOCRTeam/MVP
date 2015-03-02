package gui.frames.mainFrame;

import gui.frames.dbConfig.DBConfigFrame;
import gui.frames.modelGeneration.ModelGeneraterFrame;
import gui.models.FormField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SpringLayout;

import net.sourceforge.tess4j.TesseractException;
import processor.ContentProcessor;
import processor.DBWriterImpl;
import processor.ImageProcessor;
import dataModel.ConfigField;
import dataModel.Field;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8211256500380495618L;
	public final String title = "Fax to EMR";

	JMenuBar bar = null;
	FileLoadingPanel flpanel;
	PreviewPreparePanel previewPanel;

	public MainFrame() {
		setTitle(title);
		Dimension dimension = new Dimension(800, 700);
		setSize(dimension);
		setPreferredSize(dimension);

		initMenu();
		init();

		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initMenu() {
		bar = new JMenuBar();
		bar.setPreferredSize(new Dimension((int) getSize().getWidth(), 23));
		JMenu modelMenu = new JMenu("model");
		modelMenu.setMnemonic('m');

		JMenuItem generate = new JMenuItem("generate");
		generate.setMnemonic('g');
		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelGeneraterFrame modelFrame = new ModelGeneraterFrame();
			}
		});

		modelMenu.add(generate);
		bar.add(modelMenu);

		JMenu configMenu = new JMenu("config");
		configMenu.setMnemonic('c');

		JMenuItem dbConfig = new JMenuItem("database configuration");
		dbConfig.setMnemonic('d');
		dbConfig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DBConfigFrame();
			}
		});

		configMenu.add(dbConfig);
		bar.add(configMenu);

		add(bar);
		validate();
	}

	private void init() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		flpanel = new FileLoadingPanel(this);
		flpanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		add(flpanel);

		layout.putConstraint(SpringLayout.NORTH, flpanel, 5, SpringLayout.SOUTH, bar);
		layout.putConstraint(SpringLayout.EAST, flpanel, -25, SpringLayout.EAST, this);

		previewPanel = new PreviewPreparePanel();
		add(previewPanel);

		layout.putConstraint(SpringLayout.NORTH, previewPanel, 5, SpringLayout.SOUTH, bar);
		layout.putConstraint(SpringLayout.WEST, previewPanel, 5, SpringLayout.WEST, this);
	}

	public void processPicture(String picFilePath) {
		if (null == previewPanel.getModelFile())
			return;

		List<FormField> model = FormField.loadModel(previewPanel.getModelFile());
		List<ConfigField> transformModel = new ArrayList<ConfigField>();
		for (FormField ff : model) {
			transformModel.add(ff.toConfigField());
		}
		ImageProcessor processor = new ImageProcessor();
		List<Field> process = processor.process(transformModel, picFilePath);
		ContentProcessor contentProcessor = new ContentProcessor();
		try {
			contentProcessor.process(process);
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		DBWriterImpl dbWriter = new DBWriterImpl();
		dbWriter.writeToDB(process);
	}

	public boolean modelReady() {
		return null != previewPanel.getModelFile();
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
	}
}
