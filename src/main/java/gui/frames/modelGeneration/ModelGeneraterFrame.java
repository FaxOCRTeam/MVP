package gui.frames.modelGeneration;

import gui.interfaces.SavableFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class ModelGeneraterFrame extends SavableFrame{
	private static final long serialVersionUID = 5866100335982142250L;
	static final int scrollbarWidth = 24;
	static final int scrollbarHeight = 47;
	public final String title = "Model Generator";
	Dimension frameDimension;
	JScrollPane scrollPane;
	private FormPanel selectionPanel;
	
	
	
	public ModelGeneraterFrame() {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frameDimension = new Dimension(900, 700);
		setSize(frameDimension);
		setPreferredSize(frameDimension);
		setLayout(new GridLayout(1, 2));
		pack();
		setVisible(true);

		init();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void init() {
		initPanels();
		initActions();
	}

	private void initPanels() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);// set LayoutManager
		selectionPanel = new FormPanel();

		scrollPane = new JScrollPane(selectionPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		scrollPane.setBounds(50, 30, 300, 50);

		scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		// panel1.add(scrollPane);
		add(scrollPane);

		JPanel rightPanel = new JPanel();
		Dimension rpDimension = new Dimension(240, 600);
		rightPanel.setPreferredSize(rpDimension);
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		initSubRightPanel(rightPanel);
		add(rightPanel);
		
		
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, scrollPane, -45, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, rightPanel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, rightPanel, -45, SpringLayout.SOUTH, this);
		
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, rightPanel, -22, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.EAST, scrollPane, -5, SpringLayout.WEST, rightPanel);
		
		
		
		validate();
		pack();
		repaint();
	}

	void initSubRightPanel(JPanel rightPanel) {
		SpringLayout layout = new SpringLayout();
		rightPanel.setLayout(layout);
		ControlPanel controlPanel_TR = new ControlPanel(selectionPanel);
		controlPanel_TR.setBorder(BorderFactory.createLineBorder(Color.blue));
		Dimension trDim = new Dimension(rightPanel.getWidth()-5, 280);
		controlPanel_TR.setPreferredSize(trDim);
		controlPanel_TR.setSize(trDim);
		rightPanel.add(controlPanel_TR);
		selectionPanel.addCoordinatesNotifier(controlPanel_TR);

		ModelPanel modelPanel_BR = new ModelPanel(this);
		modelPanel_BR.setBorder(BorderFactory.createLineBorder(Color.red));
		rightPanel.add(modelPanel_BR);
		modelPanel_BR.setMainCoordiante(controlPanel_TR);
		modelPanel_BR.addCoordinatesNotifier(controlPanel_TR);
		controlPanel_TR.addModelAddNotifier(modelPanel_BR);
		
		layout.putConstraint(SpringLayout.NORTH, controlPanel_TR, 1, SpringLayout.NORTH, rightPanel);
		layout.putConstraint(SpringLayout.NORTH, modelPanel_BR, 5, SpringLayout.SOUTH, controlPanel_TR);
		layout.putConstraint(SpringLayout.WEST, controlPanel_TR, 1, SpringLayout.WEST, rightPanel);
		layout.putConstraint(SpringLayout.WEST, modelPanel_BR, 1, SpringLayout.WEST, rightPanel);
		layout.putConstraint(SpringLayout.EAST, controlPanel_TR, -3, SpringLayout.EAST, rightPanel);
		layout.putConstraint(SpringLayout.EAST, modelPanel_BR, -3, SpringLayout.EAST, rightPanel);
		layout.putConstraint(SpringLayout.SOUTH, modelPanel_BR, -5, SpringLayout.SOUTH, rightPanel);
		
		rightPanel.validate();
		rightPanel.repaint();
	}
	
	public void resetTitle(boolean withStar) {
		if (withStar)
			setTitle(title + "*");
		else
			setTitle(title);
		repaint();
	}

	public static void main(String[] args) {
		new ModelGeneraterFrame();
	}

	void initActions() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setPreferredSize(e.getComponent().getSize());
				validate();
				repaint();
			}
		});
	}

 }
