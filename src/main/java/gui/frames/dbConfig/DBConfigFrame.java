package gui.frames.dbConfig;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import utils.ConfigurationUtil;

public class DBConfigFrame extends JFrame {
	String configKey = "dao";
	Map<String, String> config = new HashMap<String, String>();
	List<String> keys;
	SpringLayout layout;

	public DBConfigFrame() {
		initData();
		Dimension dim = new Dimension(300, 400);
		setSize(dim);
		setPreferredSize(dim);

		layout = new SpringLayout();
		setLayout(layout);
		
		initComponent();
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	void initData() {
		keys = ConfigurationUtil.keys(configKey);
		for (String key : keys) {
			config.put(key, ConfigurationUtil.get(configKey, key));
		}
//		Collections.sort(keys);
	}

	void initComponent() {

		JPanel formPanel = new JPanel();
		Dimension formDime = new Dimension(290, 300);
		formPanel.setSize(formDime);
		formPanel.setPreferredSize(formDime);
		formPanel.setBorder(BorderFactory.createTitledBorder(//
				BorderFactory.createLineBorder(Color.gray), "DataBase Configuration"));
		initFormPanel(formPanel);

		add(formPanel);
		layout.putConstraint(SpringLayout.NORTH, formPanel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, formPanel, 5, SpringLayout.WEST, this);

	}

	void initFormPanel(JPanel panel) {
		SpringLayout pLayout = new SpringLayout();
		panel.setLayout(pLayout);
		Component previous = null;
		for (String key : keys) {
			JLabel titleLabel = new JLabel(key);
			JTextField content = new JTextField(config.get(key));
			panel.add(titleLabel);
			panel.add(content);

			if (null == previous) {
				pLayout.putConstraint(SpringLayout.NORTH, titleLabel, 5, //
						SpringLayout.NORTH, panel);
				pLayout.putConstraint(SpringLayout.NORTH, content, 5, //
						SpringLayout.NORTH, panel);
			} else {
				pLayout.putConstraint(SpringLayout.NORTH, titleLabel, 5, //
						SpringLayout.SOUTH, previous);
				pLayout.putConstraint(SpringLayout.NORTH, content, 5, //
						SpringLayout.SOUTH, previous);
			}
			pLayout.putConstraint(SpringLayout.WEST, titleLabel, 5, SpringLayout.WEST, panel);
			pLayout.putConstraint(SpringLayout.EAST, content, -10, SpringLayout.EAST, panel);
			pLayout.putConstraint(SpringLayout.WEST, content, 5, SpringLayout.EAST, titleLabel);

			previous = titleLabel;
		}
		panel.validate();
		panel.repaint();
	}
	public static void main(String[] args) {
		new DBConfigFrame();
	}
}
