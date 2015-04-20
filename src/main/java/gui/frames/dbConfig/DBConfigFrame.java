package gui.frames.dbConfig;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import utils.ConfigurationUtil;
import utils.Pair;

public class DBConfigFrame extends JFrame {
	private static final long serialVersionUID = -1592789028157705734L;
	String configKey = "dao";
	Map<String, String> config = new HashMap<String, String>();
	List<Pair<JTextField, String>> fields = new ArrayList<Pair<JTextField, String>>();
	List<String> keys;
	SpringLayout layout;

	public DBConfigFrame() {
		initData();
		Dimension dim = new Dimension(315, 400);
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
		keys = ConfigurationUtil.keys(configKey);
		for (String key : keys) {
			config.put(key, ConfigurationUtil.get(configKey, key));
		}
		Collections.sort(keys);
	}

	void initComponent() {

		JPanel formPanel = new JPanel();
		Dimension formDime = new Dimension(290, 250);
		formPanel.setSize(formDime);
		formPanel.setPreferredSize(formDime);
		formPanel.setBorder(BorderFactory.createTitledBorder(//
				BorderFactory.createLineBorder(Color.gray), "DataBase Configuration"));
		initFormPanel(formPanel);

		add(formPanel);
		layout.putConstraint(SpringLayout.NORTH, formPanel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, formPanel, 5, SpringLayout.WEST, this);
		// layout.putConstraint(SpringLayout.EAST, formPanel, -15,
		// SpringLayout.EAST, this);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Pair<JTextField, String> field : fields) {
					String content = field.getV1().getText();
					String key = field.getV2();
					if (!content.equals(config.get(key)))
						ConfigurationUtil.update(configKey, key, content);
				}
			}
		});
		add(saveButton);
		layout.putConstraint(SpringLayout.NORTH, saveButton, 5, SpringLayout.SOUTH, formPanel);
		layout.putConstraint(SpringLayout.EAST, saveButton, -25, SpringLayout.EAST, this);
	}

	void initFormPanel(JPanel panel) {
		SpringLayout pLayout = new SpringLayout();
		panel.setLayout(pLayout);
		Component previous = null;
		for (String key : keys) {
			JLabel titleLabel = new JLabel(key);
			JTextField content = new JTextField(config.get(key));

			fields.add(new Pair<JTextField, String>(content, key));

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
